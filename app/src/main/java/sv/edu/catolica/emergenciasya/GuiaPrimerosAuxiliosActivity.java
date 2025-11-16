package sv.edu.catolica.emergenciasya;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import static sv.edu.catolica.emergenciasya.PrimerosAuxiliosAdapter.EXTRA_TEMA;

public class GuiaPrimerosAuxiliosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private GuiaPrimerosAuxiliosAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_primeros_auxilios);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String tema = getIntent().getStringExtra(EXTRA_TEMA);
        if (tema == null) {
            tema = "Guía de primeros auxilios";
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(tema);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Views
        recyclerView = findViewById(R.id.recycler_view_guia);
        searchEditText = findViewById(R.id.search_edit_text);
        progressBar = findViewById(R.id.progress_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar pasos según el tema
        progressBar.setVisibility(ProgressBar.VISIBLE);
        List<GuiaItem> pasos = obtenerPasosParaTema(tema);
        progressBar.setVisibility(ProgressBar.GONE);

        adapter = new GuiaPrimerosAuxiliosAdapter(pasos);
        recyclerView.setAdapter(adapter);

        // Filtro de búsqueda
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private List<GuiaItem> obtenerPasosParaTema(String tema) {
        List<GuiaItem> pasos = new ArrayList<>();

        if (tema.contains("RCP")) {
            pasos.add(new GuiaItem(
                    "Verificar el entorno",
                    "Asegúrate de que el lugar sea seguro para ti y la víctima."
            ));
            pasos.add(new GuiaItem(
                    "Comprobar respuesta",
                    "Sacude suavemente a la persona y pregúntale en voz alta si se encuentra bien."
            ));
            pasos.add(new GuiaItem(
                    "Llamar a emergencias",
                    "Si no responde, llama al número de emergencias de tu país o pide a alguien que lo haga."
            ));
            pasos.add(new GuiaItem(
                    "Iniciar compresiones",
                    "Coloca tus manos en el centro del pecho y realiza compresiones fuertes y rápidas (100-120 por minuto)."
            ));
            pasos.add(new GuiaItem(
                    "Continuar hasta ayuda",
                    "No detengas las compresiones hasta que llegue ayuda profesional o la persona muestre signos de vida."
            ));
        } else if (tema.contains("Atragantamiento")) {
            pasos.add(new GuiaItem(
                    "Confirmar atragantamiento",
                    "Pregunta si la persona puede hablar o toser. Si no puede hacerlo, puede tratarse de una obstrucción completa."
            ));
            pasos.add(new GuiaItem(
                    "Golpes en la espalda",
                    "Da hasta 5 golpes fuertes entre los omóplatos con el talón de la mano."
            ));
            pasos.add(new GuiaItem(
                    "Maniobra de Heimlich",
                    "Colócate detrás de la persona, rodea su cintura y realiza compresiones abdominales hacia adentro y arriba."
            ));
        } else if (tema.contains("Hemorragias")) {
            pasos.add(new GuiaItem(
                    "Protegerse",
                    "Si es posible, utiliza guantes o una barrera para evitar contacto directo con la sangre."
            ));
            pasos.add(new GuiaItem(
                    "Presión directa",
                    "Aplica presión firme sobre la zona que sangra con una gasa o paño limpio."
            ));
            pasos.add(new GuiaItem(
                    "Elevar la zona",
                    "Si es una extremidad, intenta mantenerla elevada por encima del nivel del corazón."
            ));
        } else if (tema.contains("Quemaduras")) {
            pasos.add(new GuiaItem(
                    "Enfriar la zona",
                    "Coloca la quemadura bajo agua fresca (no helada) durante al menos 10-20 minutos."
            ));
            pasos.add(new GuiaItem(
                    "No aplicar sustancias",
                    "No uses hielo, pasta de dientes, mantequilla u otros remedios caseros."
            ));
            pasos.add(new GuiaItem(
                    "Cubrir la quemadura",
                    "Cubre con un apósito estéril o paño limpio sin hacer presión."
            ));
        } else if (tema.contains("Convulsiones")) {
            pasos.add(new GuiaItem(
                    "Proteger la cabeza",
                    "Coloca algo blando debajo de la cabeza de la persona para evitar golpes."
            ));
            pasos.add(new GuiaItem(
                    "Retirar objetos",
                    "Aparta objetos cercanos con los que pueda golpearse durante la convulsión."
            ));
            pasos.add(new GuiaItem(
                    "No sujetar ni introducir objetos",
                    "No intentes sujetar a la persona ni pongas nada en su boca."
            ));
            pasos.add(new GuiaItem(
                    "Controlar el tiempo",
                    "Si la convulsión dura más de 5 minutos, llama a emergencias de inmediato."
            ));
        } else {
            pasos.add(new GuiaItem(
                    "Información no disponible",
                    "Pronto se agregarán pasos detallados para este tema de primeros auxilios."
            ));
        }

        return pasos;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
