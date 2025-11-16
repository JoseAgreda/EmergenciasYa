package sv.edu.catolica.emergenciasya;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "incidentes")
public class Incident {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String titulo;

    private String ubicacion;

    private String descripcion;

    // Guardaremos la fecha/hora como milisegundos (System.currentTimeMillis())
    private long fechaRegistro;

    public Incident(@NonNull String titulo, String ubicacion, String descripcion, long fechaRegistro) {
        this.titulo = titulo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NonNull String titulo) {
        this.titulo = titulo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(long fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
