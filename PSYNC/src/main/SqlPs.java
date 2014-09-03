package main;

import util.Sql;
import main.entidades.Descripcion;
import main.entidades.Imagen;
import main.entidades.Producto;
import main.ftp.FTPProveedor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agárimo
 */
public class SqlPs {

    private static Sql bd;
    private static ResultSet rs;

    /**
     * Tipo 0 = Búsqueda por id || Tipo 1 = Búsqueda por Nombre
     *
     * @param producto
     * @param tipo
     * @return
     */
    public static Producto cargaProductoFinal(Producto producto, int tipo) {
        Producto aux = null;
        try {
            bd = new Sql(Main.conEmg);
            switch (tipo) {
                case 0:
                    rs = bd.ejecutarQueryRs(producto.SQLBuscarId());
                    break;
                case 1:
                    rs = bd.ejecutarQueryRs(producto.SQLBuscarNombre());
                    break;
            }

            if (rs.next()) {
                aux = new Producto(rs.getInt("id_producto"), rs.getInt("id_categoria"), rs.getString("nombre_producto"),
                        rs.getBoolean("activo"),rs.getInt("stock"),rs.getDouble("precio"));
            }
            rs.close();
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    public static FTPProveedor cargaFTPProveedor(FTPProveedor server) {
        FTPProveedor aux = null;
        try {
            bd = new Sql(Main.conEmg);
            rs = bd.ejecutarQueryRs(server.SQLBuscar());

            if (rs.next()) {
                aux = new FTPProveedor(rs.getInt("id_ftp_proveedor"), rs.getString("nombre_ftp"), rs.getString("login_ftp"), rs.getString("pass_ftp"),
                        rs.getString("host_ftp"), rs.getString("archivo_ftp"));
            }

            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    public static Descripcion cargarDescripcion(Descripcion descripcion) {
        Descripcion aux = null;
        try {
            bd = new Sql(Main.conEmg);
            rs = bd.ejecutarQueryRs(descripcion.SQLBuscar());

            if (rs.next()) {
                aux = new Descripcion(rs.getInt("id_descripcion"), rs.getString("descripcion"));
            }

            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    public static String cargaNombrePresta(int id) {
        String aux = null;
        try {
            bd = new Sql(Main.conPresta);
            rs = bd.ejecutarQueryRs("SELECT name FROM admin_electropresta.EM_product_lang WHERE id_product=" + id);

            if (rs.next()) {
                aux = rs.getString("name");
            }

            rs.close();
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aux;
    }

    public static void actualizaActivos() {
        String queryFalse = "UPDATE admin_electropresta.EM_product_shop SET active=0 where id_product IN(SELECT id_product FROM admin_electropresta.EM_stock_available where quantity<=0)";
//        String queryTrue = "UPDATE admin_electropresta.EM_product_shop SET active=1 where id_product IN(SELECT id_product FROM admin_electropresta.EM_stock_available where quantity>0)";

        try {
            bd = new Sql(Main.conPresta);
            bd.ejecutar(queryFalse);
//            bd.ejecutar(queryTrue);
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Listados">
    public static List listadoId(String query) {
        List list = new ArrayList();
        int aux;

        try {
            bd = new Sql(Main.conEmg);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = rs.getInt(1);
                list.add(aux);
            }

            rs.close();
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static List<Producto> listaProductoFinal(String query) {
        List<Producto> list = new ArrayList();
        Producto aux;

        try {
            bd = new Sql(Main.conEmg);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Producto(rs.getInt("id_producto"), rs.getInt("id_categoria"), rs.getString("nombre_producto"),
                        rs.getBoolean("activo"),rs.getInt("stock"),rs.getDouble("precio"));
                list.add(aux);
            }

            rs.close();
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public static List<Imagen> listarImagenes(String query) {
        List<Imagen> list = new ArrayList();
        Imagen aux;

        try {
            bd = new Sql(Main.conEmg);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Imagen(rs.getInt("id_imagen"), rs.getInt("id_producto"), rs.getString("nombre_imagen"));
                list.add(aux);
            }

            rs.close();
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<Descripcion> listarDescripciones(String query) {
        List<Descripcion> list = new ArrayList();
        Descripcion aux;

        try {
            bd = new Sql(Main.conEmg);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Descripcion(rs.getInt("id_descripcion"), rs.getString("descripcion"));
                list.add(aux);
            }

            rs.close();
            bd.close();

        } catch (SQLException ex) {
            Logger.getLogger(SqlPs.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    //</editor-fold>
}
