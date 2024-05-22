package com.logistn.IdentityService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidToken {
    @Id
    private String accessTokenId;

    private String refreshTokenId;
    private String prevTokenId;
    private Date refreshTime;
}
