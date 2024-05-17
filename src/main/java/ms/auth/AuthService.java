package ms.auth;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import ms.entities.User;
import ms.exception.PrimaryKeyViolationException;
import ms.jwt.JwtService;
import ms.entities.Role;
import ms.services.UserService;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

	@Autowired
	UserService userService;

	@Autowired
	JwtService jwtService;

	@Autowired
	PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        try {
			log.info("Iniciando login" + request.toString());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            Optional<User> users = userService.findByUsername(request.getUsername());
			log.info("Recuperando user desde login" + users.get().toString());
			if(users.isPresent()) {
				User user = users.get();
				log.info("usuario login "+ user.toString());
				String token = jwtService.getToken(user);
                log.info("token " + token);
				user.setToken(token);
				user.setIsActive(true);
				userService.updateUser(user);
				return AuthResponse.builder()
						.token(token)
						.code(200) // Código de éxito
						.message("Usuario autenticado exitosamente")
						.build();
			}else{
				log.warn("Usuario No encontrado " );
				return AuthResponse.builder()
						.code(403) // Código de estado 403 (Forbidden)
						.message("Error de autenticación Usuario No encontrado" )
						.build();
			}
        } catch (AuthenticationException ex) {
            // Manejar el error de autenticación y retornar una respuesta con el código de estado 403
            return AuthResponse.builder()
                    .code(403) // Código de estado 403 (Forbidden)
                    .message("Error de autenticación: " + ex.getMessage())
                    .build();
        }
    }

    public  AuthResponse register(RegisterRequest request) {
		User user = new User();
		try {
    	Optional<User> existingUser = userService.findByEmail(request.getUsername());
        
        // Verificar si el usuario ya existe
        if (!existingUser.isPresent()) {

		    user.setId(UUID.randomUUID());
			user.setName("name user");
			user.setEmail("name.user@email.cl");
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			user.setUsername(request.getUsername());
			user.setIsActive(true);
			user.setModified(new Date());
			if(user.getRole() == null)
				user.setRole( Role.USER);

			userService.createUser(user);
        }else{
			 throw new Exception("El usuario existe");
		}

        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .code(201) // Código de éxito
            .message("Usuario registrado correctamente")
            .build();
        
    	}catch (Exception ex) {
    	     return AuthResponse.builder()
    	                    .code(403) // Código de estado 403 (Forbidden)
    	                    .message("Error al consultar el usuario: " + ex.getMessage())
    	                    .build();
        }
    }

}
