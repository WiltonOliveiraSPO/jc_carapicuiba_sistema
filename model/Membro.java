package model;

import java.time.LocalDateTime;

public class Membro {

    private int codMembro;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String cpf;
    private LocalDateTime dataOutorga;

    public int getCodMembro() {
        return codMembro;
    }

    public void setCodMembro(int codMembro) {
        this.codMembro = codMembro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDateTime getDataOutorga() {
        return dataOutorga;
    }

    public void setDataOutorga(LocalDateTime dataOutorga) {
        this.dataOutorga = dataOutorga;
    }
    
    @Override
    public String toString() {
        return codMembro + " - " + nome;
    }

}
