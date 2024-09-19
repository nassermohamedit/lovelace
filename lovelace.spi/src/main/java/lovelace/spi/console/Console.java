package lovelace.spi.console;


import java.io.IOException;

public interface Console {

    void begin();

    void end();

    void clear();

    Input in();

    Output out();

}
