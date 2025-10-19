package utp.blogdinamico.blogweb.modelos.servicio;

import utp.blogdinamico.blogweb.modelos.principales.Categoria;
import utp.blogdinamico.blogweb.modelos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio para la gestión de categorías.
 * Proporciona métodos para obtener listas ligeras de categorías para formularios.
 */
@Service
public class ServicioCategoria {
    
    private final CategoriaRepository categoriaRepository;

    public ServicioCategoria(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Obtiene todas las categorías de la base de datos.
     * Gracias al FetchType.LAZY en la relación con Publicacion, solo se cargan
     * los datos de la tabla 'categorias', haciendo la lista ligera.
     * @return Lista de todas las entidades Categoria.
     */
    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }
    
    /**
     * Busca una categoría por su ID.
     * @param id El ID de la categoría a buscar.
     * @return Optional con la categoría si se encuentra.
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }
}
