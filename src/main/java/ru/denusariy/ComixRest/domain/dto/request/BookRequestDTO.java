package ru.denusariy.ComixRest.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.denusariy.ComixRest.domain.enums.Format;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {
    @Schema(description = "Название книги")
    @NotBlank(message = "Название книги не должно быть пустым")
    private String title;
    @Schema(description = "Год издания книги")
    @Min(value = 1900, message = "Год издания должен быть больше 1900")
    private int year;
    @Enumerated(EnumType.STRING)
    @Schema(description = "Формат книги")
    private Format format;
    @Schema(description = "Наличие альтернативной обложки")
    private boolean isAltCover;
    @Schema(description = "Наличие автографа")
    private boolean isAutograph;
    @Schema(description = "Чей автограф")
    private String signature;
}
