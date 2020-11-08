package com.example.unicondo;

public class Classe_Noticias {
    private String ID;
    private String titulo;
    private String data;
    private String texto;

    public Classe_Noticias(String id, String titulo, String data, String texto) {
        this.ID = id;
        this.titulo = titulo;
        this.data = data;
        this.texto = texto;
    }

    public String getID() {
        return ID;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getData() {
        return data;
    }

    public String getTexto() {
        return texto;
    }
}
