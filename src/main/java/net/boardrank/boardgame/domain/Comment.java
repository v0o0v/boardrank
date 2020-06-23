package net.boardrank.boardgame.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.NoArgsConstructor;
import net.boardrank.global.config.AWSConfig;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@NoArgsConstructor
@DynamoDBTable(tableName = "boardrank_comment")
public class Comment {

    @Id
    private CommentKey id;

    private Long gameMatchId;

    private LocalDateTime createdAt;

    private String content;

    private Long accountId;

    private String accountName;

    public Comment(Long gameMatchId, Long accountId, String accountName, String content) {
        this.createdAt = LocalDateTime.now();
        this.id = new CommentKey(gameMatchId, this.createdAt);
        this.gameMatchId = gameMatchId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.content = content;
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

    @DynamoDBAttribute(attributeName = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @DynamoDBAttribute(attributeName = "accountId")
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @DynamoDBAttribute(attributeName = "accountName")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
