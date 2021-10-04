package paket.projekat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.view.Window;
//import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    ImageView imageViewUserImage;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;

    Button buttonUpdate, buttonDelete;
    EditText editTextFirstName, editTextSecondName, editTextUserName, editTextDay,
            editTextMonth, editTextYear, editTextNumbOfTelephone, editTextGender;
    TextView textViewEmail;

    String firstName = "", secondName = "", userName = "", dateOfBirth = "", password = "",
            day = "", month = "", year = "", numbOfTelephone = "", gender = "", email = "", userId = "", imageURL = "";

    String userType = "";

    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabaseReference;

    FirebaseUser firebaseUserForDeleting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove action bar and title bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        //
        setContentView(R.layout.activity_update);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Izmeni nalog");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        imageViewUserImage = findViewById(R.id.imageViewUserImage2);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        editTextFirstName = findViewById(R.id.editTextFirstName2);
        editTextSecondName = findViewById(R.id.editTextSecondName2);
        editTextUserName = findViewById(R.id.editTextUserName2);
        editTextDay = findViewById(R.id.editTextDay2);
        editTextMonth = findViewById(R.id.editTextMonth2);
        editTextYear = findViewById(R.id.editTextYear2);
        editTextNumbOfTelephone = findViewById(R.id.editTextNumbOfTelephone2);
        editTextGender = findViewById(R.id.editTextGender2);
        textViewEmail = findViewById(R.id.textViewEmail1);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // OVDE PRVO PRIMAM USERID IZ KLASE "CustomAdapter.java"
        // I SETUJEM PROMENLJIVU "userId"
        userId = getIntent().getExtras().getString("userID");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        // Ako koristim userType, dodati slanje i u drugoj aktivnosti koja poziva aktivnost updateActivity
        /*userType = getIntent().getExtras().getString("userType");

        if(userType == "user") {

        }*/

        initializationFields();

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

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = editTextFirstName.getText().toString();
                final String secondName = editTextSecondName.getText().toString();
                final String userName = editTextUserName.getText().toString();
                final String day = editTextDay.getText().toString();
                final String month = editTextMonth.getText().toString();
                final String year = editTextYear.getText().toString();
                final String dateOfBirth = day + "." + month + "." + year;
                final String numbOfTelephone = editTextNumbOfTelephone.getText().toString();
                final String gender = editTextGender.getText().toString();
                final String email = textViewEmail.getText().toString();
                String errorMessage = "";

                if(firstName.isEmpty() || secondName.isEmpty() || userName.isEmpty() ||
                        day.isEmpty() || month.isEmpty() || year.isEmpty() ||
                        numbOfTelephone.isEmpty() || gender.isEmpty() || email.isEmpty()) {
                    Toast.makeText(UpdateActivity.this,"Potrebno je da su sva polja popunjena",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(DataValidation.validationUserData(firstName, secondName, userName, password, day, month, year, numbOfTelephone, gender, email) == "") {
                        updateUserAccount(firstName, secondName, userName, password, dateOfBirth, numbOfTelephone, gender, email);
                    }
                    else {
                        errorMessage = DataValidation.validationUserData(firstName, secondName, userName, password, day, month, year, numbOfTelephone, gender, email);
                        Toast.makeText(UpdateActivity.this, errorMessage,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateActivity.this);
                dialog.setTitle("Da li si siguran da želiš da izbrišeš ovaj nalog?");
                dialog.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // LOGUJEM SE DA BIH VRATIO USER-A KOG ZELIM DA OBRISEM(MORA ZBOG BRISANJA NALOGA) - PRONACI DRUGI NACIN
                        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(UpdateActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Prvo brisem korisnicku sliku iz Firebase Storage
                                    StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(imageURL);
                                    mStorage.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                            else {
                                                Toast.makeText(UpdateActivity.this, "Greška! Korisnička slika nije izbrisana.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    //  Zatim brisem korisnicke podatke iz Firebase Realtime Database
                                    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                                    mDatabaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                            }
                                            else {
                                                Toast.makeText(UpdateActivity.this, "Greška! Korisnički nalog nije izbrisan.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    //  Zatim brisem korisnicki nalog iz Firebase Authentication
                                    firebaseUserForDeleting = mFirebaseAuth.getCurrentUser();
                                    firebaseUserForDeleting.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(UpdateActivity.this, "Korisnički nalog je uspešno izbrisan.", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(UpdateActivity.this, "Greška! Korisnički nalog nije izbrisan.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    adminLogin();
                                }
                                else {
                                    Toast.makeText(UpdateActivity.this, "Greška! Korisnički nalog nije izbrisan.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
    }

    private void initializationFields() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("firstName").getValue(String.class) != null)
                    firstName = snapshot.child("firstName").getValue(String.class);
                if(snapshot.child("secondName").getValue(String.class) != null)
                    secondName = snapshot.child("secondName").getValue(String.class);
                if(snapshot.child("userName").getValue(String.class) != null)
                    userName = snapshot.child("userName").getValue(String.class);
                if(snapshot.child("password").getValue(String.class) != null)
                    password = snapshot.child("password").getValue(String.class);
                if(snapshot.child("numbOfTelephone").getValue(String.class) != null)
                    numbOfTelephone = snapshot.child("numbOfTelephone").getValue(String.class);
                if(snapshot.child("gender").getValue(String.class) != null)
                    gender = snapshot.child("gender").getValue(String.class);
                if(snapshot.child("email").getValue(String.class) != null)
                    email = snapshot.child("email").getValue(String.class);
                if(snapshot.child("dateOfBirth").getValue(String.class) != null)
                    dateOfBirth = snapshot.child("dateOfBirth").getValue(String.class);
                if(snapshot.child("imageURL").getValue(String.class) != null)
                    imageURL = snapshot.child("imageURL").getValue(String.class);
                // MORAM DA PODESIM SLIKU
                //imageViewUserImage.setImageResource(snapshot.child("imageURL").getValue().toString());
                editTextFirstName.setText(firstName);
                editTextSecondName.setText(secondName);
                editTextUserName.setText(userName);
                editTextNumbOfTelephone.setText(numbOfTelephone);
                editTextGender.setText(gender);
                textViewEmail.setText(email);
                // Izdvajam dan, mesec i godinu, zbog polja: editTextDay, editTextMonth i editTextYear
                int state = 0;
                for(int i = 0; i < dateOfBirth.length(); i++) {
                    if(dateOfBirth.charAt(i) != '.') {
                        switch(state) {
                            case 0:
                                day += dateOfBirth.charAt(i);
                                break;
                            case 1:
                                month += dateOfBirth.charAt(i);
                                break;
                            case 2:
                                year += dateOfBirth.charAt(i);
                                break;
                        }
                    }
                    if(dateOfBirth.charAt(i) == '.') { state++; }
                }
                editTextDay.setText(day);
                editTextMonth.setText(month);
                editTextYear.setText(year);
                // Setovanje imageView-a pomocu klase Picasso koja omogucava ucitavanje slike na osnovu URL
                Picasso.with(getApplicationContext()).load(imageURL).into(imageViewUserImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateUserAccount(String firstName, String secondName, String userName, String password, String dateOfBirth, String numbOfTelephone, String gender, String email) {
        // Azuriranje korisnickog naloga na osnovu userId-a u Firebase Realtime Database
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        try {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("firstName", firstName);
            hashMap.put("secondName", secondName);
            hashMap.put("gender", gender);
            hashMap.put("dateOfBirth", dateOfBirth);
            hashMap.put("userName", userName);
            // EMAIL I PASSWORD SE NE AZURIRAJU!
            //hashMap.put("email", email);
            //hashMap.put("password", password);
            hashMap.put("numbOfTelephone", numbOfTelephone);
            mDatabaseReference.updateChildren(hashMap);
            Toast.makeText(UpdateActivity.this,"Korisničke informacije su uspešno ažurirane.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(UpdateActivity.this,"Greška! Korisničke informacije nisu ažurirane." + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        try {
            uploadImage(pickedImgUri, userName, userId);
        }
        catch (Exception e) {
            Toast.makeText(UpdateActivity.this,"Korisnička slika nije ažurirana.", Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(UpdateActivity.this, AdminActivity.class));
        finish();
    }

    private void uploadImage(Uri pickedImgUri, String userName, String userId) {
        // dodajem prvo korisnicku sliku u firebase storage i vracam url
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
                        try {
                            // Azurira se url slike na Firebase Realtime Database
                            String sDownloadUri = uri.toString();
                            Map<String, Object> hashMap = new HashMap<>();
                            hashMap.put("imageURL", sDownloadUri);
                            mDatabaseReference.updateChildren(hashMap);
                            Toast.makeText(UpdateActivity.this, "Korisnička slika je uspešno ažurirana.", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Toast.makeText(UpdateActivity.this,"Greška! Korisnička slika nije ažurirana." + e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(UpdateActivity.this,"Prihvatite traženu dozvolu",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
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

    void adminLogin() {
        mFirebaseAuth.signInWithEmailAndPassword("admin@gmail.com", "1234qwer").addOnCompleteListener(UpdateActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(UpdateActivity.this, AdminActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(UpdateActivity.this,"Greška! Admin nije ulogovan.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
