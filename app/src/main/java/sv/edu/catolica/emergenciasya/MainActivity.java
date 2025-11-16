package sv.edu.catolica.emergenciasya;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView sosCard, cardNumerosOficiales, cardPrimerosAuxilios, cardContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // === Referencias de las cards existentes ===
        sosCard = findViewById(R.id.sos_card);
        cardNumerosOficiales = findViewById(R.id.card_numeros_oficiales);
        cardPrimerosAuxilios = findViewById(R.id.card_primeros_auxilios);
        cardContactos = findViewById(R.id.card_contactos);

        // === Acciones para cada card ===

        sosCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlarmaActivity.class);
            startActivity(intent);
        });

        cardNumerosOficiales.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NumerosOficialesActivity.class);
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

        // === Nuevas secciones (no cambian el diseño actual) ===

        findViewById(R.id.sos_card).setOnLongClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroIncidentesActivity.class);
            startActivity(intent);
            return true;
        });

        findViewById(R.id.card_primeros_auxilios).setOnLongClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LocalizacionActivity.class);
            startActivity(intent);
            return true;
        });

        findViewById(R.id.card_contactos).setOnLongClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(intent);
            return true;
        });
    }
}
