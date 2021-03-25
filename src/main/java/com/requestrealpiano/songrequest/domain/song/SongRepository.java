package com.requestrealpiano.songrequest.domain.song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s WHERE LOWER(s.songTitle) LIKE LOWER(CONCAT('%', :songTitle, '%')) " +
                                  "AND LOWER(s.artist) = LOWER(:artist)")
    Optional<Song> findBySongTitleAndArtist(@Param("songTitle") String songTitle, @Param("artist") String artist);
}
