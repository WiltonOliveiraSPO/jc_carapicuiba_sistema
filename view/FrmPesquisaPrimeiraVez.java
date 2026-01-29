package view;

import dao.PrimeiraVezDAO;
import model.PrimeiraVez;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FrmPesquisaPrimeiraVez extends JFrame {

    private JTextField txtNome;
    private JTable tabela;
    private DefaultTableModel model;

    private final PrimeiraVezDAO dao = new PrimeiraVezDAO();
    private final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public FrmPesquisaPrimeiraVez() {

        setTitle("Pesquisa – Primeira Vez");
        setSize(900, 500);
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
        btnPDF.setToolTipText("Gerar PDF do registro selecionado");

        ImageIcon iconPdf = new ImageIcon(
                new ImageIcon("C:\\jc_carapicuiba\\icons\\pdf_relatorio.jpg")
                        .getImage()
                        .getScaledInstance(28, 28, Image.SCALE_SMOOTH)
        );
        btnPDF.setIcon(iconPdf);

        btnPDF.addActionListener(e -> gerarPDF());

        p.add(btnPDF);

        return p;
    }

    private void gerarPDF() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um registro na tabela!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {

            // ===== DADOS DA LINHA SELECIONADA =====
            String codigo = model.getValueAt(linha, 0).toString();
            String nome = model.getValueAt(linha, 1).toString();
            String endereco = model.getValueAt(linha, 2).toString();
            String telefone = model.getValueAt(linha, 3).toString();
            String email = model.getValueAt(linha, 4).toString();
            String data = model.getValueAt(linha, 5).toString();
            String membroResp = model.getValueAt(linha, 6).toString();

            // ===== ARQUIVO =====
            com.itextpdf.text.Document document =
                    new com.itextpdf.text.Document(
                            com.itextpdf.text.PageSize.A4);

            String nomeArquivo =
                    "primeira_vez_" + codigo + ".pdf";

            com.itextpdf.text.pdf.PdfWriter.getInstance(
                    document,
                    new java.io.FileOutputStream(nomeArquivo)
            );

            document.open();

            // ===== LOGO =====
            com.itextpdf.text.Image logo =
                    com.itextpdf.text.Image.getInstance(
                            "C:\\jc_carapicuiba\\icons\\izunome_img.jpg");

            logo.scaleToFit(80, 80);
            logo.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
            document.add(logo);

            // ===== TÍTULO =====
            com.itextpdf.text.Font tituloFont =
                    new com.itextpdf.text.Font(
                            com.itextpdf.text.Font.FontFamily.HELVETICA,
                            14,
                            com.itextpdf.text.Font.BOLD);

            com.itextpdf.text.Paragraph titulo =
                    new com.itextpdf.text.Paragraph(
                            "\nJohrei Center Carapicuíba\n" +
                            "Registro – Primeira Vez\n\n",
                            tituloFont);

            titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(titulo);

            // ===== TEXTO =====
            com.itextpdf.text.Font textoFont =
                    new com.itextpdf.text.Font(
                            com.itextpdf.text.Font.FontFamily.HELVETICA,
                            11);

            document.add(new com.itextpdf.text.Paragraph("Código: " + codigo, textoFont));
            document.add(new com.itextpdf.text.Paragraph("Nome: " + nome, textoFont));
            document.add(new com.itextpdf.text.Paragraph("Endereço: " + endereco, textoFont));
            document.add(new com.itextpdf.text.Paragraph("Telefone: " + telefone, textoFont));
            document.add(new com.itextpdf.text.Paragraph("Email: " + email, textoFont));
            document.add(new com.itextpdf.text.Paragraph("Data Primeira Vez: " + data, textoFont));
            document.add(new com.itextpdf.text.Paragraph(
                    "Membro Responsável: " + membroResp, textoFont));

            // ===== RODAPÉ =====
            document.add(new com.itextpdf.text.Paragraph(
                    "\nRelatório gerado em: " +
                    java.time.LocalDateTime.now().format(dtf),
                    textoFont));

            document.close();

            JOptionPane.showMessageDialog(
                    this,
                    "PDF gerado com sucesso!\nArquivo: " + nomeArquivo,
                    "PDF",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar PDF:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }


	// ================= TABELA =================
    private JScrollPane criarTabela() {

    	model = new DefaultTableModel(
    	        new Object[]{
    	                "Código",
    	                "Nome",
    	                "Endereço",
    	                "Telefone",
    	                "Email",
    	                "Data Primeira Vez",
    	                "Membro Responsável"
    	        }, 0
    	) {
    	    public boolean isCellEditable(int r, int c) {
    	        return false;
    	    }
    	};


        tabela = new JTable(model);
        tabela.setRowHeight(24);

        return new JScrollPane(tabela);
    }

    // ================= PESQUISA =================
    private void pesquisar() {

        model.setRowCount(0);

        List<Object[]> lista =
                dao.pesquisarComMembro(txtNome.getText());

        for (Object[] o : lista) {
            model.addRow(new Object[]{
                    o[0],
                    o[1],
                    o[2],
                    o[3],
                    o[4],
                    ((java.time.LocalDateTime) o[5]).format(dtf),
                    o[6]
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrmPesquisaPrimeiraVez::new);
    }
}
