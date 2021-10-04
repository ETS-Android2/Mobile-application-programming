package paket.projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    ImageView imageViewPostImage;
    EditText editTextPostTitle, editTextPostDescription;
    Button buttonPostUpdate;

    String postId, postTitle, postDescription, postImageUrl;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        postId = getIntent().getStringExtra("postId");

        imageViewPostImage = findViewById(R.id.imageViewPostImage2);
        editTextPostTitle = findViewById(R.id.editTextPostTitle2);
        editTextPostDescription = findViewById(R.id.editTextPostDescription2);
        buttonPostUpdate = findViewById(R.id.buttonPostUpdate);

        initializationFields(postId);

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

        buttonPostUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String postTitle = editTextPostTitle.getText().toString();
                final String postDescription = editTextPostDescription.getText().toString();

                if(postTitle.isEmpty() || postDescription.isEmpty()) {
                    Toast.makeText(EditPostActivity.this,"Potrebno je da su sva polja popunjena.",Toast.LENGTH_SHORT).show();
                }
                else {
                    updatePost(postId, postTitle, postDescription, postImageUrl);
                }
            }
        });
    }

    private void initializationFields(String postId) {
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("postTitle").getValue(String.class) != null)
                    postTitle = snapshot.child("postTitle").getValue(String.class);
                if(snapshot.child("postDescription").getValue(String.class) != null)
                    postDescription = snapshot.child("postDescription").getValue(String.class);
                if(snapshot.child("postImageUrl").getValue(String.class) != null)
                    postImageUrl = snapshot.child("postImageUrl").getValue(String.class);

                editTextPostTitle.setText(postTitle);
                editTextPostDescription.setText(postDescription);
                // Setovanje imageView-a pomocu klase Picasso koja omogucava ucitavanje slike na osnovu URL
                if(!postImageUrl.equals("noImage")) {
                    Picasso.with(getApplicationContext()).load(postImageUrl).into(imageViewPostImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updatePost(String postId, String postTitle, String postDescription, String postImageUrl) {
        progressDialog.setMessage("Ažuriranje posta...");
        progressDialog.show();

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        try {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("postTitle", postTitle);
            hashMap.put("postDescription", postDescription);

            mDatabaseReference.updateChildren(hashMap);
        }
        catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(EditPostActivity.this,"Greška! Post nije ažuriran. " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(pickedImgUri != null) {
            updateImage(pickedImgUri, postId);
        }
    }

    private void updateImage(Uri pickedImgUri, String postId) {
        try {
            // dodajem prvo korisnicku sliku u firebase storage i vracam url
            StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("Posts");
            final StorageReference imageFilePath = mStorage.child("post_" + postId);

            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // slika je uspesno dodata
                    // sada vracamo url slike
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // uri sadrzi korisnicki url slike
                            try {
                                // Azurira se url slike na Firebase Realtime Database
                                DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
                                String sDownloadUri = uri.toString();
                                Map<String, Object> hashMap = new HashMap<>();
                                hashMap.put("postImageUrl", sDownloadUri);
                                mDatabaseReference.updateChildren(hashMap);
                                Toast.makeText(EditPostActivity.this, "Slika posta je uspešno ažurirana.", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e) {
                                Toast.makeText(EditPostActivity.this, "Greška! Slika posta nije ažurirana(#2).", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            });
        }
        catch (Exception e) {
            Toast.makeText(EditPostActivity.this, "Greška! Slika posta nije ažurirana(#1).", Toast.LENGTH_SHORT).show();
        }
        finally {
            progressDialog.dismiss();
        }
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(EditPostActivity.this,"Prihvatite traženu dozvolu",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(EditPostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
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
}