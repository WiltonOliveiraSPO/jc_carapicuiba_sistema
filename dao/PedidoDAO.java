package dao;

import model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void inserir(Pedido p) {
        String sql = """
            INSERT INTO pedidos
            (dt_pedidos, qtde_agradecimento, qtde_graca, qtde_elevacao, qtde_anivfalec)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(p.getDtPedidos()));
            ps.setInt(2, p.getQtdeAgradecimento());
            ps.setInt(3, p.getQtdeGraca());
            ps.setInt(4, p.getQtdeElevacao());
            ps.setInt(5, p.getQtdeAnivFalec());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Pedido p) {
        String sql = """
            UPDATE pedidos SET
              dt_pedidos=?,
              qtde_agradecimento=?,
              qtde_graca=?,
              qtde_elevacao=?,
              qtde_anivfalec=?
            WHERE cod_pedidos=?
        """;

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(p.getDtPedidos()));
            ps.setInt(2, p.getQtdeAgradecimento());
            ps.setInt(3, p.getQtdeGraca());
            ps.setInt(4, p.getQtdeElevacao());
            ps.setInt(5, p.getQtdeAnivFalec());
            ps.setInt(6, p.getCodPedidos());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir(int codigo) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("DELETE FROM pedidos WHERE cod_pedidos=?")) {

            ps.setInt(1, codigo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listar() {
        List<Pedido> lista = new ArrayList<>();

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("SELECT * FROM pedidos ORDER BY dt_pedidos DESC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido();
                p.setCodPedidos(rs.getInt("cod_pedidos"));
                p.setDtPedidos(rs.getDate("dt_pedidos").toLocalDate());
                p.setQtdeAgradecimento(rs.getInt("qtde_agradecimento"));
                p.setQtdeGraca(rs.getInt("qtde_graca"));
                p.setQtdeElevacao(rs.getInt("qtde_elevacao"));
                p.setQtdeAnivFalec(rs.getInt("qtde_anivfalec"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
