package us.dashernet.piv;

import java.io.*;
import java.security.*;
import javax.net.ssl.*;
import java.net.URL;
import sun.security.pkcs11.SunPKCS11;
 
class SslClientAuth {
 
    public static void main(String[] args) {
        // Do global inititing
        Security.addProvider(new PivCertChoosingProvider());
        Security.addProvider(new SunPKCS11("/etc/java-7-openjdk/security/piv.cfg"));
        Security.setProperty("ssl.KeyManagerFactory.algorithm", "pivcert");
        if (args.length != 1) {
            System.out.println("Usage: SslClientAuth <url>");
        }
        else try{

            KeyStore ks = KeyStore.getInstance("PKCS11", "SunPKCS11-PIVProxy");
            char[] pin = System.console().readPassword("Enter Pin: ");
            ks.load(null, pin);

            URL url = new URL(args[0]);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(ks, pin);
            context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            conn.setSSLSocketFactory(context.getSocketFactory());

            printContent(conn);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    };

    private static void printContent(HttpsURLConnection conn) {
      if(conn != null){
        try {
          BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
          String input;
     
          while ((input = br.readLine()) != null){
            System.out.println(input);
          }
          br.close();
     
        } catch (IOException e) {
           e.printStackTrace();
        }
      }
    }
}
