package view;

import dao.MembroDAO;
import dao.PrimeiraVezDAO;
import model.Membro;
import model.PrimeiraVez;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FrmPrimeiraVez extends JFrame {

    private JTextField txtCodigo, txtNome,
            txtEndereco, txtTelefone, txtEmail, txtData;

    private JComboBox<Membro> cbMembro;

    private List<PrimeiraVez> lista;
    private int posicao = -1;

    private final PrimeiraVezDAO dao = new PrimeiraVezDAO();
    private final MembroDAO membroDAO = new MembroDAO();

    private final Color fundo = new Color(45,45,45);
    private final Color verde = new Color(0,153,102);
    private final Color verdeHover = new Color(0,180,120);

    private final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public FrmPrimeiraVez() {
        setTitle("Cadastro – Primeira Vez");
        setSize(650, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(fundo);
        setLayout(new BorderLayout());

        add(painelBotoes(), BorderLayout.NORTH);
        add(formulario(), BorderLayout.CENTER);

        carregarMembros();
        carregarLista();
        novo();

        setVisible(true);
    }

    // ================= BOTÕES =================
    private JPanel painelBotoes() {
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
    private JPanel formulario() {
        JPanel p = new JPanel(new GridLayout(7,2,10,10));
        p.setBorder(new EmptyBorder(20,30,20,30));
        p.setBackground(fundo);

        p.add(label("Código:"));
        txtCodigo = campo(false); p.add(txtCodigo);

        p.add(label("Nome (Primeira vez):"));
        txtNome = campo(true); p.add(txtNome);

        p.add(label("Membro Responsável:"));
        cbMembro = new JComboBox<>();
        p.add(cbMembro);

        p.add(label("Endereço:"));
        txtEndereco = campo(true); p.add(txtEndereco);

        p.add(label("Telefone:"));
        txtTelefone = campo(true); p.add(txtTelefone);

        p.add(label("Email:"));
        txtEmail = campo(true); p.add(txtEmail);

        p.add(label("Data Primeira Vez (dd/MM/yyyy HH:mm):"));
        txtData = campo(true); p.add(txtData);

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

    // ================= DADOS =================
    private void carregarMembros() {
        cbMembro.removeAllItems();
        for (Membro m : membroDAO.listar()) {
            cbMembro.addItem(m); // exibe "cod - nome"
        }
    }

    // ================= AÇÕES =================
    private void novo() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtData.setText("");

        if (cbMembro.getItemCount() > 0)
            cbMembro.setSelectedIndex(0);

        posicao = -1;
        txtNome.requestFocus();
    }

    private void salvar() {
        try {
            PrimeiraVez p = new PrimeiraVez();

            p.setNome(txtNome.getText());

            Membro m = (Membro) cbMembro.getSelectedItem();
            p.setCodMembro(m.getCodMembro()); // grava só o código

            p.setEndereco(txtEndereco.getText());
            p.setTelefone(txtTelefone.getText());
            p.setEmail(txtEmail.getText());
            p.setDataPrimVez(LocalDateTime.parse(txtData.getText(), dtf));

            dao.inserir(p);
            carregarLista();
            JOptionPane.showMessageDialog(this,"Registro salvo!");
            novo();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao salvar.\nVerifique os dados e a data."
            );
        }
    }

    private void editar() {
        if (txtCodigo.getText().isEmpty()) return;

        try {
            PrimeiraVez p = new PrimeiraVez();
            p.setCodPrimVez(Integer.parseInt(txtCodigo.getText()));
            p.setNome(txtNome.getText());

            Membro m = (Membro) cbMembro.getSelectedItem();
            p.setCodMembro(m.getCodMembro());

            p.setEndereco(txtEndereco.getText());
            p.setTelefone(txtTelefone.getText());
            p.setEmail(txtEmail.getText());
            p.setDataPrimVez(LocalDateTime.parse(txtData.getText(), dtf));

            dao.atualizar(p);
            carregarLista();
            JOptionPane.showMessageDialog(this,"Registro atualizado!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Erro ao editar!");
        }
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
        PrimeiraVez p = lista.get(posicao);

        txtCodigo.setText(String.valueOf(p.getCodPrimVez()));
        txtNome.setText(p.getNome());
        txtEndereco.setText(p.getEndereco());
        txtTelefone.setText(p.getTelefone());
        txtEmail.setText(p.getEmail());
        txtData.setText(p.getDataPrimVez().format(dtf));

        for (int i = 0; i < cbMembro.getItemCount(); i++) {
            if (cbMembro.getItemAt(i).getCodMembro() == p.getCodMembro()) {
                cbMembro.setSelectedIndex(i);
                break;
            }
        }
    }

    private void primeiro(){ if(!lista.isEmpty()){ posicao=0; mostrar(); } }
    private void ultimo(){ if(!lista.isEmpty()){ posicao=lista.size()-1; mostrar(); } }
    private void anterior(){ if(posicao>0){ posicao--; mostrar(); } }
    private void proximo(){ if(posicao<lista.size()-1){ posicao++; mostrar(); } }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrmPrimeiraVez::new);
    }
}
