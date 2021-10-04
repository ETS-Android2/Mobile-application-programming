package paket.maticnaknjigarodjenih.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainAdminActivity extends AppCompatActivity {
    Button buttonUpravljajKorisnicima, buttonUpravljajMKR, buttonLogoutAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        buttonUpravljajKorisnicima = findViewById(R.id.buttonUpravljajKorisnicima);
        buttonUpravljajMKR = findViewById(R.id.buttonUpravljajMKR);
        buttonLogoutAdmin = findViewById(R.id.buttonLogoutAdmin);

        buttonUpravljajKorisnicima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdminActivity.this, AdminActivity2.class);
                startActivity(intent);
            }
        });

        buttonUpravljajMKR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainAdminActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        buttonLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}