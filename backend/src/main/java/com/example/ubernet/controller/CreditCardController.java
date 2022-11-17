package com.example.ubernet.controller;

import com.example.ubernet.dto.CreditCardDTO;
import com.example.ubernet.dto.MessageDTO;
import com.example.ubernet.dto.MessageResponse;
import com.example.ubernet.model.CreditCard;
import com.example.ubernet.model.Message;
import com.example.ubernet.model.StringResponse;
import com.example.ubernet.service.CreditCardService;
import com.example.ubernet.service.MessageService;
import com.example.ubernet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(value = "/creditCard", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreditCardController {
    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @PutMapping(value = "/add/{email}")
    public ResponseEntity<CreditCard> addCreditCardInfo(@PathVariable String email, @RequestBody CreditCardDTO creditCardDTO) {
        if (!creditCardService.validateCreditCardInfo(creditCardDTO))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CreditCard card = creditCardService.addCreditCardInfo(email, creditCardDTO);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }
}