package main.entidades;

/**
 *
 * @author Agárimo
 */
public class Descripcion {

    private int id;
    private String descripcion;

    public Descripcion() {

    }

    public Descripcion(int id) {
        this.id = id;
    }

    public Descripcion(int id, String descripcion) {
        this.id = id;
        if (descripcion == null) {
            this.descripcion = "";
        } else {
            this.descripcion = descripcion;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String SQLCrear() {
        String query = "INSERT INTO psync.descripcion (id,descripcion) values("
                + this.id + ","
                + util.Varios.entrecomillar(this.descripcion)
                + ")";
        return query;
    }

    public String SQLEditar() {
        String query = "UPDATE psync.descripcion SET "
                + "descripcion=" + util.Varios.entrecomillar(this.descripcion) + " "
                + "WHERE id=" + this.id;
        return query;
    }

    public String SQLBuscar() {
        String query = "SELECT * FROM psync.descripcion WHERE id=" + this.id;
        return query;
    }

    public String updateDescription() {
        String query = "UPDATE admin_electropresta.EM_product_lang SET "
                + "description=" + util.Varios.entrecomillar(this.descripcion) + " "
                + "WHERE id_product=" + this.id + " AND id_lang=1";
        return query;
    }
}
