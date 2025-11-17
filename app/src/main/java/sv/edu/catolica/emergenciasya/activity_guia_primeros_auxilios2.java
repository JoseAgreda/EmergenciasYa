package sv.edu.catolica.emergenciasya;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class activity_guia_primeros_auxilios2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GuiaAdapter adapter;
    private List<Guia> listaDeGuias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_primeros_auxilios2);

        // --- Configuración de la Barra de Herramientas (Toolbar) ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Muestra la flecha de regreso
        }
        // Acción para el botón de regreso
        toolbar.setNavigationOnClickListener(v -> finish());

        // --- Configuración del RecyclerView ---
        recyclerView = findViewById(R.id.recycler_view_guia);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- Cargar los datos y conectar el adaptador ---
        cargarDatosDeGuias();
        adapter = new GuiaAdapter(this, listaDeGuias);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Este método crea la lista con los 5 temas de primeros auxilios.
     */
    // Dentro de la clase activity_guia_primeros_auxilios2.java

    /**
     * Este método crea la lista con los 5 temas, incluyendo los pasos detallados.
     */
    private void cargarDatosDeGuias() {
        listaDeGuias = new ArrayList<>();
        // Usamos el nuevo constructor: (título, descCorta, descLarga, icono)

        // 1. Quemaduras
        listaDeGuias.add(new Guia(
                "Quemaduras",
                "Acciones básicas en caso de quemaduras.",
                "1. Enfría la zona con agua a temperatura ambiente durante 10–20 minutos.\n\n" +
                        "2. No revientes ampollas ni apliques remedios caseros (pasta, aceite, etc.).\n\n" +
                        "3. Cubre la quemadura con una gasa limpia y seca, sin apretar.\n\n" +
                        "4. Acude a un centro médico si la quemadura es extensa o profunda.",
                R.drawable.fire
        ));

        // 2. RCP
        listaDeGuias.add(new Guia(
                "RCP Básica",
                "Reanimación cardiopulmonar para adultos.",
                "1. Llama a emergencias (911).\n\n" +
                        "2. Coloca a la persona boca arriba sobre una superficie firme.\n\n" +
                        "3. Coloca el talón de una mano en el centro del pecho y la otra mano encima, entrelazando los dedos.\n\n" +
                        "4. Realiza compresiones fuertes y rápidas (100–120 por minuto), hundiendo el pecho unos 5 cm.\n\n" +
                        "5. Continúa hasta que llegue la ayuda o la persona reaccione.",
                R.drawable.ic_rcp
        ));

        // 3. Fracturas
        listaDeGuias.add(new Guia(
                "Fracturas",
                "Inmovilización y cuidados ante una posible fractura.",
                "1. No muevas a la persona si sospechas una lesión en cabeza, cuello o espalda.\n\n" +
                        "2. Inmoviliza la zona afectada en la posición en que la encontraste. No intentes enderezar el hueso.\n\n" +
                        "3. Usa tablas, cartón o periódicos enrollados como férula, amarrando con vendas o tiras de tela por encima y por debajo de la lesión.\n\n" +
                        "4. Aplica hielo envuelto en un paño para reducir la hinchazón y el dolor.\n\n" +
                        "5. Busca atención médica de inmediato.",
                R.drawable.ic_fracturas
        ));

        // 4. Atragantamiento
        listaDeGuias.add(new Guia(
                "Atragantamiento (Maniobra de Heimlich)",
                "Cómo actuar ante una obstrucción de la vía aérea.",
                "1. Anima a la persona a toser con fuerza.\n\n" +
                        "2. Si no puede toser, hablar o respirar, realiza la Maniobra de Heimlich:\n" +
                        "   - Abrázala por detrás.\n" +
                        "   - Coloca un puño cerrado justo encima de su ombligo.\n" +
                        "   - Con la otra mano, agarra tu puño y realiza una compresión fuerte hacia adentro y hacia arriba.\n\n" +
                        "3. Si la persona pierde el conocimiento, llama al 911 e inicia RCP.",
                R.drawable.ic_atragamiento
        ));

        // 5. Hemorragias
        listaDeGuias.add(new Guia(
                "Hemorragias",
                "Cómo detener una hemorragia externa.",
                "1. Usa guantes si es posible para protegerte.\n\n" +
                        "2. Aplica presión directa y firme sobre la herida con una gasa o un paño limpio.\n\n" +
                        "3. Si la sangre empapa la gasa, no la retires. Coloca otra encima y sigue presionando.\n\n" +
                        "4. Si es posible, eleva la extremidad afectada por encima del nivel del corazón.\n\n" +
                        "5. Busca atención médica urgente si el sangrado es severo o no se detiene.",
                R.drawable.ic_hemo
        ));
    }

}
