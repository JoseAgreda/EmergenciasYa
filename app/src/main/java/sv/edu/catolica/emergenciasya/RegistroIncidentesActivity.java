package sv.edu.catolica.emergenciasya;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class RegistroIncidentesActivity extends AppCompatActivity {

    private EditText etTitulo, etUbicacion, etDescripcion;
    private TextView tvEmptyState;
    private RecyclerView recyclerIncidentes;
    private IncidenteAdapter adapter;
    private final List<Incident> incidentList = new ArrayList<>();

    private AppDatabase db;
    private IncidentDao incidentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_incidentes);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registro de incidentes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Inicializar DB y DAO
        db = AppDatabase.getInstance(this);
        incidentDao = db.incidentDao();

        // Referencias a vistas
        TextInputLayout inputTitulo = findViewById(R.id.input_titulo);
        TextInputLayout inputUbicacion = findViewById(R.id.input_ubicacion);
        TextInputLayout inputDescripcion = findViewById(R.id.input_descripcion);

        etTitulo = inputTitulo.getEditText();
        etUbicacion = inputUbicacion.getEditText();
        etDescripcion = inputDescripcion.getEditText();

        if (etTitulo == null || etUbicacion == null || etDescripcion == null) {
            Toast.makeText(this, "Error en la configuración de los campos de texto", Toast.LENGTH_SHORT).show();
            return;
        }

        tvEmptyState = findViewById(R.id.tv_empty_state);
        recyclerIncidentes = findViewById(R.id.recyclerIncidentes);
        recyclerIncidentes.setLayoutManager(new LinearLayoutManager(this));
        recyclerIncidentes.setHasFixedSize(true);

        adapter = new IncidenteAdapter(incidentList);
        recyclerIncidentes.setAdapter(adapter);

        MaterialButton btnGuardar = findViewById(R.id.btnGuardarIncidente);
        btnGuardar.setOnClickListener(v -> guardarIncidente());

        // Cargar incidentes desde la BD
        cargarIncidentesDesdeBD();
    }

    private void cargarIncidentesDesdeBD() {
        new Thread(() -> {
            List<Incident> desdeBD = incidentDao.getAll();
            incidentList.clear();
            if (desdeBD != null) {
                incidentList.addAll(desdeBD);
            }

            runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                actualizarEmptyState();
            });
        }).start();
    }

    private void guardarIncidente() {
        String titulo = etTitulo.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)) {
            etTitulo.setError("El título es obligatorio");
            etTitulo.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(descripcion)) {
            etDescripcion.setError("La descripción es obligatoria");
            etDescripcion.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ubicacion)) {
            ubicacion = "No especificada";
        }

        long ahora = System.currentTimeMillis();
        Incident nuevo = new Incident(titulo, ubicacion, descripcion, ahora);

        new Thread(() -> {
            long id = incidentDao.insert(nuevo);
            nuevo.setId(id);

            incidentList.add(0, nuevo); // lo agregamos al inicio
            runOnUiThread(() -> {
                adapter.notifyItemInserted(0);
                recyclerIncidentes.scrollToPosition(0);
                actualizarEmptyState();
                limpiarCampos();
                Toast.makeText(this, "Incidente guardado", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    private void actualizarEmptyState() {
        if (incidentList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recyclerIncidentes.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recyclerIncidentes.setVisibility(View.VISIBLE);
        }
    }

    private void limpiarCampos() {
        etTitulo.setText("");
        etUbicacion.setText("");
        etDescripcion.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
