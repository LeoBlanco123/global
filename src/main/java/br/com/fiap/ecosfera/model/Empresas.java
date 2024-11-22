package br.com.fiap.ecosfera.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
public class Empresas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(message = "O nome deve ter entre 5 e 100 caracteres", min = 5, max = 100)
    @NotEmpty(message = "O nome não pode ser vazio")
    private String nome;

    @NotEmpty(message = "O setor não pode ser vazio")
    private String setor;

    @NotEmpty(message = "A localização geográfica não pode ser vazia")
    private String localizacaoGeografica;

    @PositiveOrZero(message = "O carbono atual deve ser um número positivo ou zero")
    @NotNull(message = "O carbono atual não pode ser nulo")
    private Double carbonoAtual;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetaAmbiental> meta = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Relatorio> relatorios = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnaliseImpacto> analise = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensor = new ArrayList<>();
}