package utp.blogdinamico.blogweb.modelos.servicio;

import org.springframework.stereotype.Service;

/**
 * Servicio responsable de sanitizar (limpiar) el contenido HTML enviado por los usuarios
 * para prevenir ataques de Cross-Site Scripting (XSS).
 * La anotación @Service indica que esta clase contiene lógica de negocio.
 */
@Service
public class ServicioSatinizacion {

    /**
     * Limpia una cadena de HTML, eliminando etiquetas y atributos peligrosos.
     * <strong>ADVERTENCIA:</strong> Esta es una implementación de ejemplo muy básica.
     * En un proyecto real, se debe usar una librería robusta como OWASP Java HTML Sanitizer
     * para una protección completa contra ataques XSS.
     *
     * @param htmlSucio El HTML proveniente del formulario del usuario.
     * @return El HTML limpio y seguro para ser almacenado y mostrado.
     */
    public String sanitizar(String htmlSucio) {
        if (htmlSucio == null) {
            return "";
        }
        // Simulación simple: eliminar cualquier etiqueta <script> y su contenido.
        // Esto previene la inyección de scripts maliciosos básicos.
        String htmlLimpio = htmlSucio.replaceAll("(?i)<script.*?>.*?</script.*?>", " [Contenido de script eliminado] ");
        return htmlLimpio;
    }
}
