package utp.blogdinamico.blogweb.modelos.otros;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Entidad para almacenar configuraciones globales de la plataforma.
 * Se recomienda tener un solo registro.
 */
@Entity
@Table(name = "configuracion_plataforma")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracionPlataforma {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "porcentaje_comision", nullable = false)
    private double porcentajeComisionPlataforma;
}
