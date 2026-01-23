package dao;

import model.Membro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembroDAO {

    public void inserir(Membro m) {
        String sql = """
            INSERT INTO membro
            (nome, endereco, telefone, email, cpf, data_outorga)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getNome());
            ps.setString(2, m.getEndereco());
            ps.setString(3, m.getTelefone());
            ps.setString(4, m.getEmail());
            ps.setString(5, m.getCpf());
            ps.setTimestamp(6, Timestamp.valueOf(m.getDataOutorga()));

            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("CPF já cadastrado!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Membro m) {
        String sql = """
            UPDATE membro SET
              nome=?,
              endereco=?,
              telefone=?,
              email=?,
              cpf=?,
              data_outorga=?
            WHERE cod_membro=?
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getNome());
            ps.setString(2, m.getEndereco());
            ps.setString(3, m.getTelefone());
            ps.setString(4, m.getEmail());
            ps.setString(5, m.getCpf());
            ps.setTimestamp(6, Timestamp.valueOf(m.getDataOutorga()));
            ps.setInt(7, m.getCodMembro());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir(int codigo) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("DELETE FROM membro WHERE cod_membro=?")) {

            ps.setInt(1, codigo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Membro> listar() {
        List<Membro> lista = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("SELECT * FROM membro ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Membro m = new Membro();
                m.setCodMembro(rs.getInt("cod_membro"));
                m.setNome(rs.getString("nome"));
                m.setEndereco(rs.getString("endereco"));
                m.setTelefone(rs.getString("telefone"));
                m.setEmail(rs.getString("email"));
                m.setCpf(rs.getString("cpf"));

                Timestamp ts = rs.getTimestamp("data_outorga");
                if (ts != null) {
                    m.setDataOutorga(ts.toLocalDateTime());
                }

                lista.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
