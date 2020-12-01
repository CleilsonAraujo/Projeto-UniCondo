package com.example.unicondo;

public class Classe_AreasComuns {
    int id;
    String nome, imgURL, descricao;

    public Classe_AreasComuns(int id, String nome, String imgURL, String descricao) {
        this.id = id;
        this.nome = nome;
        this.imgURL = imgURL;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    @Override
    public String toString(){
        return nome;
    }
}
