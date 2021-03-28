package com.requestrealpiano.songrequest.domain.letter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LetterRepository extends JpaRepository<Letter, Long>  {

    @Query("SELECT l FROM Letter l WHERE l.createdDateTime BETWEEN :startDateTime AND :endDateTime")
    Page<Letter> findAllTodayLetters(Pageable pageable, @Param("startDateTime") LocalDateTime startDateTime,
                                                        @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT l FROM Letter l WHERE l.requestStatus = :requestStatus " +
                                    "AND l.createdDateTime BETWEEN :startDateTime AND :endDateTime")
    Page<Letter> findAllTodayLettersByRequestStatus(Pageable pageable, @Param("requestStatus")RequestStatus requestStatus,
                                                    @Param("startDateTime")LocalDateTime startDateTime,
                                                    @Param("endDateTime")LocalDateTime endDateTime);
}
