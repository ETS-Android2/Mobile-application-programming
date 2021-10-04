package paket.projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThereProfileActivity extends AppCompatActivity {
    ImageView imageViewUserImage;
    TextView textViewFirstNameAndLastName, textViewGender, textViewDateOfBirth, textViewNumbOfTelephone;

    RecyclerView recyclerViewListOfPosts;

    List<PostData> postList;
    CustomAdapter4 adapterPosts;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // ovo su podaci korisnika cijem profilu smo pristupili
    String firstName = "", secondName = "", userName = "", dateOfBirth = "", password = "",
            day = "", month = "", year = "", numbOfTelephone = "", gender = "", email = "", hisUid = "", imageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);

        Intent intent = getIntent();
        hisUid = intent.getStringExtra("userId");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageViewUserImage = findViewById(R.id.imageViewUserImage4);
        textViewFirstNameAndLastName = findViewById(R.id.textViewFirstAndLastName3);
        textViewGender = findViewById(R.id.textViewGender3);
        textViewDateOfBirth = findViewById(R.id.textViewDataOfBirth3);
        textViewNumbOfTelephone = findViewById(R.id.textViewNumbOfTelephone3);
        recyclerViewListOfPosts = findViewById(R.id.recyclerViewListOfPosts3);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // Uzimamo podatke onog korisnika cijem profilu smo pristupili
        databaseReference = firebaseDatabase.getReference("Users").child(hisUid);

        postList = new ArrayList<>();

        if(firebaseUser == null) {
            firebaseAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
        }

        initializationHisFields();
        loadHisPosts();
    }

    private void initializationHisFields() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("firstName").getValue(String.class) != null)
                    firstName = snapshot.child("firstName").getValue(String.class);
                if (snapshot.child("secondName").getValue(String.class) != null)
                    secondName = snapshot.child("secondName").getValue(String.class);
                if (snapshot.child("userName").getValue(String.class) != null)
                    userName = snapshot.child("userName").getValue(String.class);
                if (snapshot.child("password").getValue(String.class) != null)
                    password = snapshot.child("password").getValue(String.class);
                if (snapshot.child("numbOfTelephone").getValue(String.class) != null)
                    numbOfTelephone = snapshot.child("numbOfTelephone").getValue(String.class);
                if (snapshot.child("gender").getValue(String.class) != null)
                    gender = snapshot.child("gender").getValue(String.class);
                if (snapshot.child("email").getValue(String.class) != null)
                    email = snapshot.child("email").getValue(String.class);
                if (snapshot.child("dateOfBirth").getValue(String.class) != null)
                    dateOfBirth = snapshot.child("dateOfBirth").getValue(String.class);
                if (snapshot.child("imageURL").getValue(String.class) != null)
                    imageURL = snapshot.child("imageURL").getValue(String.class);
                // Inicijalizacija
                textViewFirstNameAndLastName.setText(firstName + " " + secondName);
                textViewNumbOfTelephone.setText(numbOfTelephone);
                textViewGender.setText(gender);
                textViewDateOfBirth.setText(dateOfBirth);
                // Setovanje imageView-a pomocu klase Picasso koja omogucava ucitavanje slike na osnovu URL
                try {
                    Picasso.with(getApplicationContext()).load(imageURL).into(imageViewUserImage);
                    //imageViewUserImage.setPadding(0, 0, 0, 0);
                    imageViewUserImage.setBackgroundColor(0);
                }
                catch (Exception e) {
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_acc_circle).into(imageViewUserImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadHisPosts() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // prvo prikazujemo novije postove, pa starije
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerViewListOfPosts.setLayoutManager(layoutManager);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = dbRef.orderByChild("userId").equalTo(hisUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PostData myPosts = ds.getValue(PostData.class);

                    postList.add(myPosts);

                    adapterPosts = new CustomAdapter4(ThereProfileActivity.this, postList);

                    recyclerViewListOfPosts.setAdapter(adapterPosts);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereProfileActivity.this,"" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchHisPosts(String searchQuery) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // prvo prikazujemo novije postove, pa starije
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerViewListOfPosts.setLayoutManager(layoutManager);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = dbRef.orderByChild("userId").equalTo(hisUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PostData myPosts = ds.getValue(PostData.class);

                    if(myPosts.getPostTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            myPosts.getPostDescription().toLowerCase().contains(searchQuery.toLowerCase())) {
                        postList.add(myPosts);
                    }

                    adapterPosts = new CustomAdapter4(ThereProfileActivity.this, postList);

                    recyclerViewListOfPosts.setAdapter(adapterPosts);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereProfileActivity.this,"" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_add_post).setVisible(false);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()) {
                    searchHisPosts(query);
                }
                else {
                    loadHisPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    searchHisPosts(newText);
                }
                else {
                    loadHisPosts();
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
            offlineStatus();
            firebaseAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void offlineStatus() {
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        try {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("onlineStatus", "offline");

            mDatabaseReference.updateChildren(hashMap);
        }
        catch (Exception e) {
        }
    }
}