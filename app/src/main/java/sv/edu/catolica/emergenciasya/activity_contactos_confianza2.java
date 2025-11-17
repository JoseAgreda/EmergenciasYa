package sv.edu.catolica.emergenciasya;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class activity_contactos_confianza2 extends AppCompatActivity {

    private ArrayList<Contacto> listaContactos;
    private ContactoAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout emptyStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_confianza2);

        // 1. Cargar los contactos guardados localmente desde SharedPreferences
        listaContactos = ContactStorage.loadContacts(this);

        // 2. Configurar la interfaz de usuario
        setupUI();

        // 3. Configurar el gesto de deslizar para eliminar
        setupSwipeToDelete();
    }

    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view_contacts);
        emptyStateLayout = findViewById(R.id.empty_state_layout);
        FloatingActionButton fab = findViewById(R.id.fab_add_contact);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactoAdapter(this, listaContactos);
        recyclerView.setAdapter(adapter);

        // --- CAMBIO PRINCIPAL ---
        // El botón FAB ahora abre el diálogo de creación de contacto
        fab.setOnClickListener(v -> showAddContactDialog());

        actualizarVistaVacia();
    }

    /**
     * Muestra un AlertDialog con un formulario para añadir un nuevo contacto.
     */
    private void showAddContactDialog() {
        // Inflar el layout personalizado para el diálogo
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_contacto, null);

        final EditText etContactName = dialogView.findViewById(R.id.et_contact_name);
        final EditText etContactNumber = dialogView.findViewById(R.id.et_contact_number);

        // Construir el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Añadir Contacto de Confianza")
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etContactName.getText().toString().trim();
                    String numero = etContactNumber.getText().toString().trim();

                    // Validar que los campos no estén vacíos
                    if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(numero)) {
                        Toast.makeText(this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
                    } else {
                        // Añadir el contacto y guardar la lista actualizada
                        addContact(nombre, numero);
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    // Simplemente cierra el diálogo
                    dialog.cancel();
                });

        builder.create().show();
    }

    /**
     * Añade un nuevo contacto a la lista, lo guarda y actualiza la UI.
     * @param nombre El nombre del contacto.
     * @param numero El número del contacto.
     */
    private void addContact(String nombre, String numero) {
        // 1. Añade el nuevo contacto a la lista en memoria
        listaContactos.add(new Contacto(nombre, numero));

        // 2. Guarda la lista COMPLETA actualizada en SharedPreferences
        ContactStorage.saveContacts(this, listaContactos);

        // 3. Notifica al adaptador y actualiza la UI
        adapter.notifyItemInserted(listaContactos.size() - 1);
        actualizarVistaVacia();

        Toast.makeText(this, "Contacto '" + nombre + "' añadido.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Configura el RecyclerView para permitir eliminar contactos deslizando la tarjeta.
     */
    private void setupSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Contacto contactoEliminado = listaContactos.get(position);

                // 1. Elimina el contacto de la lista en memoria
                listaContactos.remove(position);

                // 2. Guarda la lista actualizada (ya sin el contacto)
                ContactStorage.saveContacts(getApplicationContext(), listaContactos);

                // 3. Notifica al adaptador
                adapter.notifyItemRemoved(position);
                actualizarVistaVacia();

                // Muestra un mensaje con opción para deshacer
                Snackbar.make(recyclerView, "Contacto eliminado", Snackbar.LENGTH_LONG)
                        .setAction("DESHACER", v -> {
                            listaContactos.add(position, contactoEliminado);
                            ContactStorage.saveContacts(getApplicationContext(), listaContactos);
                            adapter.notifyItemInserted(position);
                            actualizarVistaVacia();
                        }).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    // --- MÉTODOS AUXILIARES ---

    private void actualizarVistaVacia() {
        if (listaContactos.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
}
