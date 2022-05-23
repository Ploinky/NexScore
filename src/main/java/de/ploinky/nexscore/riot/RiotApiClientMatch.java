package de.ploinky.nexscore.riot;

import java.util.List;

public class RiotApiClientMatch {
    public class MetaData {
        public String dataVersion;
        public String matchId;
        public List<String> participants;
    }

    private MetaData metadata;

    public MetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData metaData) {
        this.metadata = metaData;
    }
}
