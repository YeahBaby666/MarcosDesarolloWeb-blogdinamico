package utp.blogdinamico.blogweb.modelos.servicio;

import utp.blogdinamico.blogweb.modelos.pagos.Monetizacion;
import utp.blogdinamico.blogweb.modelos.pagos.TipoMonetizacion;
import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import utp.blogdinamico.blogweb.modelos.principales.Publicacion;
import utp.blogdinamico.blogweb.modelos.principales.Categoria;
import utp.blogdinamico.blogweb.modelos.principales.Comentario;
import utp.blogdinamico.blogweb.modelos.otros.Calificacion;
import utp.blogdinamico.blogweb.modelos.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Capa de servicio para la lógica de negocio relacionada con las Publicaciones,
 * Comentarios y Calificaciones.
 * Centraliza la creación, lectura y actualización de posts en la base de datos.
 */
@Service
public class ServicioPublicacion {

    private final PublicacionRepository publicacionRepository;
    private final MonetizacionRepository monetizacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ComentarioRepository comentarioRepository;
    private final CalificacionRepository calificacionRepository;

    public ServicioPublicacion(PublicacionRepository publicacionRepository,
            MonetizacionRepository monetizacionRepository,
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            ComentarioRepository comentarioRepository,
            CalificacionRepository calificacionRepository) {
        this.publicacionRepository = publicacionRepository;
        this.monetizacionRepository = monetizacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.comentarioRepository = comentarioRepository;
        this.calificacionRepository = calificacionRepository;
    }

    /**
     * Devuelve todas las publicaciones.
     */
    public List<Publicacion> obtenerTodas() {
        return publicacionRepository.findAll();
    }

    /**
     * Busca una publicación por su ID.
     */
    public Optional<Publicacion> obtenerPorId(Long id) {
        return publicacionRepository.findById(id);
    }

    /**
     * Devuelve todos los comentarios de una publicación específica, ordenados por
     * fecha.
     */
    @Transactional(readOnly = true)
    public List<Comentario> obtenerComentariosPorPublicacion(Long idPublicacion) {
        // En una aplicación real, se usaría un método específico en el repositorio:
        // return
        // comentarioRepository.findByPublicacionIdOrderByFechaCreacionDesc(idPublicacion);

        // Simulamos la búsqueda y ordenación, cargando todos los comentarios para
        // asegurar que no haya LazyInitializationException en Thymeleaf:
        return comentarioRepository.findByPublicacionIdOrderByFechaCreacionDesc(idPublicacion);
    }

    /**
     * Crea y guarda una nueva publicación, reutilizando una monetización existente
     * si es posible.
     * Versión robusta para máxima compatibilidad.
     */
    @Transactional
    public Publicacion crearPublicacion(
            String username,
            String titulo,
            String resumen,
            String contenido,
            TipoMonetizacion tipoMonetizacion,
            Long idCategoria) {

        // 1. Obtener Entidades Relacionadas (Sin cambios)
        Usuario autor = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado para el usuario: " + username));
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCategoria));

        // 2. Obtener o Crear Monetizacion (Lógica Mejorada y a prueba de balas)

        // Primero, intentamos buscar si ya existe.
        Optional<Monetizacion> monetizacionExistenteOpt = monetizacionRepository.findByTipo(tipoMonetizacion);

        Monetizacion monetizacionParaPublicacion; // Declaramos la variable que usaremos.

        if (monetizacionExistenteOpt.isPresent()) {
            // Si el Optional contiene un valor (es decir, la encontramos en la BD)...
            monetizacionParaPublicacion = monetizacionExistenteOpt.get(); // ...la usamos.
        } else {
            // Si el Optional está vacío (no se encontró en la BD)...
            Monetizacion nuevaMonetizacion = new Monetizacion();
            nuevaMonetizacion.setTipo(tipoMonetizacion);
            // ...creamos una nueva, la guardamos, y usamos esa versión recién guardada.
            monetizacionParaPublicacion = monetizacionRepository.save(nuevaMonetizacion);
        }

        // 3. Crear la Entidad Publicacion (Ahora usando nuestra variable segura)
        Publicacion nuevaPublicacion = new Publicacion();
        nuevaPublicacion.setTitulo(titulo);
        nuevaPublicacion.setResumen(resumen);
        nuevaPublicacion.setContenido(contenido);
        nuevaPublicacion.setAutor(autor);
        nuevaPublicacion.setCategoria(categoria);
        nuevaPublicacion.setMonetizacion(monetizacionParaPublicacion); // Usamos la monetización correcta
        nuevaPublicacion.setFechaCreacion(LocalDateTime.now());
        nuevaPublicacion.setCalificacionPromedio(0.0);

        // 4. Guardar la Publicacion y devolver la entidad persistida (Sin cambios)
        return publicacionRepository.save(nuevaPublicacion);
    }

    /**
     * Guarda un nuevo comentario asociado a una publicación y un usuario.
     * Este método es llamado desde el ComentarioMessageController.
     * 
     * @return La entidad Comentario persistida y con las relaciones cargadas.
     */
    @Transactional
    public Comentario guardarComentario(Long idPublicacion, String username, String contenido) {
        Optional<Publicacion> publicacionOpcional = publicacionRepository.findById(idPublicacion);
        Optional<Usuario> usuarioOpcional = usuarioRepository.findByNombreUsuario(username);

        if (publicacionOpcional.isEmpty()) {
            throw new IllegalArgumentException("Publicación no encontrada.");
        }
        if (usuarioOpcional.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        Publicacion publicacion = publicacionOpcional.get();
        Usuario usuario = usuarioOpcional.get();

        Comentario nuevoComentario = new Comentario();
        nuevoComentario.setContenido(contenido);
        nuevoComentario.setFechaCreacion(LocalDateTime.now());
        nuevoComentario.setPublicacion(publicacion);
        nuevoComentario.setUsuario(usuario);

        // El objeto retornado aquí debe ser serializado para la difusión por WebSocket.
        return comentarioRepository.save(nuevoComentario);
    }

    /**
     * Guarda una calificación Y actualiza el promedio de la publicación.
     * Todo en una única operación segura gracias a @Transactional.
     */
    @Transactional
    public Calificacion guardarCalificacion(Long idPublicacion, String username, int estrellas) {
        // 1. Búsqueda de entidades principales
        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada."));
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        // 2. ¡LA MAGIA! Buscamos si ya existe una calificación
        Calificacion calificacion = calificacionRepository.findByPublicacionAndUsuario(publicacion, usuario)
                .orElse(new Calificacion()); // Si no existe, creamos una nueva instancia

        // 3. Establecemos los valores (sea nueva o existente)
        calificacion.setEstrellas(estrellas);
        calificacion.setPublicacion(publicacion);
        calificacion.setUsuario(usuario);

        // 4. Guardamos la calificación (JPA sabe si es un INSERT o un UPDATE)
        Calificacion calificacionGuardada = calificacionRepository.save(calificacion);

        // 5. Recalculamos el promedio (esta parte se mantiene igual de eficiente)
        List<Calificacion> calificaciones = calificacionRepository.findByPublicacionId(idPublicacion);
        double promedio = calificaciones.stream()
                .mapToInt(Calificacion::getEstrellas)
                .average()
                .orElse(0.0);

        publicacion.setCalificacionPromedio(promedio);
        publicacionRepository.save(publicacion);

        return calificacionGuardada;
    }
}
