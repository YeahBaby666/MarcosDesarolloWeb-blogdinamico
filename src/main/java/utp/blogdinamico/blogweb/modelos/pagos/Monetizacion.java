package utp.blogdinamico.blogweb.modelos.pagos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

/**
 * Entidad que define cómo se monetiza una publicación.
 * Actúa como una tabla maestra o de consulta.
 */
@Entity
@Table(name = "monetizaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Monetizacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    // AÑADIMOS unique = true PARA ASEGURAR QUE CADA TIPO DE MONETIZACIÓN SEA ÚNICO EN LA BD.
    @Column(nullable = false, unique = true) 
    private TipoMonetizacion tipo;
    
    private Double precio; // Solo aplica si tipo es PAGO o SUSCRIPCION
    
    @Column(name = "url_script_anuncio")
    private String urlScriptAnuncio; // Solo aplica si tipo es ANUNCIO
}