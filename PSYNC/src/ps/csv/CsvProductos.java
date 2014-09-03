package ps.csv;

import main.entidades.Producto;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import main.Inicio;
import util.Conexion;
import main.SqlPs;
import util.Dates;
import util.Varios;

/**
 *
 * @author Agárimo
 */
public class CsvProductos extends Csv {

    public CsvProductos(File archivo, Conexion con) {
        super(archivo, con);
        super.cargar();
    }

    @Override
    public void run() {
        super.conectar();
        cargaListPublicado();
        procesar();
        super.desconectar();
    }

    private void procesar() {
        System.out.println("Iniciando actualizacion de stock");
        int contador = 1;
        String str;
        Iterator it = super.list.iterator();

        while (it.hasNext()) {
            str = (String) it.next();
            try {
                split(str);
            } catch (SQLException ex) {
                Inicio.log.escribeError("SQLException", ex.getMessage());
            }
            System.out.print("\rProcesando " + Varios.calculaProgreso(contador, super.list.size()) + "%");
            contador++;
        }
        System.out.print("\rActualizacion completada                            ");
        System.out.println();
    }

    @Override
    protected void cargaListPublicado() {
        this.listPublicado = SqlPs.listaProductoFinal("SELECT * FROM electromegusta.producto_final WHERE id_proveedor=3");
    }

    private void split(String str) throws SQLException {
        String[] split = str.split("\t");

        Producto pf = new Producto();
//        pf.setIdProveedor(3);
//        pf.setReferenciaProveedor(split[0].trim());
//        ip.setStock(Integer.parseInt(split[1].trim()));

        compruebaProducto(pf);
    }

    private void compruebaProducto(Producto pf) throws SQLException {
        Producto aux = buscarEnList(pf);
        if (aux != null) {
            actualizaStock(aux.getId());
//            actualizaStockPresta(aux);
        }
    }

    private void actualizaStock(int id) throws SQLException {
        String query = "UPDATE electromegusta.info_producto SET "
//                + "stock=" + this.ip.getStock() + ","
                + "update_stock=" + Varios.entrecomillar(Dates.imprimeFechaCompleta(Dates.curdate())) + " "
                + "WHERE id_info=" + id;
        bd.ejecutar(query);
    }
}
