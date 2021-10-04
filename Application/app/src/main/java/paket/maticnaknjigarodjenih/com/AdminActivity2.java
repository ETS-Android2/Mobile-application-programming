package paket.maticnaknjigarodjenih.com;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminActivity2 extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    DatabaseHelper databaseHelper;

    ArrayList<String> imeRoditelja, prezimeRoditelja, jmbgRoditelja;

    CustomAdapter2 customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);

        recyclerView = findViewById(R.id.recyclerView22);
        floatingActionButton = findViewById(R.id.floatingActionAdd22);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity2.this, SignUpActivity.class );
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(AdminActivity2.this);

        imeRoditelja = new ArrayList<>();
        prezimeRoditelja = new ArrayList<>();
        jmbgRoditelja = new ArrayList<>();

        dataToList();

        customAdapter = new CustomAdapter2(AdminActivity2.this, this, imeRoditelja, prezimeRoditelja, jmbgRoditelja);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminActivity2.this));
    }

    void dataToList() {
        Cursor cursor = databaseHelper.readDataFromKorisnici();

        if (cursor.getCount() == 0){
            Toast.makeText(this, "Nema podataka", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                imeRoditelja.add(cursor.getString(0));
                prezimeRoditelja.add(cursor.getString(1));
                jmbgRoditelja.add(cursor.getString(2));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            recreate();
        }
    }

}