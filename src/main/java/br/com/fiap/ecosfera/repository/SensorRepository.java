package br.com.fiap.ecosfera.repository;

import br.com.fiap.ecosfera.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor,Long> {
    List<Sensor> findByEmpresaId(Long idEmpresa);
}
