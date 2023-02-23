package ru.denusariy.ComixRest.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistsWritersResponseDTO {
    @Schema(description = "Список всех сценаристов")
    private List<String> writers;
    @Schema(description = "Список всех художников")
    private List<String> artists;
}
