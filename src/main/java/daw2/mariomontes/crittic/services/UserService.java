package daw2.mariomontes.crittic.services;

import daw2.mariomontes.crittic.dtos.RegisterRequestDTO;
import daw2.mariomontes.crittic.dtos.UserDTO;
import daw2.mariomontes.crittic.entities.Role;
import daw2.mariomontes.crittic.entities.User;
import daw2.mariomontes.crittic.mappers.UserMapper;
import daw2.mariomontes.crittic.repositories.RoleRepository;
import daw2.mariomontes.crittic.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Long getIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }

    public UserDTO getUserById(Long id) {
        return userMapper.toDTO(findEntityById(id));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        Role role = roleRepository.findByName("USER")
                .or(() -> roleRepository.findByName("ROLE_USER"))
                .orElseThrow(() -> new IllegalStateException("USER role does not exist."));

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);

        user.setFirstName(
                dto.getFirstName() == null || dto.getFirstName().isBlank()
                        ? dto.getUsername()
                        : dto.getFirstName()
        );

        user.setLastName(
                dto.getLastName() == null || dto.getLastName().isBlank()
                        ? "User"
                        : dto.getLastName()
        );

        user.setImage(dto.getImage());
        user.setDescription("");
        user.setRoles(new HashSet<>(List.of(role)));

        return userMapper.toDTO(userRepository.save(user));
    }

    public User findEntityById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist."));
    }

    public User findEntityByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist."));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}