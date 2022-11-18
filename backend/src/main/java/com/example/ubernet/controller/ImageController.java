package com.example.ubernet.controller;

import com.example.ubernet.dto.EncodedImage;
import com.example.ubernet.model.Image;
import com.example.ubernet.model.User;
import com.example.ubernet.service.CreditCardService;
import com.example.ubernet.service.ImageService;
import com.example.ubernet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@CrossOrigin(origins="*")
@RequestMapping(value = "/image")
public class ImageController {

    private final ImageService imageService;
    private final UserService userService;

    public ImageController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @PostMapping("/{email}")
    public ResponseEntity<EncodedImage> uploadProfilePhoto(@PathVariable String email, @RequestParam("file") MultipartFile file) {
        try {
            User user = userService.findByEmail(email);
            if (user == null)
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            Image image = imageService.store(email, file);
            EncodedImage encoded = imageService.encodeImage(image);

            return new ResponseEntity<>(encoded, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<EncodedImage> getUsersImage(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Image image = imageService.findUsersProfileImage(user);
        if (image == null)
            return new ResponseEntity<>(null,HttpStatus.OK);

        EncodedImage encoded = imageService.encodeImage(image);

        return new ResponseEntity<>(encoded, HttpStatus.OK);
    }
}