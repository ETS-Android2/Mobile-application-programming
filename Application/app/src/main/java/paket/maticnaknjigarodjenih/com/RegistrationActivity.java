package paket.maticnaknjigarodjenih.com;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {
    EditText editTextImeDeteta, editTextPrezimeDeteta, editTextPolDeteta, editTextJMBGdeteta, editTextMestoRodjenjaDeteta, editTextOpstinaRodjenjaDeteta, editTextDrzavaRodjenjaDeteta, editTextDrzavljanstvoDeteta;
    Button buttonVremeRodjenjaDeteta, buttonDatumRodjenjaDeteta;
    DatePickerDialog.OnDateSetListener setListener;
    int Dsat, Dminut;
    int Dgodina, Dmesec, Ddan;
    String vremeRodjenjaDeteta, datumRodjenjaDeteta;

    EditText editTextImeOca, editTextPrezimeOca, editTextPolOca, editTextJMBGoca, editTextMestoRodjenjaOca, editTextOpstinaRodjenjaOca, editTextDrzavaRodjenjaOca, editTextDrzavljanstvoOca, editTextPrebivalisteOca, editTextAdresaOca;
    Button buttonVremeRodjenjaOca, buttonDatumRodjenjaOca;
    DatePickerDialog.OnDateSetListener setListener2;
    int Osat, Ominut;
    int Ogodina, Omesec, Odan;
    String vremeRodjenjaOca, datumRodjenjaOca;

    EditText editTextImeMajke, editTextPrezimeMajke, editTextPolMajke, editTextJMBGMajke, editTextMestoRodjenjaMajke, editTextOpstinaRodjenjaMajke, editTextDrzavaRodjenjaMajke, editTextDrzavljanstvoMajke, editTextPrebivalisteMajke, editTextAdresaMajke;
    Button buttonVremeRodjenjaMajke, buttonDatumRodjenjaMajke;
    DatePickerDialog.OnDateSetListener setListener3;
    int Msat, Mminut;
    int Mgodina, Mmesec, Mdan;
    String vremeRodjenjaMajke, datumRodjenjaMajke;

    Button buttonDodaj;
    DatabaseHelper databaseHelper;
    String[] podatak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextImeDeteta = findViewById(R.id.editTextImeDeteta);
        editTextPrezimeDeteta = findViewById(R.id.editTextPrezimeDeteta);
        editTextPolDeteta = findViewById(R.id.editTextPolDeteta);
        editTextJMBGdeteta = findViewById(R.id.editTextJMBGdeteta);
        editTextMestoRodjenjaDeteta = findViewById(R.id.editTextMestoRodjenjaDeteta);
        editTextOpstinaRodjenjaDeteta = findViewById(R.id.editTextOpstinaRodjenjaDeteta);
        editTextDrzavaRodjenjaDeteta = findViewById(R.id.editTextDrzavaRodjenjaDeteta);
        editTextDrzavljanstvoDeteta = findViewById(R.id.editTextDrzavljanstvoDeteta);

        editTextImeOca = findViewById(R.id.editTextImeOca);
        editTextPrezimeOca = findViewById(R.id.editTextPrezimeOca);
        editTextPolOca = findViewById(R.id.editTextPolOca);
        editTextJMBGoca = findViewById(R.id.editTextJMBGoca);
        editTextMestoRodjenjaOca = findViewById(R.id.editTextMestoRodjenjaOca);
        editTextOpstinaRodjenjaOca = findViewById(R.id.editTextOpstinaRodjenjaOca);
        editTextDrzavaRodjenjaOca = findViewById(R.id.editTextDrzavaRodjenjaOca);
        editTextDrzavljanstvoOca = findViewById(R.id.editTextDrzavljanstvoOca);
        editTextPrebivalisteOca = findViewById(R.id.editTextPrebivalisteOca);
        editTextAdresaOca = findViewById(R.id.editTextAdresaOca);

        editTextImeMajke = findViewById(R.id.editTextImeMajke);
        editTextPrezimeMajke = findViewById(R.id.editTextPrezimeMajke);
        editTextPolMajke = findViewById(R.id.editTextPolMajke);
        editTextJMBGMajke = findViewById(R.id.editTextJMBGMajke);
        editTextMestoRodjenjaMajke = findViewById(R.id.editTextMestoRodjenjaMajke);
        editTextOpstinaRodjenjaMajke = findViewById(R.id.editTextOpstinaRodjenjaMajke);
        editTextDrzavaRodjenjaMajke = findViewById(R.id.editTextDrzavaRodjenjaMajke);
        editTextDrzavljanstvoMajke = findViewById(R.id.editTextDrzavljanstvoMajke);
        editTextPrebivalisteMajke = findViewById(R.id.editTextPrebivalisteMajke);
        editTextAdresaMajke = findViewById(R.id.editTextAdresaMajke);

        buttonVremeRodjenjaDeteta = findViewById(R.id.buttonVremeRodjenjaDeteta);
        buttonDatumRodjenjaDeteta = findViewById(R.id.buttonDatumRodjenjaDeteta);

        buttonVremeRodjenjaOca = findViewById(R.id.buttonVremeRodjenjaOca);
        buttonDatumRodjenjaOca = findViewById(R.id.buttonDatumRodjenjaOca);

        buttonVremeRodjenjaMajke = findViewById(R.id.buttonVremeRodjenjaMajke);
        buttonDatumRodjenjaMajke = findViewById(R.id.buttonDatumRodjenjaMajke);

        buttonDodaj = findViewById(R.id.buttonDodaj);

        buttonVremeRodjenjaDeteta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        RegistrationActivity.this,
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
                        RegistrationActivity.this,
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
                        RegistrationActivity.this,
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
                        RegistrationActivity.this,
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
                        RegistrationActivity.this,
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
                        RegistrationActivity.this,
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

        buttonDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nije gotovo
                databaseHelper = new DatabaseHelper(RegistrationActivity.this);
                podatak = new String[34];

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

                databaseHelper.addDataInMaticnaKnjigaRodjenih(podatak);
            }
        });
    }
}