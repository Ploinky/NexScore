package de.ploinky.nexscore.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Player2Match")
public class Player2Match {
    private String puuid;
    private String matchId;

    public Player2Match() {
        // Empty constructor for DynamoDB
        this.puuid = "";
        this.matchId = "";
    }

    @DynamoDBHashKey(attributeName = "puuid")
    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    @DynamoDBAttribute(attributeName = "matchId")
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
