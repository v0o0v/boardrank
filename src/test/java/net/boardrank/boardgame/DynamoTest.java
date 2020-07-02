package net.boardrank.boardgame;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import net.boardrank.IntegrationTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class DynamoTest extends IntegrationTest {

    @Autowired
    private AmazonDynamoDB dynamoDB;

    @Test
    public void test_createTable() throws Exception {
        //given
        String tableName = UUID.randomUUID().toString();
        String hashKeyName = UUID.randomUUID().toString();

        //when
        CreateTableResult res = createDynamoTable(tableName, hashKeyName);

        //then
        TableDescription tableDesc = res.getTableDescription();
        assertThat(tableDesc.getTableName()).isEqualTo(tableName);
        assertThat(tableDesc.getKeySchema().toString()).isEqualTo("[{AttributeName: " + hashKeyName + ",KeyType: HASH}]");
        assertThat(tableDesc.getAttributeDefinitions().toString()).isEqualTo("[{AttributeName: " + hashKeyName + ",AttributeType: S}]");
        assertThat(tableDesc.getProvisionedThroughput().getReadCapacityUnits()).isEqualTo(1000L);
        assertThat(tableDesc.getProvisionedThroughput().getWriteCapacityUnits()).isEqualTo(1000L);
        assertThat(dynamoDB.listTables().getTableNames()).hasSizeGreaterThanOrEqualTo(1);

//        deleteAllTables();
    }

    public void deleteAllTables(){
        List<String> tableNames = dynamoDB.listTables().getTableNames();
        tableNames.forEach(tableName -> {
            dynamoDB.deleteTable(tableName);
        });
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
