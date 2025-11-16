package sv.edu.catolica.emergenciasya;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ContactosConfianzaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactosConfianzaAdapter adapter;
    private List<ContactoConfianza> contactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_confianza);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactos = new ArrayList<>();
        // Sample data
        contactos.add(new ContactoConfianza("Mamá", "1234-5678"));
        contactos.add(new ContactoConfianza("Papá", "8765-4321"));

        adapter = new ContactosConfianzaAdapter(contactos);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showAddContactDialog());
    }

    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir Contacto de Confianza");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_contacto, null);
        builder.setView(view);

        final EditText nombreEditText = view.findViewById(R.id.nombreEditText);
        final EditText numeroEditText = view.findViewById(R.id.numeroEditText);

        builder.setPositiveButton("Añadir", (dialog, which) -> {
            String nombre = nombreEditText.getText().toString().trim();
            String numero = numeroEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(numero)) {
                contactos.add(new ContactoConfianza(nombre, numero));
                adapter.notifyItemInserted(contactos.size() - 1);
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
