package net.boardrank.boardgame.domain.repository;

import net.boardrank.account.domain.Account;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.domain.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    public Notice findByFromIsAndToIsAndNoticeTypeIs(Account from, Account to, NoticeType type);

}
