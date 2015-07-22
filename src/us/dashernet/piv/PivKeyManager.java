package us.dashernet.piv;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509KeyManager;

class PivKeyManager implements X509KeyManager {
  private X509KeyManager defaultKeyManager;

  public PivKeyManager(X509KeyManager defaultKeyManager) {
    this.defaultKeyManager = defaultKeyManager;
  }

  public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
    return "Certificate for PIV Authentication";
  }

  public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
    return this.defaultKeyManager.chooseServerAlias(keyType, issuers, socket);
  }
  
  public X509Certificate[] getCertificateChain(String alias) {
    return this.defaultKeyManager.getCertificateChain(alias);
  }

  public PrivateKey getPrivateKey(String alias) {
    return this.defaultKeyManager.getPrivateKey(alias);
  }

  public String[] getServerAliases(String keyType, Principal[] issuers) {
    return this.defaultKeyManager.getServerAliases(keyType, issuers);
  }

  public String[] getClientAliases(String keyType, Principal[] issuers) {
    return this.defaultKeyManager.getClientAliases(keyType, issuers);
  }
}
