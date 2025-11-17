package sv.edu.catolica.emergenciasya;

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
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView sosCard, cardNumerosOficiales, cardPrimerosAuxilios, cardContactos;

    // 1. Define un código único para la solicitud de MÚLTIPLES permisos.
    private static final int CODIGO_PERMISOS_MULTIPLES = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // === Referencias de las cards existentes ===
        sosCard = findViewById(R.id.sos_card);
        cardNumerosOficiales = findViewById(R.id.card_numeros_oficiales);
        cardPrimerosAuxilios = findViewById(R.id.card_primeros_auxilios);
        cardContactos = findViewById(R.id.card_contactos);

        // === Acciones para cada card (Listeners) ===
        setupCardClickListeners();

        // 2. Llama al método para verificar y solicitar los permisos al iniciar.
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
            Intent intent = new Intent(MainActivity.this, PrimerosAuxiliosActivity.class);
            startActivity(intent);
        });

        cardContactos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactosConfianzaActivity.class);
            startActivity(intent);
        });

        // (OnLongClickListeners omitidos por limpieza, ya que estaban comentados)
    }

    /**
     * 3. Método modificado para verificar y solicitar múltiples permisos.
     */
    private void verificarYPedirPermisos() {
        // Lista de permisos que necesita la aplicación.
        String[] permisosRequeridos = new String[]{
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.CAMERA
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
                    permisosParaSolicitar.toArray(new String[0]), // Convertimos la lista a un array
                    CODIGO_PERMISOS_MULTIPLES
            );
        }
        // Si todos los permisos ya están concedidos, no se hace nada.
    }

    /**
     * 4. Este método se ejecuta automáticamente después de que el usuario responde al diálogo.
     *      Ahora está preparado para manejar múltiples resultados.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_PERMISOS_MULTIPLES) {
            // Verificamos si hay resultados y si son del tamaño esperado.
            if (grantResults.length > 0) {
                boolean todosLosPermisosConcedidos = true;
                for (int resultado : grantResults) {
                    if (resultado != PackageManager.PERMISSION_GRANTED) {
                        todosLosPermisosConcedidos = false;
                        break; // Si un solo permiso es denegado, rompemos el bucle.
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
