package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.account.domain.Account;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToOne(fetch = FetchType.EAGER)
    private Account createdMember;

    @Enumerated(EnumType.ORDINAL)
    private GameMatchStatus gameMatchStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "gamematch_key")
    private Set<RankEntry> rankentries = new HashSet<>();

    private LocalDateTime createdTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private String matchTitle;
    private String place;

    public GameMatch(String name, Boardgame bg, Account createdMember) {
        this.boardGame = bg;
        this.createdMember = createdMember;
        this.gameMatchStatus = GameMatchStatus.init;
        this.createdTime = LocalDateTime.now();
        this.matchTitle = name;
        this.place = "";
    }

    public List<Account> getWinnerList() {
        List<Account> winnerList = new ArrayList<>();
        rankentries.forEach(rankEntry -> {
            if (rankEntry.getRank() != null && rankEntry.getRank().equals(1))
                winnerList.add(rankEntry.getAccount());
        });

        if (winnerList.isEmpty()) return null;
        return winnerList;
    }

    public String getWinnerByString() {
        List<Account> winnerList = this.getWinnerList();
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

        return "" + Duration.between(startedTime, finishedTime).abs().toMinutes();
    }

    @Override
    public String toString() {
        return "GameMatch{" +
                "id=" + id +
                ", matchTitle='" + matchTitle + '\'' +
                '}';
    }

    public void resetRank() {
        rankentries.forEach(rankEntry -> {
            rankEntry.setRank(getNumOfGreaterThanMe(rankEntry));
        });
    }

    private int getNumOfGreaterThanMe(RankEntry me) {
        return (int) (this.rankentries.stream()
                        .filter(rankEntry -> !rankEntry.equals(me))
                        .filter(rankEntry -> me.getScore() < rankEntry.getScore())
                        .count() + 1L);
    }
}
