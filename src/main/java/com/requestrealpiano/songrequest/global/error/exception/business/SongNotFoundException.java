package com.requestrealpiano.songrequest.global.error.exception.business;

import com.requestrealpiano.songrequest.global.error.response.ErrorCode;

public class SongNotFoundException extends EntityNotFoundException {

    public SongNotFoundException() {
        super(ErrorCode.SONG_NOT_FOUND);
    }
}
