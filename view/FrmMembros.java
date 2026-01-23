package view;

import dao.MembroDAO;
import model.Membro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FrmMembros extends JFrame {

    private JTextField txtCodigo, txtNome, txtEndereco, txtTelefone,
            txtEmail, txtCpf, txtOutorga;

    private List<Membro> lista;
    private int posicao = -1;

    private final MembroDAO dao = new MembroDAO();

    private final Color fundo = new Color(45,45,45);
    private final Color verde = new Color(0,153,102);
    private final Color verdeHover = new Color(0,180,120);

    private final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public FrmMembros() {
        setTitle("Cadastro de Membros");
        setSize(600, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(fundo);
        setLayout(new BorderLayout());

        add(painelBotoes(), BorderLayout.NORTH);
        add(formulario(), BorderLayout.CENTER);

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

        p.add(label("Nome:"));
        txtNome = campo(true); p.add(txtNome);

        p.add(label("Endereço:"));
        txtEndereco = campo(true); p.add(txtEndereco);

        p.add(label("Telefone:"));
        txtTelefone = campo(true); p.add(txtTelefone);

        p.add(label("Email:"));
        txtEmail = campo(true); p.add(txtEmail);

        p.add(label("CPF:"));
        txtCpf = campo(true); p.add(txtCpf);

        p.add(label("Data Outorga (dd/MM/yyyy HH:mm):"));
        txtOutorga = campo(true); p.add(txtOutorga);

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
        txtNome.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtCpf.setText("");
        txtOutorga.setText("");
        posicao = -1;
        txtNome.requestFocus();
    }

    private void salvar() {
        try {
            Membro m = new Membro();
            m.setNome(txtNome.getText());
            m.setEndereco(txtEndereco.getText());
            m.setTelefone(txtTelefone.getText());
            m.setEmail(txtEmail.getText());
            m.setCpf(txtCpf.getText());
            m.setDataOutorga(LocalDateTime.parse(txtOutorga.getText(), dtf));

            dao.inserir(m);
            carregarLista();
            JOptionPane.showMessageDialog(this,"Membro cadastrado!");
            novo();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data inválida!\nUse: dd/MM/yyyy HH:mm"
            );
        }
    }

    private void editar() {
        if (txtCodigo.getText().isEmpty()) return;

        try {
            Membro m = new Membro();
            m.setCodMembro(Integer.parseInt(txtCodigo.getText()));
            m.setNome(txtNome.getText());
            m.setEndereco(txtEndereco.getText());
            m.setTelefone(txtTelefone.getText());
            m.setEmail(txtEmail.getText());
            m.setCpf(txtCpf.getText());
            m.setDataOutorga(LocalDateTime.parse(txtOutorga.getText(), dtf));

            dao.atualizar(m);
            carregarLista();
            JOptionPane.showMessageDialog(this,"Membro atualizado!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Data inválida!");
        }
    }

    private void excluir() {
        if (txtCodigo.getText().isEmpty()) return;

        if (JOptionPane.showConfirmDialog(
                this,"Excluir membro?","Confirmação",
                JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

            dao.excluir(Integer.parseInt(txtCodigo.getText()));
            carregarLista();
            novo();
        }
    }

    // ================= NAVEGAÇÃO =================
    private void carregarLista() { lista = dao.listar(); }

    private void mostrar() {
        Membro m = lista.get(posicao);
        txtCodigo.setText(String.valueOf(m.getCodMembro()));
        txtNome.setText(m.getNome());
        txtEndereco.setText(m.getEndereco());
        txtTelefone.setText(m.getTelefone());
        txtEmail.setText(m.getEmail());
        txtCpf.setText(m.getCpf());

        if (m.getDataOutorga() != null) {
            txtOutorga.setText(m.getDataOutorga().format(dtf));
        }
    }

    private void primeiro(){ if(!lista.isEmpty()){ posicao=0; mostrar(); } }
    private void ultimo(){ if(!lista.isEmpty()){ posicao=lista.size()-1; mostrar(); } }
    private void anterior(){ if(posicao>0){ posicao--; mostrar(); } }
    private void proximo(){ if(posicao<lista.size()-1){ posicao++; mostrar(); } }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrmMembros::new);
    }
}
