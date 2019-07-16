package com.okta.examples.sso;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableResourceServer
public class DemoResourceServer  {

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    static class OAuth2SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final CustomAccessDeniedHandler customAccessDeniedHandler;

        OAuth2SecurityConfigurerAdapter(CustomAccessDeniedHandler customAccessDeniedHandler) {
            this.customAccessDeniedHandler = customAccessDeniedHandler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .exceptionHandling()
                    .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/login", "/images/**", "/favicon.ico")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/");
        }
    }
    
    public static void main(String[] args) {
        SpringApplication.run(DemoResourceServer.class, args);


    } 
    @GetMapping("/welecomeMessage")
    public String getWelcomeMessage(Principal principal) {
        return "Welcome!";
    }
    @GetMapping("/userProfile")
    @PreAuthorize("#oauth2.hasScope('profile')")
    public Map<String,String> getUserProfile(Principal principal) {
        Map<String,String> ret = new HashMap<String,String>();
        ret.put("TestName1", "TestValue1");
        ret.put("TestName2", "TestValue2");
        return ret;
    }

    @GetMapping("/userEmails")
    @PreAuthorize("#oauth2.hasScope('email')")
    public List<String> getUserEmails(Principal principal) {
        List<String> ret = new ArrayList<String>();
        ret.add("TestEmail1");
        ret.add("TestEmail2");
        return ret;
    }


}
