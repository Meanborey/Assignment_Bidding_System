package com.example.assignment_bidding_system.init;

import com.example.assignment_bidding_system.security.JpaRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2DataInitialization {

    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {

        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofDays(1))
                .reuseRefreshTokens(true)
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(true)
                .requireAuthorizationConsent(true)
                .build();

        var registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("kangchi")
                .clientSecret(passwordEncoder.encode("12345")) // store in secret manager
                .clientIdIssuedAt(Instant.now())
                .clientSecretExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS))
                .scopes(scopes -> {
                    scopes.add(OidcScopes.OPENID);
                    scopes.add(OidcScopes.PROFILE);
                    scopes.add(OidcScopes.EMAIL);
                    scopes.add(OidcScopes.PHONE);
                    scopes.add(OidcScopes.ADDRESS);
                })
                .redirectUris(uris -> {
                    uris.add("http://localhost:9000/login/oauth2/code/kangchi");
                })
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();

        jpaRegisteredClientRepository.save(registeredClient);

    }

}
