package sv.edu.catolica.emergenciasya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class activity_localizacion extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 200;

    private FusedLocationProviderClient fusedLocationClient;
    // Se elimina la variable del botón de SMS
    private MaterialButton btnCompartirTiempoReal;

    private GoogleMap mMap;
    private Location ultimaUbicacionConocida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Solo se obtiene la referencia del botón de compartir
        btnCompartirTiempoReal = findViewById(R.id.btnCompartirTiempoReal);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Se elimina toda la lógica relacionada con el botón de SMS
        btnCompartirTiempoReal.setOnClickListener(v -> {
            if (ultimaUbicacionConocida != null) {
                compartirUbicacion(ultimaUbicacionConocida);
            } else {
                Toast.makeText(this, "Ubicación no disponible. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        verificarPermisoYConfigurarMapa();
    }

    private void verificarPermisoYConfigurarMapa() {
        if (tienePermisoUbicacion()) {
            configurarMapaConUbicacion();
        } else {
            solicitarPermisoUbicacion();
        }
    }

    private void configurarMapaConUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true); // Muestra el botón de centrar

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            ultimaUbicacionConocida = location;
                            LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(miUbicacion).title("¡Estoy aquí!"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 15f));
                        } else {
                            Toast.makeText(this, "No se pudo obtener la ubicación. Activa la ubicación del dispositivo.", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private boolean tienePermisoUbicacion() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void solicitarPermisoUbicacion() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    // Se elimina el método 'enviarSmsConUbicacion'
    // Se elimina el método 'verificarDisponibilidadDeSms'

    private void compartirUbicacion(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        String urlMaps = "https://www.google.com/maps/?q=" + lat + "," + lng;
        String texto = "Mi ubicación actual (EmergenciasYa!): " + urlMaps;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, texto);

        startActivity(Intent.createChooser(shareIntent, "Compartir ubicación con"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configurarMapaConUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. El mapa no puede mostrar tu ubicación.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
