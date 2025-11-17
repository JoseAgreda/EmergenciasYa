package sv.edu.catolica.emergenciasya;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

    // NUEVOS COMPONENTES PARA SONIDO
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private int originalVolume; // Para guardar el volumen original del usuario

    // Componentes de la cámara
    private CameraManager cameraManager;
    private String cameraId;

    // Vistas de la UI
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

        // Inicializar AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Inicializar cámara para el flash
        setupCamera();

        // Listener del botón grande de alarma (card roja)
        cardAlarma.setOnClickListener(v -> toggleAlarm());

        // Listener del botón de flash
        btnFlash.setOnClickListener(v -> toggleFlash());
    }

    // =========================
    // LÓGICA DE LA ALARMA SONORA (MODIFICADA)
    // =========================
    private void toggleAlarm() {
        if (isAlarmPlaying) {
            stopAlarm();
        } else {
            startAlarm();
        }
    }

    private void startAlarm() {
        if (audioManager == null) return;

        // 1. Guardar el volumen actual del usuario para poder restaurarlo después.
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // 2. Establecer el volumen del canal de música al máximo.
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

        // 3. Preparar y reproducir el sonido de sirena con MediaPlayer.
        // Asegúrate de tener el archivo "siren_sound.mp3" en la carpeta res/raw.
        mediaPlayer = MediaPlayer.create(this, R.raw.siren_sound);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true); // Para que el sonido se repita sin parar.
            mediaPlayer.start();
            isAlarmPlaying = true;
            textAlarma.setText("DETENER");
        } else {
            Toast.makeText(this, "No se pudo reproducir la alarma", Toast.LENGTH_SHORT).show();
            // Si falla, restauramos el volumen original.
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        }
    }

    private void stopAlarm() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release(); // Liberar recursos del MediaPlayer.
            mediaPlayer = null;
        }

        // Restaurar el volumen original del usuario.
        if (audioManager != null) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
        }

        isAlarmPlaying = false;
        textAlarma.setText(getString(R.string.alarmabtn));
    }


    // ===============================================
    // LÓGICA DEL FLASH (sin cambios importantes)
    // ===============================================
    private void setupCamera() {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (cameraManager != null && cameraManager.getCameraIdList().length > 0) {
                cameraId = cameraManager.getCameraIdList()[0];
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void toggleFlash() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(this, "El flash requiere Android 6.0 o superior", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }

        if (cameraManager == null || cameraId == null) {
            Toast.makeText(this, "No se encontró cámara para usar el flash", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            isFlashOn = !isFlashOn;
            cameraManager.setTorchMode(cameraId, isFlashOn);
            btnFlash.setText(isFlashOn ? "Apagar Flash" : getString(R.string.activar_flash));
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al controlar el flash", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toggleFlash();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado, no se puede usar el flash", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // ===============================================
    // CICLO DE VIDA DE LA ACTIVIDAD (IMPORTANTE)
    // ===============================================

    // Cuando la actividad se pausa o destruye, nos aseguramos de apagar todo.
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
