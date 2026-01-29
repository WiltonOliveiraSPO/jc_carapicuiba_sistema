package dao;

import model.PrimeiraVez;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrimeiraVezDAO {

    public void inserir(PrimeiraVez p) {
        String sql = """
            INSERT INTO primvez
            (nome, cod_membro, endereco, telefone, email, data_primvez)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setInt(2, p.getCodMembro());
            ps.setString(3, p.getEndereco());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());
            ps.setTimestamp(6, Timestamp.valueOf(p.getDataPrimVez()));

            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Membro inválido ou inexistente!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizar(PrimeiraVez p) {
        String sql = """
            UPDATE primvez SET
              nome=?,
              cod_membro=?,
              endereco=?,
              telefone=?,
              email=?,
              data_primvez=?
            WHERE cod_primvez=?
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setInt(2, p.getCodMembro());
            ps.setString(3, p.getEndereco());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());
            ps.setTimestamp(6, Timestamp.valueOf(p.getDataPrimVez()));
            ps.setInt(7, p.getCodPrimVez());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir(int codigo) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("DELETE FROM primvez WHERE cod_primvez=?")) {

            ps.setInt(1, codigo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PrimeiraVez> listar() {
        List<PrimeiraVez> lista = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("SELECT * FROM primvez ORDER BY data_primvez DESC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PrimeiraVez p = new PrimeiraVez();
                p.setCodPrimVez(rs.getInt("cod_primvez"));
                p.setNome(rs.getString("nome"));
                p.setCodMembro(rs.getInt("cod_membro"));
                p.setEndereco(rs.getString("endereco"));
                p.setTelefone(rs.getString("telefone"));
                p.setEmail(rs.getString("email"));

                Timestamp ts = rs.getTimestamp("data_primvez");
                if (ts != null)
                    p.setDataPrimVez(ts.toLocalDateTime());

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<PrimeiraVez> pesquisarPorNome(String nome) {

        List<PrimeiraVez> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM primvez
            WHERE nome LIKE ?
            ORDER BY data_primvez DESC
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + nome + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PrimeiraVez p = new PrimeiraVez();
                p.setCodPrimVez(rs.getInt("cod_primvez"));
                p.setNome(rs.getString("nome"));
                p.setCodMembro(rs.getInt("cod_membro"));
                p.setEndereco(rs.getString("endereco"));
                p.setTelefone(rs.getString("telefone"));
                p.setEmail(rs.getString("email"));

                Timestamp ts = rs.getTimestamp("data_primvez");
                if (ts != null)
                    p.setDataPrimVez(ts.toLocalDateTime());

                lista.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Object[]> pesquisarComMembro(String nome) {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT p.cod_primvez,
                   p.nome,
                   p.endereco,
                   p.telefone,
                   p.email,
                   p.data_primvez,
                   m.nome AS nome_membro
            FROM primvez p
            JOIN membro m ON p.cod_membro = m.cod_membro
            WHERE p.nome LIKE ?
            ORDER BY p.data_primvez DESC
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + nome + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt("cod_primvez"),
                        rs.getString("nome"),
                        rs.getString("endereco"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getTimestamp("data_primvez").toLocalDateTime(),
                        rs.getString("nome_membro")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
