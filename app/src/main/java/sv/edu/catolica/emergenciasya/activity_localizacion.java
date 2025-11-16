package sv.edu.catolica.emergenciasya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class activity_localizacion extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 200;

    private FusedLocationProviderClient fusedLocationClient;
    private Button btnEnviarSMS;
    private Button btnCompartirTiempoReal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_localizacion);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar cliente de ubicación de Google
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnEnviarSMS = findViewById(R.id.btnEnviarSMS);
        btnCompartirTiempoReal = findViewById(R.id.btnCompartirTiempoReal);

        // Botón para enviar ubicación por SMS
        btnEnviarSMS.setOnClickListener(v -> obtenerUbicacionYEnviarSms());

        // Botón para compartir ubicación (WhatsApp, Telegram, etc.)
        btnCompartirTiempoReal.setOnClickListener(v -> obtenerUbicacionYCompartir());
    }

    // =========================
    // PERMISOS DE UBICACIÓN
    // =========================

    private boolean tienePermisoUbicacion() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void solicitarPermisoUbicacion() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION_PERMISSION
        );
    }

    // =========================
    // OBTENER UBICACIÓN Y ENVIAR SMS
    // =========================

    private void obtenerUbicacionYEnviarSms() {
        if (!tienePermisoUbicacion()) {
            solicitarPermisoUbicacion();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        enviarSmsConUbicacion(location);
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show()
                );
    }

    private void enviarSmsConUbicacion(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        String urlMaps = "https://www.google.com/maps/?q=" + lat + "," + lng;
        String mensaje = "Esta es mi ubicación actual enviada desde EmergenciasYa!: " + urlMaps;

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:")); // sin número para que el usuario elija
        smsIntent.putExtra("sms_body", mensaje);

        startActivity(smsIntent);
    }

    // =========================
    // OBTENER UBICACIÓN Y COMPARTIR (TIEMPO REAL entre comillas)
    // =========================

    private void obtenerUbicacionYCompartir() {
        if (!tienePermisoUbicacion()) {
            solicitarPermisoUbicacion();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        compartirUbicacion(location);
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al obtener la ubicación", Toast.LENGTH_SHORT).show()
                );
    }

    private void compartirUbicacion(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        String urlMaps = "https://www.google.com/maps/?q=" + lat + "," + lng;
        String texto = "Mi ubicación actual (EmergenciasYa!): " + urlMaps;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Mi ubicación actual");
        shareIntent.putExtra(Intent.EXTRA_TEXT, texto);

        startActivity(Intent.createChooser(shareIntent, "Compartir ubicación con"));
    }

    // =========================
    // RESPUESTA DE PERMISOS
    // =========================

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de ubicación concedido. Pulsa de nuevo el botón.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
