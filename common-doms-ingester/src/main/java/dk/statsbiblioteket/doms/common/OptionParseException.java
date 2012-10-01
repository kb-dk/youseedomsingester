package dk.statsbiblioteket.doms.common;

public class OptionParseException extends Exception {

    public OptionParseException(String message) {
        super(message);
    }
    
    public OptionParseException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
