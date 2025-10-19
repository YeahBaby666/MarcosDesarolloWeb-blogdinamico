package utp.blogdinamico.blogweb.modelos.otros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utp.blogdinamico.blogweb.modelos.principales.Publicacion;
import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import jakarta.persistence.*;

/**
 * Entidad que registra una calificación (estrellas) de un Usuario a una Publicación.
 */
@Entity
@Table(name = "calificaciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "publicacion_id"}) // Un usuario solo puede calificar una publicación una vez
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Calificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int estrellas; // El valor de calificación: 1 a 5

    // Relación ManyToOne: El usuario que califica
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    // Relación ManyToOne: La publicación calificada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;
}
