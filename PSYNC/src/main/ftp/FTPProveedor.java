package main.ftp;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Agárimo
 */
public class FTPProveedor implements Serializable {

    private int id;
    private String nombre;
    private String login;
    private String pass;
    private String host;
    private String archivo;
    
    public FTPProveedor(int id){
        this.id=id;
    }
    
    public FTPProveedor(String nombre){
        this.nombre=nombre;
    }

    public FTPProveedor(String nombre, String login, String pass, String host,String archivo) {
        this.nombre = nombre;
        this.login = login;
        this.pass = pass;
        this.host = host;
        this.archivo=archivo;
    }

    public FTPProveedor(int id, String nombre, String login, String pass, String host,String archivo) {
        this(nombre, login, pass, host,archivo);
        this.id = id;
    }
    
    public int getId(){
        return id;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String user) {
        this.login = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "FTPProveedor{" + "nombre=" + nombre + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.login);
        hash = 17 * hash + Objects.hashCode(this.pass);
        hash = 17 * hash + Objects.hashCode(this.host);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FTPProveedor other = (FTPProveedor) obj;
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.pass, other.pass)) {
            return false;
        }
        if (!Objects.equals(this.host, other.host)) {
            return false;
        }
        return true;
    }

    public String SQLCrear() {
        String query = "INSERT into psync.ftp (nombre,login,pass,host,archivo) values("
                + util.Varios.entrecomillar(this.nombre) + ","
                + util.Varios.entrecomillar(this.login) + ","
                + util.Varios.entrecomillar(this.pass) + ","
                + util.Varios.entrecomillar(this.host) + ","
                + util.Varios.entrecomillar(this.archivo)
                + ")";
        return query;
    }

    public String SQLBorrar() {
        String query = "DELETE from psync.ftp WHERE id=" + this.id;
        return query;
    }

    public String SQLEditar() {
        String query = "UPDATE psync.ftp SET "
                + "login=" + util.Varios.entrecomillar(this.login) + ","
                + "pass=" + util.Varios.entrecomillar(this.pass) + ","
                + "archivo=" + util.Varios.entrecomillar(this.archivo) + ","
                + "host=" + util.Varios.entrecomillar(this.host) + " "
                + "WHERE id=" + this.id;
        return query;
    }

    public String SQLBuscar() {
        String query = "SELECT * FROM psync.ftp WHERE nombre="+util.Varios.entrecomillar(this.nombre);
        return query;
    }

    public String SQLBuscarId() {
        String query = "SELECT * FROM psync.ftp WHERE id="+this.id;
        return query;
    }
}
