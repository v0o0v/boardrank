package net.boardrank.boardgame.domain.repository.dynamo;

import net.boardrank.boardgame.domain.Comment;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findAllByGameMatchIdOrderByCreatedAtAsc(Integer gameMatchId);
}