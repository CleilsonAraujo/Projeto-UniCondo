package com.example.unicondo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Classe_GerenciaAgendamentos {
    int id, idArea;
    String nomeArea, dataIni, dataFim;
    boolean marcado = false;

    public Classe_GerenciaAgendamentos(String nomeArea, int id, int idArea, String dataIni, String dataFim) {
        this.nomeArea = nomeArea;
        this.id = id;
        this.idArea = idArea;
        this.dataIni = dataIni;
        this.dataFim = dataFim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public String getDataIni() {
        return dataIni;
    }

    public void setDataIni(String dataIni) {
        this.dataIni = dataIni;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getNomeArea() {
        return nomeArea;
    }

    public void setNomeArea(String nomeArea) {
        this.nomeArea = nomeArea;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    @Override
    public String toString(){
        SimpleDateFormat sdfString = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        String[] data = dataIni.split(" ");
        String[] hora = dataIni.split(":");
        String[] hora2 = dataFim.split(":");

        Date frmtData = null;
        try {
            frmtData = sdfDate.parse(data[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return nomeArea + "\n" + sdfString.format(frmtData) + " de " + hora[0].substring(hora[0].length() - 2) +
                ":" + hora[1] + " às "+ hora2[0].substring(hora2[0].length() - 2) + ":" + hora2[1];
    }
}
