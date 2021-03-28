package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.PaginationParameters;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LettersResponse;
import com.requestrealpiano.songrequest.domain.letter.response.inner.LetterDetails;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountNotFoundException;
import com.requestrealpiano.songrequest.global.error.exception.business.LetterNotFoundException;
import com.requestrealpiano.songrequest.global.pagination.Pagination;
import com.requestrealpiano.songrequest.global.time.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.requestrealpiano.songrequest.global.constant.SortProperties.CREATED_DATE_TIME;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final AccountRepository accountRepository;
    private final SongService songService;
    private final Scheduler scheduler;

    public LettersResponse findAllLetters(PaginationParameters parameters) {
        PageRequest letterPage = Pagination.of(parameters.getPage(), parameters.getSize(), Direction.DESC, CREATED_DATE_TIME);
        LocalDateTime endDateTime = scheduler.now();
        LocalDateTime startDateTime = scheduler.defaultStartDateTimeFrom(endDateTime);
        Page<Letter> letters = letterRepository.findAllTodayLetters(letterPage, startDateTime, endDateTime);
        return LettersResponse.from(letters);
    }

    public LetterDetails findLetter(Long id) {
        Letter letter = letterRepository.findById(id).orElseThrow(LetterNotFoundException::new);
        return LetterDetails.from(letter);
    }

    @Transactional
    public LetterDetails createLetter(NewLetterRequest newLetterRequest) {
        SongRequest songRequest = newLetterRequest.getSongRequest();
        String songStory = newLetterRequest.getSongStory();
        Account account = accountRepository.findById(newLetterRequest.getAccountId()).orElseThrow(AccountNotFoundException::new);
        Song song = songService.updateRequestCountOrElseCreate(songRequest);

        Letter newLetter = letterRepository.save(Letter.of(songStory, account, song));
        return LetterDetails.from(newLetter);
    }

    public LettersResponse findLettersByStatus(RequestStatus requestStatus, PaginationParameters parameters) {
        PageRequest letterPage = Pagination.of(parameters.getPage(), parameters.getSize(), Direction.DESC, CREATED_DATE_TIME);
        LocalDateTime endDateTime = scheduler.now();
        LocalDateTime startDateTime = scheduler.defaultStartDateTimeFrom(endDateTime);
        Page<Letter> letters = letterRepository.findAllTodayLettersByRequestStatus(letterPage, requestStatus, startDateTime, endDateTime);
        return LettersResponse.from(letters);
    }
}
