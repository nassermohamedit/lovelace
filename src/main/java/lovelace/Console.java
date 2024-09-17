package lovelace;


import java.io.IOException;

public interface Console {

    void begin();

    void end();

    void clear();

    In in();

    Out out();

    interface In {

        String read() throws IOException, InterruptedException;
    }

    interface Out {

        void print(String s);

        void println(String s);
    }
}
