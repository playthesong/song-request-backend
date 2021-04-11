package live.playthesong.songrequest.global.error.exception.business;

import live.playthesong.songrequest.global.error.response.ErrorCode;

public class SongNotFoundException extends EntityNotFoundException {

    public SongNotFoundException() {
        super(ErrorCode.SONG_NOT_FOUND);
    }
}
