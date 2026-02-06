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

        ImageIcon iconOriginal =
                new ImageIcon("C:\\jc_carapicuiba\\icons\\izunome_img.jpg");

        Image imagemRedimensionada = iconOriginal.getImage()
                .getScaledInstance(450, 260, Image.SCALE_SMOOTH);

        JLabel lblImagem = new JLabel(new ImageIcon(imagemRedimensionada));
        lblImagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel painelCentral = new JPanel();
        painelCentral.setBackground(fundo);
        painelCentral.setLayout(new BoxLayout(painelCentral, BoxLayout.Y_AXIS));

        // Espaço superior (empurra tudo para cima)
        painelCentral.add(Box.createVerticalStrut(40));

        painelCentral.add(lblImagem);

        // Espaço inferior controlado
        painelCentral.add(Box.createVerticalStrut(20));

        add(painelCentral, BorderLayout.CENTER);
    }

    // ================= BOTÕES =================
    private void adicionarBotoesInferiores() {

        // Painel principal do rodapé
        JPanel painelRodape = new JPanel();
        painelRodape.setBackground(fundo);
        painelRodape.setLayout(new BorderLayout());

        // Painel dos botões (2 linhas x 5 colunas)
        JPanel painelBotoes = new JPanel(new GridLayout(2, 5, 20, 20));
        painelBotoes.setBackground(fundo);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        Map<String, Runnable> botoes = new LinkedHashMap<>();

        // ===== CADASTROS =====
        botoes.put("Dedicação Diária", FrmPedidos::new);
        botoes.put("Frequência Diária", FrmDedicacao::new);
        botoes.put("Cadastro de Membros", FrmMembros::new);
        botoes.put("Cadastro de Primeira Vez", FrmPrimeiraVez::new);
        botoes.put("Gratidão Diária", FrmGratidao::new);

        // ===== PESQUISAS =====
        botoes.put("Pesquisa de Dedicação", FrmPesquisaDedicacao::new);
        botoes.put("Pesquisa de Pedidos Diários", FrmPesquisaPedidosDiarios::new);
        botoes.put("Pesquisa de Membros",FrmPesquisaMembro::new);
        botoes.put("Pesquisa de Primeira Vez",FrmPesquisaPrimeiraVez::new);
        botoes.put("Pesquisa de Gratidão", FrmPesquisaGratidao::new);

        botoes.forEach((texto, acao) -> {
            JButton botao = criarBotao(texto);
            botao.addActionListener(e -> acao.run());
            painelBotoes.add(botao);
        });

        painelRodape.add(painelBotoes, BorderLayout.CENTER);

        add(painelRodape, BorderLayout.SOUTH);
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
