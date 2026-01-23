package view;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class TelaInicial extends JFrame {

    private final Color fundo = new Color(45, 45, 45);
    private final Color verde = new Color(0, 153, 102);

    public TelaInicial() {
        setTitle("Sistema da Igreja – Johrei Center Carapicuíba");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(fundo);

        adicionarImagemCentral();
        adicionarBotoesInferiores();

        setVisible(true);
    }

    // ================= IMAGEM CENTRAL =================
    private void adicionarImagemCentral() {
        ImageIcon icon = new ImageIcon("C:\\jc_carapicuiba\\icons\\izunome_img.jpg");
        JLabel lblImagem = new JLabel(icon);
        lblImagem.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel painelImagem = new JPanel(new BorderLayout());
        painelImagem.setBackground(fundo);
        painelImagem.add(lblImagem, BorderLayout.CENTER);

        add(painelImagem, BorderLayout.CENTER);
    }

    // ================= BOTÕES =================
    private void adicionarBotoesInferiores() {

        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painel.setBackground(fundo);

        // 🔹 Mapa para manter ordem e facilitar manutenção
        Map<String, Runnable> botoes = new LinkedHashMap<>();

        // ===== CADASTROS =====
        botoes.put("Dedicação Diária", FrmPedidos::new);
        botoes.put("Frequência Diária", FrmDedicacao::new);
        botoes.put("Cadastro de Membros", FrmMembros::new);
        botoes.put("Cadastro de Primeira Vez", FrmPrimeiraVez::new);
        botoes.put("Gratidão Diária", FrmGratidao::new);

        // ===== PESQUISAS (futuras telas) =====
        botoes.put("Pesquisa de Dedicação", () ->
                JOptionPane.showMessageDialog(this, "Tela em desenvolvimento"));

        botoes.put("Pesquisa de Frequência", () ->
                JOptionPane.showMessageDialog(this, "Tela em desenvolvimento"));

        botoes.put("Pesquisa de Membros", () ->
                JOptionPane.showMessageDialog(this, "Tela em desenvolvimento"));

        botoes.put("Pesquisa de Primeira Vez", () ->
                JOptionPane.showMessageDialog(this, "Tela em desenvolvimento"));

        botoes.put("Pesquisa de Gratidão", () ->
                JOptionPane.showMessageDialog(this, "Tela em desenvolvimento"));

        // ===== CRIAÇÃO AUTOMÁTICA =====
        botoes.forEach((texto, acao) -> {
            JButton botao = criarBotao(texto);
            botao.addActionListener(e -> acao.run());
            painel.add(botao);
        });

        add(painel, BorderLayout.SOUTH);
    }

    // ================= BOTÃO PADRÃO =================
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(220, 60));
        btn.setFocusPainted(false);
        btn.setBackground(verde);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaInicial::new);
    }
}
