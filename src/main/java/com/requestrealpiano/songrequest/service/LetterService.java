package com.requestrealpiano.songrequest.service;

import com.requestrealpiano.songrequest.domain.letter.Letter;
import com.requestrealpiano.songrequest.domain.letter.LetterRepository;
import com.requestrealpiano.songrequest.domain.letter.dto.response.LetterResponse;
import com.requestrealpiano.songrequest.global.error.LetterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LetterService {

    private final LetterRepository letterRepository;

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
}
