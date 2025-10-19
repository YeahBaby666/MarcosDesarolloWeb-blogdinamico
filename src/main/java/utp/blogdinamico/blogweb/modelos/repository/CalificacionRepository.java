package utp.blogdinamico.blogweb.modelos.repository;

import utp.blogdinamico.blogweb.modelos.otros.Calificacion;
import utp.blogdinamico.blogweb.modelos.principales.Publicacion;
import utp.blogdinamico.blogweb.modelos.principales.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    // Escencial para el promedio de calificaciones
    List<Calificacion> findByPublicacionId(Long idPublicacion);
    // ¡NUEVA HERRAMIENTA!
    Optional<Calificacion> findByPublicacionAndUsuario(Publicacion publicacion, Usuario usuario);

    
}
