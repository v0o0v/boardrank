package net.boardrank.Boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.account.domain.Account;

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
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private BoardGame boardGame;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Paticiant paticiant;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Account createdMember;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private ScoreBoard scoreBoard;

    @Enumerated(EnumType.ORDINAL)
    private GameStatus gameStatus;
    private LocalDateTime createdTime;
    private LocalDateTime finishedTime;
    private String gameTitle;
    private String chatId;

}
