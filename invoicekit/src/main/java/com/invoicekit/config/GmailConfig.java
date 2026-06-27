package com.invoicekit.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.gmail.Gmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GmailConfig {

    @Value("${gmail.client.id}")
    private String clientId;

    @Value("${gmail.client.secret}")
    private String clientSecret;

    @Value("${gmail.refresh.token}")
    private String refreshToken;

    @Bean
    public Gmail gmailService() {
        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(refreshToken);

        return new Gmail.Builder(
                credential.getTransport(),
                credential.getJsonFactory(),
                credential
        )
                .setApplicationName("InvoiceKit")
                .build();
    }
}