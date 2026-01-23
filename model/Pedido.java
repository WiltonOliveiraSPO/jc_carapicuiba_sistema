package model;

import java.time.LocalDate;

public class Pedido {

    private int codPedidos;
    private LocalDate dtPedidos;
    private int qtdeAgradecimento;
    private int qtdeGraca;
    private int qtdeElevacao;
    private int qtdeAnivFalec;

    public int getCodPedidos() {
        return codPedidos;
    }

    public void setCodPedidos(int codPedidos) {
        this.codPedidos = codPedidos;
    }

    public LocalDate getDtPedidos() {
        return dtPedidos;
    }

    public void setDtPedidos(LocalDate dtPedidos) {
        this.dtPedidos = dtPedidos;
    }

    public int getQtdeAgradecimento() {
        return qtdeAgradecimento;
    }

    public void setQtdeAgradecimento(int qtdeAgradecimento) {
        this.qtdeAgradecimento = qtdeAgradecimento;
    }

    public int getQtdeGraca() {
        return qtdeGraca;
    }

    public void setQtdeGraca(int qtdeGraca) {
        this.qtdeGraca = qtdeGraca;
    }

    public int getQtdeElevacao() {
        return qtdeElevacao;
    }

    public void setQtdeElevacao(int qtdeElevacao) {
        this.qtdeElevacao = qtdeElevacao;
    }

    public int getQtdeAnivFalec() {
        return qtdeAnivFalec;
    }

    public void setQtdeAnivFalec(int qtdeAnivFalec) {
        this.qtdeAnivFalec = qtdeAnivFalec;
    }
}
