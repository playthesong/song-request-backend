package com.requestrealpiano.songrequest.domain.letter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long>  {

    @Override
    Page<Letter> findAll(Pageable pageable);

    List<Letter> findAllByRequestStatus(RequestStatus requestStatus);
}
