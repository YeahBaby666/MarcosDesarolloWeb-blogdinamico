package utp.blogdinamico.blogweb.controlador;

import utp.blogdinamico.blogweb.modelos.pagos.TipoMonetizacion;
import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import utp.blogdinamico.blogweb.modelos.principales.Publicacion;
import utp.blogdinamico.blogweb.modelos.principales.Categoria;
import utp.blogdinamico.blogweb.modelos.principales.Comentario;
import utp.blogdinamico.blogweb.modelos.servicio.ServicioPublicacion;
import utp.blogdinamico.blogweb.modelos.servicio.ServicioUsuario;
import utp.blogdinamico.blogweb.modelos.repository.CategoriaRepository;
import utp.blogdinamico.blogweb.modelos.servicio.ServicioSatinizacion;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.List;

/**
 * Controlador principal para manejar las peticiones relacionadas con las
 * Publicaciones.
 */
@Controller
public class ControladorPublicacion {

    private final ServicioPublicacion servicioPublicacion;
    private final ServicioUsuario usuarioService;
    private final CategoriaRepository categoriaRepository;
    private final ServicioSatinizacion servicioSatinizacion;

    public ControladorPublicacion(ServicioPublicacion sp, ServicioUsuario us, CategoriaRepository cr, ServicioSatinizacion ssn) {
        this.servicioPublicacion = sp;
        this.usuarioService = us;
        this.categoriaRepository = cr;
        this.servicioSatinizacion = ssn;
    }

    /**
     * Muestra la página de inicio, cargando todas las publicaciones y la información
     * completa del usuario autenticado.
     */
    @GetMapping("/")
    public String paginaDeInicio(@AuthenticationPrincipal UserDetails userDetails, Model modelo) {
        
        List<Publicacion> publicaciones = servicioPublicacion.obtenerTodas();
        modelo.addAttribute("publicaciones", publicaciones);

        Usuario usuarioActual = null;
        boolean sesionActiva = (userDetails != null);

        if (sesionActiva) {
            Optional<Usuario> usuarioOpcional = usuarioService.obtenerPorNombreUsuario(userDetails.getUsername());
            
            if (usuarioOpcional.isPresent()) {
                usuarioActual = usuarioOpcional.get();
            }
        }

        modelo.addAttribute("sesionActiva", sesionActiva);
        modelo.addAttribute("usuarioActual", usuarioActual);

        return "index";
    }

    /**
     * Muestra la página de detalle de una publicación individual.
     * Carga el historial de comentarios y la información del usuario logueado.
     */
    @GetMapping("/publicacion/{id}")
    public String verPublicacion(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, Model modelo) {
        
        Optional<Publicacion> publicacionOpcional = servicioPublicacion.obtenerPorId(id);

        if (publicacionOpcional.isPresent()) {
            Publicacion publicacion = publicacionOpcional.get();
            
            Usuario usuarioActual = null;
            if (userDetails != null) {
                Optional<Usuario> usuarioOpcional = usuarioService.obtenerPorNombreUsuario(userDetails.getUsername());
                usuarioActual = usuarioOpcional.orElse(null);
            }
            
            // 1. Obtener y pasar la lista de comentarios para la carga inicial (historial)
            List<Comentario> comentarios = servicioPublicacion.obtenerComentariosPorPublicacion(id);
            
            modelo.addAttribute("publicacion", publicacion);
            modelo.addAttribute("comentarios", comentarios); // Historial de Comentarios
            modelo.addAttribute("sesionActiva", (userDetails != null));
            modelo.addAttribute("usuarioActual", usuarioActual); 
            
            return "publicacion-detalle";
        } else {
            return "redirect:/";
        }
    }

    /**
     * Muestra el formulario para crear una nueva publicación.
     */
    @GetMapping("/publicaciones/crear")
    public String mostrarFormularioDeCreacion(@AuthenticationPrincipal UserDetails userDetails, Model modelo) {
        
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        Optional<Usuario> usuarioOpcional = usuarioService.obtenerPorNombreUsuario(userDetails.getUsername());
        Usuario usuarioActual = usuarioOpcional.orElse(null);

        if (usuarioActual == null) {
            return "redirect:/logout"; 
        }

        List<Categoria> categorias = categoriaRepository.findAll();

        modelo.addAttribute("usuarioActual", usuarioActual);
        modelo.addAttribute("categorias", categorias);
        return "crear-publicacion";
    }

    /**
     * Procesa los datos del formulario para crear una nueva publicación.
     */
    @PostMapping("/publicaciones/crear")
    public String procesarCreacionDePublicacion(@RequestParam String titulo,
            @RequestParam String resumen,
            @RequestParam String contenido,
            @RequestParam TipoMonetizacion tipoMonetizacion,
            @RequestParam Long idCategoria,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        String contenidoLimpio = servicioSatinizacion.sanitizar(contenido);

        try {
            servicioPublicacion.crearPublicacion(
                userDetails.getUsername(),
                titulo,
                resumen,
                contenidoLimpio,
                tipoMonetizacion,
                idCategoria
            );
        } catch (Exception e) {
            System.err.println("Error al crear publicación: " + e.getMessage());
            return "redirect:/publicaciones/crear?error=true"; 
        }

        return "redirect:/";
    }
    
    /**
     * API POST para calificar una publicación (método de formulario HTML tradicional).
     */
    @PostMapping("/api/publicaciones/{idPublicacion}/calificar")
    public String calificarPublicacion(
        @PathVariable Long idPublicacion,
        @RequestParam int estrellas,
        @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return "redirect:/login"; 
        }
        
        try {
            servicioPublicacion.guardarCalificacion(
                idPublicacion, 
                userDetails.getUsername(), 
                estrellas
            );
            
            // Redirección con éxito
            return "redirect:/publicacion/" + idPublicacion + "?califExito=true";

        } catch (Exception e) {
            System.err.println("Error al guardar calificación: " + e.getMessage());
            // Redirección con error
            return "redirect:/publicacion/" + idPublicacion + "?califError=true";
        }
    }
    
    /**
     * Maneja la recepción de un nuevo comentario (POST de formulario tradicional).
     * Guarda el comentario y redirige al detalle de la publicación.
     */
    @PostMapping("/publicacion/{idPublicacion}/comentar")
    public String manejarComentarioPost(
        @PathVariable Long idPublicacion,
        @RequestParam String contenido,
        @AuthenticationPrincipal UserDetails userDetails,
        RedirectAttributes atributosRedireccion) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            servicioPublicacion.guardarComentario(
                idPublicacion,
                userDetails.getUsername(),
                contenido
            );
            // Redirección con éxito
            atributosRedireccion.addFlashAttribute("comentarioExito", true);
        } catch (Exception e) {
            System.err.println("Error al guardar el comentario (POST): " + e.getMessage());
            atributosRedireccion.addFlashAttribute("comentarioError", true);
        }

        // Siempre redirige a la página de detalle para evitar re-envío del formulario
        return "redirect:/publicacion/" + idPublicacion;
    }
}
