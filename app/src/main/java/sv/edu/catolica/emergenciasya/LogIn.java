package sv.edu.catolica.emergenciasya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity{

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Example login logic (replace later with real authentication)
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                etEmail.setError("Requerido");
                etPassword.setError("Requerido");
            } else {
                // Continue to main screen
                Intent intent = new Intent(LogIn.this, Intro.class);
                startActivity(intent);
                finish();
            }
        });

        tvRegister.setOnClickListener(v -> {
            // Later link this to RegisterActivity
        });
    }
}
