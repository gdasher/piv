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
        InputStream pivConfig = SslClientAuth.class.getClassLoader().getResourceAsStream("us/dashernet/piv/piv.cfg");
        Security.insertProviderAt(new SunPKCS11(pivConfig), 6);
        Security.setProperty("ssl.KeyManagerFactory.algorithm", "pivcert");
        Security.setProperty("ssl.SocketFactory.provider", "us.dashernet.piv.PivSSLSocketFactory");
        if (args.length != 1) {
            System.out.println("Usage: SslClientAuth <url>");
        }
        else try{

            URL url = new URL(args[0]);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
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
