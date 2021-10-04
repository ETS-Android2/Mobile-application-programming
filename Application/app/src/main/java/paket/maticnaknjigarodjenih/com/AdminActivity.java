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

public class AdminActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    DatabaseHelper databaseHelper;

    ArrayList<String> imeDeteta, prezimeDeteta, jmbgDeteta;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionAdd);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, RegistrationActivity.class );
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(AdminActivity.this);

        imeDeteta = new ArrayList<>();
        prezimeDeteta = new ArrayList<>();
        jmbgDeteta = new ArrayList<>();

        dataToList();

        customAdapter = new CustomAdapter(AdminActivity.this, this, imeDeteta, prezimeDeteta, jmbgDeteta);

        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminActivity.this));
    }

    void dataToList() {
        Cursor cursor = databaseHelper.readDataFromMaticnaKnjigaRodjenih();

        if (cursor.getCount() == 0){
            Toast.makeText(this, "Nema podataka", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                imeDeteta.add(cursor.getString(0));
                prezimeDeteta.add(cursor.getString(1));
                jmbgDeteta.add(cursor.getString(2));
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