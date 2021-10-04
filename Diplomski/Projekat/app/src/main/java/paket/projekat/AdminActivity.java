package paket.projekat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
//import android.view.Window;
//import android.view.WindowManager;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    ActionBar actionBar;
    RecyclerView recyclerViewListOfUsers;
    FloatingActionButton floatingActionButtonAddUser;
    ArrayList<String> firstName, secondName, userName, userID, imageURL;
    CustomAdapter customAdapter;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove action bar and title bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        //
        setContentView(R.layout.activity_admin);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Kontrolna tabla");

        recyclerViewListOfUsers = findViewById(R.id.recyclerViewListOfUsers);
        floatingActionButtonAddUser = findViewById(R.id.floatingActionButtonAddUser);

        firebaseAuth = FirebaseAuth.getInstance();

        dataToList();

        recyclerViewListOfUsers.setLayoutManager(new LinearLayoutManager(AdminActivity.this));

        floatingActionButtonAddUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void dataToList() {
        // U OVOJ METODI POVLACIM PODATKE IZ BAZE, SMESTIM IH U KREIRANE LISTE,
        // KREIRAM KORISNICKE ADAPTERE I DODAJEM IH U RECYCLERVIEW
        firstName = new ArrayList<>();
        secondName = new ArrayList<>();
        userName = new ArrayList<>();
        userID = new ArrayList<>();
        imageURL = new ArrayList<>();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName.clear();
                secondName.clear();
                userName.clear();
                userID.clear();
                imageURL.clear();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData userData = ds.getValue(UserData.class);

                    firstName.add(userData.getFirstName());
                    secondName.add(userData.getSecondName());
                    userName.add(userData.getUserName());
                    userID.add(userData.getUserId());
                    imageURL.add(userData.getImageURL());

                    customAdapter = new CustomAdapter(AdminActivity.this, getApplicationContext(), firstName, secondName, userName, userID, imageURL);
                    recyclerViewListOfUsers.setAdapter(customAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void searchUsers(String query) {
        firstName = new ArrayList<>();
        secondName = new ArrayList<>();
        userName = new ArrayList<>();
        userID = new ArrayList<>();
        imageURL = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName.clear();
                secondName.clear();
                userName.clear();
                userID.clear();
                imageURL.clear();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData userData = ds.getValue(UserData.class);

                    if(userData.getUserName().toLowerCase().contains(query.toLowerCase()) ||
                            userData.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                            userData.getSecondName().toLowerCase().contains(query.toLowerCase())) {
                        firstName.add(userData.getFirstName());
                        secondName.add(userData.getSecondName());
                        userName.add(userData.getUserName());
                        userID.add(userData.getUserId());
                        imageURL.add(userData.getImageURL());
                    }

                    customAdapter = new CustomAdapter(AdminActivity.this, getApplicationContext(), firstName, secondName, userName, userID, imageURL);
                    customAdapter.notifyDataSetChanged();
                    recyclerViewListOfUsers.setAdapter(customAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            recreate();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_add_post).setVisible(false);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.trim().isEmpty()) {
                    searchUsers(query);
                }
                else {
                    dataToList();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.trim().isEmpty()) {
                    searchUsers(newText);
                }
                else {
                    dataToList();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
