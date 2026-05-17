package daw2.mariomontes.crittic.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class RatingDTO {
        @Min(1)
        @Max(5)
        private Integer rating;
    }