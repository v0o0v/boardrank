package net.boardrank.boardgame.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.NoArgsConstructor;
import net.boardrank.global.config.AWSConfig;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

//Dynamo DB Attribute Class에는 Lombok 사용불가

@NoArgsConstructor
@DynamoDBTable(tableName = "boardrank_matchStatus")
public class AccountMatchStatus {

    @Id
    private AccountMatchStatusKey id;

    private Long accountId;

    private LocalDateTime createdAt;

    private Long gameMatchId;

    private Long boardgameId;

    private String attribute;

    private String value;

    public AccountMatchStatus(Long accountId, Long gameMatchId, Long boardgameId, String attribute, String value) {
        this.createdAt = LocalDateTime.now();
        this.accountId = accountId;
        this.id = new AccountMatchStatusKey(accountId, this.createdAt);
        this.gameMatchId = gameMatchId;
        this.boardgameId = boardgameId;
        this.attribute = attribute;
        this.value = value;
    }

    @DynamoDBHashKey
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @DynamoDBRangeKey
    @DynamoDBTypeConverted(converter = AWSConfig.LocalDateTimeConverter.class)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @DynamoDBAttribute
    public Long getGameMatchId() {
        return gameMatchId;
    }

    public void setGameMatchId(Long gameMatchId) {
        this.gameMatchId = gameMatchId;
    }

    @DynamoDBAttribute
    public Long getBoardgameId() {
        return boardgameId;
    }

    public void setBoardgameId(Long boardgameId) {
        this.boardgameId = boardgameId;
    }

    @DynamoDBAttribute
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @DynamoDBAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
