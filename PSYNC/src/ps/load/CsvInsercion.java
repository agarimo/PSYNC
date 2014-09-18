package ps.load;

import main.entidades.Descripcion;
import main.entidades.Imagen;
import main.entidades.Producto;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import main.Inicio;
import main.Main;
import util.Sql;
import main.SqlPs;
import util.Split;
import util.Varios;

/**
 *
 * @author Agárimo
 */
public class CsvInsercion {

    private String[] cabecera;
    private List<Producto> list;
    private Producto producto;
    private File directorio = new File("insercion");
    private File csv;

    public CsvInsercion() {
        cabecera = new String[]{"id_producto", "proveedor", "categoria", "nombre", "ref_fabricante", "ref_proveedor", "stock", "precio_venta", "imagenes", "descripcion"};
        creaArchivos();
        cargaProductos();
    }

    public void run() {
        int contador = 1;
        System.out.println("Generando CSV INSERCION");
        Iterator it = list.iterator();

        while (it.hasNext()) {
            producto = (Producto) it.next();
            procesar(producto);
            System.out.print("\rGenerando " + Varios.calculaProgreso(contador, list.size()) + "%");
            contador++;
        }
//        Split sp = new Split(csv, 10000);
//        sp.split();
        System.out.print("\rTarea Finalizada                            ");
        System.out.println();
    }

    private void cargaProductos() {
        System.out.println("Iniciando datos");
        list = SqlPs.listaProducto("SELECT * FROM psync.producto WHERE id IN "
                + "(SELECT id_producto FROM psync.pendiente_insercion)");
        System.out.println("Datos cargados");
    }

    private void creaArchivos() {
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        csv = new File(directorio, "PendienteInsercion.csv");

        if (!csv.exists()) {
            try {
                csv.createNewFile();
                escribeLinea(buildLinea(cabecera));
            } catch (IOException ex) {
                Inicio.log.escribeError("IOException", ex.getMessage());
            }
        }
    }

    private void procesar(Producto pd) {
        int a;
        String linea;
        String[] aux = new String[10];
        aux[0] = Integer.toString(pd.getId());
        aux[1] = "2";
        
        a=pd.getIdCategoria();
        if(a==1 || a==2){
            a=a+100;
        }
        aux[2] = Integer.toString(a);
        
        aux[3] = pd.getNombre();
        aux[4] = Integer.toString(pd.getId());
        aux[5] = Integer.toString(pd.getId());
        aux[6] = Integer.toString(pd.getStock());
        aux[7] = new DecimalFormat("0.00000").format(pd.getPrecio());
        aux[8] = getImagen(pd);
        aux[9] = getDescripcion(pd).trim().replace("\r", "").replace(";", "");

        linea = buildLinea(aux);
        escribeLinea(linea);
//        borraPendiente(pd.getId());
    }

    private void borraPendiente(int idProducto) {
        try {
            Sql bd = new Sql(Main.conPSync);
            bd.ejecutar("DELETE FROM psync.pendiente_insercion WHERE id_producto=" + idProducto);
            bd.close();
        } catch (SQLException ex) {
            Inicio.log.escribeError("SQLException", ex.getMessage());
        }
    }

    private String getDescripcion(Producto pf) {
        Descripcion de = SqlPs.cargarDescripcion(new Descripcion(pf.getId()));
        if (de == null) {
            return "";
        } else {
            return de.getDescripcion();
        }
    }

    private String getImagen(Producto pf) {
        String aux = "";
        Imagen imagen;
        List<Imagen> lista;
        Iterator it;
        lista = SqlPs.listarImagenes("SELECT * FROM psync.imagen WHERE id_producto=" + pf.getId());
        it = lista.iterator();

        while (it.hasNext()) {
            imagen = (Imagen) it.next();
            aux = aux + imagen.getEnlace() + ",";
        }
        if (aux.length() > 1) {
            return aux.substring(0, aux.length() - 1);
        } else {
            return "";
        }
    }

    private String buildLinea(String[] aux) {
        String str = "";

        for (String aux1 : aux) {
            str = str + aux1 + ";";
        }

        return str.substring(0, str.length() - 1);
    }

    public void escribeLinea(String linea) {
        try {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(csv, true))) {
                out.write(linea);
                out.newLine();
            }
        } catch (IOException ex) {
            Inicio.log.escribeError("IOException", ex.getMessage());
        }
    }
}
