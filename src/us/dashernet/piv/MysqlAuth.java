package us.dashernet.piv;

import java.io.*;
import java.security.*;
import javax.net.ssl.*;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;
import sun.security.pkcs11.SunPKCS11;
 
class MysqlAuth {
 
    public static void main(String[] args) {
        // Do global inititing
        Security.addProvider(new PivCertChoosingProvider());
        InputStream pivConfig = MysqlAuth.class.getClassLoader().getResourceAsStream("us/dashernet/piv/piv.cfg");
        Security.insertProviderAt(new SunPKCS11(pivConfig), 6);
        Security.setProperty("ssl.KeyManagerFactory.algorithm", "pivcert");

        if (args.length != 0) {
            System.out.println("Usage: MysqlAuth");
        } else try{
            Connection connection = connect();
            if (connection != null) {
                System.out.println("You made it, take control your database now!");
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM test");
                while (rs.next()) {
                  System.out.println("Id: " + rs.getInt("id") + " ; Val: " + rs.getString("val"));
                }
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    private static Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return null;
        }
       
        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;
       
        try {
            Properties props = new Properties();
            props.setProperty("useSSL", "true");
            props.setProperty("verifyServerCertificate", "true");
            String pin = new String(System.console().readPassword("Enter Pin: "));
            props.setProperty("clientCertificateKeyStoreType", "PKCS11");
            props.setProperty("clientCertificateKeyStoreUrl", "file:/dev/null");
            props.setProperty("clientCertificateKeyStorePassword", pin);
            props.setProperty("trustCertificateKeyStoreType", "JKS");
            props.setProperty("trustCertificateKeyStoreUrl", System.getProperty("us.dashernet.piv.cacertsUrl"));
            props.setProperty("trustCertificateKeyStorePassword", System.getProperty("us.dashernet.piv.cacertsPw"));
            props.setProperty("user", "gwdasher");
            connection = DriverManager.getConnection("jdbc:mysql://mysql.dashernet.us:3306/test", props);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

        return connection;
       
    }

}
