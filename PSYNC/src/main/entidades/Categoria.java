package main.entidades;

import java.util.Objects;

/**
 *
 * @author Agárimo
 */
public class Categoria {

    private int id;
    private String nombre;
    private int id_padre;

    public Categoria() {

    }

    public Categoria(int id) {
        this.id = id;
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre_categoria) {
        this.nombre = nombre_categoria;
    }

    public int getId_padre() {
        return id_padre;
    }

    public void setId_padre(int id_padre) {
        this.id_padre = id_padre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.nombre);
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
        final Categoria other = (Categoria) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    public String SQLCrear() {
        String query = "INSERT INTO psync.categoria (id,nombre,id_padre) values("
                + this.id + ","
                + util.Varios.entrecomillar(this.nombre) + ","
                + this.id_padre
                + ")";
        return query;
    }

    public String SQLBorrar() {
        String query = "DELETE FROM psync.categoria WHERE id=" + this.id;
        return query;
    }

    public String SQLEditar() {
        String query = "UPDATE psync.categoria SET "
                + "nombre=" + util.Varios.entrecomillar(this.nombre) + ","
                + "id_padre=" + this.id_padre + " "
                + "WHERE id=" + this.id;
        return query;
    }

    public String SQLBuscar() {
        String query = "SELECT * FROM psync.categoria WHERE nombre=" + util.Varios.entrecomillar(this.nombre);
        return query;
    }

    public String SQLBuscarId() {
        String query = "SELECT * FROM psync.categoria WHERE id=" + this.id;
        return query;
    }
}
