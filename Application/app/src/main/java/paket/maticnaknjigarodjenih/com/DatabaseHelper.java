package paket.maticnaknjigarodjenih.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "baza.db";

    public static final String TABLE_NAME = "maticnaKnjigaRodjenih";
    public static final String COL1 = "imeDeteta";
    public static final String COL2 = "prezimeDeteta";
    public static final String COL3 = "jmbgDeteta"; // primarni kljuc
    public static final String COL4 = "polDeteta";
    public static final String COL5 = "vremeRodjenjaDeteta";
    public static final String COL6 = "datumRodjenjaDeteta";
    public static final String COL7 = "mestoRodjenjaDeteta";
    public static final String COL8 = "opstinaRodjenjaDeteta";
    public static final String COL9 = "drzavaRodjenjaDeteta";
    public static final String COL10 = "drzavljanstvoDeteta";
    public static final String COL11 = "imeOca";
    public static final String COL12 = "prezimeOca";
    public static final String COL13 = "jmbgOca";   // jedinstven
    public static final String COL14 = "polOca";
    public static final String COL15 = "vremeRodjenjaOca";
    public static final String COL16 = "datumRodjenjaOca";
    public static final String COL17 = "mestoRodjenjaOca";
    public static final String COL18 = "opstinaRodjenjaOca";
    public static final String COL19 = "drzavaRodjenjaOca";
    public static final String COL20 = "drzavljanstvoOca";
    public static final String COL21 = "prebivalisteOca";
    public static final String COL22 = "adresaOca";
    public static final String COL23 = "imeMajke";
    public static final String COL24 = "prezimeMajke";
    public static final String COL25 = "jmbgMajke"; // jedinstven
    public static final String COL26 = "polMajke";
    public static final String COL27 = "vremeRodjenjaMajke";
    public static final String COL28 = "datumRodjenjaMajke";
    public static final String COL29 = "mestoRodjenjaMajke";
    public static final String COL30 = "opstinaRodjenjaMajke";
    public static final String COL31 = "drzavaRodjenjaMajke";
    public static final String COL32 = "drzavljanstvoMajke";
    public static final String COL33 = "prebivalisteMajke";
    public static final String COL34 = "adresaMajke";

    public static final String TABLE_NAME2 = "korisnici";
    public static final String COL2_1 = "ime";
    public static final String COL2_2 = "prezime";
    public static final String COL2_3 = "jmbg";
    public static final String COL2_4 = "korisnickoIme";    // primarni kljuc
    public static final String COL2_5 = "lozinka";

    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COL1 + " TEXT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT PRIMARY KEY, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT, " +
                COL7 + " TEXT, " +
                COL8 + " TEXT, " +
                COL9 + " TEXT, " +
                COL10 + " TEXT, " +
                COL11 + " TEXT, " +
                COL12 + " TEXT, " +
                COL13 + " TEXT, " +  //UNIQUE
                COL14 + " TEXT, " +
                COL15 + " TEXT, " +
                COL16 + " TEXT, " +
                COL17 + " TEXT, " +
                COL18 + " TEXT, " +
                COL19 + " TEXT, " +
                COL20 + " TEXT, " +
                COL21 + " TEXT, " +
                COL22 + " TEXT, " +
                COL23 + " TEXT, " +
                COL24 + " TEXT, " +
                COL25 + " TEXT, " + //UNIQUE
                COL26 + " TEXT, " +
                COL27 + " TEXT, " +
                COL28 + " TEXT, " +
                COL29 + " TEXT, " +
                COL30 + " TEXT, " +
                COL31 + " TEXT, " +
                COL32 + " TEXT, " +
                COL33 + " TEXT, " +
                COL34 + " TEXT);";

        String query2 = "CREATE TABLE " + TABLE_NAME2 +
                " (" + COL2_1 + " TEXT, " +
                COL2_2 + " TEXT, " +
                COL2_3 + " TEXT, " +
                COL2_4 + " TEXT PRIMARY KEY, " +
                COL2_5 + " TEXT);";

        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    public void addDataInMaticnaKnjigaRodjenih(String [] podatak) {
        String poruka = Check.validateData1(podatak);

        if(poruka == "") {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, podatak[0]);
            contentValues.put(COL2, podatak[1]);
            contentValues.put(COL3, podatak[2]);
            contentValues.put(COL4, podatak[3]);
            contentValues.put(COL5, podatak[4]);
            contentValues.put(COL6, podatak[5]);
            contentValues.put(COL7, podatak[6]);
            contentValues.put(COL8, podatak[7]);
            contentValues.put(COL9, podatak[8]);
            contentValues.put(COL10, podatak[9]);
            contentValues.put(COL11, podatak[10]);
            contentValues.put(COL12, podatak[11]);
            contentValues.put(COL13, podatak[12]);
            contentValues.put(COL14, podatak[13]);
            contentValues.put(COL15, podatak[14]);
            contentValues.put(COL16, podatak[15]);
            contentValues.put(COL17, podatak[16]);
            contentValues.put(COL18, podatak[17]);
            contentValues.put(COL19, podatak[18]);
            contentValues.put(COL20, podatak[19]);
            contentValues.put(COL21, podatak[20]);
            contentValues.put(COL22, podatak[21]);
            contentValues.put(COL23, podatak[22]);
            contentValues.put(COL24, podatak[23]);
            contentValues.put(COL25, podatak[24]);
            contentValues.put(COL26, podatak[25]);
            contentValues.put(COL27, podatak[26]);
            contentValues.put(COL28, podatak[27]);
            contentValues.put(COL29, podatak[28]);
            contentValues.put(COL30, podatak[29]);
            contentValues.put(COL31, podatak[30]);
            contentValues.put(COL32, podatak[31]);
            contentValues.put(COL33, podatak[32]);
            contentValues.put(COL34, podatak[33]);

            long result = db.insert(TABLE_NAME, null, contentValues);

            if (result == -1) {
                Toast.makeText(context, "Neuspešno dodavanje. Pokušaj da uneseš drugi JMBG u polje 'JMBG deteta'.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Uspešno dodavanje", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context, poruka, Toast.LENGTH_SHORT).show();
        }
    }

    public void addDataInKorisnici(String ime, String prezime, String jmbg, String korisnickoIme, String lozinka, String potvrda) {
        String poruka = Check.validateData2(ime, prezime, jmbg, korisnickoIme, lozinka, potvrda);

        if(poruka == "") {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2_1, ime);
            contentValues.put(COL2_2, prezime);
            contentValues.put(COL2_3, jmbg);
            contentValues.put(COL2_4, korisnickoIme);
            contentValues.put(COL2_5, lozinka);

            long result = db.insert(TABLE_NAME2, null, contentValues);

            if (result == -1) {
                Toast.makeText(context, "Neuspešno registrovanje. Pokušaj da uneseš u polje 'Korisničko ime', novo korisničko ime.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Uspešno registrovanje", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context, poruka, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validateUserInKorisnici(String korisnickoIme, String lozinka) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT korisnickoIme FROM korisnici WHERE korisnickoIme = '" + korisnickoIme + "' AND lozinka = '" + lozinka + "'";

        Cursor cursor = null;

        cursor = db.rawQuery(query, null);

        if(cursor != null) {
            if(cursor.getCount() > 0) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public String getJMBGroditeljaFromKorisnici(String korisnickoIme) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT jmbg FROM korisnici WHERE korisnickoIme = '" + korisnickoIme + "'";

        Cursor cursor = null;

        cursor = db.rawQuery(query, null);

        String jmbgRoditelja = "";

        while (cursor.moveToNext()) {
            jmbgRoditelja = cursor.getString(0);
        }
        return jmbgRoditelja;
    }

    public Cursor readOneDataFromMaticnaKnjigaRodjenih(String jmbgDeteta, String jmbgRoditelja) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String query = "SELECT * FROM maticnaKnjigaRodjenih WHERE jmbgDeteta = '" + jmbgDeteta + "' AND (jmbgOca = '" + jmbgRoditelja + "' OR jmbgMajke = '" + jmbgRoditelja + "')";

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readDataFromMaticnaKnjigaRodjenih(String jmbgRoditelja) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String query = "SELECT * FROM maticnaKnjigaRodjenih WHERE jmbgOca = '" + jmbgRoditelja + "' OR jmbgMajke = '" + jmbgRoditelja + "'";

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readDataFromMaticnaKnjigaRodjenih() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String query = "SELECT * FROM " + TABLE_NAME;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readDataFromKorisnici() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String query = "SELECT * FROM " + TABLE_NAME2;

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void updateDataInMaticnaKnjigaRodjenih(String [] podatak) {
        String poruka = Check.validateData1(podatak);

        if(poruka == "") {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL1, podatak[0]);
            contentValues.put(COL2, podatak[1]);
            contentValues.put(COL3, podatak[2]);
            contentValues.put(COL4, podatak[3]);
            contentValues.put(COL5, podatak[4]);
            contentValues.put(COL6, podatak[5]);
            contentValues.put(COL7, podatak[6]);
            contentValues.put(COL8, podatak[7]);
            contentValues.put(COL9, podatak[8]);
            contentValues.put(COL10, podatak[9]);
            contentValues.put(COL11, podatak[10]);
            contentValues.put(COL12, podatak[11]);
            contentValues.put(COL13, podatak[12]);
            contentValues.put(COL14, podatak[13]);
            contentValues.put(COL15, podatak[14]);
            contentValues.put(COL16, podatak[15]);
            contentValues.put(COL17, podatak[16]);
            contentValues.put(COL18, podatak[17]);
            contentValues.put(COL19, podatak[18]);
            contentValues.put(COL20, podatak[19]);
            contentValues.put(COL21, podatak[20]);
            contentValues.put(COL22, podatak[21]);
            contentValues.put(COL23, podatak[22]);
            contentValues.put(COL24, podatak[23]);
            contentValues.put(COL25, podatak[24]);
            contentValues.put(COL26, podatak[25]);
            contentValues.put(COL27, podatak[26]);
            contentValues.put(COL28, podatak[27]);
            contentValues.put(COL29, podatak[28]);
            contentValues.put(COL30, podatak[29]);
            contentValues.put(COL31, podatak[30]);
            contentValues.put(COL32, podatak[31]);
            contentValues.put(COL33, podatak[32]);
            contentValues.put(COL34, podatak[33]);

            long result = db.update(TABLE_NAME, contentValues, "jmbgDeteta = ?", new String[]{podatak[2]});

            if (result == -1) {
                Toast.makeText(context, "Neuspešno ažuriranje. Proveri polje 'JMBG deteta', da li je dobro unet JMBG.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Uspešno ažuriranje.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context, poruka, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDataInKorisnici(String ime, String prezime, String korisnickoIme, String lozinka, String potvrda, String jmbg) {
        String poruka = Check.validateData2(ime, prezime, jmbg, korisnickoIme, lozinka, potvrda);

        if(poruka == "") {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2_1, ime);
            contentValues.put(COL2_2, prezime);
            contentValues.put(COL2_3, jmbg);
            contentValues.put(COL2_4, korisnickoIme);
            contentValues.put(COL2_5, lozinka);

            long result = db.update(TABLE_NAME2, contentValues, "jmbg = ?", new String[]{jmbg});

            if (result == -1) {
                Toast.makeText(context, "Neuspešno ažuriranje. Proveri polje 'JMBG', da li je dobro unet JMBG.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Uspešno ažuriranje.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context, poruka, Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(String jmbgDeteta) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME, "jmbgDeteta = ?", new String[]{jmbgDeteta});

        if (result == -1) {
            Toast.makeText(context, "Neuspesno brisanje. Proveri polje 'JMBG deteta', da li je dobro unet JMBG.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Uspesno obrisano", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteDataFromKorisnici(String jmbgRoditelja) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_NAME2, "jmbg = ?", new String[]{jmbgRoditelja});

        if (result == -1) {
            Toast.makeText(context, "Neuspesno brisanje. Proveri polje 'JMBG', da li je dobro unet JMBG.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Uspesno obrisano", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readOneDataFromMaticnaKnjigaRodjenih(String jmbgDeteta) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String query = "SELECT * FROM maticnaKnjigaRodjenih WHERE jmbgDeteta='" + jmbgDeteta + "'";

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public Cursor readOneDataFromKorisnici(String jmbgRoditelja) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String query = "SELECT * FROM korisnici WHERE jmbg='" + jmbgRoditelja + "'";

        if(db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

}
