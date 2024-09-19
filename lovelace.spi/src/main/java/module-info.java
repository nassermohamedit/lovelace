/**
 * @author nasser
 */
module lovelace.spi {
    requires java.desktop;
    exports lovelace.spi.exe;
    exports lovelace.spi.console to lovelace.ui;
}
