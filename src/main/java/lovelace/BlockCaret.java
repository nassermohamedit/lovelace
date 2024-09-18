package lovelace;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.Segment;
import java.awt.*;

/**
 * @author nasser
 */
public class BlockCaret extends DefaultCaret {

    @Override
    public void paint(Graphics g) {

        if (isVisible()) {
            try {
                JTextComponent textArea = getComponent();
                g.setColor(textArea.getCaretColor());
                TextUI mapper = textArea.getUI();
                Rectangle r = mapper.modelToView(textArea, getDot());
                validateWidth(r);

                if (width > 0 && height > 0 &&
                        !contains(r.x, r.y, r.width, r.height)) {
                    Rectangle clip = g.getClipBounds();
                    if (clip != null && !clip.contains(this)) {
                        repaint();
                    }
                    damage(r);
                }
                r.height -= 2;

                Color textAreaBg = textArea.getBackground();
                if (textAreaBg == null) {
                    textAreaBg = Color.white;
                }
                g.setXORMode(textAreaBg);
                g.fillRect(r.x, r.y, r.width, r.height);
            } catch (BadLocationException ble) {
                ble.printStackTrace();
            }
        }
    }


    protected synchronized void damage(Rectangle r) {
        if (r != null) {
            validateWidth(r);
            x = r.x - 1;
            y = r.y;
            width = r.width + 4;
            height = r.height;
            repaint();
        }
    }

    void validateWidth(Rectangle rect) {
        if (rect != null && rect.width <= 1) {
            try {
                Segment seg = new Segment();
                JTextComponent textArea = getComponent();
                textArea.getDocument().getText(getDot(), 1, seg);
                Font font = textArea.getFont();
                FontMetrics fm = textArea.getFontMetrics(font);
                rect.width = fm.charWidth(seg.array[seg.offset]);

                if (rect.width == 0) {
                    rect.width = fm.charWidth(' ');
                }
            } catch (BadLocationException ble) {
                rect.width = 8;
            }

        }


    }
}
