package net.bgmgr.Boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bgmgr.account.domain.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class Game {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private BoardGame boardGame;

    @OneToOne
    private Paticiant paticiant;

    @OneToOne
    private Account createdMember;

    @OneToOne
    private ScoreBoard scoreBoard;

    @Enumerated(EnumType.ORDINAL)
    private GameStatus gameStatus;

    private LocalDateTime createdTime;
    private LocalDateTime finishedTime;
    private String gameTitle;
    private String chatId;

}
