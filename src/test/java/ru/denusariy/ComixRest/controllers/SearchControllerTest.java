package ru.denusariy.ComixRest.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.denusariy.ComixRest.domain.dto.response.ArtistsWritersResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.BookResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.ComicResponseDTO;
import ru.denusariy.ComixRest.domain.enums.Format;
import ru.denusariy.ComixRest.services.BookService;
import ru.denusariy.ComixRest.services.ComicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {

    @InjectMocks
    SearchController searchController;
    @Mock
    BookService bookServiceMock;
    @Mock
    ComicService comicServiceMock;

    @Nested
    class GetArtistWritersTest{
        @Test
        void should_ReturnValidResponseEntity_When_ArtistAndWriterListIsEmpty() {
            //given
            ArtistsWritersResponseDTO expected = new ArtistsWritersResponseDTO(Collections.emptyList(), Collections.emptyList());
            when(comicServiceMock.findArtistsWriters()).thenReturn(expected);
            //when
            ResponseEntity<?> actual = searchController.getArtistsWriters();
            //then
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }

        @Test
        void should_ReturnValidResponseEntity_When_ArtistAndWriterListIsNotEmpty() {
            //given
            List<String> artists = new ArrayList<>(List.of("Artist1", "Artist2"));
            List<String> writers = new ArrayList<>(List.of("Writer1", "Writer2"));
            ArtistsWritersResponseDTO expected = new ArtistsWritersResponseDTO(artists, writers);
            when(comicServiceMock.findArtistsWriters()).thenReturn(expected);
            //when
            ResponseEntity<?> actual = searchController.getArtistsWriters();
            //then
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }
    }

    @Nested
    class SearchComicsTests{
        List<ComicResponseDTO> comics = new ArrayList<>(List.of(
                new ComicResponseDTO("TestComic1", 2000, "Test Writer1","Test Artist1", null),
                new ComicResponseDTO("TestComic2", 2000, "Test Writer2","Test Artist2", null)
        ));

        @Nested
        class SearchByArtistTest{
            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsEmpty() {
                //given
                List<ComicResponseDTO> expected = Collections.emptyList();
                when(comicServiceMock.findByArtist(anyString())).thenReturn(Collections.emptyList());
                //when
                ResponseEntity<?> actual = searchController.searchByArtist(anyString());
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(expected, actual.getBody());
            }

            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsNotEmpty() {
                //given
                when(comicServiceMock.findByArtist(anyString())).thenReturn(comics);
                //when
                ResponseEntity<?> actual = searchController.searchByArtist(anyString());
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(comics, actual.getBody());
            }
        }

        @Nested
        class SearchByWriterTest{
            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsEmpty() {
                //given
                List<ComicResponseDTO> expected = Collections.emptyList();
                when(comicServiceMock.findByWriter(anyString())).thenReturn(Collections.emptyList());
                //when
                ResponseEntity<?> actual = searchController.searchByWriter(anyString());
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(expected, actual.getBody());
            }

            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsNotEmpty() {
                //given
                when(comicServiceMock.findByWriter(anyString())).thenReturn(comics);
                //when
                ResponseEntity<?> actual = searchController.searchByWriter(anyString());
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(comics, actual.getBody());
            }
        }

    }

    @Nested
    class SearchBookTests{
        List<BookResponseDTO> books = new ArrayList<>(List.of(
                new BookResponseDTO("Title", 2000, Format.SING, true, true,
                        "Someone", null),
                new BookResponseDTO("Title2", 2002, Format.TPB, true, true,
                        "Someone2", null)
        ));

        @Nested
        class SearchByTitleTest{

            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsEmpty() {
                //given
                List<BookResponseDTO> expected = Collections.emptyList();
                when(bookServiceMock.findByTitle(anyString())).thenReturn(Collections.emptyList());
                //when
                ResponseEntity<?> actual = searchController.searchByTitle(anyString());
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(expected, actual.getBody());
            }

            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsNotEmpty() {
                //given
                when(bookServiceMock.findByTitle(anyString())).thenReturn(books);
                //when
                ResponseEntity<?> actual = searchController.searchByTitle(anyString());
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(books, actual.getBody());
            }
        }

        @Nested
        class SearchByCoverTest{
            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsEmpty() {
                //given
                List<BookResponseDTO> expected = Collections.emptyList();
                when(bookServiceMock.findBookWithAltCover()).thenReturn(Collections.emptyList());
                //when
                ResponseEntity<?> actual = searchController.searchByCover();
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(expected, actual.getBody());
            }

            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsNotEmpty() {
                //given
                when(bookServiceMock.findBookWithAltCover()).thenReturn(books);
                //when
                ResponseEntity<?> actual = searchController.searchByCover();
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(books, actual.getBody());
            }
        }

        @Nested
        class SearchByAutographTest{
            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsEmpty() {
                //given
                List<BookResponseDTO> expected = Collections.emptyList();
                when(bookServiceMock.findBookWithAutograph()).thenReturn(Collections.emptyList());
                //when
                ResponseEntity<?> actual = searchController.searchByAutograph();
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(expected, actual.getBody());
            }

            @Test
            void should_ReturnValidResponseEntity_When_ResultListIsNotEmpty() {
                //given
                when(bookServiceMock.findBookWithAutograph()).thenReturn(books);
                //when
                ResponseEntity<?> actual = searchController.searchByAutograph();
                //then
                assertEquals(HttpStatus.OK, actual.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
                assertEquals(books, actual.getBody());
            }
        }

    }

}