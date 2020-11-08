package com.example.unicondo;

public class Classe_Condominios {
    String nome;
    int id;

    public Classe_Condominios(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return nome;
    }
}
