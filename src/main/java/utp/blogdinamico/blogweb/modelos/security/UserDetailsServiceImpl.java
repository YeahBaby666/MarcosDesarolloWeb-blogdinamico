package utp.blogdinamico.blogweb.modelos.security;

import utp.blogdinamico.blogweb.modelos.principales.Usuario;
import utp.blogdinamico.blogweb.modelos.repository.UsuarioRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementación de UserDetailsService de Spring Security.
 * Este servicio es el componente central que el framework de seguridad utiliza
 * para cargar los detalles del usuario durante el proceso de autenticación (login).
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Inyección del Repositorio de Usuario para interactuar con la base de datos.
     * @param usuarioRepository Repositorio JPA para la entidad Usuario.
     */
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carga el usuario por su nombre de usuario (username).
     * Este es el método invocado por Spring Security al intentar iniciar sesión.
     * * @param username El nombre de usuario ingresado en el formulario de login.
     * @return Un objeto UserDetails que contiene la información del usuario y sus roles/permisos.
     * @throws UsernameNotFoundException Si el usuario no existe en la base de datos.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Utilizamos el método findByNombreUsuario que creamos en el repositorio.
        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Construimos y retornamos el objeto UserDetails de Spring Security.
        // Spring Security necesita el nombre de usuario, la contraseña (ya hasheada),
        // y una lista de permisos/roles.
        return new org.springframework.security.core.userdetails.User(
                usuario.getNombreUsuario(),          // Username
                usuario.getContrasena(),             // Contraseña (ya debe estar hasheada con BCrypt)
                // Aquí definimos los roles. Para simplificar, usamos una lista vacía.
                // Si la propiedad 'admin' es true, podrías añadir el rol 'ROLE_ADMIN'
                Collections.emptyList()              
        );
    }
}
