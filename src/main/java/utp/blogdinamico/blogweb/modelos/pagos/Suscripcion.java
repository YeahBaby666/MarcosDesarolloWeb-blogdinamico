package utp.blogdinamico.blogweb.modelos.pagos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para registrar las relaciones de seguidores/seguidos.
 */
@Entity
@Table(name = "suscripciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suscripcion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha_inicio", updatable = false)
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin; // Opcional para suscripciones de tiempo limitado

    // Relación ManyToOne: El usuario que paga/sigue
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suscriptor_id", nullable = false)
    private Usuario suscriptor;
    
    // Relación ManyToOne: El usuario al que se sigue/paga (autor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @PrePersist
    protected void onCreate() {
        if (fechaInicio == null) {
            fechaInicio = LocalDateTime.now();
        }
    }
}
