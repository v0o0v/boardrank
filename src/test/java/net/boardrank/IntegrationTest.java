package net.boardrank;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.boardrank.boardgame.domain.repository.jpa.AccountRepository;
import net.boardrank.boardgame.domain.repository.jpa.BoardgameRepository;
import net.boardrank.boardgame.domain.repository.jpa.GameMatchRepository;
import net.boardrank.boardgame.domain.repository.jpa.NoticeRepository;
import net.boardrank.boardgame.service.AccountService;
import net.boardrank.boardgame.service.BoardgameService;
import net.boardrank.boardgame.service.GameMatchService;
import net.boardrank.boardgame.service.NoticeService;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Ignore
public class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AmazonDynamoDB dynamoDB;

    @Autowired
    protected GameMatchService gameMatchService;

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected BoardgameService boardgameService;

    @Autowired
    protected BoardgameRepository boardgameRepository;

    @Autowired
    protected GameMatchRepository gameMatchRepository;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected NoticeService noticeService;

    @Autowired
    protected NoticeRepository noticeRepository;


    protected void initOAuth2Accout(String email) {
        Map<String, Object> attr = new HashMap<>();
        attr.put("email", email);
        attr.put("groups", "ROLE_USER");
        attr.put("sub", 123);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        OidcIdToken oidcIdToken = new OidcIdToken("tokenValue1", null, null, attr);
        OAuth2User principal = new DefaultOidcUser(Arrays.asList(authority), oidcIdToken);
        OAuth2AuthenticationToken oAuth2AuthenticationToken
                = new OAuth2AuthenticationToken(principal, Arrays.asList(authority), "google");

        SecurityContextHolder.getContext().setAuthentication(oAuth2AuthenticationToken);
    }

    public CreateTableResult createDynamoTable(String tableName, String hashKeyName) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition(hashKeyName, ScalarAttributeType.S));

        List<KeySchemaElement> ks = new ArrayList<>();
        ks.add(new KeySchemaElement(hashKeyName, KeyType.HASH));

        ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput(1000L, 1000L);

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withAttributeDefinitions(attributeDefinitions)
                .withKeySchema(ks)
                .withProvisionedThroughput(provisionedthroughput);

        return dynamoDB.createTable(request);
    }


}
