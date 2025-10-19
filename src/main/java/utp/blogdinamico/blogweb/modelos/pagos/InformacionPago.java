package utp.blogdinamico.blogweb.modelos.pagos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import jakarta.persistence.*;

/**
 * Entidad que almacena los detalles de pago del usuario (Yape, Plin, etc.)
 */
@Entity
@Table(name = "informacion_pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformacionPago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;
    
    @Column(name = "detalles_cuenta", nullable = false)
    private String detallesCuenta;

    // Relación OneToOne: Una InfoPago pertenece a un solo Usuario (Lado de la FK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
