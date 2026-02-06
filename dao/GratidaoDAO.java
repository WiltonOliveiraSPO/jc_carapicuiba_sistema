package dao;

import model.Gratidao;
import javax.swing.JOptionPane;
import dao.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GratidaoDAO {

    private Connection con;
    //private final GratidaoDAO dao = new GratidaoDAO();

    public GratidaoDAO() {
        try {
            this.con = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão", e);
        }
    }


    // ================= INSERIR =================
    public void inserir(Gratidao g) {

        String sql = """
            INSERT INTO gratidao
            (cod_membro, dt_gratidao, vl_gratidao, tipo_gratidao)
            VALUES (?,?,?,?)
        """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, g.getCodMembro());
            pst.setTimestamp(2, Timestamp.valueOf(g.getData()));
            pst.setDouble(3, g.getValor());
            pst.setString(4, g.getTipo());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir gratidão", e);
        }
    }

    // ================= ATUALIZAR =================
    public void atualizar(Gratidao g) {

        String sql = """
            UPDATE gratidao SET
                cod_membro = ?,
                dt_gratidao = ?,
                vl_gratidao = ?,
                tipo_gratidao = ?
            WHERE cod_gratidao = ?
        """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, g.getCodMembro());
            pst.setTimestamp(2, Timestamp.valueOf(g.getData()));
            pst.setDouble(3, g.getValor());
            pst.setString(4, g.getTipo());
            pst.setInt(5, g.getCodGratidao());

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar gratidão", e);
        }
    }

    // ================= EXCLUIR =================
    public void excluir(int codigo) {

        String sql = "DELETE FROM gratidao WHERE cod_gratidao = ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, codigo);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir gratidão", e);
        }
    }

    // ================= LISTAR =================
    public List<Gratidao> listar() {

        List<Gratidao> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM gratidao
            ORDER BY dt_gratidao DESC
        """;

        try (PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Gratidao g = new Gratidao();

                g.setCodGratidao(rs.getInt("cod_gratidao"));
                g.setCodMembro(rs.getInt("cod_membro"));

                Timestamp ts = rs.getTimestamp("dt_gratidao");
                g.setData(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());

                g.setValor(rs.getDouble("vl_gratidao"));
                g.setTipo(rs.getString("tipo_gratidao"));

                lista.add(g);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar gratidões", e);
        }

        return lista;
    }
    


   
 // ================= PESQUISA POR DATA =================
    public List<Object[]> pesquisarPorPeriodo(
            LocalDateTime inicio, LocalDateTime fim) {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT g.cod_gratidao,
                   m.nome,
                   g.dt_gratidao,
                   g.vl_gratidao,
                   g.tipo_gratidao
            FROM gratidao g
            JOIN membro m ON m.cod_membro = g.cod_membro
            WHERE g.dt_gratidao BETWEEN ? AND ?
            ORDER BY g.dt_gratidao
        """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setTimestamp(1, Timestamp.valueOf(inicio));
            pst.setTimestamp(2, Timestamp.valueOf(fim));

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getTimestamp(3).toLocalDateTime(),
                        rs.getDouble(4),
                        rs.getString(5)
                });
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar por período", e);
        }

        return lista;
    }

    // ================= PESQUISA POR TIPO =================
    public List<Object[]> pesquisarPorTipo(String tipo) {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT g.cod_gratidao,
                   m.nome,
                   g.dt_gratidao,
                   g.vl_gratidao,
                   g.tipo_gratidao
            FROM gratidao g
            JOIN membro m ON m.cod_membro = g.cod_membro
            WHERE g.tipo_gratidao = ?
            ORDER BY g.dt_gratidao
        """;

        try (PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, tipo);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getTimestamp(3).toLocalDateTime(),
                        rs.getDouble(4),
                        rs.getString(5)
                });
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao pesquisar por tipo", e);
        }

        return lista;
    }

}
