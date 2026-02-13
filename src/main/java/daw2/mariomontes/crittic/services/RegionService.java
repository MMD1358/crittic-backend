package org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.mappers.RegionMapper;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;

@Service
public class RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Obtiene todas las regiones con paginación y las convierte en una página de RegionDTO.
     *
     * @param pageable Objeto de paginación que define la página, el tamaño y la ordenación.
     * @return Página de RegionDTO.
     */
    public Page<RegionDTO> getAllRegions(Pageable pageable) {
        logger.info("Solicitando todas las regiones con paginación: página {}, tamaño {}",
                pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Region> regions = regionRepository.findAll(pageable);
            logger.info("Se han encontrado {} regiones en la página actual.", regions.getNumberOfElements());
            return regions.map(regionMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al obtener la lista paginada de regiones: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene una región por su ID y la convierte en un RegionDTO.
     *
     * @param id Identificador único de la región.
     * @return Optional con el RegionDTO correspondiente.
     */
    public Optional<RegionDTO> getRegionById(Long id) {
        Optional<Region> region = regionRepository.findById(id);
        return region.map(regionMapper::toDTO);
    }

    public RegionDTO createRegion(RegionCreateDTO createDTO) {
        logger.info("Creando una nueva región con código {}", createDTO.getCode());

        // Verificar si ya existe una región con ese mismo código
        if (regionRepository.existsRegionByCode(createDTO.getCode())) {
            throw new IllegalArgumentException("El código de la región ya existe.");
        }

        // Procesar la imagen si se proporciona
        String fileName = null;
        if (createDTO.getImageFile() != null && !createDTO.getImageFile().isEmpty()) {
            fileName = fileStorageService.saveFile(createDTO.getImageFile());
            if (fileName == null) {
                throw new RuntimeException("Error al guardar la imagen.");
            }
        }

        // Crear la entidad Region
        Region region = regionMapper.toEntity(createDTO);
        region.setImage(fileName);

        // Guardar la nueva región
        Region savedRegion = regionRepository.save(region);
        logger.info("Región creada exitosamente con ID {}", savedRegion.getId());

        // Convertir la entidad guardada a DTO y devolverla
        return regionMapper.toDTO(savedRegion);
    }

    public RegionDTO updateRegion(Long id, RegionCreateDTO updateDTO) {
        logger.info("Actualizando región con ID {}", id);

        // Buscar la región existente
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La región no existe."));

        // Verificar si el código ya está en uso por otra categoria
        if (regionRepository.existsRegionByCodeAndNotId(updateDTO.getCode(), id)) {
            throw new IllegalArgumentException("El código de la región ya está en uso.");
        }

        // Procesar la imagen si se proporciona
        String fileName = existingRegion.getImage(); // Conservar la imagen existente por defecto
        if (updateDTO.getImageFile() != null && !updateDTO.getImageFile().isEmpty()) {
            fileName = fileStorageService.saveFile(updateDTO.getImageFile());
            if (fileName == null) {
                throw new RuntimeException("Error al guardar la nueva imagen.");
            }
        }

        // Actualizar los datos de la región
        existingRegion.setCode(updateDTO.getCode());
        existingRegion.setImage(fileName);

        // Guardar los cambios
        Region updatedRegion = regionRepository.save(existingRegion);
        logger.info("Región con ID {} actualizada exitosamente.", updatedRegion.getId());

        // Convertir la entidad actualizada a DTO y devolverla
        return regionMapper.toDTO(updatedRegion);
    }

    /**
     * Elimina una región específica por su ID.
     *
     * @param id ID de lar egión a eliminar.
     * @throws IllegalArgumentException Si la región no existe.
     */
    public void deleteRegion(Long id) {
        logger.info("Buscando región con ID{}", id);

        // Buscar la región
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La región no existe."));

        // Eliminar la imagen asociada si existe
        if (region.getImage() != null && !region.getImage().isEmpty()) {
            fileStorageService.deleteFile(region.getImage());
            logger.info("Imagen asociada a la región con ID {} eliminada.", id);
        }

        // Eliminar la región
        regionRepository.deleteById(id);
        logger.info("Región con ID {} eliminada exitosamente.", id);
    }

}