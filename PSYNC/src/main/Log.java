package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Dates;

/**
 *
 * @author Agárimo
 */
public class Log {

    private File dir;
    private File archivo;
    private boolean general;

    public Log() {
        this.general = true;
        creaFichero();
        this.archivo = new File(dir, "log.log");
        creaArchivo();
    }

    public Log(String nombre) {
        this.general = false;
        creaFichero();
        this.archivo = new File(dir, nombre + Dates.imprimeFecha(Dates.curdate()) + ".log");
        creaArchivo();
    }

    private void creaFichero() {
        if (Main.isLinux) {
            this.dir = new File("appEMG/log");
        } else {
            this.dir = new File("log");
        }
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    private void creaArchivo() {
        if (!this.archivo.exists()) {
            try {
                this.archivo.createNewFile();
                if (general) {
                    escribeMsg("--LOG DE EJECUCION--");
                } else {
                    escribeMsg("REGISTRO DE EVENTOS DE EJECUCIÓN -- " + Dates.imprimeFecha(Dates.curdate()));
                }
            } catch (IOException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void escribeMsg(String msg) {
        String hora = Dates.imprimeFechaCompleta(Dates.curdate());

        try {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(archivo, true))) {
                out.write(hora + "\t" + msg);
                out.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void escribeError(String error, String msg) {
        String hora = Dates.imprimeFechaCompleta(Dates.curdate());

        try {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(archivo, true))) {
                out.write(hora + "\t" + error + "\t" + msg);
                out.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
