package ru.denusariy.ComixRest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.denusariy.ComixRest.domain.dto.request.BookRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.BookResponseDTO;
import ru.denusariy.ComixRest.domain.enums.Format;
import ru.denusariy.ComixRest.services.BookService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock
    private BookService bookServiceMock;
    @InjectMocks
    private BookController bookController;

    @Nested
    class GetAllBooksTest{
        private Pageable pageable;

        @BeforeEach
        void setup() {
            this.pageable = PageRequest.of(1, 2, Sort.by("title"));
        }
        @Test
        void should_ReturnValidResponseEntity_When_ListIsEmpty() {
            //given
            Page<BookResponseDTO> expected = new PageImpl<>(Collections.emptyList(), pageable, 0L);
            when(bookServiceMock.findAllWithPagination(anyInt(), anyInt())).thenReturn(expected);
            //when
            ResponseEntity<?> actual = bookController.getAllBooks(anyInt(), anyInt());
            //then
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }

        @Test
        void should_ReturnValidResponseEntity_When_ListIsNotEmpty() {
            //given
            List<BookResponseDTO> books = new ArrayList<>(List.of(
                    new BookResponseDTO("Title", 2000, Format.SING, false, false, null, null),
                    new BookResponseDTO("Title2", 2002, Format.SING, false, false, null, null)
            ));
            Page<BookResponseDTO> expected = new PageImpl<>(books, pageable, books.size());
            when(bookServiceMock.findAllWithPagination(anyInt(), anyInt())).thenReturn(expected);
            //when
            ResponseEntity<?> actual = bookController.getAllBooks(1, 2);
            //then
            assertNotNull(actual);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }
    }

    @Nested
    class ShowTest{
        @Test
        void should_ReturnValidResponseEntity_When_BookIsPresent() {
            //given
            BookResponseDTO expected = new BookResponseDTO("Title", 2000, Format.SING, false,
                    false, null, null);
            when(bookServiceMock.findOne(1)).thenReturn(expected);
            //when
            ResponseEntity<?> actual = bookController.show(1);
            //then
            assertNotNull(actual);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }
    }

    @Nested
    class CreateTest{
        @Test
        void should_ReturnValidResponseEntity_When_ResponseIsValid() {
            //given
            BookRequestDTO requestDTO = new BookRequestDTO("Title", 2000, Format.SING, false,
                    false, null);
            BookResponseDTO expected = new BookResponseDTO("Title", 2000, Format.SING, false,
                    false, null, null);
            when(bookServiceMock.save(requestDTO)).thenReturn(expected);
            //when
            ResponseEntity<?> actual = bookController.create(requestDTO);
            //then
            assertNotNull(actual);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }
    }
}