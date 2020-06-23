package net.boardrank.boardgame.domain.repository.dynamo;

import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.domain.CommentKey;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface CommentRepository extends CrudRepository<Comment, CommentKey> {

    List<Comment> findAllByGameMatchIdOrderByCreatedAtAsc(Long gameMatchId);

    List<Comment> findAllByGameMatchIdOrderByCreatedAtDesc(Long gameMatchId);

}