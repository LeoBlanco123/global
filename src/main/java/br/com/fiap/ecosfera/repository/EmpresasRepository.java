package br.com.fiap.ecosfera.repository;

import br.com.fiap.ecosfera.model.Empresas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresasRepository extends JpaRepository<Empresas,Long> {
}
