package sv.edu.catolica.emergenciasya;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NumerosOficialesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NumerosEmergenciaAdapter adapter;
    private List<NumeroEmergencia> numeros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeros_oficiales);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        numeros = new ArrayList<>();
        numeros.add(new NumeroEmergencia("Policía Nacional Civil", "911"));
        numeros.add(new NumeroEmergencia("Bomberos de El Salvador", "913"));
        numeros.add(new NumeroEmergencia("Cruz Roja Salvadoreña", "2222-5155"));
        numeros.add(new NumeroEmergencia("Cruz Verde Salvadoreña", "2284-5792"));
        numeros.add(new NumeroEmergencia("Comandos de Salvamento", "2284-5792"));
        numeros.add(new NumeroEmergencia("Protección Civil", "2281-0888"));
        numeros.add(new NumeroEmergencia("AES El Salvador", "2506-9000"));
        numeros.add(new NumeroEmergencia("Sistema de Emergencias Médicas", "132"));

        adapter = new NumerosEmergenciaAdapter(this, numeros);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
