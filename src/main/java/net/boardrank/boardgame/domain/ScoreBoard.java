package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.account.domain.Account;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class ScoreBoard {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "scoreboard_key")
    private Set<RankEntry> rankEntrySet;

    public List<Account> getWinnerList() {
        if(rankEntrySet == null || rankEntrySet.isEmpty())
            return null;

        List<Account> winnerList = new ArrayList<>();
        rankEntrySet.forEach(rankEntry -> {
            if(rankEntry.getRank().equals(1))
                winnerList.add(rankEntry.getAccount());
        });

        if(winnerList.isEmpty()) return null;
        return winnerList;
    }
}

