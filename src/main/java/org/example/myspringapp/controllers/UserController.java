package org.example.myspringapp.controllers;

import org.example.myspringapp.models.AuthenticationRequest;
import org.example.myspringapp.models.AuthenticationResponse;
import org.example.myspringapp.models.ApiResponse;
import org.example.myspringapp.utils.JwtUtil;
import org.example.myspringapp.utils.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.example.myspringapp.models.AppUser;
import org.example.myspringapp.repository.AppUserRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Operation(
            summary = "Authenticate user and generate JWT token"
    )
    @PostMapping("/api/authenticate")
    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        logger.info("Authentication attempt for user: {}", authenticationRequest.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            logger.warn("Failed authentication for user: {}", authenticationRequest.getUsername());
            return ResponseEntity.status(401).body(new ApiResponse<>(ApiConstants.API_VERSION, "Incorrect username or password"));
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails.getUsername());
        logger.info("JWT generated for user: {}", authenticationRequest.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(ApiConstants.API_VERSION, new AuthenticationResponse(jwt)));
    }

    @Operation(
            summary = "hello world",
            security = @SecurityRequirement(name = "bearerAuth")
    )
     @GetMapping("/hello")
     public String sayHello(@io.swagger.v3.oas.annotations.Parameter(hidden = true)
                                @RequestHeader(name = "Authorization", required = true) String authorizationHeader) {
         return "Hello";
     }

    @Operation(
            summary = "Register a new user"
    )
    @PostMapping("/api/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody AppUser appUser) {
        logger.info("Signup attempt for user: {}", appUser.getUsername());
        if (appUserRepository.findByUsername(appUser.getUsername()) != null) {
            logger.warn("Signup failed: Username already exists: {}", appUser.getUsername());
            return ResponseEntity.badRequest().body(new ApiResponse<>(ApiConstants.API_VERSION, "Username already exists"));
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
        logger.info("User registered successfully: {}", appUser.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(ApiConstants.API_VERSION, "User registered successfully"));
    }

    @Operation(
            summary = "List all usernames",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/api/all-users")
    public ResponseEntity<ApiResponse<List<String>>> listAllUsernames() {
        logger.info("/api/all-users endpoint called");
        Iterable<AppUser> users = appUserRepository.findAll();
        List<String> usernames = new ArrayList<>();
        users.forEach(user -> usernames.add(user.getUsername()));
        logger.info("Fetched {} usernames", usernames.size());
        return ResponseEntity.ok(new ApiResponse<>(ApiConstants.API_VERSION, usernames));
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            logger.info("Application started. Inspecting Spring Boot beans...");
//            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            logger.debug("Total beans loaded: {}", beanNames.length);
        };
    }

}
