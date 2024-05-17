package ms.controller;

import lombok.extern.slf4j.Slf4j;
import ms.entities.Phone;
import ms.entities.User;
import ms.services.PhoneService;
import ms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PhoneService phoneService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users=  null;
        List<Map<String, Object>> userMaps = null;
        try {
            users = userService.findAllWithPhones();
            userMaps = users.stream()
                    .map(user -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", user.getId());
                        userMap.put("name", user.getName());
                        userMap.put("email", user.getEmail());
                        userMap.put("created", user.getCreated());
                        userMap.put("role", user.getRole());
                        userMap.put("modified", user.getModified());
                        userMap.put("lastlogin", user.getLastLogin());
                        userMap.put("isactive", user.getIsActive());
                        userMap.put("token", user.getToken());
                        // Mapeo de los teléfonos
                        List<Map<String, Object>> phoneMaps = user.getPhones().stream()
                                .map(phone -> {
                                    Map<String, Object> phoneMap = new HashMap<>();
                                    phoneMap.put("id", phone.getId());
                                    phoneMap.put("number", phone.getNumber());
                                    // Agrega otros campos del teléfono que necesites
                                    return phoneMap;
                                })
                                .collect(Collectors.toList());
                        userMap.put("phones", phoneMaps);
                        log.info("phones " + phoneMaps.toString());
                        return userMap;
                    })
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "Hubo un problema en la solicitud" + e.getMessage()));
        }
        return new ResponseEntity<>(userMaps, HttpStatus.OK);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        log.info("PathVariable UUID id : {}" + id);
    
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                log.info("Usuario no encontrado con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("mensaje", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            log.error("Error al obtener el usuario con ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "Error interno del servidor"));
        }
    }


    // Crear un nuevo usuario

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (user.getPhones() == null || user.getPhones().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("mensaje", "Error al crear el usuario: debe tener al menos un teléfono"));
            }
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setIsActive(false);
            User newUser = userService.createUser(user);
            if(!user.getPhones().isEmpty()) {
                Set<Phone> userPhones = user.getPhones();
                newUser.setPhones(userPhones);
                for (Phone phone : userPhones) {
                    phone.setUser_id(user.getId());
                    phone.setUser(user);
                    phoneService.createPhone(phone);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            log.error("Error al crear el usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "Error al crear el usuario: " + e.getMessage()));
        }
    }


    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User userDetails) {
        try {
            Optional<User> user = userService.getUserById(id);
            User updatedUser = null;
            if(user.isPresent()) {
                updatedUser = user.get();
                updatedUser.setName(userDetails.getName());
                updatedUser.setEmail(userDetails.getEmail());
                updatedUser.setPassword(new BCryptPasswordEncoder().encode(userDetails.getPassword()));
            }else{
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body((User) Collections.singletonMap("mensaje", "Usuario no encontrado"));
            }
            userService.updateUser(updatedUser);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
              log.error(e.getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "Error al actualizar el usuario: " + e.getMessage()));
        }
    }


    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                List<Phone> userPhones = phoneService.getPhonesByUserId(user.get().getId());
                for (Phone phone : userPhones) {
                    phoneService.deletePhone(phone.getId());
                }
                userService.deleteUser(id);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body((User) Collections.singletonMap("mensaje", "Usuario no encontrado"));
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("mensaje", "Error al eliminar el usuario: " + e.getMessage()));
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
