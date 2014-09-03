package main.ftp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Agárimo
 */
public class ClienteFTP {

    private String username, password, host;
    private boolean login;
    FTPClient ftp;

    public ClienteFTP(FTPProveedor server) {
        this.username = server.getLogin();
        this.password = server.getPass();
        this.host = server.getHost();
        ftp = new FTPClient();
        try {
            ftp.connect(this.host);
            this.login = ftp.login(this.username, this.password);
            if (this.login) {
//                System.out.println("Login success...");
            } else {
                System.out.println("Failure success...");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setDirectorio(String directorio) throws IOException {
        ftp.changeWorkingDirectory(directorio);
    }

    public List getLista() throws IOException {
        List lista = new ArrayList(); // creamos una array List
        if (this.login) { // si es true
            FTPFile[] ftpFiles = ftp.listFiles();
            for (FTPFile ftpFile : ftpFiles) {
                lista.add(ftpFile.getName());
            }
        } else {
            System.out.println("No logeado...");
        }
        return lista;
    }

    public String directorioActual() {
        try {
            return ftp.printWorkingDirectory();
        } catch (IOException e) {
            System.out.println("IOException =" + e.getMessage());
            return null;
        }
    }

    public boolean getFichero(String origen, String destino) {
        try {
            try (InputStream in = ftp.retrieveFileStream(origen); FileOutputStream out = new FileOutputStream(new File(destino))) {
                int b = 0;
                while (b != -1) {
                    b = in.read();
                    if (b != -1) {
                        out.write(b);
                    }
                }
                out.close();
                in.close();
            }
            return true;
        } catch (IOException e) {
            System.out.println("IOException =" + e.getMessage());
            return false;
        }
    }

    public boolean setFichero(File origen, String destino) {
        try {
            ftp.setFileType(ftp.BINARY_FILE_TYPE);
            BufferedInputStream in;
            in=new BufferedInputStream(new FileInputStream(origen));
            ftp.enterLocalPassiveMode();
            ftp.storeFile(destino,in);
            in.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ClienteFTP.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void desconectar() {
        try {
            this.ftp.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(ClienteFTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
