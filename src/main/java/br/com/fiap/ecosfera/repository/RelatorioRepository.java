package br.com.fiap.ecosfera.repository;

import br.com.fiap.ecosfera.model.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio,Long> {
    List<Relatorio> findByEmpresaId(Long idEmpresa);
}
