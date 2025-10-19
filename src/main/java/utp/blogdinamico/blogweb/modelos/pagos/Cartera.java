package utp.blogdinamico.blogweb.modelos.pagos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa la cartera de un usuario para gestionar fondos.
 */
@Entity
@Table(name = "carteras")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cartera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double saldo = 0.0;

    // Relación OneToOne: Una Cartera pertenece a un solo Usuario (Lado de la FK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    // Relación OneToMany: Una Cartera tiene muchas Transacciones
    @OneToMany(mappedBy = "cartera", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaccion> transacciones;
}
