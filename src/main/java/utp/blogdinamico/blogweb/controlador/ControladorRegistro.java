
package utp.blogdinamico.blogweb.controlador;

import utp.blogdinamico.blogweb.modelos.servicio.ServicioUsuario;
import utp.blogdinamico.blogweb.modelos.principales.Usuario; // Necesitamos la entidad Usuario para el registro
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Importaciones necesarias de Spring Security
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Controlador dedicado a manejar la creación (registro) de nuevos usuarios.
 */
@Controller
public class ControladorRegistro {

    private final ServicioUsuario usuarioService;
    private final UserDetailsService userDetailsService; // Para cargar el UserDetails del nuevo usuario

    // El constructor debe inyectar el UserDetailsService
    public ControladorRegistro(ServicioUsuario usuarioService, UserDetailsService userDetailsService) {
        this.usuarioService = usuarioService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/registro")
    public String mostrarPaginaDeRegistro() {
        return "registro";
    }

    
    @PostMapping("/registro")
    public String manejarRegistro(@RequestParam String nombreUsuario, 
                                  @RequestParam String contrasena, 
                                  RedirectAttributes atributosRedireccion) {
        
        

        // 1. Intentar registrar el usuario y manejar la posible excepción (ej: usuario ya existe)
        try {
            // Asumimos que el servicio ahora retorna la entidad Usuario persistida al ser exitoso.
            // Si el nombre de usuario ya existe, el servicio debería lanzar una excepción (ej: DataIntegrityViolationException).
            @SuppressWarnings("unused") // Evitamos la advertencia de variable no usada
            Usuario usuarioRegistrado = usuarioService.registrarNuevoUsuario(nombreUsuario, contrasena);

        } catch (Exception e) {
            // Si hay un error (ej: nombre de usuario ya existe), volvemos al registro
            String errorMessage = "Error de registro. El nombre de usuario ya podría estar en uso.";
            System.err.println("Error al registrar usuario: " + e.getMessage());
            atributosRedireccion.addFlashAttribute("errorRegistro", errorMessage);
            return "redirect:/registro";
        }

        // 2. Autenticación Programática (Loguear al usuario automáticamente)
        try {
            // Cargar los detalles de seguridad del usuario recién creado
            UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);
            
            // Crear el token de autenticación
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            
            // Establecer el contexto de seguridad (¡El usuario ahora está logueado en la sesión!)
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Redirigir a la página principal, saltándose el formulario de login
            return "redirect:/";

        } catch (Exception e) {
            // Si falla la autenticación (no debería pasar si el registro fue exitoso)
            System.err.println("Error durante el login programático después del registro: " + e.getMessage());
            atributosRedireccion.addFlashAttribute("errorLogin", "Registro exitoso, pero login automático fallido. Intenta iniciar sesión manualmente.");
            return "redirect:/login";
        }
    }
}
