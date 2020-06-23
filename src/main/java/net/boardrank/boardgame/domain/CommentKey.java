package net.boardrank.boardgame.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import net.boardrank.global.config.AWSConfig;

import java.time.LocalDateTime;

public class CommentKey {

    private Long gameMatchId;

    private LocalDateTime createdAt;

    public CommentKey(Long gameMatchId, LocalDateTime createdAt) {
        this.gameMatchId = gameMatchId;
        this.createdAt = createdAt;
    }

    @DynamoDBHashKey(attributeName = "gameMatchId")
    public Long getGameMatchId() {
        return gameMatchId;
    }

    public void setGameMatchId(Long gameMatchId) {
        this.gameMatchId = gameMatchId;
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
