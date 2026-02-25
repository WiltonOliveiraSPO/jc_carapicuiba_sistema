package view;

import dao.PedidoDAO;
import model.Pedido;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.time.ZoneId;

public class FrmPedidos extends JFrame {

    private JTextField txtCodigo;
    private JDateChooser dcData;    
    private JTextField txtAgradecimento, txtGraca, txtElevacao, txtAnivFalec;

    private List<Pedido> lista;
    private int posicao = -1;

    private final PedidoDAO dao = new PedidoDAO();

    private final Color fundo = new Color(45,45,45);
    private final Color verde = new Color(0,153,102);
    private final Color verdeHover = new Color(0,180,120);

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public FrmPedidos() {
        setTitle("Pedidos");
        setSize(560, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(fundo);
        setLayout(new BorderLayout());

        add(criarPainelBotoes(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);

        carregarLista();
        novo();

        setVisible(true);
    }

    // ================= BOTÕES =================
    private JPanel criarPainelBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER,8,10));
        p.setBackground(fundo);

        p.add(botaoTexto("|<", e -> primeiro()));
        p.add(botaoTexto("<", e -> anterior()));
        p.add(botaoTexto(">", e -> proximo()));
        p.add(botaoTexto(">|", e -> ultimo()));

        p.add(botaoIcone("inserir.jpg","Novo", e -> novo()));
        p.add(botaoIcone("salvar.jpg","Salvar", e -> salvar()));
        p.add(botaoIcone("editar.jpg","Editar", e -> editar()));
        p.add(botaoIcone("excluir.jpg","Excluir", e -> excluir()));

        return p;
    }

    private JButton botaoTexto(String t, java.awt.event.ActionListener a) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(50,40));
        b.setBackground(verde);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        aplicarHover(b);
        b.addActionListener(a);
        return b;
    }

    private JButton botaoIcone(String icone, String tip,
                               java.awt.event.ActionListener a) {

        ImageIcon i = new ImageIcon(
                new ImageIcon("C:\\jc_carapicuiba\\icons\\" + icone)
                        .getImage().getScaledInstance(22,22,Image.SCALE_SMOOTH));

        JButton b = new JButton(i);
        b.setToolTipText(tip);
        b.setPreferredSize(new Dimension(50,40));
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBackground(verde);
        aplicarHover(b);
        b.addActionListener(a);
        return b;
    }

    private void aplicarHover(JButton b) {
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(verdeHover);
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(verde);
            }
        });
    }

    // ================= FORM =================
    private JPanel criarFormulario() {
        JPanel p = new JPanel(new GridLayout(6,2,10,10));
        p.setBorder(new EmptyBorder(20,30,20,30));
        p.setBackground(fundo);

        p.add(label("Código:"));
        txtCodigo = campo(false); p.add(txtCodigo);

        p.add(label("Data:"));

        dcData = new JDateChooser();
        dcData.setDateFormatString("dd/MM/yyyy");
        dcData.setDate(new Date());
        dcData.setFont(new Font("Arial", Font.PLAIN, 13));

        p.add(dcData);

        p.add(label("Agradecimento:"));
        txtAgradecimento = campo(true); p.add(txtAgradecimento);

        p.add(label("Graça:"));
        txtGraca = campo(true); p.add(txtGraca);

        p.add(label("Elevação:"));
        txtElevacao = campo(true); p.add(txtElevacao);

        p.add(label("Aniv./Falec.:"));
        txtAnivFalec = campo(true); p.add(txtAnivFalec);

        return p;
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial",Font.BOLD,13));
        return l;
    }

    private JTextField campo(boolean e) {
        JTextField t = new JTextField();
        t.setEditable(e);
        return t;
    }

    // ================= AÇÕES =================
    private void novo() {
        txtCodigo.setText("");
        dcData.setDate(new Date());
        txtAgradecimento.setText("");
        txtGraca.setText("");
        txtElevacao.setText("");
        txtAnivFalec.setText("");
        posicao = -1;
        txtAgradecimento.requestFocus();
    }

    private void salvar() {
        try {
            Pedido p = new Pedido();
            if (dcData.getDate() == null) {
        JOptionPane.showMessageDialog(this,"Selecione uma data!");
        return;
    }

            LocalDate dataSelecionada = dcData.getDate()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

            p.setDtPedidos(dataSelecionada);            
            p.setQtdeAgradecimento(Integer.parseInt(txtAgradecimento.getText()));
            p.setQtdeGraca(Integer.parseInt(txtGraca.getText()));
            p.setQtdeElevacao(Integer.parseInt(txtElevacao.getText()));
            p.setQtdeAnivFalec(Integer.parseInt(txtAnivFalec.getText()));

            dao.inserir(p);
            carregarLista();
            JOptionPane.showMessageDialog(this,"Pedido registrado!");
            novo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Preencha somente números!");
        }
    }

    private void editar() {
        if (txtCodigo.getText().isEmpty()) return;

        Pedido p = new Pedido();
        p.setCodPedidos(Integer.parseInt(txtCodigo.getText()));
        if (dcData.getDate() == null) {
            JOptionPane.showMessageDialog(this,"Selecione uma data!");
            return;
        }

        LocalDate dataSelecionada = dcData.getDate()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();

        p.setDtPedidos(dataSelecionada);
        p.setQtdeAgradecimento(Integer.parseInt(txtAgradecimento.getText()));
        p.setQtdeGraca(Integer.parseInt(txtGraca.getText()));
        p.setQtdeElevacao(Integer.parseInt(txtElevacao.getText()));
        p.setQtdeAnivFalec(Integer.parseInt(txtAnivFalec.getText()));

        dao.atualizar(p);
        carregarLista();
        JOptionPane.showMessageDialog(this,"Pedido atualizado!");
    }

    private void excluir() {
        if (txtCodigo.getText().isEmpty()) return;

        if (JOptionPane.showConfirmDialog(
                this,"Excluir registro?","Confirmação",
                JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

            dao.excluir(Integer.parseInt(txtCodigo.getText()));
            carregarLista();
            novo();
        }
    }

    // ================= NAVEGAÇÃO =================
    private void carregarLista() { lista = dao.listar(); }

    private void mostrar() {
        Pedido p = lista.get(posicao);
        txtCodigo.setText(String.valueOf(p.getCodPedidos()));
        Date data = Date.from(
        p.getDtPedidos()
         .atStartOfDay(ZoneId.systemDefault())
         .toInstant()
        );

        dcData.setDate(data);
        txtAgradecimento.setText(String.valueOf(p.getQtdeAgradecimento()));
        txtGraca.setText(String.valueOf(p.getQtdeGraca()));
        txtElevacao.setText(String.valueOf(p.getQtdeElevacao()));
        txtAnivFalec.setText(String.valueOf(p.getQtdeAnivFalec()));
    }

    private void primeiro(){ if(!lista.isEmpty()){ posicao=0; mostrar(); } }
    private void ultimo(){ if(!lista.isEmpty()){ posicao=lista.size()-1; mostrar(); } }
    private void anterior(){ if(posicao>0){ posicao--; mostrar(); } }
    private void proximo(){ if(posicao<lista.size()-1){ posicao++; mostrar(); } }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrmPedidos::new);
    }
}

