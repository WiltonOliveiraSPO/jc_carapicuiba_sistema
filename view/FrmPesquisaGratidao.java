package view;

import dao.GratidaoDAO;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Image;
import java.io.FileOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

public class FrmPesquisaGratidao extends JFrame {

    private JTable tabela;
    private DefaultTableModel model;

    private JDateChooser dcDataIni, dcDataFim;
    private JComboBox<String> cbTipo;

    private final GratidaoDAO dao = new GratidaoDAO();

    private final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String[] TIPOS = {
            "Mensal","Diário","Gratidão Especial","Cultos Especiais",
            "Pedido de Prece","Solo Sagrado","Outorga de Ohikari",
            "Outorga de Shoko","Reoutorga","Paraíso no lar",
            "Sorei Saishi","Construção","Cerimonial"
    };

    public FrmPesquisaGratidao() {

        setTitle("Pesquisa – Donativos de Gratidão");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(criarAbas(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JTabbedPane criarAbas() {
        JTabbedPane tab = new JTabbedPane();
        tab.add("Por Data", painelPorData());
        tab.add("Por Tipo", painelPorTipo());
        return tab;
    }

    private JPanel painelPorData() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        p.add(new JLabel("Data Inicial:"));
        dcDataIni = new JDateChooser();
        dcDataIni.setDateFormatString("dd/MM/yyyy");
        dcDataIni.setPreferredSize(new Dimension(120, 25));
        p.add(dcDataIni);

        p.add(new JLabel("Data Final:"));
        dcDataFim = new JDateChooser();
        dcDataFim.setDateFormatString("dd/MM/yyyy");
        dcDataFim.setPreferredSize(new Dimension(120, 25));
        p.add(dcDataFim);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisarPorData());
        p.add(btnPesquisar);

        p.add(botaoPDF());

        return p;
    }

    private JPanel painelPorTipo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        p.add(new JLabel("Tipo:"));
        cbTipo = new JComboBox<>(TIPOS);
        p.add(cbTipo);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisarPorTipo());
        p.add(btnPesquisar);

        p.add(botaoPDF());

        return p;
    }

    private JScrollPane criarTabela() {

        model = new DefaultTableModel(
                new Object[]{"Código","Membro","Data","Valor","Tipo"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(24);

        return new JScrollPane(tabela);
    }

    private void pesquisarPorData() {

        if (dcDataIni.getDate() == null || dcDataFim.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione a data inicial e final.");
            return;
        }

        try {

            Date di = dcDataIni.getDate();
            Date df = dcDataFim.getDate();

            LocalDateTime inicio = di.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .atStartOfDay();

            LocalDateTime fim = df.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .atTime(23,59);

            preencherTabela(dao.pesquisarPorPeriodo(inicio, fim));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao pesquisar por data.");
        }
    }

    private void pesquisarPorTipo() {
        preencherTabela(
                dao.pesquisarPorTipo(cbTipo.getSelectedItem().toString()));
    }

    private void preencherTabela(List<Object[]> lista) {

        model.setRowCount(0);

        for (Object[] o : lista) {
            model.addRow(new Object[]{
                    o[0],
                    o[1],
                    ((LocalDateTime) o[2]).format(dtf),
                    o[3],
                    o[4]
            });
        }
    }

    private void gerarPDF() {

        if (tabela.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum registro para exportar.");
            return;
        }

        String pasta = "C:\\jc_carapicuiba\\relatorios";
        String caminho = pasta + "\\gratidao.pdf";

        try {

            java.io.File dir = new java.io.File(pasta);
            if (!dir.exists()) dir.mkdirs();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(caminho));
            document.open();

            Paragraph titulo = new Paragraph(
                    "Relatório de Donativos de Gratidão\n\n",
                    new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            PdfPTable pdfTable = new PdfPTable(tabela.getColumnCount());
            pdfTable.setWidthPercentage(100);

            for (int i = 0; i < tabela.getColumnCount(); i++) {
                pdfTable.addCell(new PdfPCell(
                        new Phrase(tabela.getColumnName(i))));
            }

            for (int r = 0; r < tabela.getRowCount(); r++) {
                for (int c = 0; c < tabela.getColumnCount(); c++) {
                    Object v = tabela.getValueAt(r, c);
                    pdfTable.addCell(v != null ? v.toString() : "");
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(this,
                    "PDF gerado em:\n" + caminho);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar PDF.");
        }
    }

    private JButton botaoPDF() {

        JButton b = new JButton();

        ImageIcon icon = new ImageIcon(
                new ImageIcon("C:\\jc_carapicuiba\\icons\\pdf_relatorio.jpg")
                        .getImage()
                        .getScaledInstance(26, 26, Image.SCALE_SMOOTH)
        );

        b.setIcon(icon);
        b.setToolTipText("Gerar PDF");
        b.addActionListener(e -> gerarPDF());

        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrmPesquisaGratidao::new);
    }
}
