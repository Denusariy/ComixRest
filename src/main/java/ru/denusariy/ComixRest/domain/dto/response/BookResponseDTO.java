package ru.denusariy.ComixRest.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.denusariy.ComixRest.domain.enums.Format;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    @Schema(description = "Название книги")
    private String title;
    @Schema(description = "Год издания книги")
    private int year;
    @Schema(description = "Формат книги")
    private Format format;
    @Schema(description = "Наличие альтернативной обложки")
    private boolean isAltCover;
    @Schema(description = "Наличие автографа")
    private boolean isAutograph;
    @Schema(description = "Чей автограф")
    private String signature;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Schema(description = "Содержимое")
    private List<ComicResponseDTO> comics;
}
