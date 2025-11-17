package sv.edu.catolica.emergenciasya;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de ayuda para guardar y cargar la lista de contactos
 * usando SharedPreferences y la librería Gson.
 */
public class ContactStorage {

    // Nombre del archivo de preferencias y la clave donde guardaremos la lista
    private static final String PREFS_NAME = "EmergenciasYaPrefs";
    private static final String CONTACTS_KEY = "lista_contactos_confianza";

    /**
     * Guarda la lista completa de contactos en SharedPreferences.
     * @param context El contexto de la aplicación.
     * @param contactos La lista de contactos a guardar.
     */
    public static void saveContacts(Context context, List<Contacto> contactos) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        // Convierte la lista de contactos a una cadena JSON
        String jsonContactos = gson.toJson(contactos);

        // Guarda la cadena JSON
        editor.putString(CONTACTS_KEY, jsonContactos);
        editor.apply();
    }

    /**
     * Carga la lista de contactos desde SharedPreferences.
     * @param context El contexto de la aplicación.
     * @return La lista de contactos guardada, o una lista vacía si no hay ninguna.
     */
    public static ArrayList<Contacto> loadContacts(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        // Lee la cadena JSON guardada
        String jsonContactos = sharedPreferences.getString(CONTACTS_KEY, null);

        // Si no hay nada guardado, devuelve una lista vacía
        if (jsonContactos == null) {
            return new ArrayList<>();
        }

        // Define el tipo de dato que esperamos (una lista de Contacto)
        Type type = new TypeToken<ArrayList<Contacto>>() {}.getType();

        // Convierte la cadena JSON de vuelta a una lista de objetos Contacto
        return gson.fromJson(jsonContactos, type);
    }
}
