package view;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    private final Color backgroundColor;

    public RoundedButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 14));
        setPreferredSize(new Dimension(200, 60));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g);
        g2.dispose();
    }
}
