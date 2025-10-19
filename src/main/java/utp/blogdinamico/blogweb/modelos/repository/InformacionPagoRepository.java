package utp.blogdinamico.blogweb.modelos.repository;

import utp.blogdinamico.blogweb.modelos.pagos.InformacionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformacionPagoRepository extends JpaRepository<InformacionPago, Long> {
}
