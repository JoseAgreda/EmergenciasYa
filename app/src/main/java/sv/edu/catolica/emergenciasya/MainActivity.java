package sv.edu.catolica.emergenciasya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton; // Importar ImageButton
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 1. Añadir la variable para el botón de configuración
    private ImageButton btnSettings;
    private MaterialCardView sosCard, cardNumerosOficiales, cardPrimerosAuxilios, cardContactos, cardLocalizacion, cardRegistro;

    private static final int CODIGO_PERMISOS_MULTIPLES = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // === Referencias de las vistas ===
        sosCard = findViewById(R.id.sos_card);
        cardNumerosOficiales = findViewById(R.id.card_numeros_oficiales);
        cardPrimerosAuxilios = findViewById(R.id.card_primeros_auxilios);
        cardContactos = findViewById(R.id.card_contactos);
        cardLocalizacion = findViewById(R.id.card_localizacion);
        cardRegistro = findViewById(R.id.card_registro);

        // 2. Obtener la referencia del nuevo botón de configuración
        btnSettings = findViewById(R.id.btn_settings);

        // === Acciones para cada vista (Listeners) ===
        setupClickListeners();

        // Llama al método para verificar y solicitar los permisos al iniciar.
        verificarYPedirPermisos();
    }

    private void setupClickListeners() {
        // --- Listener para el nuevo botón de configuración ---
        // 3. Añadir la acción para el botón de configuración
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConfiguracionActivity.class);
            startActivity(intent);
        });

        // --- Listeners para las tarjetas ---
        sosCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_alarma_personal.class);
            startActivity(intent);
        });

        cardNumerosOficiales.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_numeros_oficiales.class);
            startActivity(intent);
        });

        cardPrimerosAuxilios.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_guia_primeros_auxilios2.class);
            startActivity(intent);
        });

        cardContactos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_contactos_confianza2.class);
            startActivity(intent);
        });

        cardLocalizacion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_localizacion.class);
            startActivity(intent);
        });

        cardRegistro.setOnClickListener(v -> {
            // Asegúrate de tener una actividad llamada activity_registro_incidentes.class
            Intent intent = new Intent(MainActivity.this, RegistroIncidentesActivity.class);
            startActivity(intent);
        });
    }

    private void verificarYPedirPermisos() {
        String[] permisosRequeridos = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        List<String> permisosParaSolicitar = new ArrayList<>();

        for (String permiso : permisosRequeridos) {
            if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
                permisosParaSolicitar.add(permiso);
            }
        }

        if (!permisosParaSolicitar.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permisosParaSolicitar.toArray(new String[0]),
                    CODIGO_PERMISOS_MULTIPLES
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_PERMISOS_MULTIPLES) {
            if (grantResults.length > 0) {
                boolean todosLosPermisosConcedidos = true;
                for (int resultado : grantResults) {
                    if (resultado != PackageManager.PERMISSION_GRANTED) {
                        todosLosPermisosConcedidos = false;
                        break;
                    }
                }

                if (todosLosPermisosConcedidos) {
                    Toast.makeText(this, "¡Todos los permisos han sido concedidos!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Algunos permisos fueron denegados. Ciertas funciones podrían no estar disponibles.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
