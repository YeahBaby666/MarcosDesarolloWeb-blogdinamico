package utp.blogdinamico.blogweb.modelos.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador dedicado a manejar la vista del formulario de login.
 * No maneja el proceso POST de autenticación; eso lo hace Spring Security.
 */
@Controller
public class ControladorLogin {

    /**
     * Muestra la página de login y pasa mensajes de error o logout al modelo.
     * Spring Security redirige aquí en caso de fallo (?error) o cierre de sesión (?logout).
     * * @param error Indica si el login falló (añadido por Spring Security).
     * @param logout Indica si el usuario cerró sesión (añadido por Spring Security).
     * @param modelo Objeto para pasar datos a la vista de Thymeleaf.
     * @return El nombre de la plantilla de Thymeleaf ("login").
     */
    @GetMapping("/login")
    public String mostrarPaginaDeLogin(
        @RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "logout", required = false) String logout,
        Model modelo) {

        // Si Spring Security detecta un error de autenticación, añade la variable 'error'
        if (error != null) {
            modelo.addAttribute("errorLogin", true);
        }

        // Si Spring Security completa el cierre de sesión, añade la variable 'logout'
        if (logout != null) {
            modelo.addAttribute("logoutExitoso", true);
        }
        
        return "login";
    }
}
