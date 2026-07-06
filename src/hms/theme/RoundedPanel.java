package hms.theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {

    private int arc;
    private Color borderColor;
    private int borderWidth = 1;
    private boolean shadowEnabled = false;
    private int shadowSize = 6;
    private Color shadowColor = new Color(0, 0, 0, 70);

    // Track internal padding separately so we can dynamically adjust 
    // the true border bounds based on shadow size.
    private final Insets internalPadding = new Insets(12, 14, 12, 14);

    public RoundedPanel() {
        this(16);
    }

    public RoundedPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
        setBackground(UIManager.getColor("Panel.background"));
        this.borderColor = UIManager.getColor("Component.borderColor");
        updateLogicalBorder();
    }

    /** 
     * Recalculates the border layout bounds. Combining the shadow offset 
     * and the internal padding guarantees that child components never 
     * bleed into the shadow or border space.
     */
    private void updateLogicalBorder() {
        int bottomRightSpacing = shadowEnabled ? shadowSize : 0;
        
        setBorder(BorderFactory.createEmptyBorder(
                internalPadding.top,
                internalPadding.left,
                internalPadding.bottom + bottomRightSpacing,
                internalPadding.right + bottomRightSpacing
        ));
    }

    // --- Getters & Setters ---

    public void setArc(int arc) {
        this.arc = arc;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        repaint();
    }

    public void setShadowEnabled(boolean enabled) {
        this.shadowEnabled = enabled;
        updateLogicalBorder();
        revalidate();
        repaint();
    }

    public void setShadowColor(Color color) {
        this.shadowColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int shadowOffset = shadowEnabled ? shadowSize : 0;
            int w = getWidth() - shadowOffset;
            int h = getHeight() - shadowOffset;

            // 1. Paint Shadow (Behind everything)
            if (shadowEnabled) {
                for (int i = shadowSize; i > 0; i--) {
                    int alpha = Math.max(0, shadowColor.getAlpha() - (i * (shadowColor.getAlpha() / shadowSize)));
                    g2.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), alpha));
                    // Push shadow down and right relative to layout step sizing
                    g2.fill(new RoundRectangle2D.Float(i, i, w - 1, h - 1, arc, arc));
                }
            }

            // 2. Paint Background
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc));

        } finally {
            g2.dispose();
        }
        
        // Super handles painting children inside safe layout bounds computed via updateLogicalBorder()
        super.paintComponent(g); 
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (borderColor == null || borderWidth <= 0) return;

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));

            int shadowOffset = shadowEnabled ? shadowSize : 0;
            float offset = borderWidth / 2f;
            
            // Deflate border geometry inward relative to stroke thickness to completely avoid clip cutting.
            float w = getWidth() - shadowOffset - borderWidth;
            float h = getHeight() - shadowOffset - borderWidth;

            g2.draw(new RoundRectangle2D.Float(offset, offset, w, h, arc, arc));
        } finally {
            g2.dispose();
        }
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        // Essential override when painting custom shapes over transparent backgrounds
        return false;
    }
}