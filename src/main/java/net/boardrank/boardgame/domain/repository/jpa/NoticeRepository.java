package net.boardrank.boardgame.domain.repository.jpa;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.domain.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    public Notice findByFromIsAndToIsAndNoticeTypeIs(Account from, Account to, NoticeType type);

    public List<Notice> findAllByToIsOrderByCreatedTimeDesc(Account to);

    public Set<Notice> findAllByGameMatchIsAndNoticeTypeIs(GameMatch gameMatch, NoticeType type);

}
