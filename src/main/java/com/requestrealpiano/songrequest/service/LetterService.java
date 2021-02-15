package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.dto.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.global.error.AccountNotFoundException;
import com.requestrealpiano.songrequest.global.error.LetterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final AccountRepository accountRepository;
    private final SongService songService;

    public List<LetterResponse> findAllLetters() {
        List<Letter> letters = letterRepository.findAll();
        return letters.stream()
                      .map(LetterResponse::from)
                      .collect(Collectors.toList());
    }

    public LetterResponse findLetter(Long id) {
        Letter letter = letterRepository.findById(id).orElseThrow(LetterNotFoundException::new);
        return LetterResponse.from(letter);
    }

    public LetterResponse createNewLetter(NewLetterRequest newLetterRequest) {
        SongRequest songRequest = newLetterRequest.getSongRequest();
        String songStory = newLetterRequest.getSongStory();
        Account account = accountRepository.findById(newLetterRequest.getAccountId()).orElseThrow(AccountNotFoundException::new);
        Song song = songService.findSongByRequest(songRequest);

        Letter newLetter = letterRepository.save(Letter.of(songStory, account, song));
        return LetterResponse.from(newLetter);
    }
}
