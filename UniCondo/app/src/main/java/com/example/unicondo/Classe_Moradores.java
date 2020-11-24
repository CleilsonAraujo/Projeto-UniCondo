package com.example.unicondo;

public class Classe_Moradores {
    String nome;
    int id, idTipoMorador;
    boolean marcado = false;

    public Classe_Moradores(String nome, int id, int idTipoMorador) {
        this.nome = nome;
        this.id = id;
        this.idTipoMorador = idTipoMorador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTipoMorador() {
        return idTipoMorador;
    }

    public void setIdTipoMorador(int idTipoMorador) {
        this.idTipoMorador = idTipoMorador;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    @Override
    public String toString(){
        return nome;
    }
}
