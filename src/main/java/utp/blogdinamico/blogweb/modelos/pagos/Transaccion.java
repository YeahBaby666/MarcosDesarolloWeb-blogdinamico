package utp.blogdinamico.blogweb.modelos.pagos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utp.blogdinamico.blogweb.modelos.principales.Publicacion;
import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que registra cada movimiento de dinero en el sistema.
 */
@Entity
@Table(name = "transacciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double monto;
    
    @Column(name = "fecha_hora", updatable = false)
    private LocalDateTime fechaHora;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransaccion tipo;

    // Relaciones ManyToOne
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagador_id") // Puede ser null si es una comisión
    private Usuario pagador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id") // Puede ser null si es un retiro o una comisión a la plataforma
    private Usuario receptor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id") // Opcional
    private Publicacion publicacion;
    
    // Relacion ManyToOne con Cartera (la transacción afecta a una cartera)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartera_id", nullable = false)
    private Cartera cartera;

    @PrePersist
    protected void onCreate() {
        if (fechaHora == null) {
            fechaHora = LocalDateTime.now();
        }
    }
}
