package com.example.ubernet.service;

import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.CustomerPayment;
import com.example.ubernet.model.Payment;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.RideState;
import com.example.ubernet.model.enums.UserRole;
import com.example.ubernet.repository.CustomerPaymentRepository;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.PaymentRepository;
import com.example.ubernet.repository.RideRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AcceptRequestSplitFairServiceTest {

    @Mock
    private CustomerPaymentRepository customerPaymentRepository;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private RideService rideService;
    @Captor
    private ArgumentCaptor<CustomerPayment> customerPaymentArgumentCaptor;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;
    @Captor
    private ArgumentCaptor<Payment> paymentArgumentCaptor;
    @Captor
    private ArgumentCaptor<Ride> rideArgumentCaptor;

    @InjectMocks
    private AcceptRequestSplitFairService acceptRequestSplitFairService;
    @Test
    @DisplayName("Should throw BadRequestException for non existing url")
    public void shouldThrowBadRequestExceptionForNonExistingUrl() {
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class, () -> acceptRequestSplitFairService.acceptSplitFare(any()));
    }

    @Test
    @DisplayName("Should throw BadRequestException for already active customer")
    public void shouldThrowBadRequestExceptionForAlreadyActiveCustomer() {
        CustomerPayment customerPayment = createCustomerPayment();
        customerPayment.getCustomer().setActive(true);
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);
        assertThrows(BadRequestException.class, () -> acceptRequestSplitFairService.acceptSplitFare(any()));
    }

    @Test
    @DisplayName("Should throw BadRequestException if customer already payed")
    public void shouldThrowBadRequestExceptionIfCustomerAlreadyPayed() {
        CustomerPayment customerPayment = createCustomerPayment();
        customerPayment.setPayed(true);
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);
        assertThrows(BadRequestException.class, () -> acceptRequestSplitFairService.acceptSplitFare(any()));
    }

    @Test
    @DisplayName("Should throw BadRequestException if customer already payed")
    public void shouldThrowBadRequestExceptionIfReservationPassed() {
        CustomerPayment customerPayment = createCustomerPayment();
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);

        Ride ride = createOldReservedRide();
        Mockito.when(rideRepository.getRideByCustomerPaymentURL(any())).thenReturn(ride);

        assertThrows(BadRequestException.class, () -> acceptRequestSplitFairService.acceptSplitFare(any()));
    }

    @Test
    @DisplayName("Should throw BadRequestException if customer does not have enough money")
    public void shouldThrowBadRequestExceptionIfCustomerDoesNotHaveEnoughMoney() {
        CustomerPayment customerPayment = createCustomerPayment();
        customerPayment.getCustomer().setNumberOfTokens(200);
        customerPayment.setPricePerCustomer(300.00);
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);
        Ride ride = new Ride();
        ride.setPayment(createPayment(customerPayment));
        Mockito.when(rideRepository.getRideByCustomerPaymentURL(any())).thenReturn(ride);

        assertThrows(BadRequestException.class, () -> acceptRequestSplitFairService.acceptSplitFare(any()));
    }



    @Test
    @DisplayName("Should update customers payment and lower the amount of tokens of user. Everyone did not pay.")
    public void shouldUpdateCustomersPaymentAndLowerAmountOfTokensOfUserNotEveryonePayed() {
        CustomerPayment customerPayment = createCustomerPayment();
        customerPayment.getCustomer().setNumberOfTokens(300);
        customerPayment.setPricePerCustomer(200.00);
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);
        Ride ride = new Ride();
        ride.setPayment(createPayment(customerPayment));
        Mockito.when(rideRepository.getRideByCustomerPaymentURL(any())).thenReturn(ride);
        acceptRequestSplitFairService.acceptSplitFare(any());
        verify(customerPaymentRepository, times(1)).save(customerPaymentArgumentCaptor.capture());
        verify(customerRepository, times(1)).save(customerArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should update customers payment and lower the amount of tokens of user. Everyone did pay. Ride status should be RESERVED")
    public void shouldUpdateCustomersPaymentAndLowerAmountOfTokensOfUserEveryonePayedRideStatusBecomesReserved() {
        CustomerPayment customerPayment = createCustomerPayment();
        customerPayment.getCustomer().setNumberOfTokens(300);
        customerPayment.setPricePerCustomer(200.00);
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);

        Ride ride = new Ride();
        ride.setReservation(true);
        ride.setPayment(createPaymentOtherPayed(customerPayment));
        ride.setScheduledStart(LocalDateTime.now().plusMinutes(20));
        Mockito.when(rideRepository.getRideByCustomerPaymentURL(any())).thenReturn(ride);

        acceptRequestSplitFairService.acceptSplitFare(any());

        verify(customerPaymentRepository, times(1)).save(customerPaymentArgumentCaptor.capture());
        verify(customerRepository, times(1)).save(customerArgumentCaptor.capture());

        assertEquals(true, ride.getPayment().getIsAcceptedPayment());
        assertEquals(RideState.RESERVED, ride.getRideState());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should update customers payment and lower the amount of tokens of user. Everyone did pay. Ride status should be WAITING because it is sooner than 10 minutes.")
    public void shouldUpdateCustomersPaymentAndLowerAmountOfTokensOfUserEveryonePayedForReservationRideStatusBecomesWaitingBecauseOfTime() {
        CustomerPayment customerPayment = createCustomerPayment();
        customerPayment.getCustomer().setNumberOfTokens(300);
        customerPayment.setPricePerCustomer(200.00);
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);

        Ride ride = new Ride();
        ride.setReservation(true);
        ride.setPayment(createPaymentOtherPayed(customerPayment));
        ride.setScheduledStart(LocalDateTime.now().plusMinutes(5));
        Mockito.when(rideRepository.getRideByCustomerPaymentURL(any())).thenReturn(ride);

        acceptRequestSplitFairService.acceptSplitFare(any());

        verify(customerPaymentRepository, times(1)).save(customerPaymentArgumentCaptor.capture());
        verify(customerRepository, times(1)).save(customerArgumentCaptor.capture());

        assertEquals(true, ride.getPayment().getIsAcceptedPayment());
        assertEquals(RideState.WAITING, ride.getRideState());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should update customers payment and lower the amount of tokens of user. Everyone did pay. Ride status should be WAITING because it is not reservation.")
    public void shouldUpdateCustomersPaymentAndLowerAmountOfTokensOfUserEveryonePayedForNotReservedRideRideStatusBecomesWaiting() {
        CustomerPayment customerPayment = createCustomerPayment();
        customerPayment.getCustomer().setNumberOfTokens(300);
        customerPayment.setPricePerCustomer(200.00);
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);

        Ride ride = new Ride();
        ride.setReservation(false);
        ride.setPayment(createPaymentOtherPayed(customerPayment));
        Mockito.when(rideRepository.getRideByCustomerPaymentURL(any())).thenReturn(ride);

        acceptRequestSplitFairService.acceptSplitFare(any());

        verify(customerPaymentRepository, times(1)).save(customerPaymentArgumentCaptor.capture());
        verify(customerRepository, times(1)).save(customerArgumentCaptor.capture());

        assertEquals(true, ride.getPayment().getIsAcceptedPayment());
        assertEquals(RideState.WAITING, ride.getRideState());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should send car to customers.")
    public void shouldSetRidePositionsAndNotifyCustomers() {
        CustomerPayment customerPayment = createCustomerPayment();
        customerPayment.getCustomer().setNumberOfTokens(300);
        customerPayment.setPricePerCustomer(200.00);
        Mockito.when(customerPaymentRepository.findByUrl(any())).thenReturn(customerPayment);

        Ride ride = new Ride();
        ride.setReservation(false);
        ride.setPayment(createPaymentOtherPayed(customerPayment));
        Mockito.when(rideRepository.getRideByCustomerPaymentURL(any())).thenReturn(ride);

        acceptRequestSplitFairService.acceptSplitFare(any());

        verify(customerPaymentRepository, times(1)).save(customerPaymentArgumentCaptor.capture());
        verify(customerRepository, times(1)).save(customerArgumentCaptor.capture());

        assertEquals(true, ride.getPayment().getIsAcceptedPayment());
        assertEquals(RideState.WAITING, ride.getRideState());

        verify(paymentRepository, times(1)).save(paymentArgumentCaptor.capture());
        verify(rideRepository, times(1)).save(rideArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should not send car to customers. There is non available. Ride is not set to canceled because ride is reserved for future.")
    public void shouldTryToSendCarsButRideShouldRemainTheSameBecauseItIsFutureReservationAndSettingCarsFailed() {
        Ride ride = new Ride();
        ride.setReservation(true);
        ride.setScheduledStart(LocalDateTime.now().plusMinutes(5));
        Mockito.when(rideService.setRidePositions(any())).thenThrow(BadRequestException.class);
        acceptRequestSplitFairService.sendCarToCustomers(ride);
        verify(rideRepository, times(0)).save(rideArgumentCaptor.capture());  // ride was not set to canceled
    }

    @Test
    @DisplayName("Should not send car to customers. There is non available. Ride is set to canceled because ride is reserved for past.")
    public void shouldTryToSendCarsButRideShouldBeSetToCanceledBecauseItIsPastReservationAndSettingCarsFailed() {
        Ride ride = new Ride();
        Customer customerA = createCustomer("a@gmail.com");
        Customer customerB = createCustomer("b@gmail.com");
        ride.setCustomers(List.of(customerA, customerB));
        ride.setReservation(true);
        CustomerPayment customerPaymentA = new CustomerPayment();
        customerPaymentA.setCustomer(customerA);
        CustomerPayment customerPaymentB = new CustomerPayment();
        customerPaymentB.setCustomer(customerB);
        Payment payment = new Payment();
        payment.setCustomers(List.of(customerPaymentA, customerPaymentB));
        ride.setPayment(payment);
        ride.setScheduledStart(LocalDateTime.now().minusMinutes(5));
        Mockito.when(rideService.setRidePositions(any())).thenThrow(BadRequestException.class);
        acceptRequestSplitFairService.sendCarToCustomers(ride);
        verify(customerRepository, times(ride.getCustomers().size())).save(customerArgumentCaptor.capture());   // customers set to active
    }

    @Test
    @DisplayName("Should not send car to customers. There is non available. Ride is set to canceled because ride is not future reservation.")
    public void shouldTryToSendCarsButRideShouldBeSetToCanceledBecauseItIsNotReservationAndSettingCarsFailed() {
        Ride ride = new Ride();
        Customer customerA = createCustomer("a@gmail.com");
        Customer customerB = createCustomer("b@gmail.com");
        ride.setCustomers(List.of(customerA, customerB));
        ride.setReservation(false);
        CustomerPayment customerPaymentA = new CustomerPayment();
        customerPaymentA.setCustomer(customerA);
        CustomerPayment customerPaymentB = new CustomerPayment();
        customerPaymentB.setCustomer(customerB);
        Payment payment = new Payment();
        payment.setCustomers(List.of(customerPaymentA, customerPaymentB));
        ride.setPayment(payment);
        Mockito.when(rideService.setRidePositions(any())).thenThrow(BadRequestException.class);
        acceptRequestSplitFairService.sendCarToCustomers(ride);
        verify(customerRepository, times(ride.getCustomers().size())).save(customerArgumentCaptor.capture());   // customers set to active
    }

    private Payment createPaymentOtherPayed(CustomerPayment customerPayment) {
        CustomerPayment customerPayment2 = new CustomerPayment();
        customerPayment2.setCustomer(createCustomer("a@gmail.com"));
        customerPayment2.setPayed(true);
        Payment payment = new Payment();
        payment.setCustomers(List.of(customerPayment, customerPayment2));
        return payment;
    }
    private Payment createPayment(CustomerPayment customerPayment) {
        CustomerPayment customerPayment2 = new CustomerPayment();
        customerPayment2.setCustomer(createCustomer("a@gmail.com"));
        customerPayment2.setPayed(false);
        Payment payment = new Payment();
        payment.setCustomers(List.of(customerPayment, customerPayment2));
        return payment;
    }

    private Ride createOldReservedRide() {
        Ride ride = new Ride();
        ride.setReservation(true);
        ride.setScheduledStart(LocalDateTime.now().minusMinutes(10));
        return ride;
    }


    private CustomerPayment createCustomerPayment() {
        CustomerPayment customerPayment = new CustomerPayment();
        customerPayment.setCustomer(createCustomer("pera@gmail.com"));
        customerPayment.setPayed(false);
        customerPayment.setPricePerCustomer(200.0);
        return customerPayment;
    }

    private Customer createCustomer(String email) {
        Customer customer = new Customer();
        customer.setBlocked(false);
        customer.setEmail(email);
        customer.setRole(UserRole.CUSTOMER);
        customer.setSurname("Customeric");
        customer.setName("Customer");
        customer.setPassword("customer");
        customer.setNumberOfTokens(1000);
        customer.setActive(false);
        return customer;
    }
}
