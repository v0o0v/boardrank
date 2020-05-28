package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.service.NoticeService;

import java.util.List;

@Route(layout = MainLayout.class)
public class ToDoListView extends VerticalLayout {

    AccountService accountService;

    NoticeService noticeService;

    GameMatchService gameMatchService;


    public ToDoListView(AccountService accountService
            , NoticeService noticeService
            , GameMatchService gameMatchService
    ) {
        this.accountService = accountService;
        this.noticeService = noticeService;
        this.gameMatchService = gameMatchService;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        initComponent();
    }

    private void initComponent() {
        List<Notice> noticeList = noticeService.getNoticeListOfToAccount(accountService.getCurrentAccount());
        noticeList.stream().forEach(notice -> {
            switch (notice.getNoticeType()) {
                case friendRequest:
                    add(new NoticeRequestFriendView(accountService, notice));
                    break;
                case matchAcceptRequest:
                    add(new NoticeAcceptMatchResultView(gameMatchService, notice, accountService.getCurrentAccount()));
                    break;
                default:
            }
        });
    }

}