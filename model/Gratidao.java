package model;

import java.time.LocalDateTime;

public class Gratidao {

    private int codGratidao;
    private int codMembro;
    private LocalDateTime data;
    private double valor;
    private String tipo;

    public int getCodGratidao() {
        return codGratidao;
    }

    public void setCodGratidao(int codGratidao) {
        this.codGratidao = codGratidao;
    }

    public int getCodMembro() {
        return codMembro;
    }

    public void setCodMembro(int codMembro) {
        this.codMembro = codMembro;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
