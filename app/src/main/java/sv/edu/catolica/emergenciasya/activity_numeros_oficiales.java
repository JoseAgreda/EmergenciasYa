package sv.edu.catolica.emergenciasya; // Este es tu paquete, está correcto.

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class activity_numeros_oficiales extends AppCompatActivity {

    // 1. Declaramos las vistas (las tarjetas de tu XML)
    private MaterialCardView cardPnc, cardBomberos, cardCruzRoja, cardCruzVerde, cardComandos, cardProteccionCivil, cardAes, cardSem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Tu código original para la UI de borde a borde.
        setContentView(R.layout.activity_numeros_oficiales);

        // 2. Enlazamos las vistas del XML con nuestras variables en Java
        // Es importante hacer esto después de setContentView()
        cardPnc = findViewById(R.id.card_pnc);
        cardBomberos = findViewById(R.id.card_bomberos);
        cardCruzRoja = findViewById(R.id.card_cruz_roja);
        cardCruzVerde = findViewById(R.id.card_cruz_verde);
        cardComandos = findViewById(R.id.card_comandos);
        cardProteccionCivil = findViewById(R.id.card_proteccion_civil); // <-- CORREGIDO
        cardAes = findViewById(R.id.card_aes);
        cardSem = findViewById(R.id.card_del_sur); // El ID en tu XML para SEM es "card_del_sur"

        // 3. Configuramos los "listeners" para que cada tarjeta reaccione a un clic.
        setupListeners();

        // Tu código original para manejar los insets (espacios de las barras del sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Método para centralizar la configuración de todos los OnClickListeners.
     */
    private void setupListeners() {
        // IMPORTANTE: Reemplaza estos números con los correctos y verificados.
        cardPnc.setOnClickListener(v -> realizarLlamada("911"));
        cardBomberos.setOnClickListener(v -> realizarLlamada("913"));
        cardCruzRoja.setOnClickListener(v -> realizarLlamada("22225155"));
        cardCruzVerde.setOnClickListener(v -> realizarLlamada("22845792"));
        cardComandos.setOnClickListener(v -> realizarLlamada("22211310"));
        cardProteccionCivil.setOnClickListener(v -> realizarLlamada("22810888"));
        cardAes.setOnClickListener(v -> realizarLlamada("25069000")); // Número de atención al cliente de ejemplo
        cardSem.setOnClickListener(v -> realizarLlamada("132"));
    }

    /**
     * Este método crea y ejecuta el Intent para iniciar una llamada telefónica.
     * @param numero El número de teléfono al que se va a llamar.
     */
    private void realizarLlamada(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            Toast.makeText(this, "Número de teléfono no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creamos un Intent con la acción ACTION_CALL para hacer una llamada directa.
        Intent intentDeLlamada = new Intent(Intent.ACTION_CALL);
        // Formateamos el número con el prefijo "tel:" que el sistema necesita.
        intentDeLlamada.setData(Uri.parse("tel:" + numero));

        try {
            // Inicia la actividad de llamada del sistema.
            startActivity(intentDeLlamada);
        } catch (SecurityException e) {
            // Este error saldrá si olvidaste añadir el permiso en AndroidManifest.xml
            Toast.makeText(this, "Permiso de llamada denegado. Habilítalo en la configuración de la app.", Toast.LENGTH_LONG).show();
            e.printStackTrace(); // Imprime el error en el Logcat para depuración.
        }
    }
}
