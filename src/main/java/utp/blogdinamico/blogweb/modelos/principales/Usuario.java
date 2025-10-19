package utp.blogdinamico.blogweb.modelos.principales;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import utp.blogdinamico.blogweb.modelos.pagos.Cartera;
import utp.blogdinamico.blogweb.modelos.pagos.InformacionPago;
import utp.blogdinamico.blogweb.modelos.pagos.Suscripcion;
import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa a un usuario del sistema.
 * Mapeada a la tabla 'usuarios' en PostgreSQL.
 */
@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, name = "nombre_usuario")
    private String nombreUsuario; // username unico
    
    @Column(nullable = false)
    private String contrasena;
    
    private boolean admin;
    private boolean autenticado;

    // --- Relaciones OneToMany (El usuario es el autor/creador) ---
    
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicacion> publicaciones; // publicaciones creadas por el usuario
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comentario> comentarios; // comentarios hechos por el usuario
    
    // --- Relaciones OneToOne (Actualizado) ---
    
    // Cartera - Un usuario tiene una cartera (Dueño de la FK)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cartera cartera; 
    
    // Información de Pago - Un usuario tiene una información de pago
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private InformacionPago informacionPago;
    
    // --- Relaciones de Suscripción (Actualizado) ---
    
    // suscriptores: Usuarios que me siguen (Yo soy el 'autor' o 'seguido')
    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<Suscripcion> suscriptores; 
    
    // suscripciones: Usuarios a los que yo sigo (Yo soy el 'suscriptor' o 'seguidor')
    @OneToMany(mappedBy = "suscriptor", fetch = FetchType.LAZY)
    private List<Suscripcion> suscripciones; 
}
