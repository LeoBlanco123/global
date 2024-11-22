package br.com.fiap.ecosfera.repository;

import br.com.fiap.ecosfera.model.MetaAmbiental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaAmbientalRepository extends JpaRepository<MetaAmbiental,Long> {
    List<MetaAmbiental> findByEmpresaId(Long idEmpresa);
}
