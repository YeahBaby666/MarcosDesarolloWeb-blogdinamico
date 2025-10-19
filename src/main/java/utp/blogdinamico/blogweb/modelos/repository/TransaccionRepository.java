package utp.blogdinamico.blogweb.modelos.repository;

import utp.blogdinamico.blogweb.modelos.pagos.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
