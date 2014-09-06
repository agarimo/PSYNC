package ps.csv;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import main.Inicio;
import main.Log;
import main.entidades.Descripcion;
import main.entidades.Producto;
import util.Conexion;
import util.Varios;

/**
 *
 * @author Agárimo
 */
public class CsvProductos extends Csv {

    public CsvProductos(File archivo, Conexion con) {
        super(archivo, con);
        super.cargar();
        logProducto=new Log("PRODUCTOS");
    }

    @Override
    public void run() {
        super.conectar();
        procesar();
        super.desconectar();
    }

    private void procesar() {
        System.out.println("Iniciando actualizacion de PRODUCTOS");
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

    private void split(String str) throws SQLException {
        String[] split = str.split(";");
        int aux;
        Producto pf = new Producto();
        Descripcion ds = new Descripcion();
        
        pf.setId(Integer.parseInt(split[0]));
        pf.setNombre(split[1].trim());
        pf.setIdCategoria(Integer.parseInt(split[3]));
        pf.setStock(Integer.parseInt(split[4]));
        pf.setPrecio(Double.parseDouble(split[5].replace(",", ".").trim()));
        aux=Integer.parseInt(split[6]);
        if(aux==0){
            pf.setActivo(false);
        }else{
            pf.setActivo(true);
        }
        
        ds.setId(Integer.parseInt(split[0]));
        ds.setDescripcion(split[2].trim());
        
        creaProducto(pf);
        creaDescripcion(ds);
    }

    private void creaProducto(Producto pf) throws SQLException {
        int id = bd.buscar(pf.SQLBuscarId());

        if (id < 1) {
            logProducto.escribeMsg("Nuevo Producto: " + pf.getNombre());
            System.out.println(pf.SQLCrear());
            bd.ejecutar(pf.SQLCrear());
            bd.ejecutar(pf.SQLCreaPendiente());
        } 
    }
    
    private void creaDescripcion(Descripcion ds) throws SQLException{
        bd.ejecutar(ds.SQLCrear());
    }
}
