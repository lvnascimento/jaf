package br.edu.leonardo.jaf.net;

/**
 * This exception is related to errors that occur is a network operation.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class NetworkException extends Exception {

    public NetworkException() {
    }

    public NetworkException(String string) {
        super(string);
    }

    public NetworkException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public NetworkException(Throwable thrwbl) {
        super(thrwbl);
    }

    public NetworkException(String string, Throwable thrwbl, boolean bln, boolean bln1) {
        super(string, thrwbl, bln, bln1);
    }
    
}
