package net.boardrank.boardgame.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import net.boardrank.global.config.AWSConfig;

import java.time.LocalDateTime;

public class AccountMatchStatusKey {

    private Long accountId;

    private LocalDateTime createdAt;

    public AccountMatchStatusKey(Long accountId, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.createdAt = createdAt;
    }

    @DynamoDBHashKey(attributeName = "accountId")
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @DynamoDBRangeKey(attributeName = "createdAt")
    @DynamoDBTypeConverted(converter = AWSConfig.LocalDateTimeConverter.class)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
