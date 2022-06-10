package de.ploinky.nexscore.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.List;

@DynamoDBTable(tableName = "Match")
public class Match  {
    private String matchId;
    private String puuidWinner;

    private List<Player> players;

    public Match() {
        // Empty constructor for DynamoDB
        this.matchId = "";
        this.puuidWinner = "";
    }

    public Match(String matchId) {
        this.matchId = matchId;
    }

    @DynamoDBHashKey(attributeName = "matchId")
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    @DynamoDBAttribute(attributeName = "puuid")
    public String getPuuidWinner() {
        return puuidWinner;
    }

    public void setPuuidWinner(String puuidWinner) {
        this.puuidWinner = puuidWinner;
    }
}
