package net.boardrank.global;

import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.domain.repository.dynamo.AccountMatchStatusRepository;
import net.boardrank.boardgame.domain.repository.dynamo.CommentRepository;
import net.boardrank.boardgame.domain.repository.dynamo.CommentPagingAndSortingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

//        Comment comment = new Comment();
//        comment.setCreatedAt(LocalDateTime.now());
//        comment.setContent("으헤헤헤");
//        comment.setGameMatchId(1);
//        commentRepository.save(comment);
//
//        Comment comment2 = new Comment();
//        comment2.setCreatedAt(LocalDateTime.now());
//        comment2.setContent("으헤헤헤");
//        comment2.setGameMatchId(1);
//        commentRepository.save(comment2);

//        List<Comment> allByGameMatchIdOrderByCreatedAt = commentRepository.findAllByGameMatchIdOrderByCreatedAtAsc(1);
//
//        Page<Comment> createdAt = commentPagingAndSortingRepository
//                .findAllByGameMatchId(1
//                        , PageRequest.of(1, 3, Sort.by("createdAt").ascending()));
//
//
//        System.out.println();


//        ================================================================================

//        AccountMatchStatus status = new AccountMatchStatus(1L, "v0o0v", 2L, AccountMatchStatusAttribute.WinOrLose.name(), "1");
//        AccountMatchStatus status2 = new AccountMatchStatus(10L, "v0o0v", 2L, AccountMatchStatusAttribute.WinOrLose.name(), "1");
//
//        this.accountMatchStatusRepository.save(status);
//        this.accountMatchStatusRepository.save(status2);
//
//        List<AccountMatchStatus> all = this.accountMatchStatusRepository.findAllByAccountIdOrderByCreatedAtAsc(1L);

//        ================================================================================

        Comment comment = new Comment(1L, 2L, "vvv", "content1");
        commentRepository.save(comment);

        Comment comment2 = new Comment(1L, 2L, "vvv", "content1");
        commentRepository.save(comment2);

        Comment comment3 = new Comment(1L, 2L, "vvv", "content1");
        commentRepository.save(comment3);


        List<Comment> allByGameMatchIdOrderByCreatedAtAsc = commentRepository.findAllByGameMatchIdOrderByCreatedAtAsc(1L);

        List<Comment> allByGameMatchIdOrderByCreatedAtDesc = commentRepository.findAllByGameMatchIdOrderByCreatedAtDesc(1L);

        commentRepository.delete(comment);

        commentRepository.deleteAll(Arrays.asList(comment2, comment3));
    }
}
