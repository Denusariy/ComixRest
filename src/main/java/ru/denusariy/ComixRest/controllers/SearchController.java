package ru.denusariy.ComixRest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.denusariy.ComixRest.domain.dto.response.ArtistsWritersResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.BookResponseDTO;
import ru.denusariy.ComixRest.domain.dto.response.ComicResponseDTO;
import ru.denusariy.ComixRest.services.BookService;
import ru.denusariy.ComixRest.services.ComicService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final BookService bookService;
    private final ComicService comicService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Получить списки всех художников и сценаристов для создания выпадающих списков",
            responses = @ApiResponse(responseCode = "200", description = "OK"))
    public ResponseEntity<ArtistsWritersResponseDTO> getArtistsWriters() {
        return ResponseEntity.ok(comicService.findArtistsWriters());
    }

    @GetMapping(value = "/writer", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Получить список комиксов с указанным в запросе сценаристом, ключ \"query\"", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    public ResponseEntity<List<ComicResponseDTO>> searchByWriter(@RequestParam("query") String query) {
        return ResponseEntity.ok(comicService.findByWriter(query));
    }

    @GetMapping(value = "/artist", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Получить список комиксов с указанным в запросе художником, ключ \"query\"", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    public ResponseEntity<List<ComicResponseDTO>> searchByArtist(@RequestParam("query") String query) {
        return ResponseEntity.ok(comicService.findByArtist(query));
    }

    @GetMapping(value = "/title", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Получить список книг, содержащих в названии указанную строку, ключ \"query\"", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    public ResponseEntity<List<BookResponseDTO>> searchByTitle(@RequestParam("query") String query) {
        return ResponseEntity.ok(bookService.findByTitle(query));
    }

    @GetMapping(value = "/alt_cover", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Получить список книг с альтернативной обложкой",
            responses = @ApiResponse(responseCode = "200", description = "OK"))
    public ResponseEntity<List<BookResponseDTO>> searchByCover() {
        return ResponseEntity.ok(bookService.findBookWithAltCover());
    }

    @GetMapping(value = "/autograph", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Получить список книг с автографом",
            responses = @ApiResponse(responseCode = "200", description = "OK"))
    public ResponseEntity<List<BookResponseDTO>> searchByAutograph() {
        return ResponseEntity.ok(bookService.findBookWithAutograph());
    }
}
