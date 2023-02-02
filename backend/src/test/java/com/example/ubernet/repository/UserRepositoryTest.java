package com.example.ubernet.repository;

import com.example.ubernet.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldReturnUserWhenFindingByValidEmail() {
        User u = new User();
        u.setEmail("user15831@gmail.com");

        testEntityManager.merge(u);
        testEntityManager.flush();

        User foundUser = userRepository.findByEmail(u.getEmail()).orElse(null);
        assertEquals(u.getEmail(), foundUser.getEmail());
    }

    @Test
    public void shouldReturnCustomerWhenFindingByValidEmail() {
        Customer u = new Customer();
        u.setEmail("user15831@gmail.com");

        testEntityManager.merge(u);
        testEntityManager.flush();

        User foundUser = userRepository.findByEmail(u.getEmail()).orElse(null);
        assertEquals(u.getEmail(), foundUser.getEmail());
    }

    @Test
    public void shouldReturnDriverWhenFindingByValidEmail() {
        Driver u = new Driver();
        u.setEmail("user15831@gmail.com");

        testEntityManager.merge(u);
        testEntityManager.flush();

        User foundUser = userRepository.findByEmail(u.getEmail()).orElse(null);
        assertEquals(u.getEmail(), foundUser.getEmail());
    }

    @Test
    public void shouldReturnAdminWhenFindingByValidEmail() {
        Admin u = new Admin();
        u.setEmail("user15831@gmail.com");

        testEntityManager.merge(u);
        testEntityManager.flush();

        User foundUser = userRepository.findByEmail(u.getEmail()).orElse(null);
        assertEquals(u.getEmail(), foundUser.getEmail());
    }

    @Test
    public void shouldReturnNullWhenFindingByInvalidEmail() {
        User u = new User();
        u.setEmail("user15831@gmail.com");

        testEntityManager.merge(u);
        testEntityManager.flush();

        User foundUser = userRepository.findByEmail(u.getEmail()).orElse(null);
        assertEquals(u.getEmail(), foundUser.getEmail());
    }
}
