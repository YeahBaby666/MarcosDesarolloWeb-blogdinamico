package utp.blogdinamico.blogweb.modelos.repository;

import utp.blogdinamico.blogweb.modelos.pagos.Monetizacion;
import utp.blogdinamico.blogweb.modelos.pagos.TipoMonetizacion;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonetizacionRepository extends JpaRepository<Monetizacion, Long> {

    Optional<Monetizacion> findByTipo(TipoMonetizacion tipoMonetizacion);
    
    // Los métodos CRUD básicos son provistos automáticamente
}
