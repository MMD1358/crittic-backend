package daw2.mariomontes.crittic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "videogame")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Videogame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videogame_id")
    private Integer videogameId;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Size(max = 50)
    @Column(length = 50)
    private String genre;

    @Size(max = 255)
    @Column(length = 255)
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
