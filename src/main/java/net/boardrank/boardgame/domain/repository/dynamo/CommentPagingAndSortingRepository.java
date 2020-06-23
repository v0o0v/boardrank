package net.boardrank.boardgame.domain.repository.dynamo;

import net.boardrank.boardgame.domain.Comment;
import net.boardrank.boardgame.domain.CommentKey;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

@EnableScan
public interface CommentPagingAndSortingRepository extends PagingAndSortingRepository<Comment, CommentKey> {
    Page<Comment> findAllByGameMatchId(Integer gameMatchId, Pageable pageable);
}