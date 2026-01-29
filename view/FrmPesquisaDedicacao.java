package view;

import dao.DedicacaoDAO;
import model.Dedicacao;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

//===== iText PDF =====
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.FlowLayout;
import java.awt.*;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class FrmPesquisaDedicacao extends JFrame {

	private JDateChooser dcDataIni;
	private JDateChooser dcDataFim;
	private JTable tabela;
    private JLabel lblTotMembros, lblTotFreq, lblTotPrim;

    private DedicacaoDAO dao = new DedicacaoDAO();

    public FrmPesquisaDedicacao() {
        setTitle("Pesquisa de Dedicação Diária");
        setSize(900, 600);
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
                new Object[]{"Data", "Membros", "Frequentadores", "Primeira Vez"}, 0
        ));
        tabela.setRowHeight(25);
        return new JScrollPane(tabela);
    }

    // ================= RODAPÉ =================
    private JPanel criarRodape() {

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        lblTotMembros = new JLabel("Total Membros: 0");
        lblTotFreq = new JLabel("Total Frequentadores: 0");
        lblTotPrim = new JLabel("Total 1ª Vez: 0");

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

        painel.add(lblTotMembros);
        painel.add(lblTotFreq);
        painel.add(lblTotPrim);
        return painel;
    }

    // ================= PESQUISA =================
    private void pesquisar() {

        try {
        	if (dcDataIni.getDate() == null || dcDataFim.getDate() == null) {
        	    JOptionPane.showMessageDialog(this,
        	            "Selecione a data inicial e final!",
        	            "Aviso", JOptionPane.WARNING_MESSAGE);
        	    return;
        	}

        	LocalDate ini = dcDataIni.getDate()
        	        .toInstant()
        	        .atZone(java.time.ZoneId.systemDefault())
        	        .toLocalDate();

        	LocalDate fim = dcDataFim.getDate()
        	        .toInstant()
        	        .atZone(java.time.ZoneId.systemDefault())
        	        .toLocalDate();

            List<Dedicacao> lista =
                    dao.pesquisarPorPeriodo(Date.valueOf(ini), Date.valueOf(fim));

            DefaultTableModel model = (DefaultTableModel) tabela.getModel();
            model.setRowCount(0);

            int totMembros = 0, totFreq = 0, totPrim = 0;

            for (Dedicacao d : lista) {
                model.addRow(new Object[]{
                        d.getDtDedicacao(),
                        d.getQtdeMembros(),
                        d.getQtdeFrequentadores(),
                        d.getQtdePrimVez()
                });

                totMembros += d.getQtdeMembros();
                totFreq += d.getQtdeFrequentadores();
                totPrim += d.getQtdePrimVez();
            }

            lblTotMembros.setText("Total Membros: " + totMembros);
            lblTotFreq.setText("Total Frequentadores: " + totFreq);
            lblTotPrim.setText("Total 1ª Vez: " + totPrim);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Datas inválidas! Use yyyy-mm-dd",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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
                    new FileOutputStream("dedicacao_diaria.pdf"));

            document.open();

            // ===== IMAGEM =====
            Image logo = Image.getInstance(
                    "C:\\jc_carapicuiba\\icons\\izunome_img.jpg");
            logo.scaleToFit(80, 80);
            logo.setAlignment(Image.ALIGN_LEFT);
            document.add(logo);

            // ===== TÍTULO =====
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

            Paragraph titulo = new Paragraph(
                    "Johrei Center Carapicuíba - Dedicação Diária - Frequência\n\n",
                    tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // ===== TABELA =====
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 2f, 3f, 2f});

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

            String[] headers = {"Data", "Membros", "Frequentadores", "Primeira Vez"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            DefaultTableModel model = (DefaultTableModel) tabela.getModel();

            int totMembros = 0, totFreq = 0, totPrim = 0;

            for (int i = 0; i < model.getRowCount(); i++) {

                table.addCell(model.getValueAt(i, 0).toString());
                table.addCell(model.getValueAt(i, 1).toString());
                table.addCell(model.getValueAt(i, 2).toString());
                table.addCell(model.getValueAt(i, 3).toString());

                totMembros += Integer.parseInt(model.getValueAt(i, 1).toString());
                totFreq += Integer.parseInt(model.getValueAt(i, 2).toString());
                totPrim += Integer.parseInt(model.getValueAt(i, 3).toString());
            }

            document.add(table);

            // ===== TOTAIS =====
            document.add(new Paragraph("\n"));

            Font totalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

            Paragraph totais = new Paragraph(
                    "Total Geral:\n" +
                    "Membros: " + totMembros + "\n" +
                    "Frequentadores: " + totFreq + "\n" +
                    "Primeira Vez: " + totPrim,
                    totalFont);

            totais.setAlignment(Element.ALIGN_RIGHT);
            document.add(totais);

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
