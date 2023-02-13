package ru.denusariy.ComixRest.services;

import ru.denusariy.ComixRest.domain.dto.request.BookRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.BookResponseDTO;


import java.util.List;
import java.util.Map;

public interface BookService {
    BookResponseDTO findOne(int id);
    List<BookResponseDTO> findAllWithPagination(Integer page, Integer size);
    BookResponseDTO save(BookRequestDTO bookRequestDTO);
    BookResponseDTO update(int id, Map<String, Object> fields);
    String delete(int id);
    List<BookResponseDTO> findByTitle(String query);
    List<BookResponseDTO> findBookWithAltCover();
    List<BookResponseDTO> findBookWithAutograph();
}
