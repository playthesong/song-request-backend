package com.requestrealpiano.songrequest.domain.letter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long>  {

    List<Letter> findAllByRequestStatus(RequestStatus requestStatus);
}
