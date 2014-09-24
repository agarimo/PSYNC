package main;

import ps.csv.Csv;
import ps.csv.CsvProductos;
import ps.csv.CsvUpdate;
import ps.csv.CsvCategorias;
import ps.load.CsvInsercion;
import ps.csv.RecursosWeb;
import main.entidades.Descripcion;
import main.entidades.Producto;
import main.ftp.ClienteFTP;
import main.ftp.FTPProveedor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Sql;

/**
 *
 * @author Agárimo
 */
public class Inicio {

    private static final File file = new File("temp.csv");
    public static Log log;
    private static FTPProveedor server;
    private static FTPProveedor publicacion;
    private boolean categorias = false;
    private boolean productos = false;
    private boolean update = false;
    private boolean insercion = false;
//    private boolean activaTarifa = false;
//    private boolean activaStock = false;
//    private boolean contenido = false;
//    private boolean insercion = false;
//    private boolean cargaDatos = false;
//    private boolean update = false;
    public static boolean offline = false;
//    private boolean activa = false;
//    private int tipoContenido = 0;

    public Inicio(String[] aux) {
        log = new Log();
        List list = Arrays.asList(aux);
        
        if (list.contains("-categorias")){
            categorias=true;
        }
        
        if (list.contains("-productos")){
            productos=true;
        }
        
        if(list.contains("-update")){
            update=true;
        }
        
        if(list.contains("-insercion")){
            insercion=true;
        }

//        if (list.contains("-activatarifa")) {
//            activa = true;
//            activaTarifa = true;
//        }
//
//        if (list.contains("-activa")) {
//            activa = true;
//        }
//
//        if (list.contains("-activastock")) {
//            activaStock = true;
//        }
//
//        if (list.contains("-contenido")) {
//            contenido = true;
//            tipoContenido = 0;
//        }
//
//        if (list.contains("-contenidotexto")) {
//            contenido = true;
//            tipoContenido = 1;
//        }
//        if (list.contains("-contenidoimagen")) {
//            contenido = true;
//            tipoContenido = 2;
//        }
//
//        if (list.contains("-insercion")) {
//            insercion = true;
//        }
//
//        if (list.contains("-carga")) {
//            cargaDatos = true;
//        }
//
//        if (list.contains(("-offline"))) {
//            offline = true;
//        }
//
//        if (list.contains(("-update"))) {
//            update = true;
//        }
    }

    public void run() throws IOException {
        
        if(categorias){
            log.escribeMsg("Ejecutando categorias");
            categorias();
        }
        
        if(productos){
            log.escribeMsg("Ejecutando productos");
            productos();
        }
        
        if(update){
            log.escribeMsg("Ejecutando update");
            update();
        }
        
        if(insercion){
            log.escribeMsg("Ejecutando insercion");
            insercion();
        }
        
//        if (activa) {
//            if (!activaTarifa) {
//                log.escribeMsg("Ejecutando activa");
//            } else {
//                log.escribeMsg("Ejecutando activaTarifa");
//            }
//            activaTarifa();
//        }
//
//        if (activaStock) {
//            log.escribeMsg("Ejecutando activaStock");
//            activaStock();
//        }
//
//        if (contenido) {
//            log.escribeMsg("Ejecutando contenido");
//            contenido(this.tipoContenido);
//        }
//
//        if (insercion) {
//            log.escribeMsg("Ejecutando insercion");
//            insercion();
//        }
//
//        if (cargaDatos) {
//            log.escribeMsg("Ejecutando cargaDatos");
//            cargaDatos();
//        }
//
//        if (update) {
//            log.escribeMsg("Ejecutando update");
//            update();
//        }

        log.escribeMsg("--------------------");
        limpiar();
    }
    
    private void categorias() throws IOException{
        server = SqlPs.cargaFTPProveedor(new FTPProveedor("CATEGORIAS"));
        getCsv(server);
    }
    
    private void productos() throws IOException{
        server = SqlPs.cargaFTPProveedor(new FTPProveedor("PRODUCTOS"));
        getCsv(server);
    }
    
    private void update() throws IOException{
        server = SqlPs.cargaFTPProveedor(new FTPProveedor("UPDATE"));
        getCsv(server);
    }
    
    private void insercion(){
        CsvInsercion ci = new CsvInsercion();
        ci.run();
    }

//    private void activaTarifa() throws IOException {
//        server = SqlPs.cargaFTPProveedor(new FTPProveedor("ACTIVA TARIFA"));
//        getCsv(server);
//    }
//
//    private void activaStock() throws IOException {
//        server = SqlPs.cargaFTPProveedor(new FTPProveedor("ACTIVA STOCK"));
//        getCsv(server);
//        SqlPs.actualizaActivos();
//    }
//
//    private void contenido(int tipo) {
//        publicacion = SqlPs.cargaFTPProveedor(new FTPProveedor("PUBLICACION"));
//        server = SqlPs.cargaFTPProveedor(new FTPProveedor("ACTIVA CONTENIDO"));
//        getContenido(server, publicacion, tipo);
//        server = SqlPs.cargaFTPProveedor(new FTPProveedor("BLUEVISION CONTENIDO"));
//        getContenido(server, publicacion, tipo);
//    }
//
//    private void cargaDatos() {
//        System.out.println("Cargando descripciones en la BBDD PrestaShop");
//        Descripcion des;
//        List<Descripcion> list = SqlPs.listarDescripciones("SELECT * FROM electromegusta.descripcion;");
//        Iterator it = list.iterator();
//        int contador = 1;
//        try {
//            Sql bd = new Sql(Main.conPresta);
//
//            while (it.hasNext()) {
//                des = (Descripcion) it.next();
//                bd.ejecutar(des.updateDescription());
//                System.out.print("\rProcesando " + util.Varios.calculaProgreso(contador, list.size()) + "%");
//                contador++;
//            }
//            System.out.print("\rActualizacion completada                            ");
//            System.out.println();
//            bd.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    private void getContenido(FTPProveedor server, FTPProveedor publicacion, int tipo) {
//        RecursosWeb rw = new RecursosWeb(tipo, server, publicacion);
//        rw.run();
//    }

    private void getCsv(FTPProveedor server) throws IOException {
        System.out.println("Cargando CSV " + server.getNombre());
        Csv aux;
        ClienteFTP ftp = new ClienteFTP(server);
        ftp.getFichero(server.getArchivo(), "temp.csv");
        ftp.desconectar();
        System.out.println("Carga completa");
        switch (server.getNombre()) {
            case "CATEGORIAS":
                aux = new CsvCategorias(file, Main.conPSync);
                aux.run();
                break;
            case "PRODUCTOS":
                aux = new CsvProductos(file,Main.conPSync);
                aux.run();
                break;
            case "UPDATE":
                aux = new CsvUpdate(file,Main.conPSync);
                aux.run();
                break;
        }
    }

    private void limpiar() throws IOException {
        File dir = new File("contenidoWeb");
        Files.deleteIfExists(file.toPath());
        Files.deleteIfExists(dir.toPath());
    }
}
