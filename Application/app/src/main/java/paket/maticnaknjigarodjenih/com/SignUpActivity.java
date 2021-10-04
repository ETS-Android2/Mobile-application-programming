package paket.maticnaknjigarodjenih.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextIme, editTextPrezime, editTextKorisnickoIme, editTextLozinka, editTextPotvrdaLozinke, editTextJMBG;
    Button buttonRegistracija, buttonPrijava;

    DatabaseHelper databaseHelper;

    String ime = "", prezime = "", korisnickoIme = "", lozinka = "", potvrda = "", jmbg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextIme = findViewById(R.id.editTextIme_2);
        editTextPrezime = findViewById(R.id.editTextPrezime_2);
        editTextKorisnickoIme = findViewById(R.id.editTextKorisnickoIme_2);
        editTextLozinka = findViewById(R.id.editTextSifra_2);
        editTextPotvrdaLozinke = findViewById(R.id.editTextPotvrdaSifra_2);
        editTextJMBG = findViewById(R.id.editTextJMBG_2);
        buttonRegistracija = findViewById(R.id.buttonRegistracija_2);
        buttonPrijava = findViewById(R.id.buttonPrijava_2);

        buttonRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper = new DatabaseHelper(SignUpActivity.this);

                ime = editTextIme.getText().toString();
                prezime = editTextPrezime.getText().toString();
                jmbg = editTextJMBG.getText().toString();
                korisnickoIme = editTextKorisnickoIme.getText().toString();
                lozinka = editTextLozinka.getText().toString();
                potvrda = editTextPotvrdaLozinke.getText().toString();

                databaseHelper.addDataInKorisnici(ime, prezime, jmbg, korisnickoIme, lozinka, potvrda);
            }
        });

        buttonPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}