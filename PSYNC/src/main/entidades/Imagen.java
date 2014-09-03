package main.entidades;



/**
 *
 * @author Agárimo
 */
public class Imagen {

    private int id;
    private int id_producto;
    private String nombre;
    
    public Imagen(int id){
        this.id=id;
    }
    
    public Imagen(int id_producto,String nombre){
        this.id_producto=id_producto;
        this.nombre=nombre;
    }
    
    public Imagen(int id, int id_producto, String nombre){
        this.id=id;
        this.id_producto=id_producto;
        this.nombre=nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    //TODO crear una variable dominio y reemplazar para hacerlo más portable.
    public String getEnlace(){
        return "http://electromegusta.es/img/p/image/"+this.nombre;
    }
    
    public String SQLCrear() {
        String query = "INSERT INTO psync.imagen (id_producto,nombre) values("
                + this.id_producto + ","
                + util.Varios.entrecomillar(this.nombre)
                + ")";
        return query;
    }
    
    public String SQLBuscar(){
        String query="SELECT * FROM psync.imagen WHERE nombre="+util.Varios.entrecomillar(this.nombre);
        
        return query;
    }
}
