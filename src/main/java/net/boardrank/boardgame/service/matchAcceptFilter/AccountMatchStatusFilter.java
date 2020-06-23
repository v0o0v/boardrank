package net.boardrank.boardgame.service.matchAcceptFilter;

import net.boardrank.boardgame.domain.Account;
import net.boardrank.boardgame.domain.AccountMatchStatus;
import net.boardrank.boardgame.domain.GameMatch;
import net.boardrank.boardgame.service.AccountMatchStatusAttribute;
import net.boardrank.boardgame.service.GameMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//dynamo db access 효율을 위해 여러가지 속성을 한번에 처리
@Component
public class AccountMatchStatusFilter implements MatchAcceptFilter {


    @Autowired
    GameMatchService gameMatchService;

    @Value("${net.boardrank.point.win}")
    Integer winPoint;

    @Value("${net.boardrank.point.lose}")
    Integer losePoint;

    @Override
    public void handle(GameMatch gameMatch) {
        List<AccountMatchStatus> statusList = new ArrayList<>();

        //승패여부
        gameMatch.getRankentries().forEach(rankEntry -> {
            Account account = rankEntry.getAccount();
            if (gameMatch.getWinnerList().contains(rankEntry.getAccount())) {
                statusList.add(new AccountMatchStatus(
                        account.getId()
                        , gameMatch.getId()
                        , gameMatch.getBoardGame().getId()
                        , AccountMatchStatusAttribute.WinOrLose.name()
                        , "WIN"
                ));
            } else {
                statusList.add(new AccountMatchStatus(
                        account.getId()
                        , gameMatch.getId()
                        , gameMatch.getBoardGame().getId()
                        , AccountMatchStatusAttribute.WinOrLose.name()
                        , "LOSE"
                ));
            }
        });

        //플레이시간
        skipForDuplicate();
        gameMatch.getRankentries().forEach(rankEntry -> {
            Account account = rankEntry.getAccount();
            statusList.add(new AccountMatchStatus(
                    account.getId()
                    , gameMatch.getId()
                    , gameMatch.getBoardGame().getId()
                    , AccountMatchStatusAttribute.PlayTime.name()
                    , gameMatch.getPlayingTime()
            ));
        });

        //획득한 BP(모든 게임 입력)
        skipForDuplicate();
        gameMatch.getRankentries().forEach(rankEntry -> {
            Account account = rankEntry.getAccount();
            statusList.add(new AccountMatchStatus(
                    account.getId()
                    , gameMatch.getId()
                    , gameMatch.getBoardGame().getId()
                    , AccountMatchStatusAttribute.EarnBP.name()
                    , "" + gameMatch.calcBoardrankPoint(winPoint, losePoint, rankEntry)
            ));
        });

        //획득한 AP(모든 게임 입력)
        skipForDuplicate();
        gameMatch.getRankentries().forEach(rankEntry -> {
            Account account = rankEntry.getAccount();
            statusList.add(new AccountMatchStatus(
                    account.getId()
                    , gameMatch.getId()
                    , gameMatch.getBoardGame().getId()
                    , AccountMatchStatusAttribute.EarnAP.name()
                    , "" + gameMatch.calcAngelPoint(rankEntry)
            ));
        });


        gameMatchService.addAccountMatchStatusList(statusList);
    }

    public void skipForDuplicate(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
