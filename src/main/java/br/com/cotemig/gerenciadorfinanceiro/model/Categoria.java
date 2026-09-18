package br.com.cotemig.gerenciadorfinanceiro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @Column(length = 20)
    private String cor;

    @Column(length = 50)
    private String icone;

    @Column(nullable = false)
    private Boolean ativa;

    public Categoria() {
    }

    public Categoria(String nome, TipoTransacao tipo, String cor, String icone) {
        this.nome = nome;
        this.tipo = tipo;
        this.cor = cor;
        this.icone = icone;
        this.ativa = true;
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

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public Boolean getAtiva() {
        return ativa;
    }
}