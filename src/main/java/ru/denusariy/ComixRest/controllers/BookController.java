package ru.denusariy.ComixRest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.denusariy.ComixRest.domain.dto.request.BookRequestDTO;
import ru.denusariy.ComixRest.domain.dto.response.BookResponseDTO;
import ru.denusariy.ComixRest.services.BookService;
import javax.validation.Valid;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Получение списка всех книг. Пагинация по ключам \"page\" и \"size\" в запросе",
            responses = @ApiResponse(responseCode = "200", description = "OK"))
    public ResponseEntity<Page<BookResponseDTO>> getAllBooks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookService.findAllWithPagination(page, size));
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Получение книги по id", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<BookResponseDTO> show(@PathVariable("id") int id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookService.findOne(id));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Сохранение новой книги. Присутствует валидация", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    public ResponseEntity<BookResponseDTO> create(@RequestBody @Valid BookRequestDTO bookRequestDTO) {
        BookResponseDTO responseDTO = bookService.save(bookRequestDTO);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDTO);
    }

    @PatchMapping (value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Изменение книги по id", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<BookResponseDTO> edit(@PathVariable("id") int id,
                                                @RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(bookService.update(id, fields));
    }
    @DeleteMapping(value = "/{id}", produces = {MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Удаление книги по id", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(bookService.delete(id));
    }
}
