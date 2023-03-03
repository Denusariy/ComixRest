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
import ru.denusariy.ComixRest.domain.dto.request.ComicRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.ArtistsWritersResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.BookResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.ComicResponseDTO;
import ru.denusariy.ComixRest.domain.entity.Book;
import ru.denusariy.ComixRest.domain.entity.Comic;
import ru.denusariy.ComixRest.exception.BookNotFoundException;
import ru.denusariy.ComixRest.exception.ComicNotFoundException;
import ru.denusariy.ComixRest.repositories.BookRepository;
import ru.denusariy.ComixRest.repositories.ComicRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComicServiceImplTest {
    @InjectMocks
    private ComicServiceImpl comicService;
    @Spy
    private ComicRepository comicRepositoryMock;
    @Spy
    private BookRepository bookRepositoryMock;
    @Spy
    private ModelMapper modelMapperMock;

    @Nested
    class ConvertToComicResponseDTOTest{
        @Test
        void should_CompareComicToComicResponseDTO(){
            //given
            Comic comic = new Comic(1, "TestComic", 2000, "Test Writer", "Test Artist", null);
            ComicResponseDTO expected = new ComicResponseDTO("TestComic", 2000, "Test Writer",
                    "Test Artist", null);
            //when
            ComicResponseDTO actual = modelMapperMock.map(comic, ComicResponseDTO.class);
            //then
            assertAll(
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getWriter(), actual.getWriter()),
                    () -> assertEquals(expected.getArtist(), actual.getArtist()),
                    () -> assertEquals(expected.getBook(), actual.getBook())
            );
        }
    }

    @Nested
    class ConvertToBookTest {
        @Test
        void should_CompareComicRequestDTOToComic() {
            //given
            ComicRequestDTO requestDTO = new ComicRequestDTO("TestComic", 2000, "Test Writer",
                    "Test Artist", 1);
            Comic expected = new Comic(0, "TestComic", 2000, "Test Writer", "Test Artist", null);
            //when
            Comic actual = modelMapperMock.map(requestDTO, Comic.class);
            //then
            assertAll(
                    () -> assertEquals(expected.getId(), actual.getId()),
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getWriter(), actual.getWriter()),
                    () -> assertEquals(expected.getArtist(), actual.getArtist()),
                    () -> assertEquals(expected.getBook(), actual.getBook())
            );
        }
    }

    @Nested
    class SaveTest{
        private ComicRequestDTO requestDTO;
        private Book book;

        @BeforeEach
        void setup() {
            this.requestDTO = new ComicRequestDTO("TestComic", 2000, "Test Writer",
                    "Test Artist", 0);
            this.book = new Book(0, null, 0, null, false, false,
                    null, null);
        }

        @Test
        void should_ThrowBookNotFoundException_When_BookIdIsNotValid() {
            //given
            when(bookRepositoryMock.findById(anyInt())).thenThrow(BookNotFoundException.class);
            //when
            Executable executable = () -> comicService.save(requestDTO);
            //then
            assertThrows(BookNotFoundException.class, executable);
            verify(comicRepositoryMock, never()).save(any());
        }

        @Test
        void should_ReturnResponseDTO_When_SaveNewComic() {
            //given
            BookResponseDTO bookResponseDTO = modelMapperMock.map(book, BookResponseDTO.class);
            Comic comic = new Comic(0, "TestComic", 2000, "Test Writer",
                    "Test Artist", book);
            ComicResponseDTO expected = new ComicResponseDTO("TestComic", 2000, "Test Writer",
                    "Test Artist", bookResponseDTO);
            when(bookRepositoryMock.findById(0)).thenReturn(Optional.of(book));
            when(comicRepositoryMock.save(comic)).thenReturn(comic);
            //when
            ComicResponseDTO actual = comicService.save(requestDTO);
            //then
            assertAll(
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getWriter(), actual.getWriter()),
                    () -> assertEquals(expected.getArtist(), actual.getArtist()),
                    () -> assertEquals(expected.getBook(), actual.getBook())
            );
            verify(comicRepositoryMock).save(comic);
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
        void should_ThrowComicNotFoundException_When_IdIsNotValid() {
            //given
            when(comicRepositoryMock.findById(anyInt())).thenThrow(ComicNotFoundException.class);
            //when
            Executable executable = () -> comicService.update(anyInt(), fields);
            //then
            assertThrows(ComicNotFoundException.class, executable);
        }

        @Test
        void should_UpdateComicAndReturnResponseDTO_When_IdIsValid() {
            //given
            Book book = new Book(0, null, 0, null, false, false,
                    null, null);
            BookResponseDTO bookResponseDTO = modelMapperMock.map(book, BookResponseDTO.class);
            Comic comic = new Comic(1, "TestComic", 2000, "Test Writer",
                    "Test Artist", book);
            ComicResponseDTO expected = new ComicResponseDTO("Test", 2020, "Test Writer",
                    "Test Artist", bookResponseDTO);
            when(comicRepositoryMock.findById(1)).thenReturn(Optional.of(comic));
            //when
            ComicResponseDTO actual = comicService.update(1, fields);
            //then
            assertAll(
                    () -> assertEquals(expected.getTitle(), actual.getTitle()),
                    () -> assertEquals(expected.getYear(), actual.getYear()),
                    () -> assertEquals(expected.getWriter(), actual.getWriter()),
                    () -> assertEquals(expected.getArtist(), actual.getArtist()),
                    () -> assertEquals(expected.getBook(), actual.getBook())
            );
        }
    }

    @Nested
    class DeleteTest{
        private Comic comic;

        @BeforeEach
        void setup() {
            this.comic = new Comic(1, "TestComic", 2000, "Test Writer",
                    "Test Artist", null);
        }
        @Test
        void should_ReturnTitle_When_IdIsValid() {
            //given
            String expected = "TestComic";
            when(comicRepositoryMock.findById(1)).thenReturn(Optional.of(comic));
            //when
            String actual = comicService.delete(1);
            //then
            assertEquals(expected, actual);
            verify(comicRepositoryMock).delete(comic);
        }

        @Test
        void should_ThrowComicNotFoundException_When_IdIsNotValid() {
            //given
            when(comicRepositoryMock.findById(anyInt())).thenThrow(ComicNotFoundException.class);
            //when
            Executable executable = () -> comicService.delete(anyInt());
            //then
            assertThrows(ComicNotFoundException.class, executable);
            verify(comicRepositoryMock, never()).delete(comic);
        }
    }

    @Nested
    class AllArtistsAndWritersTests{
        private List<Comic> comics;

        @BeforeEach
        void setup() {
            this.comics = new ArrayList<>(3);
            comics.add(new Comic(0, "Comic1", 2000, "Writer 1","Artist 1", null));
            comics.add(new Comic(1, "Comic2", 2000, "Writer 2","Artist 2", null));
        }

        @Nested
        class FindArtistsWritersTest {
            @Test
            void should_ReturnResponseDTO() {
                //given
                List<String> writers = Arrays.asList("Writer 2", "Writer 1");
                List<String> artists = Arrays.asList("Artist 1", "Artist 2");
                when(comicRepositoryMock.findAll()).thenReturn(comics);
                ArtistsWritersResponseDTO expected = new ArtistsWritersResponseDTO(writers, artists);
                //when
                ArtistsWritersResponseDTO actual = comicService.findArtistsWriters();
                //then
                assertAll(
                        () -> assertArrayEquals(expected.getArtists().toArray(), actual.getArtists().toArray()),
                        () -> assertArrayEquals(expected.getWriters().toArray(), actual.getWriters().toArray())
                );
            }
        }

        @Nested
        class AllWritersTest{
            @Test
            void should_ReturnEmptyList_When_ComicsListIsEmpty() {
                //given
                List<String> expected = Collections.emptyList();
                when(comicRepositoryMock.findAll()).thenReturn(Collections.emptyList());
                //when
                List<String> actual = comicService.allWriters();
                //then
                assertArrayEquals(expected.toArray(), actual.toArray());
            }

            @Test
            void should_ReturnWritersList_When_ComicsListIsNotEmpty() {
                //given
                List<String> expected = Arrays.asList("Writer 2", "Writer 1");
                when(comicRepositoryMock.findAll()).thenReturn(comics);
                //when
                List<String> actual = comicService.allWriters();
                //then
                assertArrayEquals(expected.toArray(), actual.toArray());
            }

            @Test
            void should_ReturnWritersList_When_SeveralWritersInOneComic() {
                //given
                comics.add(new Comic(3, "Comic3", 2000, "Writer 3, Writer 4","Artist 2", null));
                List<String> expected = Arrays.asList("Writer 4", "Writer 3", "Writer 2", "Writer 1");
                when(comicRepositoryMock.findAll()).thenReturn(comics);
                //when
                List<String> actual = comicService.allWriters();
                //then
                assertArrayEquals(expected.toArray(), actual.toArray());
            }

            @Test
            void should_ReturnWritersList_When_WritersRepeat() {
                //given
                comics.add(new Comic(3, "Comic3", 2000, "Writer 2","Artist 2", null));
                List<String> expected = Arrays.asList("Writer 2", "Writer 1");
                when(comicRepositoryMock.findAll()).thenReturn(comics);
                //when
                List<String> actual = comicService.allWriters();
                //then
                assertArrayEquals(expected.toArray(), actual.toArray());
            }
        }

        @Nested
        class AllArtistsTest{
            @Test
            void should_ReturnEmptyList_When_ComicsListIsEmpty() {
                //given
                List<String> expected = Collections.emptyList();
                when(comicRepositoryMock.findAll()).thenReturn(Collections.emptyList());
                //when
                List<String> actual = comicService.allArtists();
                //then
                assertArrayEquals(expected.toArray(), actual.toArray());
            }

            @Test
            void should_ReturnArtistsList_When_ComicsListIsNotEmpty() {
                //given
                List<String> expected = Arrays.asList("Artist 1", "Artist 2");
                when(comicRepositoryMock.findAll()).thenReturn(comics);
                //when
                List<String> actual = comicService.allArtists();
                //then
                assertArrayEquals(expected.toArray(), actual.toArray());
            }

            @Test
            void should_ReturnArtistsList_When_SeveralArtistsInOneComic() {
                //given
                comics.add(new Comic(3, "Comic3", 2000, "Writer 3","Artist 3, Artist 4", null));
                List<String> expected = Arrays.asList("Artist 3", "Artist 4", "Artist 1", "Artist 2");
                when(comicRepositoryMock.findAll()).thenReturn(comics);
                //when
                List<String> actual = comicService.allArtists();
                //then
                assertArrayEquals(expected.toArray(), actual.toArray());
            }

            @Test
            void should_ReturnArtistsList_When_ArtistsRepeat() {
                //given
                comics.add(new Comic(3, "Comic3", 2000, "Writer 2","Artist 2", null));
                List<String> expected = Arrays.asList("Artist 1", "Artist 2");
                when(comicRepositoryMock.findAll()).thenReturn(comics);
                //when
                List<String> actual = comicService.allArtists();
                //then
                assertArrayEquals(expected.toArray(), actual.toArray());
            }
        }
    }

    @Nested
    class FindByWriterTest {
        private List<Comic> comics;

        @BeforeEach
        void setup() {
            this.comics = new ArrayList<>(Collections.singletonList(new Comic(1, "TestComic", 2000,
                    "Test Writer","Test Artist", null)));
        }
        @Test
        void should_ReturnEmptyList_When_ResultListIsEmpty() {
            //given
            List<ComicResponseDTO> expected = Collections.emptyList();
            when(comicRepositoryMock.findByWriterContains(anyString())).thenReturn(Collections.emptyList());
            //when
            List<ComicResponseDTO> actual = comicService.findByWriter(anyString());
            //then
            assertEquals(expected, actual);
        }

        @Test
        void should_ReturnListComicResponseDTO_When_ResultListIsNotEmpty() {
            //given
            List<ComicResponseDTO> expected = Collections.singletonList(new ComicResponseDTO("TestComic", 2000,
                    "Test Writer","Test Artist", null));
            when(comicRepositoryMock.findByWriterContains("Test Writer")).thenReturn(comics);
            //when
            List<ComicResponseDTO> actual = comicService.findByWriter("Test Writer");
            //then
            assertEquals(expected, actual);
        }
    }

    @Nested
    class FindByArtistTest {
        private List<Comic> comics;

        @BeforeEach
        void setup() {
            this.comics = new ArrayList<>(Collections.singletonList(new Comic(1, "TestComic", 2000,
                    "Test Writer","Test Artist", null)));
        }
        @Test
        void should_ReturnEmptyList_When_ResultListIsEmpty() {
            //given
            List<ComicResponseDTO> expected = Collections.emptyList();
            when(comicRepositoryMock.findByArtistContains(anyString())).thenReturn(Collections.emptyList());
            //when
            List<ComicResponseDTO> actual = comicService.findByArtist(anyString());
            //then
            assertEquals(expected, actual);
        }

        @Test
        void should_ReturnListComicResponseDTO_When_ResultListIsNotEmpty() {
            //given
            List<ComicResponseDTO> expected = Collections.singletonList(new ComicResponseDTO("TestComic", 2000,
                    "Test Writer","Test Artist", null));
            when(comicRepositoryMock.findByArtistContains("Test Writer")).thenReturn(comics);
            //when
            List<ComicResponseDTO> actual = comicService.findByArtist("Test Writer");
            //then
            assertEquals(expected, actual);
        }
    }

}