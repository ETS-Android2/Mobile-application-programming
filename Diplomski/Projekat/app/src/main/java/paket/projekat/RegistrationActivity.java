package paket.projekat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
//import android.view.Window;
//import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    ActionBar actionBar;

    ImageView imageViewUserImage;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    Button buttonSignup, buttonLogin;
    EditText editTextFirstName, editTextSecondName, editTextUserName, editTextPassword, editTextConfirmPassword,
            editTextDay, editTextMonth, editTextYear, editTextNumbOfTelephone, editTextGender, editTextEmail;

    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove action bar and title bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Registracija");

        imageViewUserImage = findViewById(R.id.imageViewUserImage);
        buttonSignup = findViewById(R.id.buttonRegistration2);
        buttonLogin = findViewById(R.id.buttonLogin2);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextSecondName = findViewById(R.id.editTextSecondName);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPass);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextDay = findViewById(R.id.editTextDay);
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextYear = findViewById(R.id.editTextYear);
        editTextNumbOfTelephone = findViewById(R.id.editTextNumbOfTelephone);
        editTextGender = findViewById(R.id.editTextGender);
        editTextEmail = findViewById(R.id.editTextEmail2);

        mFirebaseAuth = FirebaseAuth.getInstance();

        imageViewUserImage.setOnClickListener(new View.OnClickListener() {
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

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                finish();
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = editTextFirstName.getText().toString();
                final String secondName = editTextSecondName.getText().toString();
                final String userName = editTextUserName.getText().toString();
                final String password = editTextPassword.getText().toString();
                final String confirmPassword = editTextConfirmPassword.getText().toString();
                final String day = editTextDay.getText().toString();
                final String month = editTextMonth.getText().toString();
                final String year = editTextYear.getText().toString();
                final String dateOfBirth = day + "." + month + "." + year;
                final String numbOfTelephone = editTextNumbOfTelephone.getText().toString();
                final String gender = editTextGender.getText().toString();
                final String email = editTextEmail.getText().toString();
                String errorMessage = "";

                if(firstName.isEmpty() || secondName.isEmpty() || userName.isEmpty() ||
                        password.isEmpty() || confirmPassword.isEmpty() || day.isEmpty() || month.isEmpty() || year.isEmpty() ||
                        numbOfTelephone.isEmpty() || gender.isEmpty() || email.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this,"Greška! Potrebno je da su sva polja popunjena",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(DataValidation.validationUserData(firstName, secondName, userName, password, day, month, year, numbOfTelephone, gender, email) == "") {
                        if(password.equals(confirmPassword)) {
                            createUserAccount(firstName, secondName, userName, password, dateOfBirth, numbOfTelephone, gender, email);
                        }
                        else {
                            Toast.makeText(RegistrationActivity.this, "Greška! Lozinke se ne poklapaju.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        errorMessage = "Greška! " + DataValidation.validationUserData(firstName, secondName, userName, password, day, month, year, numbOfTelephone, gender, email);
                        Toast.makeText(RegistrationActivity.this, errorMessage,Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void createUserAccount(String firstName, String secondName, String userName, String password, String dateOfBirth, String numbOfTelephone, String gender, String email) {
        // Kreiranje korisnickog naloga sa email-om i password-om
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this,"Greška pri registrovanju",Toast.LENGTH_SHORT).show();
                }
                else {  // uspesno kreiran nalog
                    Toast.makeText(RegistrationActivity.this,"Uspešno kreiran nalog",Toast.LENGTH_SHORT).show();

                    // sada cuvamo korisnicke informacije u Firebase Realtime Database
                    FirebaseUser rUser = mFirebaseAuth.getCurrentUser();
                    assert rUser != null;
                    String userId = rUser.getUid();
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId", userId);
                    hashMap.put("firstName", firstName);
                    hashMap.put("secondName", secondName);
                    hashMap.put("gender", gender);
                    hashMap.put("dateOfBirth", dateOfBirth);
                    hashMap.put("userName", userName);
                    hashMap.put("email", email);
                    hashMap.put("password", password);
                    hashMap.put("numbOfTelephone", numbOfTelephone);
                    hashMap.put("imageURL", "default");
                    hashMap.put("onlineStatus", "online");
                    hashMap.put("typingTo", "No one");

                    mDatabaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // korisnicke info su uspesno dodate
                                Toast.makeText(RegistrationActivity.this,"Korisnik je uspešno registrovan.",Toast.LENGTH_SHORT).show();
                                uploadImage(pickedImgUri, userName, userId, rUser);
                            }
                            else {
                                Toast.makeText(RegistrationActivity.this,"Greska! Korisnik nije registrovan.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    private void uploadImage(Uri pickedImgUri, String userName, String userId, FirebaseUser currentUser) {
        // dodajem prvo korisnicku sliku u firebase storage i vracam url
        //menjao
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("profile_images");
        final StorageReference imageFilePath = mStorage.child(userName+".jpg");

        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // slika je uspesno dodata
                // sada vracamo url slike
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri sadrzi korisnicki url slike
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userId)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profleUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Azurira se url slike na Firebase Realtime Database
                                    String sDownloadUri = uri.toString();
                                    Map<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("imageURL", sDownloadUri);
                                    mDatabaseReference.updateChildren(hashMap);

                                    Toast.makeText(RegistrationActivity.this,"Korisnik je uspešno registrovan",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegistrationActivity.this,"Prihvatite traženu dozvolu",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
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
            imageViewUserImage.setImageURI(pickedImgUri);
        }
    }
}
