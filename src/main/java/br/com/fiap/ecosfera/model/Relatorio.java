package br.com.fiap.ecosfera.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Relatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A emissão de gases não pode ser nula")
    private Double emissaoGases;

    @NotNull(message = "O uso de água não pode ser nulo")
    private Double usoAgua;

    @NotNull(message = "A energia consumida não pode ser nula")
    private Double energiaConsumida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresas empresa;
}
