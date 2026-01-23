package model;

import java.time.LocalDateTime;

public class PrimeiraVez {

    private int codPrimVez;
    private String nome;
    private int codMembro;
    private String endereco;
    private String telefone;
    private String email;
    private LocalDateTime dataPrimVez;

    public int getCodPrimVez() {
        return codPrimVez;
    }

    public void setCodPrimVez(int codPrimVez) {
        this.codPrimVez = codPrimVez;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodMembro() {
        return codMembro;
    }

    public void setCodMembro(int codMembro) {
        this.codMembro = codMembro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDataPrimVez() {
        return dataPrimVez;
    }

    public void setDataPrimVez(LocalDateTime dataPrimVez) {
        this.dataPrimVez = dataPrimVez;
    }
}
