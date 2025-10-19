package utp.blogdinamico.blogweb.modelos.principales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa las categorías de las publicaciones.
 */
@Entity
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre; // nombre unico
    
    @Column(length = 500)
    private String descripcion;

    // Relación OneToMany: Una Categoría puede tener muchas Publicaciones
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicacion> publicaciones;
}
