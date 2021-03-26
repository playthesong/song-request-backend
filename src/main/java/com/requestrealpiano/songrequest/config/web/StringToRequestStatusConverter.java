package com.requestrealpiano.songrequest.config.web;

import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToRequestStatusConverter implements Converter<String, RequestStatus> {

    @Override
    public RequestStatus convert(String value) {
        return RequestStatus.valueOf(value.toUpperCase());
    }
}
