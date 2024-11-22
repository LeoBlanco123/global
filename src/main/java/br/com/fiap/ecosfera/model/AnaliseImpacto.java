package br.com.fiap.ecosfera.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class AnaliseImpacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "A recomendação não pode ser nula")
    private String recomendacoes;

    @NotNull(message = "O cumprimento de metas não pode ser nulo")
    @Positive(message = "O cumprimento de metas deve ser um número positivo")
    private Double cumprimentoMetas;

    @NotNull(message = "O progresso da pegada de carbono não pode ser nulo")
    @Positive(message = "O progresso da pegada de carbono deve ser um número positivo")
    private Double progressoPegadaCarbono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    private Empresas empresa;
}
