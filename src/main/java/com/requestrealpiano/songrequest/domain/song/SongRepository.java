package com.requestrealpiano.songrequest.domain.song;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {

    Optional<Song> findBySongTitleContainingIgnoreCaseAndArtistIgnoreCase(String songTitle, String artist);
}
