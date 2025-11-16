package sv.edu.catolica.emergenciasya;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IncidentDao {

    @Query("SELECT * FROM incidentes ORDER BY fechaRegistro DESC")
    List<Incident> getAll();

    @Insert
    long insert(Incident incident);

    @Query("DELETE FROM incidentes")
    void deleteAll();
}
