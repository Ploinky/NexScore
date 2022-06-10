package de.ploinky.nexscore.riot;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class RiotApiClientMatch {

    public RiotApiClientMatch() {}

    public static class MetadataDto {
        public String dataVersion;
        public String matchId;
        public List<String> participants;
    }

    public static class InfoDto {
        public long gameCreation;
        public long gameDuration;
        public long gameEndTimestamp;
        public long gameId;
        public String gameMode;
        public String gameName;
        public long gameStartTimestamp;
        public String gameType;
        public String gameVersion;
        public int mapId;
        public List<ParticipantDto> participants;
        public String platformId;
        public int queueId;
        public List<TeamDto> teams;
        public String tournamentCode;
    }

    public static class ParticipantDto {
        public int assists;
        public int baronKills;
        public int bountyLevel;
        public int champExperience;
        public int champLevel;
        public int championId;
        public String championName;
        public int championTransform;
        public int consumablesPurchased;
        public int damageDealtToBuildings;
        public int damageDealtToObjectives;
        public int damageDealtToTurrets;
        public int damageSelfMitigated;
        public int deaths;
        public int detectorWardsPlaced;
        public int doubleKills;
        public int dragonKills;
        public boolean firstBloodAssist;
        public boolean firstBloodKill;
        public boolean firstTowerAssist;
        public boolean firstTowerKill;
        public boolean gameEndedInEarlySurrender;
        public boolean gameEndedInSurrender;
        public int goldEarned;
        public int goldSpent;
        public String individualPosition;
        public int inhibitorKills;
        public int inhibitorTakedowns;
        public int inhibitorLost;
        public int item0;
        public int item1;
        public int item2;
        public int item3;
        public int item4;
        public int item5;
        public int item6;
        public int itemsPurchased;
        public int killingSprees;
        public int kills;
        public String lane;
        public int largestCriticalStrike;
        public int largestKillingSpree;
        public int largestMultiKill;
        public int longestTimeSpentLiving;
        public int magicDamageDealt;
        public int magicDamageDealtToChampions;
        public int magicDamageTaken;
        public int neutralMinionsKilled;
        public int nexusKills;
        public int nexusTakedowns;
        public int nexusLost;
        public int objectivesStolen;
        public int objectivesStolenAssist;
        public int participantId;
        public int pentaKills;
        public PerksDto perks;
        public int physicalDamageDealt;
        public int physicalDamageDealtToChampions;
        public int physicalDamageTaken;
        public int profileIcon;
        public String puuid;
        public int quadraKills;
        public String riotIdName;
        public String riotIdTagline;
        public String role;
        public int sightWardsBoughInGame;
        public int spell1Casts;
        public int spell2Casts;
        public int spell3Casts;
        public int spell4Casts;
        public int summoner1Casts;
        public int summoner1Id;
        public int summoner2Casts;
        public int summoner2Id;
        public String summonerId;
        public int summonerLevel;
        public String summonerName;
        public boolean teamEarlySurrendered;
        public int teamId;
        public String teamPosition;
        public int timeCCingOthers;
        public int timePlayed;
        public int totalDamageDealt;
        public int totalDamageDealtToChampions;
        public int totalDamageShieldedOnTeammates;
        public int totalDamageTaken;
        public int totalHeal;
        public int totalHealsOnTeammates;
        public int totalMinionsKilled;
        public int totalTimeCCDealt;
        public int totalTimeSpentDead;
        public int totalUnitsHealed;
        public int tripleKills;
        public int trueDamageDealt;
        public int trueDamageDealtToChampions;
        public int trueDamageTaken;
        public int turretKills;
        public int turretTakedowns;
        public int turretsLost;
        public int unrealKills;
        public int visionScore;
        public int visionWardsBoughtInGame;
        public int wardsKilled;
        public int wardsPlaced;
        public boolean win;
    }

    public static class PerksDto {
        public PerkStatsDto statPerks;
        public List<PerkStyleDto> styles;
    }

    public static class PerkStatsDto {
        public int defense;
        public int flex;
        public int offense;
    }

    public static class PerkStyleDto {
        public String description;
        public List<PerkStyleSelectionDto> selections;
        public int style;
    }

    public static class PerkStyleSelectionDto {
        public int perk;
        public int var1;
        public int var2;
        public int var3;
    }

    public static class TeamDto {
        public List<BanDto> bans;
        public ObjectivesDto objectives;
        public int teamId;
        public boolean win;
    }

    public static class BanDto {
        public int championId;
        public int pickTurn;
    }

    public static class ObjectivesDto {
        public ObjectiveDto baron;
        public ObjectiveDto champion;
        public ObjectiveDto dragon;
        public ObjectiveDto inhibitor;
        public ObjectiveDto riftHerald;
        public ObjectiveDto tower;
    }

    public static class ObjectiveDto {
        public boolean first;
        public int kills;
    }

    public MetadataDto metadata;

    public InfoDto info;
}
