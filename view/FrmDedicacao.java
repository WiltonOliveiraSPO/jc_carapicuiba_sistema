package view;

import dao.DedicacaoDAO;
import model.Dedicacao;

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

public class FrmDedicacao extends JFrame {

    private JTextField txtCodigo;
    private JDateChooser dcData;
    private JTextField txtMembros;
    private JTextField txtFrequentadores;
    private JTextField txtPrimeiraVez;

    private List<Dedicacao> lista;
    private int posicao = -1;

    private final DedicacaoDAO dao = new DedicacaoDAO();

    private final Color fundo = new Color(45, 45, 45);
    private final Color verde = new Color(0, 153, 102);
    private final Color verdeHover = new Color(0, 180, 120);

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public FrmDedicacao() {
        setTitle("Dedicação Diária");
        setSize(520, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(fundo);
        setLayout(new BorderLayout());

        add(criarPainelBotoes(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);

        carregarLista();
        limparCamposInicial();

        setVisible(true);
    }

    // ================= PAINEL DE BOTÕES =================
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        painel.setBackground(fundo);

        painel.add(criarBotaoTexto("|<", e -> primeiro()));
        painel.add(criarBotaoTexto("<", e -> anterior()));
        painel.add(criarBotaoTexto(">", e -> proximo()));
        painel.add(criarBotaoTexto(">|", e -> ultimo()));

        painel.add(criarBotaoIcone("inserir.jpg", "Novo", e -> novo()));
        painel.add(criarBotaoIcone("salvar.jpg", "Salvar", e -> salvar()));
        painel.add(criarBotaoIcone("editar.jpg", "Editar", e -> editar()));
        painel.add(criarBotaoIcone("excluir.jpg", "Excluir", e -> excluir()));

        return painel;
    }

    // ---------- BOTÃO TEXTO (NAVEGAÇÃO) ----------
    private JButton criarBotaoTexto(String texto, java.awt.event.ActionListener acao) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(50, 40));
        btn.setFocusPainted(false);
        btn.setBackground(verde);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.addActionListener(acao);
        aplicarHover(btn);
        return btn;
    }

    // ---------- BOTÃO ÍCONE ----------
    private JButton criarBotaoIcone(String nomeIcone, String tooltip,
                                    java.awt.event.ActionListener acao) {

        ImageIcon icon = new ImageIcon("C:\\jc_carapicuiba\\icons\\" + nomeIcone);
        Image img = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton btn = new JButton(icon);
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(50, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBackground(verde);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(acao);

        aplicarHover(btn);

        return btn;
    }

    // ---------- HOVER ----------
    private void aplicarHover(JButton btn) {
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(verdeHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(verde);
            }
        });
    }

    // ================= FORMULÁRIO =================
    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridLayout(5, 2, 10, 10));
        painel.setBackground(fundo);
        painel.setBorder(new EmptyBorder(20, 30, 20, 30));

        painel.add(criarLabel("Código:"));
        txtCodigo = criarCampo(false);
        painel.add(txtCodigo);

        painel.add(criarLabel("Data:"));

        dcData = new JDateChooser();
        dcData.setDateFormatString("dd/MM/yyyy");
        dcData.setFont(new Font("Arial", Font.PLAIN, 13));
        dcData.setDate(new Date()); // data atual ao abrir

        painel.add(dcData);

        painel.add(criarLabel("Qtde Membros:"));
        txtMembros = criarCampo(true);
        painel.add(txtMembros);

        painel.add(criarLabel("Qtde Frequentadores:"));
        txtFrequentadores = criarCampo(true);
        painel.add(txtFrequentadores);

        painel.add(criarLabel("Qtde Primeira Vez:"));
        txtPrimeiraVez = criarCampo(true);
        painel.add(txtPrimeiraVez);

        return painel;
    }

    private JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        return lbl;
    }

    private JTextField criarCampo(boolean editavel) {
        JTextField txt = new JTextField();
        txt.setEditable(editavel);
        txt.setFont(new Font("Arial", Font.PLAIN, 13));
        return txt;
    }

    // ================= DADOS =================
    private void carregarLista() {
        lista = dao.listar();
    }

    private void mostrarRegistro() {
        if (lista.isEmpty() || posicao < 0) return;

        Dedicacao d = lista.get(posicao);
        txtCodigo.setText(String.valueOf(d.getCodDedicacao()));
        Date data = Date.from(
                d.getDtDedicacao()
                 .atStartOfDay(ZoneId.systemDefault())
                 .toInstant()
        );
        dcData.setDate(data);
        txtMembros.setText(String.valueOf(d.getQtdeMembros()));
        txtFrequentadores.setText(String.valueOf(d.getQtdeFrequentadores()));
        txtPrimeiraVez.setText(String.valueOf(d.getQtdePrimVez()));
    }

    private void limparCamposInicial() {
        txtCodigo.setText("");
        dcData.setDate(new Date());
        txtMembros.setText("");
        txtFrequentadores.setText("");
        txtPrimeiraVez.setText("");
    }

    // ================= CRUD =================
    private void novo() {
        txtCodigo.setText("");
        dcData.setDate(new Date());
        txtMembros.setText("");
        txtFrequentadores.setText("");
        txtPrimeiraVez.setText("");

        txtMembros.requestFocus();
        posicao = -1;
    }

    private void salvar() {
        try {
            Dedicacao d = new Dedicacao();
            if (dcData.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma data!");
                return;
            }

            LocalDate dataSelecionada = dcData.getDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            d.setDtDedicacao(dataSelecionada);
            d.setQtdeMembros(Integer.parseInt(txtMembros.getText()));
            d.setQtdeFrequentadores(Integer.parseInt(txtFrequentadores.getText()));
            d.setQtdePrimVez(Integer.parseInt(txtPrimeiraVez.getText()));

            dao.inserir(d);
            carregarLista();
            JOptionPane.showMessageDialog(this, "Dedicação registrada com sucesso!");
            novo();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Preencha apenas valores numéricos!");
        }
    }

    private void editar() {
        if (txtCodigo.getText().isEmpty()) return;

        try {
            Dedicacao d = new Dedicacao();
            d.setCodDedicacao(Integer.parseInt(txtCodigo.getText()));
            if (dcData.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma data!");
                return;
            }

            LocalDate dataSelecionada = dcData.getDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            d.setDtDedicacao(dataSelecionada);
            d.setQtdeMembros(Integer.parseInt(txtMembros.getText()));
            d.setQtdeFrequentadores(Integer.parseInt(txtFrequentadores.getText()));
            d.setQtdePrimVez(Integer.parseInt(txtPrimeiraVez.getText()));

            dao.atualizar(d);
            carregarLista();
            JOptionPane.showMessageDialog(this, "Registro atualizado!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao editar!");
        }
    }

    private void excluir() {
        if (txtCodigo.getText().isEmpty()) return;

        int op = JOptionPane.showConfirmDialog(
                this,
                "Deseja excluir este registro?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {
            dao.excluir(Integer.parseInt(txtCodigo.getText()));
            carregarLista();
            novo();
        }
    }

    // ================= NAVEGAÇÃO =================
    private void primeiro() {
        if (!lista.isEmpty()) {
            posicao = 0;
            mostrarRegistro();
        }
    }

    private void ultimo() {
        if (!lista.isEmpty()) {
            posicao = lista.size() - 1;
            mostrarRegistro();
        }
    }

    private void anterior() {
        if (posicao > 0) {
            posicao--;
            mostrarRegistro();
        }
    }

    private void proximo() {
        if (posicao < lista.size() - 1) {
            posicao++;
            mostrarRegistro();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrmDedicacao::new);
    }
}
