package ru.denusariy.ComixRest.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
public class ComicRequestDTO {
    @Schema(description = "Название комикса")
    @NotBlank(message = "Название комикса не должно быть пустым")
    private String title;
    @Schema(description = "Год выпуска комикса")
    @Min(value = 1900, message = "Год выпуска должен быть больше 1900")
    private int year;
    @Schema(description = "Сценарист")
    @NotBlank(message = "У комикса должен быть сценарист")
    private String writer;
    @Schema(description = "Художник")
    @NotBlank(message = "У комикса должен быть художник")
    private String artist;
    @Schema(description = "id книги, в которой создаем комикс")
    private int bookId;
}
