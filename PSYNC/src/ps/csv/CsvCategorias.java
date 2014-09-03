package ps.csv;

import main.entidades.Categoria;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import main.Inicio;
import main.Log;
import util.Conexion;
import util.Varios;

/**
 *
 * @author Agárimo
 */
public class CsvCategorias extends Csv {

    public CsvCategorias(File archivo, Conexion con) {
        super(archivo, con);
        super.cargar();
        logCategoria = new Log("CATEGORIAS");
    }

    @Override
    public void run() {
        super.conectar();
        procesar();
        super.desconectar();
    }

    private void procesar() {
        System.out.println("Iniciando actualizacion CATEGORIAS");
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
        
        Categoria cat = new Categoria();
        cat.setId(Integer.parseInt(split[0].trim()));
        cat.setNombre(split[1]);
        cat.setId_padre(Integer.parseInt(split[2]));
        
        creaCategoria(cat);
    }

    private void creaCategoria(Categoria aux) throws SQLException {
        int id = bd.buscar(aux.SQLBuscarId());

        if (id < 1) {
            logCategoria.escribeMsg("Nueva Categoria: " + aux.getNombre());
            bd.ejecutar(aux.SQLCrear());
        }else{
            logCategoria.escribeMsg("Categoria Modificada: " + aux.getNombre());
            bd.ejecutar((aux.SQLEditar()));
        }
    }
}