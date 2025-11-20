package sv.edu.catolica.emergenciasya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

public class ConfiguracionActivity extends AppCompatActivity {

    private LinearLayout optionLanguage;
    private LinearLayout optionLogout;
    private TextView tvCurrentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargar y aplicar el idioma guardado al iniciar la actividad
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String language = prefs.getString("user_lang", "es");
        setLocale(language);

        setContentView(R.layout.activity_configuracion);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.configuracion);

        optionLanguage = findViewById(R.id.option_language);
        optionLogout = findViewById(R.id.option_logout);
        tvCurrentLanguage = findViewById(R.id.tv_current_language);

        updateLanguageTextView(language);
        setupClickListeners();
    }

    private void setupClickListeners() {
        optionLanguage.setOnClickListener(v -> showLanguageSelectionDialog());
        optionLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    // ========= MÉTODO CORREGIDO =========
    private void showLanguageSelectionDialog() {
        final String[] displayLanguages = {"Español", "English", "Português"};
        final String[] languageCodes = {"es", "en", "pt"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.idioma)
                .setItems(displayLanguages, (dialog, which) -> {
                    String selectedLangCode = languageCodes[which];

                    // 1. Guardar la nueva preferencia de idioma.
                    saveLanguagePreference(selectedLangCode);

                    // 2. Aplicar el locale INMEDIATAMENTE en la actividad actual.
                    //    Este es el paso clave que faltaba.
                    setLocale(selectedLangCode);

                    // 3. Reiniciar la app para aplicar el cambio globalmente.
                    recreateApp();
                });
        builder.create().show();
    }
    // ========= FIN DE LA CORRECCIÓN =========

    private void saveLanguagePreference(String langCode) {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        prefs.edit().putString("user_lang", langCode).apply();
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void recreateApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // Cerramos la actividad actual para evitar comportamientos extraños
        finish();
    }

    private void updateLanguageTextView(String langCode) {
        switch (langCode) {
            case "en":
                tvCurrentLanguage.setText("English");
                break;
            case "pt":
                tvCurrentLanguage.setText("Português");
                break;
            case "es":
            default:
                tvCurrentLanguage.setText("Español");
                break;
        }
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cerrar_sesi_n)
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton(R.string.cerrar_sesi_n, (dialog, which) -> logoutUser())
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ConfiguracionActivity.this, LogIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
