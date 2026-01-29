package view;

import dao.PedidoDAO;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

//===== iText PDF =====
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.FileOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class FrmPesquisaPedidosDiarios extends JFrame {

    private JDateChooser dcDataIni;
    private JDateChooser dcDataFim;
    private JTable tabela;

    private JLabel lblTotAgra, lblTotGraca, lblTotElev, lblTotAniv;

    private PedidoDAO dao = new PedidoDAO();

    public FrmPesquisaPedidosDiarios() {

        setTitle("Pesquisa de Pedidos Diários");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(criarPainelFiltro(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ================= FILTROS =================
    private JPanel criarPainelFiltro() {

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        painel.add(new JLabel("Data Inicial:"));
        dcDataIni = new JDateChooser();
        dcDataIni.setDateFormatString("dd/MM/yyyy");
        dcDataIni.setPreferredSize(new Dimension(120, 25));
        painel.add(dcDataIni);

        painel.add(new JLabel("Data Final:"));
        dcDataFim = new JDateChooser();
        dcDataFim.setDateFormatString("dd/MM/yyyy");
        dcDataFim.setPreferredSize(new Dimension(120, 25));
        painel.add(dcDataFim);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisar());
        painel.add(btnPesquisar);

        return painel;
    }

    // ================= TABELA =================
    private JScrollPane criarTabela() {

        tabela = new JTable(new DefaultTableModel(
                new Object[]{
                        "Data",
                        "Agradecimento",
                        "Graça",
                        "Elevação",
                        "Aniv. Falecimento"
                }, 0
        ));

        tabela.setRowHeight(25);
        return new JScrollPane(tabela);
    }

    // ================= RODAPÉ =================
    private JPanel criarRodape() {

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        lblTotAgra = new JLabel("Agradecimento: 0");
        lblTotGraca = new JLabel("Graça: 0");
        lblTotElev = new JLabel("Elevação: 0");
        lblTotAniv = new JLabel("Aniv. Falec.: 0");

        JButton btnPDF = new JButton();
        btnPDF.setToolTipText("Exportar para PDF");

        ImageIcon iconPdf = new ImageIcon(
                new ImageIcon("C:\\jc_carapicuiba\\icons\\pdf_relatorio.jpg")
                        .getImage()
                        .getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH)
        );

        btnPDF.setIcon(iconPdf);
        btnPDF.addActionListener(e -> exportarPDF());

        painel.add(btnPDF);
        painel.add(lblTotAgra);
        painel.add(lblTotGraca);
        painel.add(lblTotElev);
        painel.add(lblTotAniv);

        return painel;
    }

    // ================= PESQUISA =================
    private void pesquisar() {

        if (dcDataIni.getDate() == null || dcDataFim.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione a data inicial e final!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate ini = dcDataIni.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        LocalDate fim = dcDataFim.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();

        List<Pedido> lista = dao.listar()
                .stream()
                .filter(p -> !p.getDtPedidos().isBefore(ini)
                          && !p.getDtPedidos().isAfter(fim))
                .toList();

        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);

        int totAgra = 0, totGraca = 0, totElev = 0, totAniv = 0;

        for (Pedido p : lista) {

            model.addRow(new Object[]{
                    p.getDtPedidos(),
                    p.getQtdeAgradecimento(),
                    p.getQtdeGraca(),
                    p.getQtdeElevacao(),
                    p.getQtdeAnivFalec()
            });

            totAgra += p.getQtdeAgradecimento();
            totGraca += p.getQtdeGraca();
            totElev += p.getQtdeElevacao();
            totAniv += p.getQtdeAnivFalec();
        }

        lblTotAgra.setText("Agradecimento: " + totAgra);
        lblTotGraca.setText("Graça: " + totGraca);
        lblTotElev.setText("Elevação: " + totElev);
        lblTotAniv.setText("Aniv. Falec.: " + totAniv);
    }

    // ================= PDF =================
    private void exportarPDF() {

        if (tabela.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum dado para exportar!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document,
                    new FileOutputStream("pedidos_diarios.pdf"));

            document.open();

            Image logo = Image.getInstance(
                    "C:\\jc_carapicuiba\\icons\\izunome_img.jpg");
            logo.scaleToFit(80, 80);
            document.add(logo);

            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

            Paragraph titulo = new Paragraph(
                    "Johrei Center Carapicuíba - Pedidos Diários\n\n",
                    tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            String[] headers = {
                    "Data", "Agradecimento", "Graça", "Elevação", "Aniv. Falecimento"
            };

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            DefaultTableModel model = (DefaultTableModel) tabela.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    table.addCell(model.getValueAt(i, j).toString());
                }
            }

            document.add(table);
            document.close();

            JOptionPane.showMessageDialog(this,
                    "PDF gerado com sucesso!",
                    "PDF", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar PDF:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
