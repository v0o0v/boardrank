package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.account.domain.Account;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class Notice {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Account from;

    @ManyToOne
    private Account to;

    @ManyToOne
    private GameMatch gameMatch;

    @Enumerated(EnumType.ORDINAL)
    private NoticeType noticeType;

    private boolean isRead = false;

    public Notice(NoticeType noticeType) {
        this.noticeType = noticeType;
    }
}
