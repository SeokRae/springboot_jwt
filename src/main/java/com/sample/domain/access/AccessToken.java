package com.sample.domain.access;

import com.sample.domain.TimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class AccessToken extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String accessToken;

    @Builder
    public AccessToken(String userName, String accessToken) {
        this.userName = userName;
        this.accessToken = accessToken;
    }

    public void updateToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

