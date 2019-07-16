package com.okta.examples.sso;

import java.util.List;
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
    @Value("#{ @environment['spring.security.oauth2.resource.server'] }")
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

        String welcomeMessage = this.webClient.get().uri(this.resourceServerUrl + "/welecomeMessage").retrieve()
                .bodyToMono(String.class).block();

        Map<String, String> profile = this.webClient.get().uri(this.resourceServerUrl + "/userProfile").retrieve()
                .bodyToMono(Map.class).block();

        try {
            List<String> emails = this.webClient.get().uri(this.resourceServerUrl + "/userEmails").retrieve()
                    .bodyToMono(List.class).block();

            if (emails != null) {
                for (int i = 1; i <= emails.size(); i++) {
                    profile.put("Email " + i, emails.get(i - 1));
                }
            }
        } catch (Exception e) {
            mav.addObject("emailError", true);
        }

        mav.addObject("profile", profile);
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
