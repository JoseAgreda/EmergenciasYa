package sv.edu.catolica.emergenciasya;

public class ContactoConfianza {
    private String nombre;
    private String numero;

    public ContactoConfianza(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }
}
