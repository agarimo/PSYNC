package main.entidades;

import java.util.Objects;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agárimo
 */
public class Producto {

    private int id;
    private int idCategoria;
    private String nombre;
    private int stock;
    private double precio;
    private boolean isActivo;

    public Producto() {
    }

    public Producto(int id) {
        this.id = id;
    }

    public Producto(int idCategoria, String nombre, boolean isActivo,int stock, double precio) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.isActivo = isActivo;
        this.stock=stock;
        this.precio=precio;
    }

    public Producto(int id, int idCategoria, String nombre, boolean isActivo,int stock, double precio) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.isActivo = isActivo;
        this.stock=stock;
        this.precio=precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return isActivo;
    }

    public void setActivo(boolean isActivo) {
        this.isActivo = isActivo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isIsActivo() {
        return isActivo;
    }

    public void setIsActivo(boolean isActivo) {
        this.isActivo = isActivo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + this.idCategoria;
        hash = 29 * hash + Objects.hashCode(this.nombre);
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
        final Producto other = (Producto) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.idCategoria != other.idCategoria) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    public String SQLCrear() {
        String query = "INSERT INTO psync.producto (id,id_categoria,nombre,activo,stock,precio,update_precio,update_stock) values("
                + this.id + ","
                + this.idCategoria + ","
                + util.Varios.entrecomillar(this.nombre) + ","
                + this.isActivo + ","
                + this.stock + ","
                + this.precio + ","
                + "curdate()" + ","
                + "curdate()"
                + ")";
        return query;
    }

    public String SQLEditar() {
        String query = "UPDATE psync.producto SET "
                + "id_categoria=" + this.idCategoria + ","
                + "nombre=" + this.nombre + ","
                + "stock=" + this.stock + ","
                + "precio=" + this.precio + ","
                + "activo=" + this.isActivo + ","
                + "update_precio=" + Varios.entrecomillar(Dates.imprimeFechaCompleta(Dates.curdate())) + ","
                + "update_stock=" + Varios.entrecomillar(Dates.imprimeFechaCompleta(Dates.curdate())) + " "
                + "WHERE id=" + this.id;
        return query;
    }

    public String SQLCreaPendiente() {
        String query = "INSERT INTO psync.pendiente_insercion (id_producto) values(" + this.id + ")";
        return query;
    }

    public String SQLBorrar() {
        String query = "DELETE FROM psync.producto WHERE id=" + this.id;
        return query;
    }

    public String SQLBuscarNombre() {
        String query = "SELECT * FROM psync.producto WHERE nombre=" + util.Varios.entrecomillar(this.nombre);
        return query;
    }

    public String SQLBuscarId() {
        String query = "SELECT * FROM psync.producto WHERE id=" + this.id;
        return query;
    }

    public String updatePrice() {
        String query = "UPDATE admin_electropresta.EM_product_shop SET "
                + "price=" + this.precio + " "
                + "WHERE id_product=" + this.id;
        return query;
    }

    public String updateStock() {
        String query = "UPDATE admin_electropresta.EM_stock_available SET "
                + "quantity=" + this.stock + " "
                + "WHERE id_product=" + this.id;
        return query;
    }

    public String updateActivo() {
        String query = "UPDATE admin_electropresta.EM_product_shop SET "
                + "active=" + this.isActivo + " "
                + "where id_product=" + this.id;
        return query;
    }
}
