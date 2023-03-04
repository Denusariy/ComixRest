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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-book-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-book-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    BookRepository bookRepository;

    @Nested
    class ShowOneBookIT{
        @Test
        void should_ReturnValidResponseEntity_When_BookIsPresent() throws Exception {
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/books/1");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                {
                                    "title": "Batman New52",
                                    "format": "TPB",
                                    "year": 2010,
                                    "altCover": false,
                                    "autograph": false,
                                    "signature": null,
                                    "comics": [
                                        {
                                            "title": "Batman New52 Vol 1 #1",
                                            "year": 2010,
                                            "writer": "Jeff Parker",
                                            "artist": "Ron Lim"
                                        }]
                                }
                                """));
        }
        @Test
        void should_ReturnErrorResponseEntity_When_BookIsNotPresent() throws Exception {
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/books/55");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                {
                                    "message":"Книга с данным id не найдена!"
                                }
                    """));
        }
    }
    @Nested
    class ShowPageAllBooksIT {
        @Test
        void should_ReturnValidPageResponseEntity_When_BookListIsNotEmpty() throws Exception {
            //given
            var requestBuilder = MockMvcRequestBuilders.get("/books");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                {
                                    "content":[
                                        {
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
                                        }],
                                    "pageable":
                                        {
                                            "sort":
                                                {
                                                    "empty":false,
                                                    "sorted":true,
                                                    "unsorted":false
                                                },
                                            "offset":0,
                                            "pageNumber":0,
                                            "pageSize":20,
                                            "paged":true,
                                            "unpaged":false
                                        },
                                    "last":true,
                                    "totalElements":2,
                                    "totalPages":1,
                                    "size":20,
                                    "number":0,
                                    "sort":
                                        {
                                            "empty":false,
                                            "sorted":true,
                                            "unsorted":false
                                        },
                                    "first":true,
                                    "numberOfElements":2,
                                    "empty":false
                                }"""));
        }
    }
    @Nested
    class SaveNewBookIT {
        @Test
        void should_ReturnValidResponseEntityWithNewBook_When_RequestToSaveIsValid() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "title": "Spider-Man",
                        "format": "SING",
                        "year": 2020,
                        "altCover": false,
                        "autograph": false,
                        "signature": null
                    }""");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                        {
                            "title": "Spider-Man",
                            "format": "SING",
                            "year": 2020,
                            "altCover": false,
                            "autograph": false,
                            "signature": null,
                            "comics":null
                        }"""
                    ));
            assertEquals(3, bookRepository.count());
        }

        @Test
        void should_ReturnErrorMessage_When_RequestToSaveIsNotValid() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "title": "",
                        "format": "SING",
                        "year": 0,
                        "altCover": false,
                        "autograph": false,
                        "signature": null
                    }""");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                        {
                            "errors":
                                {
                                    "year":"Год издания должен быть больше 1900",
                                    "title":"Название книги не должно быть пустым"
                                }
                        }"""));
            assertEquals(2, bookRepository.count());
        }
    }

    @Nested
    class DeleteBookIT{
        @Test
        void should_ReturnValidResponseEntity_When_DeletedBookIsPresent() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.delete("/books/1");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string("Batman New52"));
            assertEquals(1, bookRepository.count());
        }

        @Test
        void should_ReturnErrorResponseEntity_When_DeletedBookIsNotPresent() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.delete("/books/55");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                {
                                    "message":"Книга с данным id не найдена!"
                                }
                    """));
        }
    }

    @Nested
    class UpdateBookIT {
        @Test
        void should_ReturnValidResponseEntityWithUpdatedBook_When_RequestToUpdateIsValid() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.patch("/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "title": "Spider-Man",
                        "year": 2020
                    }""");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                        {
                            "title": "Spider-Man",
                            "format": "TPB",
                            "year": 2020,
                            "altCover": false,
                            "autograph": false,
                            "signature": null,
                            "comics": [
                                    {
                                        "title": "Batman New52 Vol 1 #1",
                                        "year": 2010,
                                        "writer": "Jeff Parker",
                                        "artist": "Ron Lim"
                                    }]
                        }"""
                    ));
            assertEquals(2, bookRepository.count());
        }

        @Test
        void should_ReturnErrorResponseEntity_When_UpdatedBookIsNotPresent() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.patch("/books/55")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "title": "Spider-Man",
                        "year": 2020
                    }""");
            //when
            mockMvc.perform(requestBuilder)
                    //then
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                                {
                                    "message":"Книга с данным id не найдена!"
                                }
                    """));
        }
    }
}