package br.edu.leonardo.jaf.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Collection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * This class provides utility methods for SSL connections.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class SSLUtils {
    
    /**
     * This method returns the socket factory related to the default SSL context.
     * 
     * @return The socket factory.
     * @throws NetworkException If an error occurred during the process of obtaining the 
     *                          socket factory.
     */
    public static SSLSocketFactory getDefaultSocketFactory() throws NetworkException {
        try {
            SSLContext sslContext = SSLContext.getDefault();
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException ex) {
            throw new NetworkException(ex);
        }
    }
    
    /**
     * This method creates an one way socket factory using the given certificate file and the secure 
     * socket protocol, using the "X.509" certificate factory type.
     * 
     * @param caCrtFile The certificate file.
     * @param secureSocketProtocol The standard name of the requested protocol. See the SSLContext section in the
     *                             <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SSLContext">Java Cryptography Architecture Standard Algorithm Name Documentation </a>
     *                             for information about standard protocol names.
     * @return The socket factory.
     * @throws NetworkException If an error occurred during the process of obtaining the 
     *                          socket factory.
     */
    public static SSLSocketFactory getOneWaySocketFactory(final File caCrtFile, final String secureSocketProtocol) throws NetworkException {
        return getOneWaySocketFactory(caCrtFile, "X.509", secureSocketProtocol);
    }
    
    /**
     * This method creates an one way socket factory using the given certificate file, the certificate 
     * factory type, and the secure socket protocol.
     * 
     * @param caCrtFile The certificate file.
     * @param certFactoryType The name of the requested certificate type. See the CertificateFactory section in the 
     *                        <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#CertificateFactory">Java Cryptography Architecture Standard Algorithm Name Documentation</a>
     *                        for information about standard certificate types.
     * @param secureSocketProtocol The standard name of the requested protocol. See the SSLContext section in the
     *                             <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SSLContext">Java Cryptography Architecture Standard Algorithm Name Documentation </a>
     *                             for information about standard protocol names.
     * @return The socket factory.
     * @throws NetworkException If an error occurred during the process of obtaining the 
     *                          socket factory.
     */
    public static SSLSocketFactory getOneWaySocketFactory(final File caCrtFile, final String certFactoryType, final String secureSocketProtocol) throws NetworkException {
        FileInputStream caCrtFileInputStream = null;
        try {
            // Load all certificates in the server-side CA certificate chain.
            caCrtFileInputStream = new FileInputStream(caCrtFile);
            CertificateFactory cf = CertificateFactory.getInstance(certFactoryType);
            Collection<? extends Certificate> certs = cf.generateCertificates(caCrtFileInputStream);
            
            // Store the certificates in the KeyStore
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null, null);
            int index = 0;
            for (Certificate cert : certs) {
                caKs.setCertificateEntry("server_ca_" + index++, cert);
            }   
            
            // Create the TrustManager
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(caKs);
            
            // Utilize the TrustManager to construct the SSLContext and obtain the SSLSocketFactory.
            SSLContext sslContext = SSLContext.getInstance(secureSocketProtocol);
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | KeyManagementException | IOException ex) {
            throw new NetworkException(ex);
        } finally {
            try {
                if(caCrtFileInputStream != null)
                    caCrtFileInputStream.close();
            } catch (IOException ex) {
                throw new NetworkException(ex);
            }
        }
    }
}
