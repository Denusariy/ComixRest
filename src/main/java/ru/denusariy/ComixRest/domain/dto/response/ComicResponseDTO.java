package ru.denusariy.ComixRest.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ComicResponseDTO {
    @Schema(description = "Название комикса")
    private String title;
    @Schema(description = "Год выпуска комикса")
    private int year;
    @Schema(description = "Сценарист")
    private String writer;
    @Schema(description = "Художник")
    private String artist;
    @JsonIgnore
    private BookResponseDTO book;
}
