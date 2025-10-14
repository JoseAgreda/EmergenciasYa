
package sv.edu.catolica.emergenciasya;

import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Intro extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2500; // 2.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //noinspection deprecation
        new Handler().postDelayed(() -> {
            // Navigate to main screen
            Intent intent = new Intent(Intro.this, LogIn.class);
            startActivity(intent);
            finish(); // close splash so user can’t go back
        }, SPLASH_DELAY);
    }
}