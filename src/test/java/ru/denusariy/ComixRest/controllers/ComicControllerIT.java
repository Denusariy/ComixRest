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
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.denusariy.ComixRest.repositories.ComicRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-book-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/create-book-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ComicControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ComicRepository comicRepository;

    @Nested
    class SaveNewComicIT{
        @Test
        void should_ReturnValidResponseEntityWithNewComic_When_RequestToSaveIsValid() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.post("/comics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "title": "Batman new52 Vol 1 #2",
                        "year": 2011,
                        "writer": "John Byrne",
                        "artist": "Frank Miller",
                        "book": 2
                    }""");
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                            {
                                "title": "Batman new52 Vol 1 #2",
                                "year": 2011,
                                "writer": "John Byrne",
                                "artist": "Frank Miller"
                            }"""));
            assertEquals(4, comicRepository.count());
        }
    }
}