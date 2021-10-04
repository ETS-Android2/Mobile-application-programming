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

public class FriendRequestActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerViewListOfFriendRequest;
    List<FriendRequestData> friendRequestList;
    List<UserData> userList;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    CustomAdapter7 adapterFriendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Zahtevi za prijateljstvo");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("FriendRequest").child("user_"+firebaseUser.getUid());

        recyclerViewListOfFriendRequest = findViewById(R.id.recyclerViewListOfFriendRequest);
        recyclerViewListOfFriendRequest.setHasFixedSize(true);
        recyclerViewListOfFriendRequest.setLayoutManager(new LinearLayoutManager(FriendRequestActivity.this));

        friendRequestList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendRequestList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    FriendRequestData friendRequestData = ds.getValue(FriendRequestData.class);
                    friendRequestList.add(friendRequestData);
                }
                loadFriendRequest();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadFriendRequest() {
        userList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData user = ds.getValue(UserData.class);
                    for(FriendRequestData friendRequestData : friendRequestList) {
                        if(user.getUserId() != null && user.getUserId().equals(friendRequestData.getId())) {
                            userList.add(user);
                            break;
                        }
                    }

                    adapterFriendRequest = new CustomAdapter7(FriendRequestActivity.this, userList);
                    adapterFriendRequest.notifyDataSetChanged();
                    recyclerViewListOfFriendRequest.setAdapter(adapterFriendRequest);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void searchUsers(String query) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData userData = ds.getValue(UserData.class);

                    for(FriendRequestData friendRequestData : friendRequestList) {
                        if(userData.getUserId() != null && userData.getUserId().equals(friendRequestData.getId())) {
                            if(userData.getUserName().toLowerCase().contains(query.toLowerCase()) ||
                                    userData.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                                    userData.getSecondName().toLowerCase().contains(query.toLowerCase())) {
                                userList.add(userData);
                            }
                            break;
                        }
                    }

                    adapterFriendRequest = new CustomAdapter7(getApplicationContext(), userList);
                    adapterFriendRequest.notifyDataSetChanged();
                    recyclerViewListOfFriendRequest.setAdapter(adapterFriendRequest);
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
                    loadFriendRequest();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.trim().isEmpty()) {
                    searchUsers(newText);
                }
                else {
                    loadFriendRequest();
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