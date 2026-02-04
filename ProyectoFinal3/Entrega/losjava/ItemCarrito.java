
public class ItemCarrito {

    private int idProducto;
    private String nombre;
    private double precio;
    private int cantidad;
    private String imagen;

    // constructor with all its attributes
    public ItemCarrito(int idProducto, String nombre, double precio, int cantidad, String imagen) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imagen = imagen;
    }

    public ItemCarrito() {

    }

    // getters
    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    // setters
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double calcularMonto() {
        Double producto = this.precio * this.cantidad;
        return producto;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
