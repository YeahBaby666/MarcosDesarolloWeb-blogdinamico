package utp.blogdinamico.blogweb.modelos.repository;

import utp.blogdinamico.blogweb.modelos.otros.ConfiguracionPlataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionPlataformaRepository extends JpaRepository<ConfiguracionPlataforma, Long> {
}
