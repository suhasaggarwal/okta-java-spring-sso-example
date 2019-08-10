package com.okta.examples.sso;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DemoResourceServer  {

    public static void main(String[] args) {
        SpringApplication.run(DemoResourceServer.class, args);


    } 
    @GetMapping("/welecomeMessage")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public String getWelcomeMessage(Principal principal) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
        String fullName = jwtAuth.getToken().getClaimAsString("fullName");
        
        return "Welcome " + fullName + "!";
    }

    @GetMapping("/userEmail")
    @PreAuthorize("hasAuthority('SCOPE_email')")
    public String getUserEmail(Principal principal) {
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) principal;
        String email = jwtAuth.getToken().getClaimAsString("userEmail");
        return email;
    }


}
