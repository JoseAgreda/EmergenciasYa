package sv.edu.catolica.emergenciasya;

import android.Manifest; // Importa la clase Manifest
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 1. Añade la variable para la nueva tarjeta
    private MaterialCardView sosCard, cardNumerosOficiales, cardPrimerosAuxilios, cardContactos, cardLocalizacion, cardRegistro;

    // Código único para la solicitud de MÚLTIPLES permisos.
    private static final int CODIGO_PERMISOS_MULTIPLES = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // === Referencias de las cards ===
        sosCard = findViewById(R.id.sos_card);
        cardNumerosOficiales = findViewById(R.id.card_numeros_oficiales);
        cardPrimerosAuxilios = findViewById(R.id.card_primeros_auxilios);
        cardContactos = findViewById(R.id.card_contactos);
        // 2. Obtiene la referencia de la nueva tarjeta
        cardLocalizacion = findViewById(R.id.card_localizacion);
        cardRegistro = findViewById(R.id.card_registro);

        // === Acciones para cada card (Listeners) ===
        setupCardClickListeners();

        // Llama al método para verificar y solicitar los permisos al iniciar.
        verificarYPedirPermisos();
    }

    private void setupCardClickListeners() {
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

        // 3. Añade la acción para la nueva tarjeta de localización
        cardLocalizacion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_localizacion.class);
            startActivity(intent);
        });
        cardRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroIncidentesActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Método modificado para verificar y solicitar múltiples permisos.
     */
    private void verificarYPedirPermisos() {
        // Lista de permisos que necesita la aplicación.
        // 4. AÑADE EL PERMISO DE UBICACIÓN
        String[] permisosRequeridos = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        // Lista para guardar los permisos que aún no han sido concedidos.
        List<String> permisosParaSolicitar = new ArrayList<>();

        // Recorremos la lista de permisos requeridos para ver cuáles faltan.
        for (String permiso : permisosRequeridos) {
            if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
                permisosParaSolicitar.add(permiso);
            }
        }

        // Si la lista de permisos a solicitar no está vacía, los pedimos.
        if (!permisosParaSolicitar.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permisosParaSolicitar.toArray(new String[0]),
                    CODIGO_PERMISOS_MULTIPLES
            );
        }
    }

    /**
     * Este método se ejecuta automáticamente después de que el usuario responde al diálogo.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_PERMISOS_MULTIPLES) {
            if (grantResults.length > 0) {
                boolean todosLosPermisosConcedidos = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        todosLosPermisosConcedidos = false;
                        // Opcional: Muestra qué permiso fue denegado
                        // Log.d("Permissions", "Permiso denegado: " + permissions[i]);
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
