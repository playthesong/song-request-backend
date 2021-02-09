package com.requestrealpiano.songrequest.domain.song.searchapi.translator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.requestrealpiano.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import com.requestrealpiano.songrequest.domain.song.searchapi.response.SearchApiResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JsonTranslator {

    // TODO: JsonProcessingException 처리
    public SearchApiResponse mapToLastFmResponse(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode dataNode = rootNode.get("results");
        List<LastFmTrack> tracks = extractTracks(dataNode, objectMapper);
        return SearchApiResponse.from(tracks);
    }

    // 프론트 구현 이후 사용 여부 결정
    private int extractTotalCount(JsonNode dataNode) {
        JsonNode totalCountNode = dataNode.get("opensearch:totalResults");
        return totalCountNode.asInt();
    }

    private List<LastFmTrack> extractTracks(JsonNode dataNode, ObjectMapper objectMapper) {
        JsonNode trackDataNode = dataNode.get("trackmatches");
        JsonNode tracksNode = trackDataNode.get("track");
        List<LastFmTrack> tracks = new ArrayList<>();
        for (JsonNode trackNode : tracksNode) {
            LastFmTrack track = objectMapper.convertValue(trackNode, LastFmTrack.class);
            tracks.add(track);
        }
        return tracks;
    }
}
