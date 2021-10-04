package paket.maticnaknjigarodjenih.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editTextKorisnikoIme, editTextSifra;
    Button buttonPrijava, buttonRegistracija;

    String korisnickoIme, sifra, jmbgRoditelja;
    boolean rezultat;
    Bundle extra;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextKorisnikoIme = findViewById(R.id.editTextKorisnickoIme);
        editTextSifra = findViewById(R.id.editTextSifra);
        buttonPrijava = findViewById(R.id.buttonPrijava);
        buttonRegistracija = findViewById(R.id.buttonRegistracija);

       buttonPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nije gotovo
                korisnickoIme = editTextKorisnikoIme.getText().toString();
                sifra = editTextSifra.getText().toString();

                databaseHelper = new DatabaseHelper(MainActivity.this);

                rezultat = databaseHelper.validateUserInKorisnici(korisnickoIme, sifra);

                if(rezultat) {
                    jmbgRoditelja = databaseHelper.getJMBGroditeljaFromKorisnici(korisnickoIme);

                    extra = new Bundle();
                    extra.putString("korisnickoIme", korisnickoIme);
                    extra.putString("jmbgRoditelja", jmbgRoditelja);

                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    intent.putExtras(extra);
                    startActivity(intent);
                }
                else if(korisnickoIme.equals("admin") && sifra.equals("12345")) {
                    Intent intent = new Intent(MainActivity.this, MainAdminActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Izabrali ste nepostojece korisnicko ime ili lozinku", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}