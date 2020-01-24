package com.okta.examples.sso;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SpringBootApplication
public class SingleSignOnApplication {

    private WebClient webClient;
    @Value("#{ @environment['resourceServer.url'] }")
    private String resourceServerUrl;

    public static void main(String[] args) {
        SpringApplication.run(SingleSignOnApplication.class, args);
    }

    public SingleSignOnApplication(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user.getUserInfo());
        Map<String,String> userBasicProfile = new HashMap<String,String>();
        userBasicProfile.put("First Name",user.getGivenName());
        userBasicProfile.put("Middle Initial",user.getMiddleName());
        userBasicProfile.put("Last Name",user.getFamilyName());
        userBasicProfile.put("Nick Name",user.getNickName());
        String welcomeMessage = this.webClient.get().uri(this.resourceServerUrl + "/welecomeMessage").retrieve()
                .bodyToMono(String.class).block();
        mav.addObject("welcomeMessage",welcomeMessage);
        
        try {
            String email = this.webClient.get().uri(this.resourceServerUrl + "/userEmail").retrieve()
                    .bodyToMono(String.class).block();

            if (email != null) {
                userBasicProfile.put("Email", email);
                
            }
        } catch (Exception e) {
            mav.addObject("emailError", true);
        }

        mav.addObject("profile", userBasicProfile);
        
        mav.setViewName("home");
        return mav;
    }

    @Configuration
    public static class OktaWebClientConfig {

        @Bean
        WebClient webClient(ClientRegistrationRepository clientRegistrations,
                OAuth2AuthorizedClientRepository authorizedClients) {
            ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                    clientRegistrations, authorizedClients);
            oauth2.setDefaultOAuth2AuthorizedClient(true);
            oauth2.setDefaultClientRegistrationId("okta");
            return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
        }
    }
}
