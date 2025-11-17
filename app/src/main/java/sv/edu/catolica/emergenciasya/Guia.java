package sv.edu.catolica.emergenciasya;

public class Guia {
    private final String titulo;
    private final String descCorta; // Descripción para la tarjeta
    private final String descLarga;  // Descripción detallada para la alerta (los pasos)
    private final int iconoResId;

    // Constructor actualizado para incluir la descripción larga
    public Guia(String titulo, String descCorta, String descLarga, int iconoResId) {
        this.titulo = titulo;
        this.descCorta = descCorta;
        this.descLarga = descLarga;
        this.iconoResId = iconoResId;
    }

    // Métodos para obtener los datos (Getters)
    public String getTitulo() { return titulo; }
    public String getDescCorta() { return descCorta; }
    public String getDescLarga() { return descLarga; }
    public int getIconoResId() { return iconoResId; }
}
