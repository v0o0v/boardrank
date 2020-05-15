package net.boardrank.boardgame.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.domain.Notice;
import net.boardrank.boardgame.service.NoticeService;

import java.util.List;

@Route(layout = MainLayout.class)
public class ToDoListView extends VerticalLayout {

    AccountService accountService;

    NoticeService noticeService;


    public ToDoListView(AccountService accountService, NoticeService noticeService) {
        this.accountService = accountService;
        this.noticeService = noticeService;

        addClassName("list-view");
        setSizeFull();

        initComponent();
    }

    private void initComponent() {
        List<Notice> noticeList = noticeService.getNoticeListOfToAccount(accountService.getCurrentAccount());
        noticeList.stream().forEach(notice -> {
            switch (notice.getNoticeType()){
                case friendRequest:
                    add(new NoticeRequestFriendView(accountService, notice));
                    break;
                case matchAcceptRequest:
                    break;
                default:
            }
        });
    }

}