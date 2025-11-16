package sv.edu.catolica.emergenciasya;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class activity_alarma_personal extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private boolean isAlarmPlaying = false;
    private boolean isFlashOn = false;

    private Ringtone ringtone;
    private CameraManager cameraManager;
    private String cameraId;

    private TextView textAlarma;
    private MaterialButton btnFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alarma_personal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a la UI
        MaterialCardView cardAlarma = findViewById(R.id.card_alarma);
        textAlarma = findViewById(R.id.text_alarma);
        btnFlash = findViewById(R.id.btnFlash);

        // Inicializar cámara para el flash
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (cameraManager != null && cameraManager.getCameraIdList().length > 0) {
                cameraId = cameraManager.getCameraIdList()[0]; // normalmente la cámara trasera
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Listener del botón grande de alarma (card roja)
        cardAlarma.setOnClickListener(v -> toggleAlarm());

        // Listener del botón de flash
        btnFlash.setOnClickListener(v -> toggleFlash());
    }

    // =========================
    // LÓGICA DE LA ALARMA SONORA
    // =========================
    private void toggleAlarm() {
        if (isAlarmPlaying) {
            stopAlarm();
        } else {
            startAlarm();
        }
    }

    private void startAlarm() {
        // Usar tono de alarma del sistema, si no existe usar notificación
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
        if (ringtone != null) {
            ringtone.play();
            isAlarmPlaying = true;
            textAlarma.setText("DETENER");
        } else {
            Toast.makeText(this, "No se pudo reproducir la alarma", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopAlarm() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        isAlarmPlaying = false;
        // Regresar al texto original del botón
        textAlarma.setText(getString(R.string.alarmabtn));
    }

    // =========================
    // LÓGICA DEL FLASH
    // =========================
    private void toggleFlash() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(this, "El flash requiere Android 6.0 o superior", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar permiso de cámara
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION
            );
            return;
        }

        if (cameraManager == null || cameraId == null) {
            Toast.makeText(this, "No se encontró cámara para usar el flash", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            isFlashOn = !isFlashOn;
            cameraManager.setTorchMode(cameraId, isFlashOn);

            if (isFlashOn) {
                btnFlash.setText("Apagar Flash");
            } else {
                btnFlash.setText(getString(R.string.activar_flash));
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al controlar el flash", Toast.LENGTH_SHORT).show();
        }
    }

    // Manejar el resultado de la solicitud de permiso de cámara
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el usuario dio permiso, intentar encender el flash de nuevo
                toggleFlash();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado, no se puede usar el flash", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Cuando la actividad se pausa o destruye, apagamos todo
    @Override
    protected void onPause() {
        super.onPause();
        stopAlarm();
        turnOffFlashIfNeeded();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarm();
        turnOffFlashIfNeeded();
    }

    private void turnOffFlashIfNeeded() {
        if (isFlashOn && cameraManager != null && cameraId != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            isFlashOn = false;
        }
    }
}
