package com.example.vaultspringboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SecretsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecretsService.class);

    private static final String SECRET_URL_PATH = "/v1/secret/data/app-secrets";

    @Value("${vault.token}")
    private String vaultToken;
    
    @Autowired
    private RestTemplate restTemplate;

    public String getSecret(String secretKey) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-VAULT-TOKEN", vaultToken);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(SECRET_URL_PATH, HttpMethod.GET, request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode secret = root.path("data").path("data");

        LOGGER.info("{}", response);
        LOGGER.info("{}", secret.get(secretKey));
        return secret.get(secretKey).asText();
    }
    
}
