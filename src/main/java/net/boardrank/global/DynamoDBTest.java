package net.boardrank.global;

import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.domain.repository.dynamo.CommentPagingAndSortingRepository;
import net.boardrank.boardgame.domain.repository.dynamo.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DynamoDBTest implements ApplicationRunner {


    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentPagingAndSortingRepository commentPagingAndSortingRepository;


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

    }
}
