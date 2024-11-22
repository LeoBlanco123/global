package br.com.fiap.ecosfera.repository;

import br.com.fiap.ecosfera.model.AnaliseImpacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  AnaliseImpactoRepository extends JpaRepository<AnaliseImpacto,Long> {
    List<AnaliseImpacto> findByEmpresaId(Long id);
}
