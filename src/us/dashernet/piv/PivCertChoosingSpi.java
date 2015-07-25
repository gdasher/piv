package us.dashernet.piv;

import javax.net.ssl.*;
import java.security.*;

public class PivCertChoosingSpi extends KeyManagerFactorySpi {
    private KeyManager[] wrappedManagers;

    public PivCertChoosingSpi() {

    }

    public void engineInit(KeyStore ks, char[] password) 
      throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
       KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
       factory.init(ks, password);

       wrapFactory(factory);
    }

    public void engineInit(ManagerFactoryParameters params) throws InvalidAlgorithmParameterException {
       try {
         KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
         factory.init(params);

         wrapFactory(factory);
       } catch (NoSuchAlgorithmException nsae) {
         throw new InvalidAlgorithmParameterException("Should not happen", nsae);
       }
    }

    private void wrapFactory(KeyManagerFactory factory) {
       KeyManager[] managers = factory.getKeyManagers();
       this.wrappedManagers = new KeyManager[managers.length];

       for (int i = 0; i < managers.length; ++i) {
           if (managers[i] instanceof X509KeyManager) {
               wrappedManagers[i] = new PivKeyManager((X509KeyManager)managers[i]);
           } else {
               wrappedManagers[i] = managers[i];
           }
       }
    }

    public KeyManager[] engineGetKeyManagers() {
      return wrappedManagers;
    }
}
