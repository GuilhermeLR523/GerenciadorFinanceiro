package br.com.cotemig.gerenciadorfinanceiro.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConta tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoAtual;

    @Column(nullable = false)
    private LocalDate dataCriacao;

    @Column(nullable = false)
    private Boolean ativa;

    public Conta() {
    }

    public Conta(String nome, TipoConta tipo, BigDecimal saldoInicial) {
        this.nome = nome;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        this.saldoAtual = saldoInicial;
        this.dataCriacao = LocalDate.now();
        this.ativa = true;
    }

    public void adicionarSaldo(BigDecimal valor) {
        this.saldoAtual = this.saldoAtual.add(valor);
    }

    public void removerSaldo(BigDecimal valor) {
        this.saldoAtual = this.saldoAtual.subtract(valor);
    }

    public void desativar() {
        this.ativa = false;
    }

    public void ativar() {
        this.ativa = true;
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

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }
}