package view;

import dao.MembroDAO;
import model.Membro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;

public class FrmPesquisaMembro extends JFrame {

    private JTextField txtNome;
    private JTable tabela;
    private DefaultTableModel model;

    private MembroDAO dao = new MembroDAO();

    public FrmPesquisaMembro() {
        setTitle("Pesquisa de Membros");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(criarTopo(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);

        setVisible(true);
    }

    // ================= TOPO =================
    private JPanel criarTopo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        p.add(new JLabel("Nome:"));
        txtNome = new JTextField(25);
        p.add(txtNome);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisar());
        p.add(btnPesquisar);

        JButton btnPDF = new JButton();
        btnPDF.setToolTipText("Gerar PDF do membro selecionado");

        ImageIcon iconPdf = new ImageIcon(
                new ImageIcon("C:\\jc_carapicuiba\\icons\\pdf_relatorio.jpg")
                        .getImage()
                        .getScaledInstance(28, 28, java.awt.Image.SCALE_SMOOTH)
        );
        btnPDF.setIcon(iconPdf);

        btnPDF.addActionListener(e -> gerarPDF());
        p.add(btnPDF);

        return p;
    }

    // ================= TABELA =================
    private JScrollPane criarTabela() {

        model = new DefaultTableModel(
                new Object[]{"Código", "Nome", "Telefone", "Email"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(25);

        return new JScrollPane(tabela);
    }

    // ================= PESQUISAR =================
    private void pesquisar() {

        model.setRowCount(0);

        List<Membro> lista = dao.pesquisarPorNome(txtNome.getText());

        for (Membro m : lista) {
            model.addRow(new Object[]{
                    m.getCodMembro(),
                    m.getNome(),
                    m.getTelefone(),
                    m.getEmail()
            });
        }
    }

    // ================= PDF =================
    private void gerarPDF() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um membro na tabela!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int cod = Integer.parseInt(model.getValueAt(linha, 0).toString());

        Membro m = dao.listar()
                .stream()
                .filter(x -> x.getCodMembro() == cod)
                .findFirst()
                .orElse(null);

        if (m == null) return;

        try {

            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc,
                    new FileOutputStream("membro_" + cod + ".pdf"));

            doc.open();

            // ===== LOGO =====
            Image logo = Image.getInstance(
                    "C:\\jc_carapicuiba\\icons\\izunome_img.jpg");
            logo.scalePercent(10);
            logo.setAlignment(Image.ALIGN_LEFT);
            doc.add(logo);

            // ===== TÍTULO =====
            Font tituloFont =
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

            Paragraph titulo = new Paragraph(
                    "\nJohrei Center Carapicuíba - Dados cadastrais do membro\n\n",
                    tituloFont
            );
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            Font f = new Font(Font.FontFamily.HELVETICA, 11);

            doc.add(new Paragraph("Código: " + m.getCodMembro(), f));
            doc.add(new Paragraph("Nome: " + m.getNome(), f));
            doc.add(new Paragraph("Endereço: " + m.getEndereco(), f));
            doc.add(new Paragraph("Telefone: " + m.getTelefone(), f));
            doc.add(new Paragraph("Email: " + m.getEmail(), f));
            doc.add(new Paragraph("CPF: " + m.getCpf(), f));

            if (m.getDataOutorga() != null) {
                doc.add(new Paragraph(
                        "Data de Outorga: " +
                                m.getDataOutorga()
                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        f));
            }

            // ===== RODAPÉ =====
            doc.add(new Paragraph("\n\n"));

            Paragraph rodape = new Paragraph(
                    "Relatório gerado em: " +
                            LocalDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    new Font(Font.FontFamily.HELVETICA, 9)
            );
            rodape.setAlignment(Element.ALIGN_RIGHT);
            doc.add(rodape);

            doc.close();

            JOptionPane.showMessageDialog(this,
                    "PDF gerado com sucesso!",
                    "PDF", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar PDF:\n" + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}
