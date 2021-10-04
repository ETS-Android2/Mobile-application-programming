package paket.maticnaknjigarodjenih.com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserActivity extends AppCompatActivity {
    TextView textViewNaslov;
    Button buttonDodaj, buttonPreuzmi, buttonPrikazi, buttonPrikaziSve, buttonLOGOUT;
    EditText editTextJMBG;
    String korisnickoIme, jmbgRoditelja, jmbgDeteta;
    Bundle extra;

    DatabaseHelper databaseHelper;

    Bitmap bmp, scaledbmp;  // koristimo za dodavanje slike u pdf file
    Bitmap bmp2, scaledbmp2;  // koristimo za dodavanje slike u pdf file
    Date dateObj;           // koristimo za kreiranje trenutnog datuma i vremena u pdf file
    DateFormat dateFormat;  // koristimo za kreiranje trenutnog datuma i vremena u pdf file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        textViewNaslov = findViewById(R.id.textViewNaslov_3);
        editTextJMBG = findViewById(R.id.editTextJMBG_3);
        buttonDodaj = findViewById(R.id.buttonDodaj_3);
        buttonPreuzmi = findViewById(R.id.buttonPreuzmi_3);
        buttonPrikazi = findViewById(R.id.buttonPrikazi_3);
        buttonPrikaziSve = findViewById(R.id.buttonPrikaziSve_3);
        buttonLOGOUT = findViewById(R.id.buttonLOGOUT_3);

        databaseHelper = new DatabaseHelper(this);

        extra = getIntent().getExtras();
        korisnickoIme = extra.getString("korisnickoIme");
        jmbgRoditelja = extra.getString("jmbgRoditelja");

        textViewNaslov.setText("Korisnik: " + korisnickoIme);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.grb);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 90, 140, false);
        bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.mylogo);
        scaledbmp2 = Bitmap.createScaledBitmap(bmp2, 200, 200, false);

        // dodao zbog PDF-a
        //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(UserActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        buttonDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        buttonPreuzmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nije gotovo
                // treba da preuzme u formi PDF-a

                jmbgDeteta = editTextJMBG.getText().toString();
                Cursor cursor = databaseHelper.readOneDataFromMaticnaKnjigaRodjenih(jmbgDeteta, jmbgRoditelja);

                dateObj = new Date();

                if (cursor.getCount() == 0) {
                    Toast.makeText(UserActivity.this, "Nema podataka. Nije moguce sacuvati PDF fajl. Proveri polje 'JMBG'.", Toast.LENGTH_SHORT).show();
                } else {
                    String[] podatak = new String[34];

                    while (cursor.moveToNext()) {
                            podatak[0] = cursor.getString(0);   // ime deteta
                            podatak[1] = cursor.getString(1);   // prezime deteta
                            podatak[2] = cursor.getString(2);   // jmbg deteta
                            podatak[3] = cursor.getString(3);   // pol deteta
                            podatak[4] = cursor.getString(4);   // vreme rodjenja deteta
                            podatak[5] = cursor.getString(5);   // datum rodjenja deteta
                            podatak[6] = cursor.getString(6);   // mesto rodjenja deteta
                            podatak[7] = cursor.getString(7);   // opstina rodjenja deteta
                            podatak[8] = cursor.getString(8);   // drzava rodjenja deteta
                            podatak[9] = cursor.getString(9);   // drzavljanstvo deteta
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
                            podatak[20] = cursor.getString(20); // prebivaliste oca
                            podatak[21] = cursor.getString(21); // adresa oca
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
                            podatak[32] = cursor.getString(32); // prebivaliste majke
                            podatak[33] = cursor.getString(33); // adresa majke
                    }

                    PdfDocument myPdfDocument = new PdfDocument();
                    Paint myPaint = new Paint();    // koristimo za ostalo
                    Paint titlePaint = new Paint(); // koristimo za naslove

                    PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
                    Canvas canvas = myPage1.getCanvas();

                    // dodajemo fotografiju u pdf
                    canvas.drawBitmap(scaledbmp, 555, 0, myPaint);

                    // dodajemo naslov u pdf
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(35);
                    canvas.drawText("REPUBLIKA SRBIJA", 600, 200, titlePaint);  //  x==width, y==height

                    // dodajemo drugi naslov u pdf
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(45);
                    canvas.drawText("IZVOD IZ MATIČNE KNJIGE ROĐENIH", 600, 350, titlePaint);

                    // dodajemo treci naslov u pdf
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(35);
                    canvas.drawText("Podaci o detetu:", 600, 450, titlePaint);

                    // kreiramo tabelu sa kolonama(ime, prezime,...) u pdf
                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(2);
                    canvas.drawRect(20, 480, 1180, 530, myPaint);
                    //
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("ime", 40, 505, myPaint);
                    canvas.drawText("prezime", 200, 505, myPaint);
                    canvas.drawText("jmbg", 400, 505, myPaint);
                    canvas.drawText("pol", 900, 505, myPaint);
                    canvas.drawText("vreme rođenja", 1100, 505, myPaint);
                    //
                    canvas.drawLine(180, 480, 180, 530, myPaint);
                    canvas.drawLine(380, 480, 380, 530, myPaint);
                    canvas.drawLine(880, 480, 880, 530, myPaint);
                    canvas.drawLine(1080, 480, 1080, 530, myPaint);
                    // dodajemo sadrzaj tabele
                    canvas.drawText(podatak[0], 40, 550, myPaint);
                    canvas.drawText(podatak[1], 200, 550, myPaint);
                    canvas.drawText(podatak[2], 400, 550, myPaint);
                    canvas.drawText(podatak[3], 900, 550, myPaint);
                    canvas.drawText(podatak[4], 1100, 550, myPaint);

                    // kreiramo tabelu sa kolonama(datum rođenja, mesto rođenja,...) u pdf
                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(2);
                    canvas.drawRect(20, 580, 1180, 630, myPaint);
                    //
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("datum rođenja", 40, 605, myPaint);
                    canvas.drawText("mesto rođenja", 200, 605, myPaint);
                    canvas.drawText("opština rođenja", 400, 605, myPaint);
                    canvas.drawText("država rođenja", 900, 605, myPaint);
                    canvas.drawText("državljanstvo", 1100, 605, myPaint);
                    //
                    canvas.drawLine(180, 580, 180, 630, myPaint);
                    canvas.drawLine(380, 580, 380, 630, myPaint);
                    canvas.drawLine(880, 580, 880, 630, myPaint);
                    canvas.drawLine(1080, 580, 1080, 630, myPaint);
                    // dodajemo sadrzaj tabele
                    canvas.drawText(podatak[5], 40, 650, myPaint);
                    canvas.drawText(podatak[6], 200, 650, myPaint);
                    canvas.drawText(podatak[7], 400, 650, myPaint);
                    canvas.drawText(podatak[8], 900, 650, myPaint);
                    canvas.drawText(podatak[9], 1100, 650, myPaint);

                    // dodajemo cetvrti naslov u pdf
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(35);
                    canvas.drawText("Podaci o ocu:", 600, 750, titlePaint);

                    // kreiramo tabelu sa kolonama(ime, prezime,...) u pdf
                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(2);
                    canvas.drawRect(20, 780, 1180, 830, myPaint);
                    //
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("ime", 40, 805, myPaint);
                    canvas.drawText("prezime", 200, 805, myPaint);
                    canvas.drawText("jmbg", 400, 805, myPaint);
                    canvas.drawText("pol", 650, 805, myPaint);
                    canvas.drawText("vreme rođenja", 900, 805, myPaint);
                    canvas.drawText("datum rođenja", 1100, 805, myPaint);
                    //
                    canvas.drawLine(180, 780, 180, 830, myPaint);
                    canvas.drawLine(380, 780, 380, 830, myPaint);
                    canvas.drawLine(630, 780, 630, 830, myPaint);
                    canvas.drawLine(880, 780, 880, 830, myPaint);
                    canvas.drawLine(1080, 780, 1080, 830, myPaint);
                    // dodajemo sadrzaj tabele
                    canvas.drawText(podatak[10], 40, 850, myPaint);
                    canvas.drawText(podatak[11], 200, 850, myPaint);
                    canvas.drawText(podatak[12], 400, 850, myPaint);
                    canvas.drawText(podatak[13], 650, 850, myPaint);
                    canvas.drawText(podatak[14], 900, 850, myPaint);
                    canvas.drawText(podatak[15], 1100, 850, myPaint);

                    // kreiramo tabelu sa kolonama(datum rođenja, mesto rođenja,...) u pdf
                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(2);
                    canvas.drawRect(20, 880, 1180, 930, myPaint);
                    //
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("mesto rođenja", 40, 905, myPaint);
                    canvas.drawText("opština rođenja", 200, 905, myPaint);
                    canvas.drawText("država rođenja", 400, 905, myPaint);
                    canvas.drawText("državljanstvo", 650, 905, myPaint);
                    canvas.drawText("prebivalište", 900, 905, myPaint);
                    canvas.drawText("adresa", 1100, 905, myPaint);
                    //
                    canvas.drawLine(180, 880, 180, 930, myPaint);
                    canvas.drawLine(380, 880, 380, 930, myPaint);
                    canvas.drawLine(630, 880, 630, 930, myPaint);
                    canvas.drawLine(880, 880, 880, 930, myPaint);
                    canvas.drawLine(1080, 880, 1080, 930, myPaint);
                    // dodajemo sadrzaj tabele
                    canvas.drawText(podatak[16], 40, 950, myPaint);
                    canvas.drawText(podatak[17], 200, 950, myPaint);
                    canvas.drawText(podatak[18], 400, 950, myPaint);
                    canvas.drawText(podatak[19], 650, 950, myPaint);
                    canvas.drawText(podatak[20], 900, 950, myPaint);
                    canvas.drawText(podatak[21], 1100, 950, myPaint);

                    // dodajemo peti naslov u pdf
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setTextSize(35);
                    canvas.drawText("Podaci o majki:", 600, 1050, titlePaint);

                    // kreiramo tabelu sa kolonama(ime, prezime,...) u pdf
                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(2);
                    canvas.drawRect(20, 1080, 1180, 1130, myPaint);
                    //
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("ime", 40, 1105, myPaint);
                    canvas.drawText("prezime", 200, 1105, myPaint);
                    canvas.drawText("jmbg", 400, 1105, myPaint);
                    canvas.drawText("pol", 650, 1105, myPaint);
                    canvas.drawText("vreme rođenja", 900, 1105, myPaint);
                    canvas.drawText("datum rođenja", 1100, 1105, myPaint);
                    //
                    canvas.drawLine(180, 1080, 180, 1130, myPaint);
                    canvas.drawLine(380, 1080, 380, 1130, myPaint);
                    canvas.drawLine(630, 1080, 630, 1130, myPaint);
                    canvas.drawLine(880, 1080, 880, 1130, myPaint);
                    canvas.drawLine(1080, 1080, 1080, 1130, myPaint);
                    // dodajemo sadrzaj tabele
                    canvas.drawText(podatak[22], 40, 1150, myPaint);
                    canvas.drawText(podatak[23], 200, 1150, myPaint);
                    canvas.drawText(podatak[24], 400, 1150, myPaint);
                    canvas.drawText(podatak[25], 650, 1150, myPaint);
                    canvas.drawText(podatak[26], 900, 1150, myPaint);
                    canvas.drawText(podatak[27], 1100, 1150, myPaint);

                    // kreiramo tabelu sa kolonama(datum rođenja, mesto rođenja,...) u pdf
                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(2);
                    canvas.drawRect(20, 1180, 1180, 1230, myPaint);
                    //
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("mesto rođenja", 40, 1205, myPaint);
                    canvas.drawText("opština rođenja", 200, 1205, myPaint);
                    canvas.drawText("država rođenja", 400, 1205, myPaint);
                    canvas.drawText("državljanstvo", 650, 1205, myPaint);
                    canvas.drawText("prebivalište", 900, 1205, myPaint);
                    canvas.drawText("adresa", 1100, 1205, myPaint);
                    //
                    canvas.drawLine(180, 1180, 180, 1230, myPaint);
                    canvas.drawLine(380, 1180, 380, 1230, myPaint);
                    canvas.drawLine(630, 1180, 630, 1230, myPaint);
                    canvas.drawLine(880, 1180, 880, 1230, myPaint);
                    canvas.drawLine(1080, 1180, 1080, 1230, myPaint);
                    // dodajemo sadrzaj tabele
                    canvas.drawText(podatak[28], 40, 1250, myPaint);
                    canvas.drawText(podatak[29], 200, 1250, myPaint);
                    canvas.drawText(podatak[30], 400, 1250, myPaint);
                    canvas.drawText(podatak[31], 650, 1250, myPaint);
                    canvas.drawText(podatak[32], 900, 1250, myPaint);
                    canvas.drawText(podatak[33], 1100, 1250, myPaint);

                    // dodajemo datum u pdf file
                    dateFormat = new SimpleDateFormat("dd/MM/yy");
                    canvas.drawText("Datum izdavanja: " + dateFormat.format(dateObj), 20, 1900, myPaint);
                    // dodajemo vreme u pdf file
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    canvas.drawText("Vreme izdavanja: " + dateFormat.format(dateObj), 20, 1950, myPaint);

                    // dodajemo fotografiju u pdf file
                    canvas.drawBitmap(scaledbmp2, 980, 1850, myPaint);

                    myPdfDocument.finishPage(myPage1);

                    File file = new File(Environment.getExternalStorageDirectory(), "/izvod" + podatak[2] + ".pdf");

                    try {
                        myPdfDocument.writeTo(new FileOutputStream(file));
                        Toast.makeText(UserActivity.this, "Uspešno preuzimanje izvoda", Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(UserActivity.this, "Neuspešno preuzimanje izvoda", Toast.LENGTH_SHORT).show();
                    }

                    myPdfDocument.close();
                }
            }
        });

        buttonPrikazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jmbgDeteta = editTextJMBG.getText().toString();

                Cursor cursor = databaseHelper.readOneDataFromMaticnaKnjigaRodjenih(jmbgDeteta, jmbgRoditelja);

                if(cursor.getCount() == 0) {
                    Toast.makeText(UserActivity.this, "Roditelj još uvek nije registrovao ni jedno dete.", Toast.LENGTH_SHORT).show();
                }

                StringBuilder builder = new StringBuilder();

                while (cursor.moveToNext()) {
                    builder.append("imeDeteta: ").append(cursor.getString(0)).append("\n");
                    builder.append("prezimeDeteta: ").append(cursor.getString(1)).append("\n");
                    builder.append("jmbgDeteta: ").append(cursor.getString(2)).append("\n");
                    builder.append("polDeteta: ").append(cursor.getString(3)).append("\n");
                    builder.append("vremeRodjenjaDeteta: ").append(cursor.getString(4)).append("\n");
                    builder.append("datumRodjenjaDeteta: ").append(cursor.getString(5)).append("\n");
                    builder.append("mestoRodjenjaDeteta: ").append(cursor.getString(6)).append("\n");
                    builder.append("opstinaRodjenjaDeteta: ").append(cursor.getString(7)).append("\n");
                    builder.append("drzavaRodjenjaDeteta: ").append(cursor.getString(8)).append("\n");
                    builder.append("drzavljanstvoDeteta: ").append(cursor.getString(9)).append("\n");
                }

                showMessage("Spisak dece", builder.toString());
            }
        });

        buttonPrikaziSve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = databaseHelper.readDataFromMaticnaKnjigaRodjenih(jmbgRoditelja);

                if(cursor.getCount() == 0) {
                    Toast.makeText(UserActivity.this, "Roditelj još uvek nije registrovao ni jedno dete.", Toast.LENGTH_SHORT).show();
                }

                StringBuilder builder = new StringBuilder();

                while (cursor.moveToNext()) {
                    builder.append("imeDeteta: ").append(cursor.getString(0)).append("\n");
                    builder.append("prezimeDeteta: ").append(cursor.getString(1)).append("\n");
                    builder.append("jmbgDeteta: ").append(cursor.getString(2)).append("\n");
                    builder.append("polDeteta: ").append(cursor.getString(3)).append("\n");
                    builder.append("vremeRodjenjaDeteta: ").append(cursor.getString(4)).append("\n");
                    builder.append("datumRodjenjaDeteta: ").append(cursor.getString(5)).append("\n");
                    builder.append("mestoRodjenjaDeteta: ").append(cursor.getString(6)).append("\n");
                    builder.append("opstinaRodjenjaDeteta: ").append(cursor.getString(7)).append("\n");
                    builder.append("drzavaRodjenjaDeteta: ").append(cursor.getString(8)).append("\n");
                    builder.append("drzavljanstvoDeteta: ").append(cursor.getString(9)).append("\n");
                }

                showMessage("Spisak dece", builder.toString());
            }
        });

        buttonLOGOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showMessage(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(UserActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
}