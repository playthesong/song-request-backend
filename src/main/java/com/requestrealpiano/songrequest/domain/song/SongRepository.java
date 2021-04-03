package com.requestrealpiano.songrequest.domain.song;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s WHERE LOWER(s.songTitle) = LOWER(:songTitle) " +
                                  "AND LOWER(s.artist) = LOWER(:artist)")
    List<Song> findBySongTitleAndArtist(@Param("songTitle") String songTitle, @Param("artist") String artist);
}
