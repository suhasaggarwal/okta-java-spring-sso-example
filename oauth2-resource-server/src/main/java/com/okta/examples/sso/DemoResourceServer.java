package com.okta.examples.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DemoResourceServer  {

    public static void main(String[] args) {
        SpringApplication.run(DemoResourceServer.class, args);


    } 
    @GetMapping("/welecomeMessage")
    public String getWelcomeMessage(Principal principal) {
        return "Welcome!";
    }

    @GetMapping("/userProfile")
    @PreAuthorize("hasAuthority('SCOPE_profile')")
    public Map<String,String> getUserProfile(Principal principal) {
        Map<String,String> ret = new HashMap<String,String>();
        ret.put("TestName1", "TestValue1");
        ret.put("TestName2", "TestValue2");
        return ret;
    }

    @GetMapping("/userEmails")
    @PreAuthorize("hasAuthority('SCOPE_email')")
    public List<String> getUserEmails(Principal principal) {
        List<String> ret = new ArrayList<String>();
        ret.add("TestEmail1");
        ret.add("TestEmail2");
        return ret;
    }


}
