package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.account.Account;
import com.requestrealpiano.songrequest.domain.account.AccountRepository;
import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.RequestStatus;
import com.requestrealpiano.songrequest.domain.letter.request.NewLetterRequest;
import com.requestrealpiano.songrequest.domain.letter.request.PaginationParameters;
import com.requestrealpiano.songrequest.domain.letter.request.inner.SongRequest;
import com.requestrealpiano.songrequest.domain.letter.response.LetterResponse;
import com.requestrealpiano.songrequest.domain.song.Song;
import com.requestrealpiano.songrequest.global.error.exception.business.AccountNotFoundException;
import com.requestrealpiano.songrequest.global.error.exception.business.LetterNotFoundException;
import com.requestrealpiano.songrequest.global.time.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public List<LetterResponse> findAllLetters(PaginationParameters parameters) {
        Sort sortByCreatedDateTime = Sort.by(Direction.DESC, CREATED_DATE_TIME.getFieldName());
        PageRequest pageRequest = PageRequest.of(parameters.getPage(), parameters.getSize(), sortByCreatedDateTime);
        LocalDateTime endDateTime = scheduler.now();
        LocalDateTime startDateTime = scheduler.defaultStartDateTimeFrom(endDateTime);
        Page<Letter> letters = letterRepository.findAllTodayLetters(pageRequest, startDateTime, endDateTime);
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
        Song song = songService.updateRequestCountOrElseCreate(songRequest);

        Letter newLetter = letterRepository.save(Letter.of(songStory, account, song));
        return LetterResponse.from(newLetter);
    }

    public List<LetterResponse> findLettersByStatus(RequestStatus requestStatus) {
        List<Letter> letters = letterRepository.findAllByRequestStatus(requestStatus);
        return letters.stream()
                      .map(LetterResponse::from)
                      .collect(Collectors.toList());
    }
}
