package ru.denusariy.ComixRest.controllers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.denusariy.ComixRest.repositories.BookRepository;
import ru.denusariy.ComixRest.repositories.ComicRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-book-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-book-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SearchControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ComicRepository comicRepository;

    @Nested
    class GetAllArtistAndWritersIT{
        @Test
        void should_ReturnValidPageResponseEntity_When_ArtistsWritersListIsNotEmpty() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                {
                                    "artists": ["Ron Lim","Frank Miller"],
                                    "writers": ["Jonathan Hickman","Jeff Parker","John Byrne"]
                                }
                                """));
        }
    }

    @Nested
    class SearchComicsByWriterIT{
        @Test
        void should_ReturnValidPageResponseEntity_When_ResultListIsNotEmpty() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search/writer")
                    .param("query", "Jeff Parker");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                [
                                    {
                                    "title":"Batman New52 Vol 1 #1",
                                    "year":2010,
                                    "writer":"Jeff Parker",
                                    "artist":"Ron Lim"
                                    }
                                ]
                            """));
        }
        @Test
        void should_ReturnValidPageResponseEntity_When_ResultListIsEmpty() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search/writer")
                    .param("query", "Tim Roth");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("[]"));
        }
    }
    @Nested
    class SearchComicsByArtistIT{
        @Test
        void should_ReturnValidPageResponseEntity_When_ResultListIsNotEmpty() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search/artist")
                    .param("query", "Frank Miller");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                [
                                     {
                                         "title":"Batman and Robin Vol 1 #1",
                                         "year":2009,
                                         "writer":"John Byrne",
                                         "artist":"Frank Miller"
                                     },
                                     {
                                         "title":"Batman and Robin Vol 1 #2",
                                         "year":2009,
                                         "writer":"Jonathan Hickman",
                                         "artist":"Frank Miller"
                                     }
                                ]
                            """));
        }
        @Test
        void should_ReturnValidPageResponseEntity_When_ResultListIsEmpty() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search/artist")
                    .param("query", "Michael Jackson");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("[]"));
        }
    }

    @Nested
    class SearchBooksByTitleIT{
        @Test
        void should_ReturnValidPageResponseEntity_When_ResultListIsNotEmpty() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search/title")
                    .param("query", "BAT");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                [{
                                        "title":"Batman New52",
                                        "year":2010,
                                        "format":"TPB",
                                        "signature":null,
                                        "comics": [
                                            {
                                                "title": "Batman New52 Vol 1 #1",
                                                "year": 2010,
                                                "writer": "Jeff Parker",
                                                "artist": "Ron Lim"
                                            }],
                                        "altCover":false,
                                        "autograph":false
                                    },
                                    {
                                        "title":"Batman and Robin",
                                        "year":2010,
                                        "format":"TPB",
                                        "signature":"Frank Miller",
                                        "comics":[
                                            {
                                                "title": "Batman and Robin Vol 1 #1",
                                                "year": 2009,
                                                "writer": "John Byrne",
                                                "artist": "Frank Miller"
                                            },
                                            {
                                                "title": "Batman and Robin Vol 1 #2",
                                                "year": 2009,
                                                "writer": "Jonathan Hickman",
                                                "artist": "Frank Miller"
                                            }],
                                        "altCover":true,
                                        "autograph":true
                                    }]
                            """));
        }
        @Test
        void should_ReturnValidPageResponseEntity_When_ResultListIsEmpty() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search/title")
                    .param("query", "Hello World");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("[]"));
        }
    }

    @Nested
    class SearchBookByCoverIT{
        @Test
        void should_ReturnValidPageResponseEntity_When_ResultListIsNotEmpty() throws Exception {
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search/alt_cover");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                [
                                    {
                                        "title":"Batman and Robin",
                                        "year":2010,
                                        "format":"TPB",
                                        "signature":"Frank Miller",
                                        "comics":[
                                            {
                                                "title": "Batman and Robin Vol 1 #1",
                                                "year": 2009,
                                                "writer": "John Byrne",
                                                "artist": "Frank Miller"
                                            },
                                            {
                                                "title": "Batman and Robin Vol 1 #2",
                                                "year": 2009,
                                                "writer": "Jonathan Hickman",
                                                "artist": "Frank Miller"
                                            }],
                                        "altCover":true,
                                        "autograph":true
                                    }
                                ]
                            """));
        }
    }

    @Nested
    class SearchBookByAutographIT{
        @Test
        void should_ReturnValidPageResponseEntity_When_ResultListIsNotEmpty() throws Exception {
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/search/autograph");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                [
                                    {
                                        "title":"Batman and Robin",
                                        "year":2010,
                                        "format":"TPB",
                                        "signature":"Frank Miller",
                                        "comics":[
                                            {
                                                "title": "Batman and Robin Vol 1 #1",
                                                "year": 2009,
                                                "writer": "John Byrne",
                                                "artist": "Frank Miller"
                                            },
                                            {
                                                "title": "Batman and Robin Vol 1 #2",
                                                "year": 2009,
                                                "writer": "Jonathan Hickman",
                                                "artist": "Frank Miller"
                                            }],
                                        "altCover":true,
                                        "autograph":true
                                    }
                                ]
                            """));
        }
    }
}