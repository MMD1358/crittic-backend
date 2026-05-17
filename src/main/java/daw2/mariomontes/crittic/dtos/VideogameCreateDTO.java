package daw2.mariomontes.crittic.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VideogameCreateDTO {
    @NotBlank
    @Size(max = 150)
    private String title;

    private LocalDate releaseDate;

    @Size(max = 50)
    private String genre;

    @Size(max = 255)
    private String image;

    private String description;

    @NotNull
    private Integer authorId;
}
