package us.dashernet.piv;

import javax.net.ssl.SSLSocketFactory;
import java.security.*;
import javax.net.ssl.*;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;

public class PivSSLSocketFactory extends SSLSocketFactory {
  SSLSocketFactory underlyingFactory = null;

  public PivSSLSocketFactory() {
      super();
      try {
          SSLContext context = SSLContext.getInstance("TLSv1.2");
          KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
              KeyManagerFactory.getDefaultAlgorithm());
          KeyStore ks = KeyStore.getInstance("PKCS11", "SunPKCS11-PIVProxy");
          char[] pin = System.console().readPassword("Enter Pin: ");
          ks.load(null, pin);

          keyManagerFactory.init(ks, pin);
          context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
          underlyingFactory = context.getSocketFactory();
      } catch (Exception e) {
          e.printStackTrace(); 
      }
  }

  public String[] getDefaultCipherSuites() {
      return underlyingFactory.getDefaultCipherSuites();
  }

  public String[] getSupportedCipherSuites() {
      return underlyingFactory.getSupportedCipherSuites();
  }

  public Socket createSocket() throws IOException {
      return underlyingFactory.createSocket();
  }

  public Socket createSocket(String host, int port) throws IOException {
      return underlyingFactory.createSocket(host, port);
  }

  public Socket createSocket(InetAddress address, int port) throws IOException {
      return underlyingFactory.createSocket(address, port);
  }

  public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
      return underlyingFactory.createSocket(s, host, port, autoClose);
  }
  
  public Socket createSocket(String host, int port, InetAddress clientAddress, int clientPort) throws IOException { 
      return underlyingFactory.createSocket(host, port, clientAddress, clientPort);
  }

  public Socket createSocket(InetAddress address, int port,
                             InetAddress clientAddress, int clientPort) throws IOException {
      return underlyingFactory.createSocket(address, port, clientAddress, clientPort);
  }
}
