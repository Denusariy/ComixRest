package ru.denusariy.ComixRest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.denusariy.ComixRest.domain.entity.Comic;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, Integer> {
    List<Comic> findByWriterContains(String writer);
    List<Comic> findByArtistContains(String artist);
}
