package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.account.domain.Account;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class GameMatch {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private Boardgame boardGame;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Paticiant paticiant;

    @OneToOne(fetch = FetchType.EAGER)
    private Account createdMember;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private ScoreBoard scoreBoard;

    @Enumerated(EnumType.ORDINAL)
    private GameMatchStatus gameMatchStatus;

    private LocalDateTime createdTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private String matchTitle;
    private String chatId;

    public GameMatch(String name, Boardgame bg, Paticiant paticiant, Account createdMember) {
        this.boardGame = bg;
        this.paticiant = paticiant;
        this.createdMember = createdMember;
        this.gameMatchStatus = GameMatchStatus.init;
        this.createdTime = LocalDateTime.now();
        this.matchTitle = name;
    }

    public String getWinnerByString() {
        if (this.scoreBoard == null)
            return "";

        List<Account> winnerList = this.scoreBoard.getWinnerList();
        if (winnerList == null)
            return "";

        StringBuilder winnerString = new StringBuilder();
        for (Account account : winnerList)
            winnerString.append(account.getName() + " ");

        return winnerString.toString().trim();
    }

    public String getPlayingTime() {
        if (startedTime == null || finishedTime == null)
            return "";

        return ""+Duration.between(startedTime,finishedTime).abs().toMinutes();
    }
}
