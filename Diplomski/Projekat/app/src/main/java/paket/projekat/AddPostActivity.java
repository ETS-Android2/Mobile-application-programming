package paket.projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    ImageView imageViewPostImage;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    EditText editTextPostTitle, editTextPostDescription;
    Button buttonPostUpload;

    ActionBar actionBar;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        editTextPostTitle = findViewById(R.id.editTextPostTitle);
        editTextPostDescription = findViewById(R.id.editTextPostDescription);
        imageViewPostImage = findViewById(R.id.imageViewPostImage);
        buttonPostUpload = findViewById(R.id.buttonPostUpload);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Dodaj novu objavu");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        progressDialog = new ProgressDialog(this);

        imageViewPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }
            }
        });

        buttonPostUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextPostTitle.getText().toString().trim();
                String description = editTextPostDescription.getText().toString().trim();
                if(title.isEmpty()) {
                    Toast.makeText(AddPostActivity.this,"Unesi naslov objave...",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(description.isEmpty()) {
                    Toast.makeText(AddPostActivity.this,"Unesi opis objave...",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pickedImgUri == null) {
                    uploadData(title, description, "noImage");
                }
                else {
                    uploadData(title, description, pickedImgUri.toString());
                }
            }
        });
    }

    private void uploadData(String title, String description, String uri) {
        progressDialog.setMessage("Objavljivanje posta");
        progressDialog.show();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateAndTime = dtf.format(now);

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/post_" + timeStamp;

        if(!uri.equals("noImage")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);

            storageReference.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // slika je uspesno dodata
                    // sada vracamo url slike
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // uri sadrzi korisnicki url slike
                            String sDownloadUri = uri.toString();

                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("userId", firebaseUser.getUid());
                            hashMap.put("postId", timeStamp);
                            hashMap.put("postTitle", title);
                            hashMap.put("postDescription", description);
                            hashMap.put("postImageUrl", sDownloadUri);
                            hashMap.put("postTime", dateAndTime);
                            hashMap.put("postLikes", "0");
                            hashMap.put("postComments", "0");

                            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(timeStamp);

                            mDatabaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddPostActivity.this,"Post je uspešno objavljen.",Toast.LENGTH_SHORT).show();
                                    resetViews();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddPostActivity.this,"Greška! Post nije objavljen.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddPostActivity.this, "" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {  // objava bez slike
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("userId", firebaseUser.getUid());
            hashMap.put("postId", timeStamp);
            hashMap.put("postTitle", title);
            hashMap.put("postDescription", description);
            hashMap.put("postImageUrl", "noImage");
            hashMap.put("postTime", dateAndTime);
            hashMap.put("postLikes", "0");
            hashMap.put("postComments", "0");

            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(timeStamp);

            mDatabaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(AddPostActivity.this,"Post je uspešno objavljen.",Toast.LENGTH_SHORT).show();
                    resetViews();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddPostActivity.this,"Greška! Post nije objavljen.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void resetViews() {
        imageViewPostImage.setImageURI(null);
        editTextPostTitle.setText("");
        editTextPostDescription.setText("");
        pickedImgUri = null;
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(AddPostActivity.this,"Prihvatite traženu dozvolu",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(AddPostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else
            openGallery();
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            imageViewPostImage.setImageURI(pickedImgUri);
        }
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
            startActivity(new Intent(AddPostActivity.this, MainActivity.class));
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