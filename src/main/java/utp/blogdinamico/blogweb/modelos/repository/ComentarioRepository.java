package utp.blogdinamico.blogweb.modelos.repository;

import utp.blogdinamico.blogweb.modelos.principales.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    
    /**
     * Busca todos los comentarios asociados a una Publicación específica,
     * identificada por su ID, y los ordena del más reciente al más antiguo.
     * * NOTA: 'PublicacionId' es una propiedad anidada (Comentario.publicacion.id)
     * La convención JPA lo resuelve como WHERE publicacion_id = :id.
     * * @param idPublicacion El ID de la publicación.
     * @return Lista de comentarios ordenados por fecha de creación descendente.
     */
    List<Comentario> findByPublicacionIdOrderByFechaCreacionDesc(Long idPublicacion);
}
