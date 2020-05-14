package net.boardrank.boardgame.service;

import net.boardrank.account.domain.Account;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.domain.NoticeType;
import net.boardrank.boardgame.domain.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoticeService {

    @Autowired
    NoticeRepository noticeRepository;

    @Transactional
    public void noticeToMakeFriend(Account fromAccount, Account toAccount) {
        Notice notice = new Notice(NoticeType.friendRequest);
        notice.setFrom(fromAccount);
        notice.setTo(toAccount);
        noticeRepository.save(notice);
    }

    public boolean isExistNotice(NoticeType noticeType, Account from, Account to) {
        Notice notice = this.noticeRepository.findByFromIsAndToIsAndNoticeTypeIs(from, to, noticeType);
        return notice == null ? false : true;
    }
}
