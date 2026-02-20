package daw2.mariomontes.mensajeria.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false, length = 200)
    private String descripcion;

    @NotEmpty
    @Column(name = "direccion_origen", nullable = false, length = 200)
    private String direccionOrigen;

    @NotEmpty
    @Column(name = "direccion_destino", nullable = false, length = 200)
    private String direccionDestino;

    @NotEmpty
    @Column(nullable = false, length = 50)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @NotNull
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_repartidor")
    private Repartidor repartidor;

    public Envio(String descripcion, String direccionOrigen, String direccionDestino, String estado) {
        this.descripcion = descripcion;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.estado = estado;
    }
}