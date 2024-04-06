package ms.services;

import lombok.extern.slf4j.Slf4j;
import ms.entities.User;
import ms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAllWithPhones() {
        return userRepository.findAllWithPhones();
    }

    @Transactional
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) throws Exception {

        if (!user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new Exception("El formato del correo electrónico es incorrecto.");
        }
        // Verificar si el correo ya está registrado
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("El correo ya está registrado.");
        }

        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {return userRepository.findByEmail(email); }

   // public Optional<User> findByUsername(String username) {log.info(username);return userRepository.findByUsername(username); }

    public Optional<User> findByUsername(String username) {
        log.info("Buscando usuario con username: " + username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            log.info("Usuario encontrado: " + user);
        } else {
            log.info("Usuario no encontrado con username: " + username);
        }
        return userOpt;
    }

}
