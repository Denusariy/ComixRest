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
import ru.denusariy.ComixRest.repositories.ComicRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-book-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-book-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
                        "book_id": 2
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

        @Test
        void should_ReturnErrorResponseEntity_When_RequestToSaveIsNotValid() throws Exception{
            //given
            var requestBuilder = MockMvcRequestBuilders.post("/comics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "title": "",
                        "year": 0,
                        "writer": "",
                        "artist": "",
                        "book_id": 2
                    }
                    """);
            //when
            mockMvc.perform(requestBuilder)
            //then
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json( """
                            {
                                "errors":
                                    {
                                        "year": "Год выпуска должен быть больше 1900",
                                        "title": "Название комикса не должно быть пустым",
                                        "writer": "У комикса должен быть сценарист",
                                        "artist": "У комикса должен быть художник"
                                    }
                            }"""));
            assertEquals(3, comicRepository.count());
        }

        @Test
        void should_ReturnErrorResponseEntity_When_RequestToSaveContainsWrongBookId() throws Exception{
            //given
            var requestBuild = MockMvcRequestBuilders.post("/comics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "title": "Batman new52 Vol 1 #2",
                        "year": 2011,
                        "writer": "John Byrne",
                        "artist": "Frank Miller",
                        "book_id": 55
                    }
                    """);
            //when
            mockMvc.perform(requestBuild)
            //then
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                            {
                                "message":"Книга с данным id не найдена!"
                            }
                            """));
            assertEquals(3, comicRepository.count());
        }
    }

    @Nested
    class DeleteComicIT{
        @Test
        void should_ReturnValidResponseEntity_When_DeletedComicIsPresent() throws Exception{
            //given
            var requestBuild = MockMvcRequestBuilders.delete("/comics/1");
            //when
            mockMvc.perform(requestBuild)
            //then
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                    .andExpect(content().string("Batman New52 Vol 1 #1"));
            assertEquals(2, comicRepository.count());
        }

        @Test
        void should_ReturnErrorResponseEntity_When_DeletedComicIsNotPresent() throws Exception{
            //given
            var requestBuild = MockMvcRequestBuilders.delete("/comics/55");
            //when
            mockMvc.perform(requestBuild)
                    //then
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json("""
                            {
                                "message":"Комикс с данным id не найден!"
                            }
                            """));
            assertEquals(3, comicRepository.count());
        }
    }
}