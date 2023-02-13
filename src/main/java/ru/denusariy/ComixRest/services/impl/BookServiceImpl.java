package ru.denusariy.ComixRest.services.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import ru.denusariy.ComixRest.domain.dto.request.BookRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.BookResponseDTO;
import ru.denusariy.ComixRest.domain.entity.Book;
import ru.denusariy.ComixRest.repositories.BookRepository;
import ru.denusariy.ComixRest.exception.BookNotFoundException;
import ru.denusariy.ComixRest.services.BookService;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public BookResponseDTO findOne(int id) {
        return convertToBookResponseDTO(bookRepository.findById(id).orElseThrow(BookNotFoundException::new));
    }
    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAllWithPagination(Integer page, Integer size) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
            Page<Book> books = bookRepository.findAll(pageable);
            return books.getContent().stream().map(this::convertToBookResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public BookResponseDTO save(BookRequestDTO bookRequestDTO) {
        return convertToBookResponseDTO(bookRepository.save(convertToBook(bookRequestDTO)));
    }

    @Transactional
    public BookResponseDTO update(int id, Map<String, Object> fields) {
        Book bookToBeUpdated = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Book.class, key);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, bookToBeUpdated, value);
        });
        return convertToBookResponseDTO(bookToBeUpdated);
    }

    @Transactional
    public String delete(int id) {
        Book bookToDelete = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        String title = bookToDelete.getTitle();
        bookRepository.delete(bookToDelete);
        return title;
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findByTitle(String query) {
        return bookRepository.findByTitleContainsIgnoreCase(query).stream().map(this::convertToBookResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findBookWithAltCover() {
        return bookRepository.findByIsAltCoverTrue().stream().map(this::convertToBookResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findBookWithAutograph() {
        return bookRepository.findByIsAutographTrue().stream().map(this::convertToBookResponseDTO)
                .collect(Collectors.toList());
    }

    private BookResponseDTO convertToBookResponseDTO(Book book) {
        return modelMapper.map(book, BookResponseDTO.class);
    }
    private Book convertToBook(BookRequestDTO bookRequestDTO) {
        return modelMapper.map(bookRequestDTO, Book.class);
    }

}
