package daw2.mariomontes.mensajeria.repositories;

import daw2.mariomontes.mensajeria.entities.Envio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioRepository extends JpaRepository<Envio, Long> {

    Page<Envio> findAll(Pageable pageable);
/*
    Page<Envio> findByEstadoContainingIgnoreCase(String estado, Pageable pageable);
*/
    Page<Envio> findByDescripcionContainingIgnoreCase(String descripcion, Pageable pageable);

    long countByEstado(String estado);

}