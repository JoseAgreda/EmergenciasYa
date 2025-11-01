package sv.edu.catolica.emergenciasya;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnOption1, btnOption2, btnOption3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOption1 = findViewById(R.id.card_numeros_oficiales);
        btnOption2 = findViewById(R.id.card_primeros_auxilios);
        btnOption3 = findViewById(R.id.card_contactos);

        btnOption1.setOnClickListener(v -> {
            // TODO: Action for emergency call
        });

        btnOption2.setOnClickListener(v -> {
            // TODO: Open First Aid screen
        });

        btnOption3.setOnClickListener(v -> {
            // TODO: Open Trusted Contacts
        });
    }
}
