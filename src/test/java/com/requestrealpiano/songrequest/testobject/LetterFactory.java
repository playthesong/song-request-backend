package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.song.Song;

import java.util.Arrays;
import java.util.List;

public class LetterFactory {

    public static Letter createOne() {
        return Letter.builder()
                     .songStory("Song story")
                     .requestStatus(RequestStatus.WAITING)
                     .song(SongFactory.createOne())
                     .account(AccountFactory.createMember())
                     .build();
    }

    public static List<Letter> createList() {
        Letter firstLetter = Letter.builder()
                                   .songStory("Song Story 1")
                                   .requestStatus(RequestStatus.WAITING)
                                   .build();
        Letter secondLetter = Letter.builder()
                                    .songStory("Song Story 2")
                                    .requestStatus(RequestStatus.DONE)
                                    .build();
        Letter thirdLetter = Letter.builder()
                                   .songStory("Song Story 3")
                                   .requestStatus(RequestStatus.PENDING)
                                   .build();
        return Arrays.asList(firstLetter, secondLetter, thirdLetter);
    }
}
