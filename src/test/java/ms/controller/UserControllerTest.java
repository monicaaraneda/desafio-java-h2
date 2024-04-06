package ms.controller;


import ms.config.TestSecurityConfig;
import ms.entities.User;
import ms.jwt.JwtService;
import ms.services.PhoneService;
import ms.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertTrue;


@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @MockBean
    private PhoneService phoneService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    ms.jwt.JwtAuthenticationFilter jwtAuthenticationFilter;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.fromString("a0b1c2d3-e4f5-6789-0123-456789abcdef");
        user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("$2a$10$CvnTvMf85SNMku/jNKzlQenfpxz7V0lyiQxK94KREkexLMSf/OW0e");
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(jwtAuthenticationFilter)
                //.apply(springSecurity())
                .build();
    }

    private MockMvcConfigurer springSecurity() {
        return null;
    }

    @Test
    void whenGetAllUsers_thenReturns200Or404() throws Exception {
        when(userService.findAllWithPhones()).thenReturn(Collections.emptyList());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/users"))
                .andExpect(result -> assertTrue(result.getResponse().getStatus() == org.springframework.http.HttpStatus.OK.value() ||
                        result.getResponse().getStatus() == org.springframework.http.HttpStatus.NOT_FOUND.value()))
                .andExpect(result -> {
                    if (result.getResponse().getStatus() == org.springframework.http.HttpStatus.OK.value()) {
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.instanceOf(List.class))
                                .match(result);
                    } else if (result.getResponse().getStatus() == org.springframework.http.HttpStatus.NOT_FOUND.value()) {
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.mensaje", org.hamcrest.Matchers.is("Hubo un problema en la solicitud"))
                                .match(result);
                    }
                });

        verify(userService).findAllWithPhones();
    }


    @Test
    void whenGetUserById_thenReturns404() throws Exception {
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(userId);
    }

    // Más pruebas para createUser, updateUser, deleteUser...

    // Nota: para los métodos POST, PUT y DELETE, necesitarás configurar el content del request
    // y posiblemente el ObjectMapper para convertir los objetos en JSON.
}
