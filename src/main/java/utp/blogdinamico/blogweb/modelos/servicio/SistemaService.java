package utp.blogdinamico.blogweb.modelos.servicio;

import utp.blogdinamico.blogweb.modelos.principales.Categoria;
import utp.blogdinamico.blogweb.modelos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio Central del Sistema (Façade).
 * Contiene la lógica de negocio general y centraliza la obtención de datos
 * ligeros de entidades que no requieren su propio servicio dedicado (ej. Categorías).
 */
@Service
public class SistemaService {
    
    private final CategoriaRepository categoriaRepository;

    public SistemaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // --- Lógica de Categorías (Integrada) ---
    
    /**
     * Obtiene todas las categorías de la base de datos para mostrarlas en formularios.
     * Gracias al FetchType.LAZY en la relación con Publicacion, solo se cargan
     * los datos de la tabla 'categorias', haciendo la lista ligera.
     * @return Lista de todas las entidades Categoria.
     */
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }
    
    // --- Más métodos de lógica central irán aquí (ej: contar usuarios, obtener estadísticas) ---
}
