package ru.denusariy.ComixRest.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import ru.denusariy.ComixRest.domain.dto.request.BookRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.BookResponseDTO;
import ru.denusariy.ComixRest.domain.entity.Book;
import ru.denusariy.ComixRest.domain.enums.Format;
import ru.denusariy.ComixRest.exception.BookNotFoundException;
import ru.denusariy.ComixRest.repositories.BookRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;
    @Spy
    private BookRepository bookRepositoryMock;
    @Spy
    private ModelMapper modelMapperMock;

    @Nested
    class ConvertToBookResponseDTOTest{
        @Test
        void should_CompareBookToBookResponseDTO(){
            //given
            Book book = new Book(1, "TestBook", 2000, Format.SING, false, false,
                    null, null);
            BookResponseDTO expected = new BookResponseDTO("TestBook", 2000, Format.SING, false,
                    false, null, null);
            //when
            BookResponseDTO actual = modelMapperMock.map(book, BookResponseDTO.class);
            //then
            assertAll(
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getFormat(), actual.getFormat()),
                    () -> assertEquals(expected.isAltCover(), actual.isAltCover()),
                    () -> assertEquals(expected.isAutograph(), actual.isAutograph()),
                    () -> assertEquals(expected.getSignature(), actual.getSignature()),
                    () -> assertEquals(expected.getComics(), actual.getComics())
            );
        }
    }

    @Nested
    class ConvertToBookTest {
        @Test
        void should_CompareBookRequestDTOToBook() {
            //given
            BookRequestDTO requestDTO = new BookRequestDTO("TestBook", 2000, Format.SING, false,
                    false, null);
            Book expected = new Book(0, "TestBook", 2000, Format.SING, false, false,
                    null, null);
            //when
            Book actual = modelMapperMock.map(requestDTO, Book.class);
            //then
            assertAll(
                    () -> assertEquals(expected.getId(), actual.getId()),
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getFormat(), actual.getFormat()),
                    () -> assertEquals(expected.isAltCover(), actual.isAltCover()),
                    () -> assertEquals(expected.isAutograph(), actual.isAutograph()),
                    () -> assertEquals(expected.getSignature(), actual.getSignature()),
                    () -> assertEquals(expected.getComics(), actual.getComics())
            );
        }
    }

    @Nested
    class FindOneTest {

        @Test
        void should_FindOneBookResponseDTO_When_IdIsPresent() {
            //given
            Book book = new Book(1, "TestBook", 2000, Format.SING, false, false,
                    null, null);
            BookResponseDTO expected = new BookResponseDTO("TestBook", 2000, Format.SING, false,
                    false, null, null);
            when(bookRepositoryMock.findById(1)).thenReturn(Optional.of(book));
            //when
            BookResponseDTO actual = bookService.findOne(1);
            //then
            assertAll(
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getFormat(), actual.getFormat()),
                    () -> assertEquals(expected.isAltCover(), actual.isAltCover()),
                    () -> assertEquals(expected.isAutograph(), actual.isAutograph()),
                    () -> assertEquals(expected.getSignature(), actual.getSignature()),
                    () -> assertEquals(expected.getComics(), actual.getComics())
            );
        }

        @Test
        void should_ThrowBookNotFoundException_When_IdIsNotPresent() {
            //given
            when(bookRepositoryMock.findById(anyInt())).thenThrow(BookNotFoundException.class);
            //when
            Executable executable = () -> bookService.findOne(anyInt());
            //then
            assertThrows(BookNotFoundException.class, executable);
        }
    }

    @Nested
    class FindAllWithPaginationTest{
        @Test
        void should_ReturnEmptyPage_When_ListIsEmpty() {
            //given
            Pageable pageable = PageRequest.of(1, 2, Sort.by("title"));
            Page<BookResponseDTO> expected = new PageImpl(Collections.emptyList(), pageable, 0L);
            when(bookRepositoryMock.findAll(pageable)).thenReturn(new PageImpl(Collections.emptyList(), pageable, 0L));
            //when
            Page<BookResponseDTO> actual = bookService.findAllWithPagination(1, 2);
            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class SaveTest{
        @Test
        void should_SaveBookAndReturnResponseDTO() {
            //given
            BookRequestDTO requestDTO = new BookRequestDTO("TestBook", 2000, Format.SING, false,
                    false, null);
            BookResponseDTO expected = new BookResponseDTO("TestBook", 2000, Format.SING, false,
                    false, null, null);
            Book book = modelMapperMock.map(requestDTO, Book.class);
            when(bookRepositoryMock.save(book)).thenReturn(book);
            //when
            BookResponseDTO actual = bookService.save(requestDTO);
            //then
            verify(bookRepositoryMock).save(book);
            assertAll(
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getFormat(), actual.getFormat()),
                    () -> assertEquals(expected.isAltCover(), actual.isAltCover()),
                    () -> assertEquals(expected.isAutograph(), actual.isAutograph()),
                    () -> assertEquals(expected.getSignature(), actual.getSignature()),
                    () -> assertEquals(expected.getComics(), actual.getComics())
            );
        }
    }

    @Nested
    class UpdateTest{
        private Map<String, Object> fields;

        @BeforeEach
        void setup() {
            this.fields = new HashMap<>();
            fields.put("title", "Test");
            fields.put("year", 2020);
        }
        @Test
        void should_ThrowBookNotFoundException_When_IdIsNotValid() {
            //given
            when(bookRepositoryMock.findById(anyInt())).thenThrow(BookNotFoundException.class);
            //when
            Executable executable = () -> bookService.update(anyInt(), fields);
            //then
            assertThrows(BookNotFoundException.class, executable);
        }

        @Test
        void should_UpdateBookAndReturnResponseDTO_When_IdIsValid() {
            //given
            Book book = new Book(1, "TestBook", 2000, Format.SING, false, false,
                    null, null);
            BookResponseDTO expected = new BookResponseDTO("Test", 2020, Format.SING, false,
                    false, null, null);
            when(bookRepositoryMock.findById(1)).thenReturn(Optional.of(book));
            //when
            BookResponseDTO actual = bookService.update(1, fields);
            //then
            assertAll(
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getFormat(), actual.getFormat()),
                    () -> assertEquals(expected.isAltCover(), actual.isAltCover()),
                    () -> assertEquals(expected.isAutograph(), actual.isAutograph()),
                    () -> assertEquals(expected.getSignature(), actual.getSignature()),
                    () -> assertEquals(expected.getComics(), actual.getComics())
            );
        }
    }

    @Nested
    class FindByTitleTest{
        private List<Book> books;

        @BeforeEach
        void setup() {
            this.books = Collections.singletonList(new Book(1, "TestBook", 2000, Format.SING, false,
                    false, null, null));
        }
        @Test
        void should_ReturnEmptyList_When_ThereIsNoSuitableBooks() {
            //given
            List<BookResponseDTO> expected = Collections.emptyList();
            when(bookRepositoryMock.findByTitleContainsIgnoreCase("word")).thenReturn(Collections.emptyList());
            //when
            List<BookResponseDTO> actual = bookService.findByTitle("word");
            //then
            assertEquals(expected, actual);
        }

        @Test
        void should_ReturnListBookResponseDTO_When_ThereAreSuitableBooks() {
            //given
            List<BookResponseDTO> expected = Collections.singletonList(new BookResponseDTO("TestBook", 2000,
                    Format.SING, false, false, null, null));
            when(bookRepositoryMock.findByTitleContainsIgnoreCase("test")).thenReturn(books);
            //when
            List<BookResponseDTO> actual = bookService.findByTitle("test");
            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class FindBookWithAltCoverTest{
        private List<Book> books;

        @BeforeEach
        void setup() {
            this.books = Collections.singletonList(new Book(1, "TestBook", 2000, Format.SING, true,
                    false, null, null));
        }

        @Test
        void should_ReturnEmptyList_When_ThereIsNoSuitableBooks() {
            //given
            List<BookResponseDTO> expected = Collections.emptyList();
            when(bookRepositoryMock.findByIsAltCoverTrue()).thenReturn(Collections.emptyList());
            //when
            List<BookResponseDTO> actual = bookService.findBookWithAltCover();
            //then
            assertEquals(expected, actual);
        }

        @Test
        void should_ReturnListBookResponseDTO_When_ThereAreSuitableBooks() {
            //given
            List<BookResponseDTO> expected = Collections.singletonList(new BookResponseDTO("TestBook", 2000,
                    Format.SING, true, false, null, null));
            when(bookRepositoryMock.findByIsAltCoverTrue()).thenReturn(books);
            //when
            List<BookResponseDTO> actual = bookService.findBookWithAltCover();
            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class FindBookWithAutographTest{
        private List<Book> books;

        @BeforeEach
        void setup() {
            this.books = Collections.singletonList(new Book(1, "TestBook", 2000, Format.SING, true,
                    true, "anybody", null));
        }

        @Test
        void should_ReturnEmptyList_When_ThereIsNoSuitableBooks() {
            //given
            List<BookResponseDTO> expected = Collections.emptyList();
            when(bookRepositoryMock.findByIsAutographTrue()).thenReturn(Collections.emptyList());
            //when
            List<BookResponseDTO> actual = bookService.findBookWithAutograph();
            //then
            assertEquals(expected, actual);
        }

        @Test
        void should_ReturnListBookResponseDTO_When_ThereAreSuitableBooks() {
            //given
            List<BookResponseDTO> expected = Collections.singletonList(new BookResponseDTO("TestBook", 2000,
                    Format.SING, true, true, "anybody", null));
            when(bookRepositoryMock.findByIsAutographTrue()).thenReturn(books);
            //when
            List<BookResponseDTO> actual = bookService.findBookWithAutograph();
            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class DeleteTest{
        private Book book;

        @BeforeEach
        void setup() {
            this.book = new Book(1, "TestBook", 2000, Format.SING, false, false,
                    null, null);
        }
        @Test
        void should_ReturnTitle_When_IdIsValid() {
            //given
            String expected = "TestBook";
            when(bookRepositoryMock.findById(1)).thenReturn(Optional.of(book));
            //when
            String actual = bookService.delete(1);
            //then
            assertEquals(expected, actual);
        }

        @Test
        void should_ThrowBookNotFoundException_When_IdIsNotValid() {
            //given
            when(bookRepositoryMock.findById(anyInt())).thenThrow(BookNotFoundException.class);
            //when
            Executable executable = () -> bookService.delete(anyInt());
            //then
            assertThrows(BookNotFoundException.class, executable);
        }
        @Test
        void should_DeleteBook_When_InputOK() {
            //given
            when(bookRepositoryMock.findById(1)).thenReturn(Optional.of(book));
            //when
            bookService.delete(1);
            //then
            verify(bookRepositoryMock).delete(book);
        }
    }
}