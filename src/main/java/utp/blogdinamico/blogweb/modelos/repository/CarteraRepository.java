package utp.blogdinamico.blogweb.modelos.repository;

import utp.blogdinamico.blogweb.modelos.pagos.Cartera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteraRepository extends JpaRepository<Cartera, Long> {
    
    // Los métodos CRUD básicos son provistos automáticamente
}
