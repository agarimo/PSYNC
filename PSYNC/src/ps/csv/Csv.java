package ps.csv;

import main.entidades.Producto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import main.Inicio;
import main.Log;
import main.Main;
import util.Conexion;
import util.Sql;

/**
 *
 * @author Agarimo
 */
public class Csv implements Runnable {

    File archivo;
    Conexion con;
    Sql bd;
    Sql bdPresta;
    boolean activo;
    List<String> list;
    List listPublicado;
    Log logProducto;
    Log logCategoria;

    public Csv() {
    }

    public Csv(File archivo, Conexion con) {
        this.archivo = archivo;
        this.con = con;
        this.activo = false;
        list = new ArrayList();
    }

    @Override
    public void run() {
        conectar();
        desconectar();
    }

    protected void cargar() {
        FileReader fr = null;
        try {
            fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                list.add(linea);
            }
        } catch (IOException ex) {
            Inicio.log.escribeError("IOException", ex.getMessage());
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Inicio.log.escribeError("IOException", ex.getMessage());
            }
        }
    }

    protected void cargaListPublicado() {
        this.listPublicado = null;
    }

    protected Producto buscarEnList(Producto pf) {
        Producto aux;
        Iterator it = listPublicado.iterator();

        while (it.hasNext()) {
            aux = (Producto) it.next();
            if (pf.getId() == aux.getId()) {
                return aux;
            }
        }
        return null;
    }

    public void listar() {
        Iterator it = list.iterator();

        while (it.hasNext()) {
            System.out.println((String) it.next());
        }
    }

    protected void conectar() {
        try {
            bd = new Sql(con);
            if (!Inicio.offline) {
                bdPresta = new Sql(Main.conPresta);
            }
            activo = true;
        } catch (SQLException ex) {
            Inicio.log.escribeError("SQLException", ex.getMessage());
        }
    }

    protected void desconectar() {
        try {
            bd.close();
            if (!Inicio.offline) {
                bdPresta.close();
            }
            activo = false;
        } catch (SQLException ex) {
            Inicio.log.escribeError("SQLException", ex.getMessage());
        }
    }

//    protected void actualizaTarifaPresta(Producto pf) throws SQLException {
//        if (!Inicio.offline) {
//            PrestaShop ps = new PrestaShop(pf, ip);
//            bdPresta.ejecutar(ps.updatePrice());
//        }
//    }
//
//    protected void actualizaStockPresta(Producto pf) throws SQLException {
//        if (!Inicio.offline) {
//            PrestaShop ps = new PrestaShop(pf, ip);
//            bdPresta.ejecutar(ps.updateStock());
//        }
//    }
}
