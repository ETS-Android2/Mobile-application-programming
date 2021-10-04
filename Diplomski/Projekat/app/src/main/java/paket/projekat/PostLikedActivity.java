package paket.projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostLikedActivity extends AppCompatActivity {
    String postId;

    RecyclerView recyclerViewListOfPeopleWhoLikedPost;

    List<UserData> userList;
    CustomAdapter8 adapterUsers;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String myUid;

    boolean state = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_liked);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        recyclerViewListOfPeopleWhoLikedPost = findViewById(R.id.recyclerViewListOfPeopleWhoLikedPost);
        userList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        myUid = firebaseUser.getUid();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Osobe koje su lajkovale");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Likes");
        databaseReference.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String hisUid = ds.getRef().getKey();

                    getUsers(hisUid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getUsers(String hisUid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.orderByChild("userId").equalTo(hisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    UserData userData = ds.getValue(UserData.class);
                    if(userData.getUserId().equals(myUid)) {
                        if(state == true) {
                            userList.add(userData);
                            state = false;
                        }
                    }
                    else if(!userData.getUserId().equals(myUid)) {
                        userList.add(userData);
                    }
                }
                adapterUsers = new CustomAdapter8(PostLikedActivity.this, userList);
                recyclerViewListOfPeopleWhoLikedPost.setAdapter(adapterUsers);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}