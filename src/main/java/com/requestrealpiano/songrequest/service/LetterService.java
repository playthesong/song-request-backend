package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LetterResponse;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountNotFoundException;
import com.requestrealpiano.songrequest.global.error.exception.business.LetterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    @Transactional
    public LetterResponse createLetter(NewLetterRequest newLetterRequest) {
        SongRequest songRequest = newLetterRequest.getSongRequest();
        String songStory = newLetterRequest.getSongStory();
        Account account = accountRepository.findById(newLetterRequest.getAccountId()).orElseThrow(AccountNotFoundException::new);
        Song song = songService.findSongByRequest(songRequest);

        Letter newLetter = letterRepository.save(Letter.of(songStory, account, song));
        return LetterResponse.from(newLetter);
    }
}
