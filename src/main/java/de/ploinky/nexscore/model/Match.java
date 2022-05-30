package de.ploinky.nexscore.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Match")
public class Match  {
    private String matchId;

    public Match() {
        // Empty constructor for DynamoDB
        this.matchId = "";
    }

    public Match(String matchId) {
        this.matchId = matchId;
    }

    @DynamoDBAttribute(attributeName = "matchId")
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
