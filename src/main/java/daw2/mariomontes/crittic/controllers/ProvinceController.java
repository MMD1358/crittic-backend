package org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Province`.
 * Utiliza `ProvinceDAO` para interactuar con la base de datos.
 */
@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    // DAO para gestionar las operaciones de las provincias en la base de datos
    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las provincias y las pasa como atributo al modelo para que sean
     * accesibles en la vista `province.html`.
     *
     * @param model Objeto del modelo para pasar datos a la vista.

     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de provincias.
     */
    @GetMapping
    public ResponseEntity<List<Province>> getAllProvinces() {
        logger.info("Solicitando la lista de todas las provincias...");
        try {
            List<Province> provinces = provinceRepository.findAll();
            logger.info("Se han encontrado {} provincias.", provinces.size());
            return ResponseEntity.ok(provinces);
        } catch (Exception e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Province> getProvinceById(@PathVariable Long id) {
        logger.info("Buscando provincia con ID {}", id);
        try {
            Optional<Province> province = provinceRepository.findById(id);
            if (province.isPresent()) {
                logger.info("Provincia con ID {} encontrada: {}", id, province.get());
                return ResponseEntity.ok(province.get());
            } else {
                logger.warn("No se encontró ninguna provincia con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            logger.error("Error al buscar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Muestra el formulario para crear una nueva provincia.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva provincia.");
        model.addAttribute("province", new Province());
        model.addAttribute("regions", regionRepository.findAll()); // Agregar lista de regiones para elegir
        return "province-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la provincia con ID {}", id);
        Optional<Province> provinceOpt = provinceRepository.findById(id);
        if (provinceOpt.isPresent()) {
            logger.warn("No se encontró la provincia con ID {}", id);
            model.addAttribute("errorMessage", "No se encontró la provincia.");
        } else {
            model.addAttribute("province", provinceOpt);
        }
        model.addAttribute("regions", regionRepository.findAll()); // Agregar lista de regiones para elegir
        return "province-form";
    }

    /**
     * Inserta una nueva provincia en la base de datos.
     *
     * @param province Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PostMapping
    public ResponseEntity<?> createProvince(@Valid @RequestBody Province province, Locale locale) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());
        try {
            if (provinceRepository.existsProvinceByCode(province.getCode())) {
                String errorMessage = messageSource.getMessage("msg.province-controller-insert.codeExist", null, locale);
                logger.warn("Error al crear provincia: {}", errorMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
            Province savedProvince = provinceRepository.save(province);
            logger.info("Provincia creada exitosamente con ID {}", savedProvince.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProvince);
        } catch (Exception e) {
            logger.error("Error al crear la provincia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la provincia.");
        }
    }

    /**
     * Actualiza una provincia existente en la base de datos.
     *
     * @param province Objeto que contiene los datos del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvince(@PathVariable Long id, @Valid @RequestBody Province province, Locale locale) {
        logger.info("Actualizando provincia con ID {}", id);
        try {
            //Verificar si la provincia existe
            Optional<Province> existingProvince = provinceRepository.findById(id);
            if (!existingProvince.isPresent()) {
                logger.warn("No se encontró ninguna provincia con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La provincia no existe.");
            }
            // Validar si el código ya pertenece a otra provincia
            if (provinceRepository.existsProvinceByCodeAndNotId(province.getCode(), id)) {
                String errorMessage = messageSource.getMessage("msg.province-controller-update.codeExist", null, locale);
                logger.warn("Error al actualizar provincia: {}", errorMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
            // Actualizar la provincia
            province.setId(id); // Asegurarse de que el ID no cambie
            Province updatedProvince = provinceRepository.save(province);
            logger.info("Provincia con ID {} actualizada exitosamente.", id);
            return ResponseEntity.ok(updatedProvince);
        } catch (Exception e) {
            logger.error("Error al actualizar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la provincia.");
        }
    }

    /**
     * Elimina una provincia de la base de datos.
     *
     * @param id ID de la provincia a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de provincias.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long id) {
        logger.info("Eliminando provincia con ID {}", id);
        try {
            // Verificar si la provincia existe
            if (!provinceRepository.existsById(id)) {
                logger.warn("No se encontró ninguna provincia con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La provincia no existe.");
            }
            // Eliminar la provincia
            provinceRepository.deleteById(id);
            logger.info("Provincia con ID {} eliminada exitosamente.", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la provincia.");
        }
    }
}