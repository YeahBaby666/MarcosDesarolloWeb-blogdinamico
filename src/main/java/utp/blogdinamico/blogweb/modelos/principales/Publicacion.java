package utp.blogdinamico.blogweb.modelos.principales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utp.blogdinamico.blogweb.modelos.otros.Calificacion;
import utp.blogdinamico.blogweb.modelos.pagos.Monetizacion;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad principal: Publicación o post en el blog.
 * Mapeada a la tabla 'publicaciones' en PostgreSQL.
 */
@Entity
@Table(name = "publicaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(length = 500)
    private String resumen;

    @Lob // Para almacenar grandes cantidades de texto (TEXT/CLOB)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido; // Contenido completo del post

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "calificacion_promedio")
    private double calificacionPromedio; // Se mantiene, se actualiza en el servicio.

    // --- Relaciones ManyToOne (Lado que tiene la FK) ---

    // Autor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    // Categoría
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id") // No necesariamente nullable si se requiere siempre
    private Categoria categoria;

    // --- Relaciones OneToMany / OneToOne (Lado dueño de la relación) ---

    // Comentarios
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comentario> comentarios;

    // Calificaciones (Actualizado)
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Calificacion> calificaciones;

    // Monetización (¡CORREGIDO!)
    // La relación correcta es ManyToOne: Muchas publicaciones pueden tener la misma
    // monetización.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monetizacion_id") // El JoinColumn se mantiene igual
    private Monetizacion monetizacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}
