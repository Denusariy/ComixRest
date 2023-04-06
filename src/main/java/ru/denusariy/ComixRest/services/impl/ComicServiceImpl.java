package ru.denusariy.ComixRest.services.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import ru.denusariy.ComixRest.domain.dto.request.ComicRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.ArtistsWritersResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.ComicResponseDTO;
import ru.denusariy.ComixRest.domain.entity.Book;
import ru.denusariy.ComixRest.domain.entity.Comic;
import ru.denusariy.ComixRest.repositories.BookRepository;
import ru.denusariy.ComixRest.repositories.ComicRepository;
import ru.denusariy.ComixRest.exception.BookNotFoundException;
import ru.denusariy.ComixRest.exception.ComicNotFoundException;
import ru.denusariy.ComixRest.services.ComicService;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ComicServiceImpl implements ComicService {
    private final ComicRepository comicRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    //Сохранить комикс с привязкой к книге по её id
    @Transactional
    public ComicResponseDTO save(ComicRequestDTO comicRequestDTO) {
        Book book = bookRepository.findById(comicRequestDTO.getBook_id()).orElseThrow(BookNotFoundException::new);
        Comic newComic = comicRepository.save(convertToComic(comicRequestDTO));
        newComic.setBook(book);
        book.setComics(new ArrayList<>(Collections.singletonList(newComic)));
        return convertToComicResponseDTO(newComic);
    }

    //Обновить комикс по полям
    @Transactional
    public ComicResponseDTO update(int id, Map<String, Object> fields) {
        Comic comicToBeUpdated = comicRepository.findById(id).orElseThrow(ComicNotFoundException::new);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Comic.class, key);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, comicToBeUpdated, value);
        });
        return convertToComicResponseDTO(comicToBeUpdated);
    }

    //Удалить комикс по id
    @Transactional
    public String delete(int id) {
        Comic comicToDelete = comicRepository.findById(id).orElseThrow(ComicNotFoundException::new);
        String title = comicToDelete.getTitle();
        comicRepository.delete(comicToDelete);
        return title;
    }

    //Получить список всех художников и сценаристов в виде ResponseDTO
    public ArtistsWritersResponseDTO findArtistsWriters() {
        ArtistsWritersResponseDTO responseDTO = new ArtistsWritersResponseDTO();
        responseDTO.setArtists(allArtists());
        responseDTO.setWriters(allWriters());
        return responseDTO;
    }

    //Получить список всех сценаристов. Если в поле writer несколько человек, то поле делится токенайзером
    @Transactional(readOnly = true)
    public List<String> allWriters() {
        Set<String> allWriters = new HashSet<>();
        List<Comic> comics = comicRepository.findAll();
        for(Comic comic : comics){
            StringTokenizer tokenizer = new StringTokenizer(comic.getWriter(), ",");
            while(tokenizer.hasMoreTokens())
                allWriters.add(tokenizer.nextToken().trim());
        }
        return new ArrayList<>(allWriters);
    }

    //Получить список всех художников. Если в поле artist несколько человек, то поле делится токенайзером
    @Transactional(readOnly = true)
    public List<String> allArtists() {
        Set<String> allArtists = new HashSet<>();
        List<Comic> comics = comicRepository.findAll();
        for(Comic comic : comics){
            StringTokenizer tokenizer = new StringTokenizer(comic.getArtist(), ",");
            while(tokenizer.hasMoreTokens())
                allArtists.add(tokenizer.nextToken().trim());
        }
        return new ArrayList<>(allArtists);
    }

    //Получить все комиксы с заданным сценаристом
    @Transactional(readOnly = true)
    public List<ComicResponseDTO> findByWriter(String query) {
        return comicRepository.findByWriterContains(query).stream().map(this::convertToComicResponseDTO)
                .collect(Collectors.toList());
    }

    //Получить все комиксы с заданным художником
    @Transactional(readOnly = true)
    public List<ComicResponseDTO> findByArtist(String query) {
        return comicRepository.findByArtistContains(query).stream().map(this::convertToComicResponseDTO)
                .collect(Collectors.toList());
    }

    //Маппинг из ComicRequestDTO в Comic
    public Comic convertToComic(ComicRequestDTO comicRequestDTO) {
        return modelMapper.map(comicRequestDTO, Comic.class);
    }

    //Маппинг из Comic в ComicResponseDTO
    public ComicResponseDTO convertToComicResponseDTO(Comic comic) {
        return modelMapper.map(comic, ComicResponseDTO.class);
    }


}
