package live.playthesong.songrequest.domain.song.searchapi.translator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.playthesong.songrequest.domain.song.searchapi.lastfm.response.inner.LastFmTrack;
import live.playthesong.songrequest.domain.song.searchapi.response.SearchApiResponse;
import live.playthesong.songrequest.global.error.exception.parsing.SearchResultParsingException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JsonTranslator {

    public SearchApiResponse mapToLastFmResponse(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode dataNode = rootNode.get("results");
            List<LastFmTrack> tracks = extractTracks(dataNode, objectMapper);
            return SearchApiResponse.from(tracks);
        } catch (JsonProcessingException exception) {
            throw new SearchResultParsingException();
        }
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
