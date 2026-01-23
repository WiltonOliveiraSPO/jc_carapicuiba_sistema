package model;

import java.time.LocalDate;

public class Dedicacao {

    private int codDedicacao;
    private LocalDate dtDedicacao;
    private int qtdeMembros;
    private int qtdeFrequentadores;
    private int qtdePrimVez;

    public Dedicacao() {
    }

    public int getCodDedicacao() {
        return codDedicacao;
    }

    public void setCodDedicacao(int codDedicacao) {
        this.codDedicacao = codDedicacao;
    }

    public LocalDate getDtDedicacao() {
        return dtDedicacao;
    }

    public void setDtDedicacao(LocalDate dtDedicacao) {
        this.dtDedicacao = dtDedicacao;
    }

    public int getQtdeMembros() {
        return qtdeMembros;
    }

    public void setQtdeMembros(int qtdeMembros) {
        this.qtdeMembros = qtdeMembros;
    }

    public int getQtdeFrequentadores() {
        return qtdeFrequentadores;
    }

    public void setQtdeFrequentadores(int qtdeFrequentadores) {
        this.qtdeFrequentadores = qtdeFrequentadores;
    }

    public int getQtdePrimVez() {
        return qtdePrimVez;
    }

    public void setQtdePrimVez(int qtdePrimVez) {
        this.qtdePrimVez = qtdePrimVez;
    }
}
