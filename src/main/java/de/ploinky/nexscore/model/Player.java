package de.ploinky.nexscore.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Player")
public class Player {
    private String name;
    private String puuid;
    private String id;
    private String accountId;
    private String profileIconId;
    private String revisionDate;
    private String summonerLevel;

    public Player() {
        // Empty constructor for DynamoDB
        this.name = "";
        this.puuid = "";
        this.id = "";
        this.accountId = "";
        this.profileIconId = "";
        this.revisionDate = "";
        this.summonerLevel = "";
    }

    public Player(String name) {
        this.name = name;
        this.puuid = "";
        this.id = "";
        this.accountId = "";
        this.profileIconId = "";
        this.revisionDate = "";
        this.summonerLevel = "";
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBHashKey(attributeName = "puuid")
    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    @DynamoDBAttribute(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "accountId")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @DynamoDBAttribute(attributeName = "profileIconId")
    public String getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(String profileIconId) {
        this.profileIconId = profileIconId;
    }

    @DynamoDBAttribute(attributeName = "revisionDate")
    public String getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(String revisionDate) {
        this.revisionDate = revisionDate;
    }

    @DynamoDBAttribute(attributeName = "summonerLevel")
    public String getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(String summonerLevel) {
        this.summonerLevel = summonerLevel;
    }
}
