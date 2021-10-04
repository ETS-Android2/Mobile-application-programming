package paket.maticnaknjigarodjenih.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    EditText editTextImeDeteta, editTextPrezimeDeteta, editTextPolDeteta, editTextJMBGdeteta, editTextMestoRodjenjaDeteta, editTextOpstinaRodjenjaDeteta, editTextDrzavaRodjenjaDeteta, editTextDrzavljanstvoDeteta;
    Button buttonVremeRodjenjaDeteta, buttonDatumRodjenjaDeteta;
    DatePickerDialog.OnDateSetListener setListener;
    int Dsat = 0, Dminut = 0;
    int Dgodina = 0, Dmesec = 0, Ddan = 0;
    String vremeRodjenjaDeteta = "", datumRodjenjaDeteta = "";

    EditText editTextImeOca, editTextPrezimeOca, editTextPolOca, editTextJMBGoca, editTextMestoRodjenjaOca, editTextOpstinaRodjenjaOca, editTextDrzavaRodjenjaOca, editTextDrzavljanstvoOca, editTextPrebivalisteOca, editTextAdresaOca;
    Button buttonVremeRodjenjaOca, buttonDatumRodjenjaOca;
    DatePickerDialog.OnDateSetListener setListener2;
    int Osat = 0, Ominut = 0;
    int Ogodina = 0, Omesec = 0, Odan = 0;
    String vremeRodjenjaOca = "", datumRodjenjaOca = "";

    EditText editTextImeMajke, editTextPrezimeMajke, editTextPolMajke, editTextJMBGMajke, editTextMestoRodjenjaMajke, editTextOpstinaRodjenjaMajke, editTextDrzavaRodjenjaMajke, editTextDrzavljanstvoMajke, editTextPrebivalisteMajke, editTextAdresaMajke;
    Button buttonVremeRodjenjaMajke, buttonDatumRodjenjaMajke;
    DatePickerDialog.OnDateSetListener setListener3;
    int Msat = 0, Mminut = 0;
    int Mgodina = 0, Mmesec = 0, Mdan = 0;
    String vremeRodjenjaMajke = "", datumRodjenjaMajke = "";

    Button buttonAzuriraj, buttonIzbrisi;
    DatabaseHelper db;
    String[] podatak = new String[34];
    String imeDeteta  = "", prezimeDeteta  = "", jmbgDeteta  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        db = new DatabaseHelper(UpdateActivity.this);

        editTextImeDeteta = findViewById(R.id.editTextImeDeteta2);
        editTextPrezimeDeteta = findViewById(R.id.editTextPrezimeDeteta2);
        editTextPolDeteta = findViewById(R.id.editTextPolDeteta2);
        editTextJMBGdeteta = findViewById(R.id.editTextJMBGdeteta2);
        editTextMestoRodjenjaDeteta = findViewById(R.id.editTextMestoRodjenjaDeteta2);
        editTextOpstinaRodjenjaDeteta = findViewById(R.id.editTextOpstinaRodjenjaDeteta2);
        editTextDrzavaRodjenjaDeteta = findViewById(R.id.editTextDrzavaRodjenjaDeteta2);
        editTextDrzavljanstvoDeteta = findViewById(R.id.editTextDrzavljanstvoDeteta2);

        editTextImeOca = findViewById(R.id.editTextImeOca2);
        editTextPrezimeOca = findViewById(R.id.editTextPrezimeOca2);
        editTextPolOca = findViewById(R.id.editTextPolOca2);
        editTextJMBGoca = findViewById(R.id.editTextJMBGoca2);
        editTextMestoRodjenjaOca = findViewById(R.id.editTextMestoRodjenjaOca2);
        editTextOpstinaRodjenjaOca = findViewById(R.id.editTextOpstinaRodjenjaOca2);
        editTextDrzavaRodjenjaOca = findViewById(R.id.editTextDrzavaRodjenjaOca2);
        editTextDrzavljanstvoOca = findViewById(R.id.editTextDrzavljanstvoOca2);
        editTextPrebivalisteOca = findViewById(R.id.editTextPrebivalisteOca2);
        editTextAdresaOca = findViewById(R.id.editTextAdresaOca2);

        editTextImeMajke = findViewById(R.id.editTextImeMajke2);
        editTextPrezimeMajke = findViewById(R.id.editTextPrezimeMajke2);
        editTextPolMajke = findViewById(R.id.editTextPolMajke2);
        editTextJMBGMajke = findViewById(R.id.editTextJMBGMajke2);
        editTextMestoRodjenjaMajke = findViewById(R.id.editTextMestoRodjenjaMajke2);
        editTextOpstinaRodjenjaMajke = findViewById(R.id.editTextOpstinaRodjenjaMajke2);
        editTextDrzavaRodjenjaMajke = findViewById(R.id.editTextDrzavaRodjenjaMajke2);
        editTextDrzavljanstvoMajke = findViewById(R.id.editTextDrzavljanstvoMajke2);
        editTextPrebivalisteMajke = findViewById(R.id.editTextPrebivalisteMajke2);
        editTextAdresaMajke = findViewById(R.id.editTextAdresaMajke2);

        buttonVremeRodjenjaDeteta = findViewById(R.id.buttonVremeRodjenjaDeteta2);
        buttonDatumRodjenjaDeteta = findViewById(R.id.buttonDatumRodjenjaDeteta2);

        buttonVremeRodjenjaOca = findViewById(R.id.buttonVremeRodjenjaOca2);
        buttonDatumRodjenjaOca = findViewById(R.id.buttonDatumRodjenjaOca2);

        buttonVremeRodjenjaMajke = findViewById(R.id.buttonVremeRodjenjaMajke2);
        buttonDatumRodjenjaMajke = findViewById(R.id.buttonDatumRodjenjaMajke2);

        buttonAzuriraj = findViewById(R.id.buttonAzuriraj2);
        buttonIzbrisi = findViewById(R.id.buttonIzbrisi2);


        buttonVremeRodjenjaDeteta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UpdateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // ovde imam sate i minute
                                Dsat = hourOfDay;
                                Dminut = minute;
                                vremeRodjenjaDeteta = Dsat + ":" + Dminut;
                                buttonVremeRodjenjaDeteta.setText(vremeRodjenjaDeteta);
                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(Dsat, Dminut);
                timePickerDialog.show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        Dgodina = calendar.get(Calendar.YEAR);
        Dmesec = calendar.get(Calendar.MONTH);
        Ddan = calendar.get(Calendar.DAY_OF_MONTH);

        buttonDatumRodjenjaDeteta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener, Dgodina, Dmesec, Ddan);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // ovde imam godinu, mesec i dan
                Dgodina = year;
                Dmesec = month + 1;
                Ddan = dayOfMonth;
                datumRodjenjaDeteta = Ddan + "." + Dmesec + "." + Dgodina;
                buttonDatumRodjenjaDeteta.setText(datumRodjenjaDeteta);
            }
        };

        buttonVremeRodjenjaOca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UpdateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // ovde imam sate i minute
                                Osat = hourOfDay;
                                Ominut = minute;
                                vremeRodjenjaOca = Osat + ":" + Ominut;
                                buttonVremeRodjenjaOca.setText(vremeRodjenjaOca);
                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(Osat, Ominut);
                timePickerDialog.show();
            }
        });

        Ogodina = calendar.get(Calendar.YEAR);
        Omesec = calendar.get(Calendar.MONTH);
        Odan = calendar.get(Calendar.DAY_OF_MONTH);

        buttonDatumRodjenjaOca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener2, Ogodina, Omesec, Odan);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // ovde imam godinu, mesec i dan
                Ogodina = year;
                Omesec = month + 1;
                Odan = dayOfMonth;
                datumRodjenjaOca = Odan + "." + Omesec + "." + Ogodina;
                buttonDatumRodjenjaOca.setText(datumRodjenjaOca);
            }
        };

        buttonVremeRodjenjaMajke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UpdateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // ovde imam sate i minute
                                Msat = hourOfDay;
                                Mminut = minute;
                                vremeRodjenjaMajke = Msat + ":" + Mminut;
                                buttonVremeRodjenjaMajke.setText(vremeRodjenjaMajke);
                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(Msat, Mminut);
                timePickerDialog.show();
            }
        });

        Mgodina = calendar.get(Calendar.YEAR);
        Mmesec = calendar.get(Calendar.MONTH);
        Mdan = calendar.get(Calendar.DAY_OF_MONTH);

        buttonDatumRodjenjaMajke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener3, Mgodina, Mmesec, Mdan);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener3 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // ovde imam godinu, mesec i dan
                Mgodina = year;
                Mmesec = month + 1;
                Mdan = dayOfMonth;
                datumRodjenjaMajke = Mdan + "." + Mmesec + "." + Mgodina;
                buttonDatumRodjenjaMajke.setText(datumRodjenjaMajke);
            }
        };

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("NASLOV");   // promeniti

        buttonAzuriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                podatak[0] = editTextImeDeteta.getText().toString();
                podatak[1] = editTextPrezimeDeteta.getText().toString();
                podatak[2] = editTextJMBGdeteta.getText().toString();
                podatak[3] = editTextPolDeteta.getText().toString();
                podatak[4] = vremeRodjenjaDeteta;
                podatak[5] = datumRodjenjaDeteta;
                podatak[6] = editTextMestoRodjenjaDeteta.getText().toString();
                podatak[7] = editTextOpstinaRodjenjaDeteta.getText().toString();
                podatak[8] = editTextDrzavaRodjenjaDeteta.getText().toString();
                podatak[9] = editTextDrzavljanstvoDeteta.getText().toString();
                podatak[10] = editTextImeOca.getText().toString();
                podatak[11] = editTextPrezimeOca.getText().toString();
                podatak[12] = editTextJMBGoca.getText().toString();
                podatak[13] = editTextPolOca.getText().toString();
                podatak[14] = vremeRodjenjaOca;
                podatak[15] = datumRodjenjaOca;
                podatak[16] = editTextMestoRodjenjaOca.getText().toString();
                podatak[17] = editTextOpstinaRodjenjaOca.getText().toString();
                podatak[18] = editTextDrzavaRodjenjaOca.getText().toString();
                podatak[19] = editTextDrzavljanstvoOca.getText().toString();
                podatak[20] = editTextPrebivalisteOca.getText().toString();
                podatak[21] = editTextAdresaOca.getText().toString();
                podatak[22] = editTextImeMajke.getText().toString();
                podatak[23] = editTextPrezimeMajke.getText().toString();
                podatak[24] = editTextJMBGMajke.getText().toString();
                podatak[25] = editTextPolMajke.getText().toString();
                podatak[26] = vremeRodjenjaMajke;
                podatak[27] = datumRodjenjaMajke;
                podatak[28] = editTextMestoRodjenjaMajke.getText().toString();
                podatak[29] = editTextOpstinaRodjenjaMajke.getText().toString();
                podatak[30] = editTextDrzavaRodjenjaMajke.getText().toString();
                podatak[31] = editTextDrzavljanstvoMajke.getText().toString();
                podatak[32] = editTextPrebivalisteMajke.getText().toString();
                podatak[33] = editTextAdresaMajke.getText().toString();

                db.updateDataInMaticnaKnjigaRodjenih(podatak);
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
        if (getIntent().hasExtra("imeDeteta") && (getIntent().hasExtra("prezimeDeteta")) && (getIntent().hasExtra("jmbgDeteta"))) {
            jmbgDeteta = getIntent().getStringExtra("jmbgDeteta");
            dataToList(jmbgDeteta); //getIntent().getStringExtra("jmbgDeteta")

            editTextImeDeteta.setText(podatak[0]);
            editTextPrezimeDeteta.setText(podatak[1]);
            editTextJMBGdeteta.setText(podatak[2]);
            editTextPolDeteta.setText(podatak[3]);
            buttonVremeRodjenjaDeteta.setText(podatak[4]);  // proveriti
            vremeRodjenjaDeteta = podatak[4];
            buttonDatumRodjenjaDeteta.setText(podatak[5]);  // proveriti
            datumRodjenjaDeteta = podatak[5];
            editTextMestoRodjenjaDeteta.setText(podatak[6]);
            editTextOpstinaRodjenjaDeteta.setText(podatak[7]);
            editTextDrzavaRodjenjaDeteta.setText(podatak[8]);
            editTextDrzavljanstvoDeteta.setText(podatak[9]);
            editTextImeOca.setText(podatak[10]);
            editTextPrezimeOca.setText(podatak[11]);
            editTextJMBGoca.setText(podatak[12]);
            editTextPolOca.setText(podatak[13]);
            buttonVremeRodjenjaOca.setText(podatak[14]);  // proveriti
            vremeRodjenjaOca = podatak[14];
            buttonDatumRodjenjaOca.setText(podatak[15]);  // proveriti
            datumRodjenjaOca = podatak[15];
            editTextMestoRodjenjaOca.setText(podatak[16]);
            editTextOpstinaRodjenjaOca.setText(podatak[17]);
            editTextDrzavaRodjenjaOca.setText(podatak[18]);
            editTextDrzavljanstvoOca.setText(podatak[19]);
            editTextPrebivalisteOca.setText(podatak[20]);
            editTextAdresaOca.setText(podatak[21]);
            editTextImeMajke.setText(podatak[22]);
            editTextPrezimeMajke.setText(podatak[23]);
            editTextJMBGMajke.setText(podatak[24]);
            editTextPolMajke.setText(podatak[25]);
            buttonVremeRodjenjaMajke.setText(podatak[26]);  // proveriti
            vremeRodjenjaMajke = podatak[26];
            buttonDatumRodjenjaMajke.setText(podatak[27]); // proveriti
            datumRodjenjaMajke = podatak[27];
            editTextMestoRodjenjaMajke.setText(podatak[28]);
            editTextOpstinaRodjenjaMajke.setText(podatak[29]);
            editTextDrzavaRodjenjaMajke.setText(podatak[30]);
            editTextDrzavljanstvoMajke.setText(podatak[31]);
            editTextPrebivalisteMajke.setText(podatak[32]);
            editTextAdresaMajke.setText(podatak[33]);
        } else {
            Toast.makeText(this, "Nema podataka", Toast.LENGTH_SHORT).show();
        }
    }

    void dataToList(String jmbg) {
        Cursor cursor = db.readOneDataFromMaticnaKnjigaRodjenih(jmbg);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Nema podataka.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                podatak[0] = cursor.getString(0);
                podatak[1] = cursor.getString(1);
                podatak[2] = cursor.getString(2);
                podatak[3] = cursor.getString(3);
                podatak[4] = cursor.getString(4);
                podatak[5] = cursor.getString(5);
                podatak[6] = cursor.getString(6);
                podatak[7] = cursor.getString(7);
                podatak[8] = cursor.getString(8);
                podatak[9] = cursor.getString(9);
                podatak[10] = cursor.getString(10);
                podatak[11] = cursor.getString(11);
                podatak[12] = cursor.getString(12);
                podatak[13] = cursor.getString(13);
                podatak[14] = cursor.getString(14);
                podatak[15] = cursor.getString(15);
                podatak[16] = cursor.getString(16);
                podatak[17] = cursor.getString(17);
                podatak[18] = cursor.getString(18);
                podatak[19] = cursor.getString(19);
                podatak[20] = cursor.getString(20);
                podatak[21] = cursor.getString(21);
                podatak[22] = cursor.getString(22);
                podatak[23] = cursor.getString(23);
                podatak[24] = cursor.getString(24);
                podatak[25] = cursor.getString(25);
                podatak[26] = cursor.getString(26);
                podatak[27] = cursor.getString(27);
                podatak[28] = cursor.getString(28);
                podatak[29] = cursor.getString(29);
                podatak[30] = cursor.getString(30);
                podatak[31] = cursor.getString(31);
                podatak[32] = cursor.getString(32);
                podatak[33] = cursor.getString(33);
            }
        }
    }

    void confirmDialog() {
        imeDeteta = editTextImeDeteta.getText().toString();
        prezimeDeteta = editTextPrezimeDeteta.getText().toString();
        jmbgDeteta = editTextJMBGdeteta.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Obrisati dete iz matične knjige rođenih");
        builder.setMessage("Da li želite da obrišete " + imeDeteta + prezimeDeteta + ", JMBG: " + jmbgDeteta + " ?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteData(jmbgDeteta);
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