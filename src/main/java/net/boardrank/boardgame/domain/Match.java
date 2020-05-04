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
public class Match {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Boardgame boardGame;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Paticiant paticiant;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Account createdMember;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private ScoreBoard scoreBoard;

    @Enumerated(EnumType.ORDINAL)
    private MatchStatus matchStatus;
    private LocalDateTime createdTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private String matchTitle;
    private String chatId;

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
