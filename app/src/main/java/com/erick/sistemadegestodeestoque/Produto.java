package com.erick.sistemadegestodeestoque;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Produto {
    private String nome;
    private double preco;
    private int quantidade;
    private String unidadeMedida; // "UN" ou "KG"

    public Produto() {
    }

    @Ignore
    public Produto(String nome, double preco, int quantidade, String unidadeMedida) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    public Produto(String nome, double preco, int quantidade, String unidadeMedida, int id) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public int getId() {
        return id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public void setId(int id) {
        this.id = id;
    }
}
