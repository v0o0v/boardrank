package net.boardrank.boardgame.domain.repository.dynamo;

import net.boardrank.boardgame.domain.AccountMatchStatus;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface AccountMatchStatusRepository extends CrudRepository<AccountMatchStatus, String> {

    List<AccountMatchStatus> findAllByAccountIdOrderByCreatedAtAsc(Long AccountId);

}