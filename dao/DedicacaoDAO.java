package dao;

import model.Dedicacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DedicacaoDAO {

    // CREATE (INSERT)
    public void inserir(Dedicacao d) {
        String sql = """
            INSERT INTO dedicacao
            (dt_dedicacao, qtde_membros, qtde_frequentadores, qtde_primvez)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(d.getDtDedicacao()));
            ps.setInt(2, d.getQtdeMembros());
            ps.setInt(3, d.getQtdeFrequentadores());
            ps.setInt(4, d.getQtdePrimVez());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ (SELECT por ID)
    public Dedicacao buscarPorId(int codDedicacao) {
        String sql = "SELECT * FROM dedicacao WHERE cod_dedicacao = ?";
        Dedicacao d = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codDedicacao);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = new Dedicacao();
                    d.setCodDedicacao(rs.getInt("cod_dedicacao"));
                    d.setDtDedicacao(rs.getDate("dt_dedicacao").toLocalDate());
                    d.setQtdeMembros(rs.getInt("qtde_membros"));
                    d.setQtdeFrequentadores(rs.getInt("qtde_frequentadores"));
                    d.setQtdePrimVez(rs.getInt("qtde_primvez"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    // READ (SELECT todos)
    public List<Dedicacao> listar() {
        List<Dedicacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM dedicacao ORDER BY dt_dedicacao DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Dedicacao d = new Dedicacao();
                d.setCodDedicacao(rs.getInt("cod_dedicacao"));
                d.setDtDedicacao(rs.getDate("dt_dedicacao").toLocalDate());
                d.setQtdeMembros(rs.getInt("qtde_membros"));
                d.setQtdeFrequentadores(rs.getInt("qtde_frequentadores"));
                d.setQtdePrimVez(rs.getInt("qtde_primvez"));

                lista.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // UPDATE
    public void atualizar(Dedicacao d) {
        String sql = """
            UPDATE dedicacao SET
                dt_dedicacao = ?,
                qtde_membros = ?,
                qtde_frequentadores = ?,
                qtde_primvez = ?
            WHERE cod_dedicacao = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(d.getDtDedicacao()));
            ps.setInt(2, d.getQtdeMembros());
            ps.setInt(3, d.getQtdeFrequentadores());
            ps.setInt(4, d.getQtdePrimVez());
            ps.setInt(5, d.getCodDedicacao());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void excluir(int codDedicacao) {
        String sql = "DELETE FROM dedicacao WHERE cod_dedicacao = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codDedicacao);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
