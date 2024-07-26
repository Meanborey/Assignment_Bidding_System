package com.example.assignment_bidding_system.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name = "authorization_consents")
@IdClass(AuthorizationConsent.AuthorizationConsentId.class)
public class AuthorizationConsent {

    @Id
    private String registeredClientId;

    @Id
    private String principalName;

    @Column(length = 1000)
    private String authorities;

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class AuthorizationConsentId implements Serializable {
        private String registeredClientId;
        private String principalName;
    }

}
