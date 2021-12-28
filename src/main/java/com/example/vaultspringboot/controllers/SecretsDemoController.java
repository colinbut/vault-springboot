package com.example.vaultspringboot.controllers;

import com.example.vaultspringboot.service.SecretsService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretsDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecretsDemoController.class);

    private static final String HTML_SPACE = "<br />";
    @Autowired
    private SecretsService secretsService;

    @RequestMapping("/demo")
    public String demo() {
        StringBuilder response = new StringBuilder("");
        
        try {
            response.append("<h1>Vault Spring Boot Demo</h1>");
            response.append(HTML_SPACE);

            String apiKey = secretsService.getSecret("api_key");
            response.append("<p>Connecting to Custom API using API KEY: ").append(apiKey).append("</p>");
            response.append(HTML_SPACE);

            String tempPwd = secretsService.getSecret("temp_pwd");
            response.append("<p>Connecting to Backend Database with temporary password: ").append(tempPwd).append("</p>");
            response.append(HTML_SPACE);

            String genericSecret = secretsService.getSecret("generic_secret");
            response.append("<p>This is a generic secret: ").append(genericSecret).append("</p>");
            response.append(HTML_SPACE);

            return response.toString();
        } catch(JsonProcessingException ex){
            LOGGER.error("Server Error Processing returned JSON", ex);
            return null;
        } catch(Exception ex) {
            LOGGER.error("Server Error - Unable to Process", ex);
            return null;
        }
    }
    
}
