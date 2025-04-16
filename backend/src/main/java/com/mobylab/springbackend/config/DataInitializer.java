package com.mobylab.springbackend.config;

import com.mobylab.springbackend.entity.Category;
import com.mobylab.springbackend.entity.Role;
import com.mobylab.springbackend.repository.CategoryRepository;
import com.mobylab.springbackend.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@Order(1)
public class DataInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);
    
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public DataInitializer(RoleRepository roleRepository, CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        LOGGER.info("Initializing database with required data");
        
        // Initialize roles
        initRoles();
        
        // Initialize categories
        initCategories();
        
        LOGGER.info("Database initialization completed");
    }
    
    private void initRoles() {
        LOGGER.info("Checking and initializing roles");
        
        if (roleRepository.count() == 0) {
            LOGGER.info("Creating default roles");
            
            List<Role> defaultRoles = Arrays.asList(
                new Role().setId(1).setName("ADMIN"),
                new Role().setId(2).setName("USER"),
                new Role().setId(3).setName("ORGANIZER")
            );
            
            roleRepository.saveAll(defaultRoles);
            LOGGER.info("Default roles created");
        } else {
            LOGGER.info("Roles already exist, skipping initialization");
        }
    }
    
    private void initCategories() {
        LOGGER.info("Checking and initializing categories");
        
        if (categoryRepository.count() == 0) {
            LOGGER.info("Creating default categories");
            
            List<Category> defaultCategories = Arrays.asList(
                new Category()
                    .setId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                    .setName("Conference")
                    .setDescription("Professional gatherings for networking and learning"),
                new Category()
                    .setId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                    .setName("Workshop")
                    .setDescription("Hands-on sessions for skill development"),
                new Category()
                    .setId(UUID.fromString("33333333-3333-3333-3333-333333333333"))
                    .setName("Concert")
                    .setDescription("Musical performances and entertainment events"),
                new Category()
                    .setId(UUID.fromString("44444444-4444-4444-4444-444444444444"))
                    .setName("Exhibition")
                    .setDescription("Displays of art, products, or services"),
                new Category()
                    .setId(UUID.fromString("55555555-5555-5555-5555-555555555555"))
                    .setName("Webinar")
                    .setDescription("Online educational or informative sessions")
            );
            
            categoryRepository.saveAll(defaultCategories);
            LOGGER.info("Default categories created");
        } else {
            LOGGER.info("Categories already exist, skipping initialization");
        }
    }
} 