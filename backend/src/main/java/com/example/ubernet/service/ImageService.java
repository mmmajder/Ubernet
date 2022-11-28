package com.example.ubernet.service;

import com.example.ubernet.dto.EncodedImage;
import com.example.ubernet.model.Image;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserService userService;

    public ImageService(ImageRepository imageRepository, UserService userService) {
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    public Image store(String email, MultipartFile file) throws IOException {
        User user = userService.findByEmail(email);
        deactivateUsersProfileImage(user);
        Image image = new Image(user, file.getBytes());

        return imageRepository.save(image);
    }

    public Image findUsersProfileImageByEmail(String email) {
        User user = userService.findByEmail(email);

        return imageRepository.findByUserAndIsActive(user, true);
    }

    public Image findUsersProfileImage(User user) {
        return imageRepository.findByUserAndIsActive(user, true);
    }

    private Image deactivateUsersProfileImage(User user) {
        Image usersProfileImage = findUsersProfileImage(user);

        if (usersProfileImage != null) {
            usersProfileImage.setActive(false);
            imageRepository.save(usersProfileImage);
        }

        return usersProfileImage;
    }

    public EncodedImage encodeImage(Image image) {
        String encodedData = Base64.getEncoder().encodeToString(image.getData());
        EncodedImage encodedImage = new EncodedImage(encodedData);

        return encodedImage;
    }
}