package com.requestrealpiano.songrequest.domain.song.searchapi.translator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;
import com.requestrealpiano.songrequest.global.error.exception.parsing.SearchResultParsingException;
import org.springframework.stereotype.Component;

@Component
public class XmlTranslator {

    public ManiaDbClientResponse mapToManiaDbData(String xml) {
        final XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return xmlMapper.readValue(xml, ManiaDbClientResponse.class);
        } catch (JsonProcessingException exception) {
            throw new SearchResultParsingException();
        }
    }
}
