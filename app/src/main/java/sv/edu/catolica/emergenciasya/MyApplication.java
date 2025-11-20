package sv.edu.catolica.emergenciasya;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Cargar el idioma guardado al iniciar la app
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String language = prefs.getString("user_lang", Locale.getDefault().getLanguage());
        setLocale(language);
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
    