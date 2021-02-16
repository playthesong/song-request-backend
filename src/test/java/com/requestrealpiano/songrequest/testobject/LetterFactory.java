package com.requestrealpiano.songrequest.testobject;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.song.Song;

import java.util.Arrays;
import java.util.List;

public class LetterFactory {

    public static Letter createLetter() {
        return Letter.builder()
                     .songStory("Song story")
                     .requestStatus(RequestStatus.WAITING)
                     .song(SongFactory.createSong())
                     .account(AccountFactory.createMember())
                     .build();
    }

    public static List<Letter> createLetters() {
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

    public static List<Letter> createLettersOf(Account account, Song song) {
        Letter firstLetter = Letter.builder()
                                .songStory("Song Story 1")
                                .requestStatus(RequestStatus.WAITING)
                                .account(account)
                                .song(song)
                                .build();
        Letter secondLetter = Letter.builder()
                                    .songStory("Song Story 2")
                                    .requestStatus(RequestStatus.DONE)
                                    .account(account)
                                    .song(song)
                                    .build();
        Letter thirdLetter = Letter.builder()
                                   .songStory("Song Story 3")
                                   .requestStatus(RequestStatus.PENDING)
                                   .account(account)
                                   .song(song)
                                   .build();
        return Arrays.asList(firstLetter, secondLetter, thirdLetter);
    }
}
