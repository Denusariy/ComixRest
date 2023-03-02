package ru.denusariy.ComixRest.controllers;

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

    @Test
    void should_ReturnValidResponseEntity_When_BookIsPresent() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/books/1");
        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                                {
                                    "title": "Бэтмен Навсегда",
                                    "format": "TPB",
                                    "year": 2010,
                                    "altCover": false,
                                    "autograph": false,
                                    "signature": null,
                                    "comics": []
                                }
                                """));
    }

    @Test
    void should_ReturnValidPageResponseEntity_When_BookListIsNotEmpty() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/books");
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                                {"content":
                                [{
                                "title":"Бэтмен и Робин",
                                "year":2010,
                                "format":"TPB",
                                "signature":null,
                                "comics":[],
                                "altCover":false,
                                "autograph":false},
                                {"title":"Бэтмен Навсегда",
                                "year":2010,
                                "format":"TPB",
                                "signature":null,
                                "comics":[],
                                "altCover":false,
                                "autograph":false}],
                                "pageable":
                                    {"sort":
                                        {"empty":false,
                                        "sorted":true,
                                        "unsorted":false},
                                        "offset":0,
                                        "pageNumber":0,
                                        "pageSize":20,
                                        "paged":true,
                                        "unpaged":false},
                                    "last":true,
                                    "totalElements":2,
                                    "totalPages":1,
                                    "size":20,
                                    "number":0,
                                    "sort":
                                        {"empty":false,
                                        "sorted":true,
                                        "unsorted":false},
                                    "first":true,
                                    "numberOfElements":2,
                                    "empty":false}"""));
    }

//    @Test
//    void should_ReturnValidResponseEntity_When_ResponseIsValid() {
//        //given
//
//        //when
//
//        //then
//    }

//    @Test
//    void correctLoginTest() throws Exception {
//        this.mockMvc.perform(formLogin().user("name").password("password"))
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/главная страница"));
//    }
//
//    @Test
//    void badCredential() throws Exception {
//        this.mockMvc.perform(post("/login").param("user", "wrong_name"))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//    }
}