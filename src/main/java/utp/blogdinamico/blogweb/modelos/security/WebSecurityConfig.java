package utp.blogdinamico.blogweb.modelos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración principal de Spring Security para definir reglas de acceso,
 * codificación de contraseñas y el proveedor de autenticación.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Define el bean para el Codificador de Contraseñas (BCrypt).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define las reglas de seguridad a nivel HTTP (autorización de rutas).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Rutas públicas: Inicio, Login, Registro, y estáticos
                        .requestMatchers("/login", "/registro")
                        .permitAll()                        

                        // Cualquier otra solicitud requiere autenticación por defecto
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                        .logoutSuccessUrl("/login?logout"));

        return http.build();
    }
}
