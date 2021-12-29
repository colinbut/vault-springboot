package com.example.vaultspringboot.configuration;

import com.example.vaultspringboot.constants.DemoMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateConfig.class);

    @Value("${demo.mode}")
    private String demoMode;

    @Value("${vault.addr:http://localhost:8200}")
    private String vaultAddr;
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        LOGGER.info("Demo Mode is: {}", demoMode);
        RestTemplate restTemplate = restTemplateBuilder.build();
        if (demoMode.equals(DemoMode.ENV_VAR.getDemoModeConf())){
            LOGGER.info("Configuring application via Environment Variables");
            restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(System.getenv("VAULT_ADDR")));
        } else if (demoMode.equals(DemoMode.APP_CONFIG.getDemoModeConf())) {
            LOGGER.info("Configuring application via Application Configs");
            restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(vaultAddr));
        }
        return restTemplate;
    }
}
