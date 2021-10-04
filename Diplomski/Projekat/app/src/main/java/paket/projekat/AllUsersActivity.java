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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllUsersActivity extends AppCompatActivity {
    RecyclerView recyclerViewListOfUsers;
    CustomAdapter6 customAdapter6;
    List<UserData> listOfUsers;
    List<FriendsData> friendsList;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    boolean state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ostali korisnici");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        recyclerViewListOfUsers = findViewById(R.id.recyclerViewListOfUsers3);
        recyclerViewListOfUsers.setHasFixedSize(true);
        recyclerViewListOfUsers.setLayoutManager(new LinearLayoutManager(AllUsersActivity.this));
        friendsList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Friends").child("user_"+firebaseAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendsList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    FriendsData friendsData = ds.getValue(FriendsData.class);
                    friendsList.add(friendsData);
                }
                getAllUsers();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getAllUsers() {
        state = true;
        listOfUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfUsers.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData user = ds.getValue(UserData.class);

                    for(FriendsData friendsData : friendsList) {
                        if(user.getUserId() != null && user.getUserId().equals(friendsData.getId())) {
                            state = false;
                            break;
                        }
                    }

                    if(state == true && !user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        listOfUsers.add(user);
                    }

                    state = true;

                    customAdapter6 = new CustomAdapter6(AllUsersActivity.this, listOfUsers);
                    customAdapter6.notifyDataSetChanged();
                    recyclerViewListOfUsers.setAdapter(customAdapter6);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void searchUsers(String query) {
        state = true;
        listOfUsers = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfUsers.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData userData = ds.getValue(UserData.class);

                    for(FriendsData friendsData : friendsList) {
                        if(userData.getUserId() != null && userData.getUserId().equals(friendsData.getId())) {
                            state = false;
                            break;
                        }
                    }

                    if(state == true && !userData.getUserId().equals(firebaseUser.getUid())) {
                        if(userData.getUserName().toLowerCase().contains(query.toLowerCase()) ||
                                userData.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                                userData.getSecondName().toLowerCase().contains(query.toLowerCase())) {
                            listOfUsers.add(userData);
                        }
                    }

                    state = true;

                    customAdapter6 = new CustomAdapter6(getApplicationContext(), listOfUsers);
                    customAdapter6.notifyDataSetChanged();
                    recyclerViewListOfUsers.setAdapter(customAdapter6);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                if(!query.trim().isEmpty()) {
                    searchUsers(query);
                }
                else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.trim().isEmpty()) {
                    searchUsers(newText);
                }
                else {
                    getAllUsers();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}