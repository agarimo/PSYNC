package ps.csv;

import main.entidades.Producto;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import main.Inicio;
import util.Conexion;
import main.SqlPs;
import util.Varios;

/**
 *
 * @author Agarimo
 */
public class CsvUpdate extends Csv {
    
    Producto pf;

    public CsvUpdate(File archivo, Conexion con) {
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
        System.out.println("Iniciando actualizacion de tarifa");
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
        this.listPublicado = SqlPs.listaProducto("SELECT * FROM psync.producto");
    }

    private void split(String str) throws SQLException {
        String[] split = str.split(";");

        pf = new Producto();
        pf.setId(Integer.parseInt(split[0]));
        pf.setStock(Integer.parseInt(split[1]));
        pf.setPrecio(Double.parseDouble(split[2]));

        updateProducto(pf);
    }

    private void updateProducto(Producto pf) throws SQLException {
        Producto aux = buscarEnList(pf);
        if (aux != null) {
            if(!aux.equals(pf)){
                bd.ejecutar(pf.SQLUpdate());
                bd.ejecutar(pf.updatePrice());
                bd.ejecutar(pf.updateStock());
            }
        }
    }
}
