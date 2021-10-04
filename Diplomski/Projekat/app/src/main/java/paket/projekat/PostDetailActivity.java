package paket.projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    ImageView imageViewUserProfilePicture, imageViewPostImage, buttonMore, imageViewPI, imageViewLike;
    TextView textViewFirstAndSecondName, textViewPostTime, textViewPostTitle, textViewPostDesctiption, textViewPostLikes, textViewPostComments, textViewLike;
    EditText editTextComment;
    ImageButton buttonSend;
    LinearLayout profileLayout, llLike;
    RecyclerView recyclerViewListOfComments;

    List<CommentData> commentList;
    CustomAdapter9 customAdapter9;

    String myUid, myFirstname, mySecondname, myProfilePictureUrl;
    String hisUid, hisFirstname, hisSecondname, hisProfilePictureUrl;
    String postId, postTime, postTitle, postDescription, postImageUrl, postLikes, postComments;

    boolean mProcessComment = false;
    boolean mProcessLike = false;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        hisUid = intent.getStringExtra("hisUid");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Objava");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageViewUserProfilePicture = findViewById(R.id.imageViewUserProfilePicture4);
        imageViewPostImage = findViewById(R.id.imageViewPostImage2);
        imageViewLike = findViewById(R.id.imageViewLike);
        textViewLike = findViewById(R.id.textViewLike);
        textViewFirstAndSecondName = findViewById(R.id.textViewFirstAndLastName4);
        textViewPostTime = findViewById(R.id.textViewPostTime);
        textViewPostTitle = findViewById(R.id.textViewPostTitle);
        textViewPostDesctiption = findViewById(R.id.textViewPostDescription);
        textViewPostLikes = findViewById(R.id.textViewPostLikes);
        textViewPostComments = findViewById(R.id.textViewPostComments);
        buttonMore = findViewById(R.id.moreBtn);
        llLike = findViewById(R.id.linearLayoutLike);
        profileLayout = findViewById(R.id.linearLayoutProfile);
        editTextComment = findViewById(R.id.editTextComment);
        buttonSend = findViewById(R.id.sendBtn);
        imageViewPI = findViewById(R.id.imageViewPI);
        recyclerViewListOfComments = findViewById(R.id.recyclerViewListOfComments);

        firebaseAuth = FirebaseAuth.getInstance();
        myUid = firebaseAuth.getCurrentUser().getUid();

        checkUserStatus();

        loadPostData(postId);
        setLikes(postId);

        loadHisUserData(hisUid);
        loadMyUserData(myUid);

        loadComments();

        if(!hisUid.equals(myUid)) {
            buttonMore.setVisibility(View.GONE);
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost();
            }
        });

        buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMoreOptions();
            }
        });
    }

    private void loadPostData(String postId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = databaseReference.orderByChild("postId").equalTo(postId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    postTime = "" + ds.child("postTime").getValue();
                    postTitle = "" + ds.child("postTitle").getValue();
                    postDescription = "" + ds.child("postDescription").getValue();
                    postImageUrl = "" + ds.child("postImageUrl").getValue();
                    postLikes = "" + ds.child("postLikes").getValue();
                    postComments = "" + ds.child("postComments").getValue();
                    hisUid = "" + ds.child("userId").getValue();
                }

                textViewPostTitle.setText(postTitle);
                textViewPostDesctiption.setText(postDescription);
                textViewPostLikes.setText(postLikes + " Likes");
                textViewPostComments.setText(postComments + " Comments");
                textViewPostTime.setText(postTime);

                if(postImageUrl.equals("noImage")) {
                    imageViewPostImage.setVisibility(View.GONE);
                }
                else {
                    imageViewPostImage.setVisibility(View.VISIBLE);
                    try {
                        Picasso.with(getApplicationContext()).load(postImageUrl).into(imageViewPostImage);
                    } catch (Exception e) {
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setLikes(String postId) {
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postId).hasChild(myUid)){
                    imageViewLike.setImageResource(R.drawable.ic_like_blue);
                    textViewLike.setTextColor(Color.parseColor("#4B3BA0"));
                }
                else {
                    imageViewLike.setImageResource(R.drawable.ic_like);
                    textViewLike.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadHisUserData(String hisUid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("userId").equalTo(hisUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    hisFirstname = "" + ds.child("firstName").getValue();
                    hisSecondname = "" + ds.child("secondName").getValue();
                    hisProfilePictureUrl = "" + ds.child("imageURL").getValue();
                }

                textViewFirstAndSecondName.setText(hisFirstname + " " + hisSecondname);

                try {
                    Picasso.with(getApplicationContext()).load(hisProfilePictureUrl).into(imageViewUserProfilePicture);
                }
                catch (Exception e) {
                    imageViewUserProfilePicture.setImageResource(R.drawable.ic_default_person_black);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadMyUserData(String myUid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("userId").equalTo(myUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    myFirstname = "" + ds.child("firstName").getValue();
                    mySecondname = "" + ds.child("secondName").getValue();
                    myProfilePictureUrl = "" + ds.child("imageURL").getValue();
                }

                try {
                    Picasso.with(getApplicationContext()).load(myProfilePictureUrl).into(imageViewPI);
                }
                catch (Exception e) {
                    imageViewPI.setImageResource(R.drawable.ic_default_person_black);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void postComment() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Dodavanje komentara...");

        String comment = editTextComment.getText().toString().trim();

        if(comment.isEmpty()) {
            Toast.makeText(this, "Greška! Komentar je prazan.", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAndTime = dtf.format(now);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("commentId", timeStamp);
        hashMap.put("comment", comment);
        hashMap.put("commentTime", dateAndTime);
        hashMap.put("userId", myUid);

        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(PostDetailActivity.this, "Komentar je dodat.", Toast.LENGTH_SHORT).show();
                editTextComment.setText("");
                updateCommentCount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PostDetailActivity.this, "Greška! Komentar nije dodat.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void likePost() {
        mProcessLike = true;
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mProcessLike) {
                    if(snapshot.child(postId).hasChild(myUid)) {
                        postsRef.child(postId).child("postLikes").setValue(""+(Integer.parseInt(postLikes)-1));
                        likesRef.child(postId).child(myUid).removeValue();
                        mProcessLike = false;
                    }
                    else {
                        postsRef.child(postId).child("postLikes").setValue(""+(Integer.parseInt(postLikes)+1));
                        likesRef.child(postId).child(myUid).setValue("Liked");
                        mProcessLike = false;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateCommentCount() {
        mProcessComment = true;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mProcessComment) {
                    String comments = "" + snapshot.child("postComments").getValue();
                    int newCommentVal = Integer.parseInt(comments) + 1;
                    ref.child("postComments").setValue("" + newCommentVal);
                    mProcessComment = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadComments() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewListOfComments.setLayoutManager(layoutManager);

        commentList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    CommentData commentData = ds.getValue(CommentData.class);

                    commentList.add(commentData);

                    customAdapter9 = new CustomAdapter9(getApplicationContext(), commentList);
                    recyclerViewListOfComments.setAdapter(customAdapter9);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMoreOptions() {
        PopupMenu popupMenu = new PopupMenu(this, buttonMore, Gravity.END);

        // svaki korisnik moze da brise samo svoje objave
        if(hisUid.equals(myUid)) {
            buttonMore.setVisibility(View.VISIBLE);
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Obriši objavu");
            popupMenu.getMenu().add(Menu.NONE, 1, 1, "Izmeni objavu");
        }
        else {
            buttonMore.setVisibility(View.GONE);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == 0) {
                    // brisanje objave
                    beginDelete(postId, postImageUrl);
                }
                if(id == 1) {
                    // azuriranje objave
                    Intent intent = new Intent(PostDetailActivity.this, EditPostActivity.class);
                    intent.putExtra("postId", postId);
                    startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void beginDelete(String postId, String postImageUrl) {
        if(postImageUrl.equals("noImage")) {
            deleteWithoutImage(postId);
        }
        else {
            deleteWithImage(postId, postImageUrl);
        }
    }

    private void deleteWithoutImage(String postId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Brisanje...");
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("postId").equalTo(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().removeValue();
                    Toast.makeText(PostDetailActivity.this, "Objava je uspešno obrisana.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteWithImage(String postId, String postImageUrl) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Brisanje...");
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(postImageUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("postId").equalTo(postId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                            Toast.makeText(PostDetailActivity.this, "Objava je uspešno obrisana.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PostDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        menu.findItem(R.id.action_search).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout) {
            offlineStatus();
            firebaseAuth.signOut();
            checkUserStatus();
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