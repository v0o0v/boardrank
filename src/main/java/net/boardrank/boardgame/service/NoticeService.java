package net.boardrank.boardgame.service;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.domain.NoticeType;
import net.boardrank.boardgame.domain.repository.jpa.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    @Transactional
    public void noticeToAcceptMatch(GameMatch gameMatch, Account fromAccount, Account toAccount) {
        Notice notice = new Notice();
        notice.setFrom(fromAccount);
        notice.setTo(toAccount);
        notice.setGameMatch(gameMatch);
        notice.setNoticeType(NoticeType.matchAcceptRequest);
        noticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public boolean isExistNotice(NoticeType noticeType, Account from, Account to) {
        Notice notice = this.noticeRepository.findByFromIsAndToIsAndNoticeTypeIs(from, to, noticeType);
        return notice == null ? false : true;
    }

    @Transactional(readOnly = true)
    public List<Notice> getNoticeListOfToAccount(Account currentAccount) {
        return this.noticeRepository.findAllByToIsOrderByCreatedTimeDesc(currentAccount);
    }

    @Transactional
    public void finish(Notice notice) {
        noticeRepository.delete(notice);
    }

    @Transactional
    public void finishAllOnMatchAccepted(GameMatch gameMatch, NoticeType noticeType){
        Set<Notice> notices = this.noticeRepository.findAllByGameMatchIsAndNoticeTypeIs(gameMatch, noticeType);
        notices.stream().forEach(notice -> noticeRepository.delete(notice));
    }

    @Transactional
    public void removeNoticeOf(GameMatch gameMatch) {
        this.noticeRepository.deleteAllByGameMatch(gameMatch);
    }
}
