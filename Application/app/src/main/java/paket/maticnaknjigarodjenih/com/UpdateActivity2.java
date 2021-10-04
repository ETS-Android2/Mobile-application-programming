package paket.maticnaknjigarodjenih.com;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity2 extends AppCompatActivity {
    EditText editTextIme, editTextPrezime, editTextKorisnickoIme, editTextLozinka, editTextPotvrdaLozinke, editTextJMBG;
    Button buttonAzuriraj, buttonIzbrisi;

    DatabaseHelper db;

    String ime = "", prezime = "", korisnickoIme = "", lozinka = "", potvrda = "", jmbg = "", jmbgRoditelja = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update2);

        db = new DatabaseHelper(UpdateActivity2.this);

        editTextIme = findViewById(R.id.editTextIme_22);
        editTextPrezime = findViewById(R.id.editTextPrezime_22);
        editTextKorisnickoIme = findViewById(R.id.editTextKorisnickoIme_22);
        editTextLozinka = findViewById(R.id.editTextSifra_22);
        editTextPotvrdaLozinke = findViewById(R.id.editTextPotvrdaSifra_22);
        editTextJMBG = findViewById(R.id.editTextJMBG_22);
        buttonAzuriraj = findViewById(R.id.buttonAzuriraj_22);
        buttonIzbrisi = findViewById(R.id.buttonIzbrisi_22);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("NASLOV");   // promeniti

        buttonAzuriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ime = editTextIme.getText().toString();
                prezime = editTextPrezime.getText().toString();
                korisnickoIme = editTextKorisnickoIme.getText().toString();
                lozinka = editTextLozinka.getText().toString();
                potvrda = editTextPotvrdaLozinke.getText().toString();  // stavicu ono sto pise u lozinci
                jmbg = editTextJMBG.getText().toString();

                db.updateDataInKorisnici(ime, prezime, korisnickoIme, lozinka, potvrda, jmbg);
            }
        });

        buttonIzbrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        getAndSetIntentData();
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("jmbgRoditelja")) {
            jmbgRoditelja = getIntent().getStringExtra("jmbgRoditelja");
            dataToList(jmbgRoditelja);

            editTextIme.setText(ime);
            editTextPrezime.setText(prezime);
            editTextKorisnickoIme.setText(korisnickoIme);
            editTextLozinka.setText(lozinka);
            editTextPotvrdaLozinke.setText(potvrda);
            editTextJMBG.setText(jmbg);
       } else {
            Toast.makeText(this, "Nema podataka", Toast.LENGTH_SHORT).show();
        }
    }

    void dataToList(String jmbgRoditelja) {
        Cursor cursor = db.readOneDataFromKorisnici(jmbgRoditelja);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nema podataka.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                ime = cursor.getString(0);
                prezime = cursor.getString(1);
                jmbg = cursor.getString(2);
                korisnickoIme = cursor.getString(3);
                lozinka = cursor.getString(4);
                potvrda = cursor.getString(4);
            }
        }
    }

    void confirmDialog() {
        String imeRoditelja = editTextIme.getText().toString();
        String prezimeRoditelja = editTextPrezime.getText().toString();
        String jmbgRoditelja = editTextJMBG.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Obrisati nalog roditelja");
        builder.setMessage("Da li želite da obrišete " + imeRoditelja + " " + prezimeRoditelja + ", JMBG: " + jmbgRoditelja + " ?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteDataFromKorisnici(jmbgRoditelja);
                finish();
            }
        });
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}