package br.com.fiap.ecosfera.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class MetaAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "A descrição não pode ser vazia")
    private String descricao;

    @NotNull(message = "A redução de emissão não pode ser nula")
    private Double reducaoEmissao;

    @NotNull(message = "A redução de água não pode ser nula")
    private Double reducaoAgua;

    @NotNull(message = "A redução de energia não pode ser nula")
    private Double reducaoEnergia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresas empresa;
}
