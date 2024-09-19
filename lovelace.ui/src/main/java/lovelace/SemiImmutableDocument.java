package lovelace;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 * @author nasser
 */
public interface SemiImmutableDocument extends StyledDocument {


    int getMark();

    void mark(int mark);

    default void mark() {
        mark(getLength());
    }

    default void insertAndMark(String str, AttributeSet attr) {
        try {
            insertString(getLength(), str, attr);
            mark();
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
