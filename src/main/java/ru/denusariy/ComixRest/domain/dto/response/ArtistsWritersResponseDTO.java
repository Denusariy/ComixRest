package ru.denusariy.ComixRest.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ArtistsWritersResponseDTO {
    @Schema(description = "Список всех сценаристов")
    private List<String> writers;
    @Schema(description = "Список всех художников")
    private List<String> artists;
}
