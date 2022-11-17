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

    public boolean validateCreditCardInfo(CreditCardDTO creditCardDTO){
        // TODO
        return true;
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
}