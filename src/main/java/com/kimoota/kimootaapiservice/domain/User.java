package com.kimoota.kimootaapiservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @Column(nullable = false)
    private Boolean enabled = true;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    private String imageUrl;

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public User toEntity() {
        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .imageUrl(imageUrl)
                .providerId(providerId)
                .provider(provider)
                .build();
    }
}
