package sv.edu.catolica.emergenciasya;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PrimerosAuxiliosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PrimerosAuxiliosAdapter adapter;
    private List<PrimerosAuxilios> temas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeros_auxilios);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Primeros Auxilios");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Lista de temas de primeros auxilios
        temas = new ArrayList<>();
        temas.add(new PrimerosAuxilios(
                "RCP básica (Reanimación cardiopulmonar)",
                "1. Llama al 911.\n" +
                        "2. Coloca a la persona boca arriba sobre una superficie firme.\n" +
                        "3. Coloca el talón de una mano en el centro del pecho y la otra encima.\n" +
                        "4. Realiza compresiones firmes y rápidas (100–120 por minuto).\n" +
                        "5. Continúa hasta que llegue ayuda o la persona reaccione."
        ));
        temas.add(new PrimerosAuxilios(
                "Atragantamiento",
                "1. Pregunta si la persona puede toser o hablar.\n" +
                        "2. Si no puede, realiza maniobra de Heimlich (abrazar por detrás y presionar hacia adentro y arriba).\n" +
                        "3. Si pierde el conocimiento, inicia RCP y llama al 911."
        ));
        temas.add(new PrimerosAuxilios(
                "Control de hemorragias",
                "1. Usa guantes si es posible.\n" +
                        "2. Aplica presión directa con una gasa o paño limpio.\n" +
                        "3. No retires la gasa si se empapa, coloca otra encima.\n" +
                        "4. Eleva la zona afectada si es posible.\n" +
                        "5. Busca atención médica urgente si el sangrado no se detiene."
        ));
        temas.add(new PrimerosAuxilios(
                "Quemaduras",
                "1. Enfría la zona con agua a temperatura ambiente durante 10–20 minutos.\n" +
                        "2. No revientes ampollas ni apliques pasta, aceite o crema casera.\n" +
                        "3. Cubre con gasa limpia y seca.\n" +
                        "4. Acude a un centro médico si la quemadura es extensa o profunda."
        ));
        temas.add(new PrimerosAuxilios(
                "Fracturas o golpes fuertes",
                "1. Inmoviliza la zona afectada (no intentes alinear el hueso).\n" +
                        "2. Usa una tabla, periódico o lo que tengas a mano como férula.\n" +
                        "3. Aplica hielo envuelto en un paño para disminuir la inflamación.\n" +
                        "4. Lleva a la persona a emergencias lo antes posible."
        ));

        // Adapter con contexto + lista
        adapter = new PrimerosAuxiliosAdapter(this, temas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
