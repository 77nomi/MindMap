package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;


public class CustomPanel extends JPanel {
    private JButton button1;
    private JButton button2;

    public CustomPanel(JButton button1, JButton button2) {
        this.button1 = button1;
        this.button2 = button2;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 绘制连线
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2d.setComposite(alphaComposite);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.BLACK);
        g2d.draw(new Line2D.Double(button1.getBounds().getCenterX(), button1.getBounds().getCenterY(),
                button2.getBounds().getCenterX(), button2.getBounds().getCenterY()));
    }
}
