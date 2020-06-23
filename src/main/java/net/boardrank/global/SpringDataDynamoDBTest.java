package net.boardrank.global;

import net.boardrank.boardgame.domain.AccountMatchStatus;
import net.boardrank.boardgame.domain.repository.dynamo.AccountMatchStatusRepository;
import net.boardrank.boardgame.domain.repository.dynamo.CommentPagingAndSortingRepository;
import net.boardrank.boardgame.domain.repository.dynamo.CommentRepository;
import net.boardrank.boardgame.service.AccountMatchStatusAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("dev")
@Component
public class SpringDataDynamoDBTest implements ApplicationRunner {


    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentPagingAndSortingRepository commentPagingAndSortingRepository;

    @Autowired
    AccountMatchStatusRepository accountMatchStatusRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {

//        ================================================================================

//        AccountMatchStatus status = new AccountMatchStatus(1L, 2L, 3L, AccountMatchStatusAttribute.WinOrLose.name(), "aa");
//        AccountMatchStatus status2 = new AccountMatchStatus(1L, 2L, 3L, AccountMatchStatusAttribute.WinOrLose.name(), "bb");
//
//        this.accountMatchStatusRepository.save(status);
//        this.accountMatchStatusRepository.save(status2);
//
//        List<AccountMatchStatus> all = this.accountMatchStatusRepository.findAllByAccountIdOrderByCreatedAtAsc(1L);

//        ================================================================================

//        Comment comment = new Comment(1L, 2L, "vvv", "content1");
//        commentRepository.save(comment);
//
//        Comment comment2 = new Comment(1L, 2L, "vvv", "content1");
//        commentRepository.save(comment2);
//
//        Comment comment3 = new Comment(1L, 2L, "vvv", "content1");
//        commentRepository.save(comment3);
//
//
//        List<Comment> allByGameMatchIdOrderByCreatedAtAsc = commentRepository.findAllByGameMatchIdOrderByCreatedAtAsc(1L);
//
//        List<Comment> allByGameMatchIdOrderByCreatedAtDesc = commentRepository.findAllByGameMatchIdOrderByCreatedAtDesc(1L);
//
//        commentRepository.delete(comment);
//
//        commentRepository.deleteAll(Arrays.asList(comment2, comment3));
    }
}
