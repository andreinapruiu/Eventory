package com.mobylab.springbackend.config;

import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.repository.RoleRepository;
import com.mobylab.springbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Component
@Order(2) // Run after DataInitializer
public class ApplicationInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInitializer.class);

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.username}")
    private String adminUserName;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    public ApplicationInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        LOGGER.info("Checking if there is admin user");
        Optional<User> adminUser = userRepository.findByEmail(adminEmail);
        if (adminUser.isEmpty()) {
            LOGGER.info("Creating admin user");
            Role adminRole = roleRepository.getByName("ADMIN");
            Role userRole = roleRepository.getByName("USER");
            Role organizerRole = roleRepository.getByName("ORGANIZER");
            
            User user = new User();
            user.setEmail(adminEmail);
            user.setUsername(adminUserName);
            user.setPassword(passwordEncoder.encode(adminPassword));
            user.setRoles(new ArrayList<>(Arrays.asList(adminRole, userRole, organizerRole)));
            
            userRepository.save(user);
            LOGGER.info("Admin user created successfully");
        } else {
            LOGGER.info("Admin user already exists");
        }
    }
}
