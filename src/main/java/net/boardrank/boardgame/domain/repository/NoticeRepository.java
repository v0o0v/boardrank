package net.boardrank.boardgame.domain.repository;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.domain.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    public Notice findByFromIsAndToIsAndNoticeTypeIs(Account from, Account to, NoticeType type);

    public List<Notice> findAllByToIsOrderByCreatedTimeDesc(Account to);

}
