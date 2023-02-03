package com.example.ubernet.controller;

import com.example.ubernet.dto.EncodedImage;
import com.example.ubernet.model.Image;
import com.example.ubernet.model.User;
import com.example.ubernet.service.ImageService;
import com.example.ubernet.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/image")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final UserService userService;

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
    public ResponseEntity<EncodedImage> getUsersImage(@PathVariable String email) throws IOException {
        User user = userService.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Image image = imageService.findUsersProfileImage(user);
        EncodedImage encoded;

        if (image == null) {
            encoded = imageService.getEncodedDefaultProfileImage();
        } else {
            encoded = imageService.encodeImage(image);
        }

        return new ResponseEntity<>(encoded, HttpStatus.OK);
    }
}
