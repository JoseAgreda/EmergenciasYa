package sv.edu.catolica.emergenciasya; // Asegúrate de que el paquete sea el correcto

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences; // Importa SharedPreferences
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class ConfiguracionActivity extends AppCompatActivity {

    private LinearLayout optionLanguage;
    private LinearLayout optionLogout;
    private TextView tvCurrentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion); // Usa el nombre correcto de tu layout

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        optionLanguage = findViewById(R.id.option_language);
        optionLogout = findViewById(R.id.option_logout);
        tvCurrentLanguage = findViewById(R.id.tv_current_language);

        setupClickListeners();
    }

    private void setupClickListeners() {
        optionLanguage.setOnClickListener(v -> showLanguageSelectionDialog());
        optionLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void showLanguageSelectionDialog() {
        final String[] languages = getResources().getStringArray(R.array.languages);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Idioma")
                .setItems(languages, (dialog, which) -> {
                    String selectedLanguage = languages[which];
                    tvCurrentLanguage.setText(selectedLanguage);
                    Toast.makeText(this, "Idioma cambiado a: " + selectedLanguage, Toast.LENGTH_SHORT).show();
                    // Aquí iría la lógica para cambiar el idioma de la app y reiniciarla.
                });
        builder.create().show();
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí, cerrar sesión", (dialog, which) -> {
                    // Llama al método para ejecutar el cierre de sesión.
                    logoutUser();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                });
        builder.create().show();
    }

    /**
     * ========= INICIO DEL CÓDIGO A IMPLEMENTAR =========
     * Lógica para ejecutar el cierre de sesión.
     */
    private void logoutUser() {
        // 1. Accede al mismo archivo SharedPreferences "UserPrefs".
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 2. Borra los datos de la sesión.
        //    Puedes borrar solo la clave isLoggedIn o todas las claves guardadas.
        //    Borrar todas es más seguro y limpio.
        editor.clear();
        editor.apply();

        // 3. Muestra un mensaje de confirmación al usuario.
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

        // 4. Redirige al usuario a la pantalla de inicio de sesión (LogIn).
        //    Asegúrate de que el nombre de tu actividad de login sea LogIn.class.
        Intent intent = new Intent(ConfiguracionActivity.this, LogIn.class);

        // 5. Añade estas "flags" para limpiar el historial de actividades.
        //    Esto evita que el usuario pueda volver a las pantallas anteriores (como MainActivity)
        //    con el botón de "atrás" después de cerrar sesión.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish(); // Cierra esta actividad (ConfiguracionActivity).
    }
    /**
     * ========= FIN DEL CÓDIGO A IMPLEMENTAR =========
     */
}
