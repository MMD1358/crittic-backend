package daw2.mariomontes.crittic.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReviewCreateDTO {
    @NotNull
    private Integer videogameId;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private BigDecimal rating;
    @NotBlank
    private String content;
}
