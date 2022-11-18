package com.example.ubernet.service;

import com.example.ubernet.dto.CreditCardDTO;
import com.example.ubernet.model.CreditCard;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.CreditCardRepository;
import org.springframework.stereotype.Service;


@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final UserService userService;

    public CreditCardService(CreditCardRepository creditCardRepository, UserService userService) {
        this.creditCardRepository = creditCardRepository;
        this.userService = userService;
    }

    public CreditCard addCreditCardInfo(String email, CreditCardDTO creditCardDTO){
        User client = userService.findByEmail(email);
        CreditCard activeCreditCard, newCreditCard = null;

        if (client != null){
            activeCreditCard = findClientsActiveCard(client);

            if ( activeCreditCard != null) // already has an active credit card
                deactivateCreditCard(activeCreditCard);

            newCreditCard = createNewCreditCard(email, creditCardDTO);
        }

        return newCreditCard;
    }

    private CreditCard createNewCreditCard(String email, CreditCardDTO creditCardDTO){
        User client = userService.findByEmail(email);
        CreditCard card = new CreditCard(client, creditCardDTO);
        save(card);

        return card;
    }

    private CreditCard deactivateCreditCard(CreditCard creditCard){
        creditCard.setIsActive(false);
        save(creditCard);

        return creditCard;
    }

    public CreditCard save(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    private CreditCard findClientsActiveCard(User client){
        return creditCardRepository.findByClientAndIsActive(client, true);
    }

    public CreditCard findClientsActiveCardByEmail(String email){
        User client = userService.findByEmail(email);
        return creditCardRepository.findByClientAndIsActive(client, true);
    }

    public boolean validateNewCardAddition(String email, CreditCardDTO creditCardDTO){
        return validateNewCardData(creditCardDTO) && !isNewCreditCardSameAsActive(email, creditCardDTO);
    }

    private boolean validateNewCardData(CreditCardDTO creditCardDTO){
        String[] mmYySplits;
        if (creditCardDTO.getCardNumber().matches("\\d{16}") &&
                creditCardDTO.getExpirationDate().matches("\\d{2}\\/\\d{2}") &&
                creditCardDTO.getCvv().matches("\\d{3}")) {
            mmYySplits = creditCardDTO.getExpirationDate().split("/");
            if (Integer.valueOf(mmYySplits[0]) > 0 && Integer.valueOf(mmYySplits[0])  <= 12)
                return true;
        }

        return false;
    }

    private boolean isNewCreditCardSameAsActive(String email, CreditCardDTO creditCardDTO){
        CreditCard card = findClientsActiveCardByEmail(email);
        if (card == null)
            return false;

        return card.getCardNumber().equals(creditCardDTO.getCardNumber()) &&
            card.getExpirationDate().equals(creditCardDTO.getExpirationDate()) &&
            card.getCvv().equals(creditCardDTO.getCvv());
    }
}