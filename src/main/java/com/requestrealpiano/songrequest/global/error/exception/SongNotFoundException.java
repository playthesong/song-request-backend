package com.requestrealpiano.songrequest.global.error.exception;

import com.requestrealpiano.songrequest.global.error.ErrorCode;

public class SongNotFoundException extends EntityNotFoundException {

    public SongNotFoundException() {
        super(ErrorCode.SONG_NOT_FOUND);
    }
}
