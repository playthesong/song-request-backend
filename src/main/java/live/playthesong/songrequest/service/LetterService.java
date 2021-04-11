package live.playthesong.songrequest.service;

import live.playthesong.songrequest.domain.account.Account;
import live.playthesong.songrequest.domain.account.AccountRepository;
import live.playthesong.songrequest.domain.letter.Letter;
import live.playthesong.songrequest.domain.letter.LetterRepository;
import live.playthesong.songrequest.domain.letter.RequestStatus;
import live.playthesong.songrequest.domain.letter.request.DateParameters;
import live.playthesong.songrequest.domain.letter.request.LetterRequest;
import live.playthesong.songrequest.domain.letter.request.PaginationParameters;
import live.playthesong.songrequest.domain.letter.request.StatusChangeRequest;
import live.playthesong.songrequest.domain.letter.request.inner.SongRequest;
import live.playthesong.songrequest.domain.letter.response.LettersResponse;
import live.playthesong.songrequest.domain.letter.response.inner.LetterDetails;
import live.playthesong.songrequest.domain.song.Song;
import live.playthesong.songrequest.global.admin.Admin;
import live.playthesong.songrequest.global.constant.SortProperties;
import live.playthesong.songrequest.global.error.exception.business.*;
import live.playthesong.songrequest.global.pagination.Pagination;
import live.playthesong.songrequest.global.time.Scheduler;
import live.playthesong.songrequest.security.oauth.OAuthAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final AccountRepository accountRepository;
    private final SongService songService;
    private final Scheduler scheduler;
    private final Admin admin;

    public LettersResponse findAllLetters(PaginationParameters parameters) {
        PageRequest letterPage = Pagination.of(parameters.getPage(), parameters.getSize(), parameters.getDirection(), SortProperties.CREATED_DATE_TIME);
        LocalDateTime endDateTime = scheduler.now();
        LocalDateTime startDateTime = scheduler.defaultStartDateTimeFrom(endDateTime);
        Page<Letter> letters = letterRepository.findAllTodayLetters(letterPage, startDateTime, endDateTime);
        return LettersResponse.from(letters, admin.isReadyToLetter());
    }

    public LetterDetails findLetter(Long id) {
        Letter letter = letterRepository.findById(id).orElseThrow(LetterNotFoundException::new);
        return LetterDetails.from(letter);
    }

    public LettersResponse findLettersByStatus(RequestStatus requestStatus, PaginationParameters paginationParams,
                                               DateParameters dateParams) {
        PageRequest letterPage = Pagination.of(paginationParams.getPage(), paginationParams.getSize(),
                                               paginationParams.getDirection(), SortProperties.CREATED_DATE_TIME);
        LocalDateTime endDateTime = scheduler.now();
        LocalDateTime startDateTime = scheduler.customStartDateTimeFrom(endDateTime, dateParams.getDayAgo());
        Page<Letter> letters = letterRepository.findAllTodayLettersByRequestStatus(letterPage, requestStatus, startDateTime, endDateTime);
        return LettersResponse.from(letters, admin.isReadyToLetter());
    }

    @Transactional
    public LetterDetails createLetter(OAuthAccount loginAccount, LetterRequest letterRequest) {
        if (admin.isNotReadyToLetter()) {
            throw new LetterNotReadyException();
        }

        SongRequest songRequest = letterRequest.getSongRequest();
        String songStory = letterRequest.getSongStory();
        Account account = accountRepository.findById(loginAccount.getId()).orElseThrow(AccountNotFoundException::new);
        Account updatedAccount = account.increaseRequestCount();
        Song song = songService.updateRequestCountOrElseCreate(songRequest);

        Letter newLetter = letterRepository.save(Letter.of(songStory, updatedAccount, song));
        return LetterDetails.from(newLetter);
    }

    @Transactional
    public LetterDetails updateLetter(OAuthAccount loginAccount, Long letterId, LetterRequest letterRequest) {
        Letter letter = letterRepository.findById(letterId).orElseThrow(LetterNotFoundException::new);
        if (letter.hasDifferentAccount(loginAccount)) {
            throw new AccountMismatchException();
        }

        if (letter.hasSameSong(letterRequest.getSongRequest())) {
            Letter updatedLetter = letter.changeSongStory(letterRequest.getSongStory());
            return LetterDetails.from(updatedLetter);
        }

        Song song = songService.updateRequestCountOrElseCreate(letterRequest.getSongRequest());
        Letter updatedLetter = letter.update(letterRequest, song);
        return LetterDetails.from(updatedLetter);
    }

    @Transactional
    public LetterDetails changeStatus(Long letterId, StatusChangeRequest statusChangeRequest) {
        Letter letter = letterRepository.findById(letterId).orElseThrow(LetterNotFoundException::new);
        if (statusChangeRequest.getRequestStatus() == null) {
            throw new LetterStatusException();
        }
        Letter updatedLetter = letter.changeStatus(statusChangeRequest);
        return LetterDetails.from(updatedLetter);
    }

    @Transactional
    public void deleteLetter(OAuthAccount loginAccount, Long letterId) {
        Letter letter = letterRepository.findById(letterId).orElseThrow(LetterNotFoundException::new);
        if (letter.hasDifferentAccount(loginAccount) && loginAccount.isNotAdmin()) {
            throw new AccountMismatchException();
        }
        letterRepository.delete(letter);
    }

    @Transactional
    public void initializeTodayLetters() {
        LocalDateTime endDateTime = scheduler.now();
        LocalDateTime startDateTime = scheduler.initializationStartDateTimeFrom(endDateTime);
        letterRepository.deleteAllTodayLetters(startDateTime, endDateTime);
    }
}
