package org.example.myspringapp.services;

import org.example.myspringapp.repository.AppUserRepository;
import org.example.myspringapp.models.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AppUserService {
    private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser save(AppUser user) {
        logger.info("Saving user: {}", user.getUsername());
        return appUserRepository.save(user);
    }

    public Optional<AppUser> findById(Long id) {
        logger.info("Finding user by id: {}", id);
        return appUserRepository.findById(id);
    }

    @Cacheable(value = "AppUser", key = "#username")
    public AppUser findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return appUserRepository.findByUsername(username);
    }

    public List<AppUser> findAll() {
        logger.info("Fetching all users");
        return appUserRepository.findAll();
    }

    public void deleteById(Long id) {
        logger.info("Deleting user by id: {}", id);
        appUserRepository.deleteById(id);
    }
}
