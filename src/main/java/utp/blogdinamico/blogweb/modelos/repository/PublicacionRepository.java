package utp.blogdinamico.blogweb.modelos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utp.blogdinamico.blogweb.modelos.principales.Publicacion;

import java.util.List;

/**
 * Repositorio JPA para la entidad Publicacion.
 * Extiende JpaRepository para obtener métodos CRUD listos para usar.
 */
@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    // Ejemplo de método de consulta personalizado:
    // Buscar todas las publicaciones por título (ignorando mayúsculas/minúsculas)
    List<Publicacion> findByTituloContainingIgnoreCase(String titulo);

    // Buscar las publicaciones más recientes
    List<Publicacion> findTop10ByOrderByFechaCreacionDesc();
}
