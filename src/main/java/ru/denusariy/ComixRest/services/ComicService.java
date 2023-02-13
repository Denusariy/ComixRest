package ru.denusariy.ComixRest.services;

import ru.denusariy.ComixRest.domain.dto.request.ComicRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.ArtistsWritersResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.ComicResponseDTO;

import java.util.List;
import java.util.Map;


public interface ComicService {
    ComicResponseDTO save(ComicRequestDTO comicRequestDTO);
    ComicResponseDTO update(int id, Map<String, Object> fields);
    String delete(int id);
    ArtistsWritersResponseDTO findArtistsWriters();
    List<String> allWriters();
    List<String> allArtists();
    List<ComicResponseDTO> findByWriter(String query);
    List<ComicResponseDTO> findByArtist(String query);
}
