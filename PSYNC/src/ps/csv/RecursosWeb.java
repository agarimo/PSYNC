package ps.csv;

import main.entidades.Descripcion;
import main.entidades.Imagen;
import main.entidades.Producto;
import main.ftp.ClienteFTP;
import main.ftp.FTPProveedor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Inicio;
import main.Main;
import main.SqlPs;
import util.Sql;

/**
 *
 * @author Agarimo
 */
public class RecursosWeb {

    Sql bd;
    File dir = new File("recursosWeb");
    ClienteFTP cliente;
    FTPProveedor publicacion;
    FTPProveedor ftp;
    List<Producto> list;
    List listaFtp;
    boolean isValido = false;
    int tipo;

    public RecursosWeb(int tipo, FTPProveedor ftp, FTPProveedor publi) {
        System.out.println("Iniciando asociacion de textos e imagenes " + ftp.getNombre());
        this.ftp = ftp;
        this.publicacion = publi;
        this.tipo = tipo;
        dir.mkdirs();
        System.out.println("Inicializando datos");
        cliente = new ClienteFTP(ftp);
        iniciaListFtp();
        iniciaListProductos();
        if (isValido) {
            System.out.println("Carga completa");
        } else {
            System.out.println("Error en la carga");
        }
        cliente.desconectar();
    }

    public boolean isIsValido() {
        return isValido;
    }

    public void setIsValido(boolean isValido) {
        this.isValido = isValido;
    }

    private void iniciaListFtp() {
        try {
            cliente = new ClienteFTP(ftp);
            listaFtp = cliente.getLista();
            isValido = true;
        } catch (IOException ex) {
            Inicio.log.escribeError("IOException", ex.getMessage());
            isValido = false;
        }
    }

    private void iniciaListProductos() {

        switch (tipo) {
            case 0:
                list = SqlPs.listaProductoFinal("SELECT * FROM electromegusta.producto_final");
                break;
            case 1:
                list = SqlPs.listaProductoFinal("SELECT * FROM electromegusta.producto_final where id_producto not in(select id_descripcion from electromegusta.descripcion)");
                break;

            case 2:
                list = SqlPs.listaProductoFinal("SELECT * FROM electromegusta.producto_final where id_producto not in(select id_producto from electromegusta.imagen)");
                break;
        }

        if (list.size() > 0) {
            isValido = true;
        } else {
            isValido = false;
        }
    }

    public void run() {
        switch (tipo) {
            case 0:
                runTextos();
                runImagen();
                break;
            case 1:
                runTextos();
                break;
            case 2:
                runImagen();
                break;
        }
    }

    public void runRefresImage() {
        try {
            Producto aux;
            int contador = 1;
            Iterator it = list.iterator();

            bd = new Sql(Main.conEmg);
            while (it.hasNext()) {
                aux = (Producto) it.next();
                recargaImagen(aux.getId());
                System.out.println("Cargada producto " + contador + " de " + list.size());
                contador++;
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(RecursosWeb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void runTextos() {
        try {
            asociaTextos();
            Files.deleteIfExists(dir.toPath());
        } catch (SQLException | IOException ex) {
            Inicio.log.escribeError("SQLException|IOException", ex.getMessage());
        }
    }

    private void runImagen() {
        try {
            asociaImagen();
            Files.deleteIfExists(dir.toPath());
        } catch (SQLException | IOException ex) {
            Inicio.log.escribeError("SQLException|IOException", ex.getMessage());
        }
    }

    private void asociaTextos() throws SQLException, IOException {
        System.out.println("Asociando textos");
        int contador = 1;
        Producto aux;
        File file;
        Iterator it = list.iterator();

        bd = new Sql(Main.conEmg);
        while (it.hasNext()) {
            aux = (Producto) it.next();
            file = buscaArchivoTexto(Integer.toString(aux.getId()));

            if (file != null) {
                descargaTexto(file);
                asociaTexto(aux, file);
                contador++;
            } else {
                contador++;
            }
            System.out.print("\rProcesando " + util.Varios.calculaProgreso(contador, list.size()) + "%");
        }
        System.out.print("\rAsociacion completada                            ");
        System.out.println();
        bd.close();
    }

    private File buscaArchivoTexto(String referencia) {
        File file;
        if (listaFtp.contains(referencia + ".txt")) {
            file = new File(referencia + ".txt");
        } else if (listaFtp.contains(referencia + ".html")) {
            file = new File(referencia + ".html");
        } else {
            file = null;
        }

        return file;
    }

    private void descargaTexto(File file) {
        cliente = new ClienteFTP(ftp);
        cliente.getFichero(file.getName(), file.getAbsolutePath());
        cliente.desconectar();
    }

    private void asociaTexto(Producto producto, File file) throws SQLException, IOException {
        String aux;
        if (file.getName().contains(".txt")) {
            aux = extraeTextoArchivo(file);
            aux = convertirHtml(aux);
        } else {
            aux = extraeTextoArchivo(file);
        }
        Files.deleteIfExists(file.toPath());
        creaDescripcion(producto, aux);
    }

    private void creaDescripcion(Producto producto, String aux) throws SQLException {
        String query;
        Descripcion des = new Descripcion(producto.getId(), aux);

        if (bd.buscar(des.SQLBuscar()) > 0) {
            query = des.SQLEditar();
        } else {
            query = des.SQLCrear();
        }
        bd.ejecutar(query);
    }

    private String extraeTextoArchivo(File file) {
        String aux = util.Files.leeArchivo(file);
        aux = aux.trim();
        aux = aux.replace("'", "´");
        return aux;
    }

//    private String extraeTextoHtml(File file) {
//        String aux;
//        HTMLTextParser htp = new HTMLTextParser();
//        aux = htp.htmltoText(file.getAbsolutePath());
//        aux = aux.trim();
//        aux = aux.replace("'", "´");
//        return aux;
//    }
    private String convertirHtml(String s) {
        StringBuilder builder = new StringBuilder();
        boolean previousWasASpace = false;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                if (previousWasASpace) {
                    builder.append("&nbsp;");
                    previousWasASpace = false;
                    continue;
                }
                previousWasASpace = true;
            } else {
                previousWasASpace = false;
            }
            switch (c) {
                case '<':
                    builder.append("&lt;");
                    break;
                case '>':
                    builder.append("&gt;");
                    break;
                case '&':
                    builder.append("&amp;");
                    break;
                case '"':
                    builder.append("&quot;");
                    break;
                case '\n':
                    builder.append("<br>");
                    break;
                // We need Tab support here, because we print StackTraces as HTML
                case '\t':
                    builder.append("&nbsp; &nbsp; &nbsp;");
                    break;
                default:
                    if (c < 128) {
                        builder.append(c);
                    } else {
                        builder.append("&#").append((int) c).append(";");
                    }
            }
        }
        return builder.toString();
    }

    private void asociaImagen() throws SQLException, IOException {
        System.out.println("Asociando imagenes");
        int contador = 1;
        Producto aux;
        Iterator it = list.iterator();

        bd = new Sql(Main.conEmg);
        while (it.hasNext()) {
            aux = (Producto) it.next();

            buscaImagen(Integer.toString(aux.getId()),aux.getId());
            contador++;

            System.out.print("\rProcesando " + util.Varios.calculaProgreso(contador, list.size()) + "%");
        }
        System.out.print("\rAsociacion completada                            ");
        System.out.println();
        bd.close();
    }

    private void buscaImagen(String referencia, int idProducto) throws SQLException, IOException {
        File file;
        File aux;

        if (listaFtp.contains(referencia + ".jpg")) {
            file = new File(referencia + ".jpg");
        } else if (listaFtp.contains(referencia + ".bmp")) {
            file = new File(referencia + ".bmp");
        } else if (listaFtp.contains(referencia + ".jpeg")) {
            file = new File(referencia + ".jpeg");
        } else {
            file = null;
        }

        Imagen imagen;

        if (file != null) {
            imagen = new Imagen(idProducto, file.getName());
            if (bd.buscar(imagen.SQLBuscar()) < 0) {
                bd.ejecutar(imagen.SQLCrear());
                descargaImagen(file);
                subirImagen(new File(dir, file.getName()));
                aux = new File(dir, file.getName());
                Files.deleteIfExists(aux.toPath());
            }
            buscaImagen(iteraNombre(file.getName()), idProducto);
        }
    }

    private void recargaImagen(int idProducto) throws SQLException, IOException {
        File file;
        File aux;

        if (listaFtp.contains(idProducto + ".jpg")) {
            file = new File(idProducto + ".jpg");
        } else if (listaFtp.contains(idProducto + ".bmp")) {
            file = new File(idProducto + ".bmp");
        } else if (listaFtp.contains(idProducto + ".jpeg")) {
            file = new File(idProducto + ".jpeg");
        } else {
            file = null;
        }

        Imagen imagen;
        if (file != null) {
            imagen = new Imagen(idProducto, file.getName());
            if (bd.buscar(imagen.SQLBuscar()) < 1) {
                bd.ejecutar(imagen.SQLCrear());
            }
            descargaImagen(file);
            subirImagen(new File(dir, file.getName()));
            buscaImagen(iteraNombre(file.getName()), idProducto);
        }
    }

    private void descargaImagen(File file) {
        System.out.println("Descargando imagen " + file.getName());
        cliente = new ClienteFTP(ftp);
        cliente.getFichero(file.getName(), new File(dir, file.getName()).getAbsolutePath());
        cliente.desconectar();
    }

    private void subirImagen(File file) {
        System.out.println("Cargando imagen" + file.getName());
        cliente = new ClienteFTP(publicacion);
        cliente.setFichero(file, file.getName());
        cliente.desconectar();
    }

    private String iteraNombre(String nombre) {
        String aux;

        if (nombre.contains("-")) {
            String[] split = nombre.split("-");
            String[] split1 = split[1].split("\\.");
            aux = split[0];
            aux = aux + "-" + iteraNumero(split1[0]);
        } else {
            aux = nombre.substring(0, nombre.length() - 4) + "-1";
        }

        return aux;
    }

    private String iteraNumero(String num) {
        int a = Integer.parseInt(num);
        a++;
        return Integer.toString(a);
    }
}
