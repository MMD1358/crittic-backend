package org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.controllers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Request;
import jakarta.validation.constraints.NotNull;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.dtos.UserDTO;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.services.UserService;
import org.iesalixar.daw2.mariomontes.dwese_ticket_logger_api.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil  jwtUtil;

    @GetMapping
    public ResponseEntity<@NotNull UserDTO> getUser(@RequestHeader("Authorization") String tokenHeader) {
        logger.info("Solicitando la información del usuario logueado");

        String token = tokenHeader.replace("Bearer", "");

        Long id = jwtUtil.extractClaim(token, claims -> claims.get("id", Long.class));

        try {
            UserDTO userDTO = userService.getUserById(id);

            logger.info("Se han encontrado el usuario con identificador {}.", id);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            logger.error("Error al listar el usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
