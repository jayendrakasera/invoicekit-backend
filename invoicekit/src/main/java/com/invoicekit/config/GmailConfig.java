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
    public Gmail gmailService() throws Exception {

        GoogleCredential credential =
                new GoogleCredential.Builder()
                        .setTransport(
                                com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport()
                        )
                        .setJsonFactory(
                                com.google.api.client.json.gson.GsonFactory.getDefaultInstance()
                        )
                        .setClientSecrets(clientId, clientSecret)
                        .build();

        credential.setRefreshToken(refreshToken);
        credential.refreshToken();

        return new Gmail.Builder(
                com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
                com.google.api.client.json.gson.GsonFactory.getDefaultInstance(),
                credential
        )
                .setApplicationName("InvoiceKit")
                .build();
    }
}