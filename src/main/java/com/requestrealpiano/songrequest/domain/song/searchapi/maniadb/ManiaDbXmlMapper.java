package com.requestrealpiano.songrequest.domain.song.searchapi.maniadb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.requestrealpiano.songrequest.domain.song.searchapi.maniadb.response.ManiaDbClientResponse;

public class ManiaDbXmlMapper {

    // TODO: JsonProcessingException 처리
    public static ManiaDbClientResponse mapXmlToData(String xml) throws JsonProcessingException {
        final XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return xmlMapper.readValue(xml, ManiaDbClientResponse.class);
    }
}
