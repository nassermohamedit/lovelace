package lovelace;

import javax.swing.text.*;

/**
 * @author nasser
 */
public class ConsoleDocument extends DefaultStyledDocument implements SemiImmutableDocument {

    private int mark = 0;

    public ConsoleDocument() {
        super();
        init();
    }

    private void init() {
   }

    @Override
    public int getMark() {
        return mark;
    }

    @Override
    public void mark(int mark) {
        this.mark = mark;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        checkOffset(offs);
        super.insertString(offs, str, a);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        checkOffset(offs + len - 1);
        super.remove(offs, len);
    }

    public void insertString(String str, AttributeSet a) {
        int offs = getLength();
        try {
            insertString(offs, str, a);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void insertAndMark(String str, AttributeSet a) {
        insertString(str, a);
        mark();
    }

    public String getMutText() {
        String text;
        try {
            text = getText(mark, getLength() - mark);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        return text;
    }


    private void checkOffset(int offs) throws BadLocationException {
        if (offs < mark)
            throw new BadLocationException("Inchangeable section", offs);
    }
}
