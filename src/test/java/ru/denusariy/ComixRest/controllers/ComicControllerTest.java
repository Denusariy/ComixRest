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
import ru.denusariy.ComixRest.domain.dto.request.ComicRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.ComicResponseDTO;
import ru.denusariy.ComixRest.services.ComicService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComicControllerTest {
    @InjectMocks
    ComicController comicController;

    @Mock
    ComicService comicServiceMock;

    @Nested
    class CreateTest{
        @Test
        void should_ReturnValidResponseEntity_When_SaveRequestIsValid() {
            //given
            ComicRequestDTO newComic = new ComicRequestDTO("TestComic", 2000, "Test Writer",
                    "Test Artist", 0);
            ComicResponseDTO expected =  new ComicResponseDTO("TestComic", 2000, "Test Writer",
                    "Test Artist", null);
            when(comicServiceMock.save(newComic)).thenReturn(expected);
            //when
            ResponseEntity<?> actual = comicController.create(newComic);
            //then
            assertNotNull(actual);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }
    }

    @Nested
    class DeleteTest {
        @Test
        void should_ReturnValidResponseEntity_When_DeleteRequestIsValid() {
            //given
            String expected = "Some_name";
            when(comicServiceMock.delete(1)).thenReturn(expected);
            //when
            ResponseEntity<?> actual = comicController.delete(1);
            //then
            assertNotNull(actual);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.TEXT_PLAIN, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }
    }

    @Nested
    class EditTest {
        @Test
        void should_ReturnValidResponseEntity_When_UpdateRequestIsValid() {
            //given
            Map<String, Object> fields = new HashMap<>();
            fields.put("title", "Test");
            ComicResponseDTO expected = new ComicResponseDTO("Test", 2000, "Test Writer",
                    "Test Artist", null);
            when(comicServiceMock.update(1, fields)).thenReturn(expected);
            //when
            ResponseEntity<?> actual = comicController.edit(1, fields);
            //then
            assertNotNull(actual);
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertEquals(MediaType.APPLICATION_JSON, actual.getHeaders().getContentType());
            assertEquals(expected, actual.getBody());
        }
    }

}