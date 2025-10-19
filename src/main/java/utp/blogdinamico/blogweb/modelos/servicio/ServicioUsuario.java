package utp.blogdinamico.blogweb.modelos.servicio;

import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import utp.blogdinamico.blogweb.modelos.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio para la gestión de la entidad Usuario,
 * incluyendo el registro y la obtención de datos completos.
 */
@Service
public class ServicioUsuario {

    private final UsuarioRepository usuarioRepository;    
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias
    public ServicioUsuario(UsuarioRepository usuarioRepository,                           
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;        
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en la base de datos, inicializando sus entidades OneToOne
     * obligatorias (Cartera e InformacionPago).
     * * @param nombreUsuario El nombre de usuario único.
     * @param contrasena La contraseña en texto plano.
     * @return El usuario registrado.
     * @throws IllegalArgumentException Si el nombre de usuario ya está en uso.
     */
    @Transactional
    public Usuario registrarNuevoUsuario(String nombreUsuario, String contrasena) {
        
        // 1. Validar unicidad del nombre de usuario
        if (usuarioRepository.findByNombreUsuario(nombreUsuario).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        
        // 2. Crear y codificar la contraseña
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setContrasena(passwordEncoder.encode(contrasena)); // Contraseña hasheada
        nuevoUsuario.setAdmin(false);
        nuevoUsuario.setAutenticado(false); // Nuevo usuario no es admin ni autenticado por defecto
        
        
        // 7. Persistir. Importante: Guardamos el usuario primero para que tenga un ID.
        // JPA maneja las cascadas (si CascadeType.ALL está activo) pero guardamos explícitamente las relaciones bidireccionales.
        Usuario usuarioPersistido = usuarioRepository.save(nuevoUsuario);
        
        
        return usuarioPersistido;
    }

    /**
     * Busca y obtiene la entidad Usuario completa por su nombre de usuario.
     * Utilizado principalmente por el ControladorPublicacion para obtener la entidad
     * JPA completa después de la autenticación de Spring Security.
     * @param nombreUsuario El nombre de usuario a buscar.
     * @return Un Optional con el Usuario, o vacío si no se encuentra.
     */
    public Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }
}
