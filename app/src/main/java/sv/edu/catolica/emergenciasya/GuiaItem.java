package sv.edu.catolica.emergenciasya;

public class GuiaItem {
    private final String titulo;
    private final String descripcion;

    public GuiaItem(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
