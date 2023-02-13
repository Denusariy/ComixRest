package ru.denusariy.ComixRest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.denusariy.ComixRest.domain.dto.request.ComicRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.ComicResponseDTO;
import ru.denusariy.ComixRest.services.ComicService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comics")
public class ComicController {
    private final ComicService comicService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Сохранение нового комикса. Присутствует валидация. В теле запроса необходимо указать id книги " +
            "в поле \"bookId\"", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND, не найдена книга с указанным id")
    })
    public ResponseEntity<ComicResponseDTO> create(@RequestBody @Valid ComicRequestDTO comicRequestDTO) {
        return ResponseEntity.ok(comicService.save(comicRequestDTO));
    }

    @PatchMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Изменение комикса по id. Присутствует валидация", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<ComicResponseDTO> edit(@PathVariable("id") int id,
                                                 @RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok(comicService.update(id, fields));
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Удаление комикса по id", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        return ResponseEntity.ok(comicService.delete(id));
    }
}
