package br.com.cotemig.gerenciadorfinanceiro.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "metas")
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorObjetivo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorAtual;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Column(nullable = false)
    private Boolean ativa;

    public Meta() {
    }

    public Meta(String nome, BigDecimal valorObjetivo, LocalDate dataFim) {
        this.nome = nome;
        this.valorObjetivo = valorObjetivo;
        this.valorAtual = BigDecimal.ZERO;
        this.dataInicio = LocalDate.now();
        this.dataFim = dataFim;
        this.ativa = true;
    }

    public void adicionarValor(BigDecimal valor) {
        this.valorAtual = this.valorAtual.add(valor);
    }

    public BigDecimal calcularProgresso() {
        if (valorObjetivo.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return valorAtual
                .multiply(new BigDecimal("100"))
                .divide(valorObjetivo, 2, RoundingMode.HALF_UP);
    }

    public boolean isConcluida() {
        return valorAtual.compareTo(valorObjetivo) >= 0;
    }

    public void ativar() {
        this.ativa = true;
    }

    public void desativar() {
        this.ativa = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValorObjetivo() {
        return valorObjetivo;
    }

    public void setValorObjetivo(BigDecimal valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
    }

    public BigDecimal getValorAtual() {
        return valorAtual;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getAtiva() {
        return ativa;
    }
}