package paket.projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActionBar actionBar;
    Button buttonLogin, buttonRegistration, buttonResetPassword;
    EditText editTextEmail, editTextPassword;
    String email = "", password = "";
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    DatabaseReference mDatabaseReference;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove action bar and title bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        //
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);
        editTextEmail = findViewById(R.id.editTextEmail4);
        editTextPassword = findViewById(R.id.editTextPassword);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            //OBRISATI AKO NE TREBA
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mFirebaseUser != null) {
                    Toast.makeText(MainActivity.this, "Ulogovan si", Toast.LENGTH_SHORT).show();
                    //Intent i = new Intent(MainActivity.this, UserDataActivity.class);
                    //startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Uloguj se", Toast.LENGTH_SHORT).show();
                }
            }
        };

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if(email.isEmpty()) {
                    editTextEmail.setError("Popuni polje za email");
                    editTextEmail.requestFocus();
                }
                else if(password.isEmpty()) {
                    editTextPassword.setError("Popuni polje za lozinku");
                    editTextPassword.requestFocus();
                }
                else if(email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Popuni polje za email i polje za lozinku!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Greška pri logovanju, pokušaj opet", Toast.LENGTH_SHORT).show();
                            } else {
                                //admin@gmail.com - password: 1234qwer
                                if (email.equals("admin@gmail.com")) {    // ovde je prijavljen admin
                                    intent = new Intent(MainActivity.this, AdminActivity.class);
                                    //intent.putExtra("sFirebaseAuth", FirebaseAuth.class);
                                    startActivity(intent);
                                } else {  // ovde je prijavljen korisnik
                                    // Azuriranje korisnickog naloga na osnovu userId-a u Firebase Realtime Database
                                    mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mFirebaseAuth.getCurrentUser().getUid());
                                    try {
                                        Map<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("onlineStatus", "online");
                                        hashMap.put("typingTo", "No one");
                                        mDatabaseReference.updateChildren(hashMap);
                                        intent = new Intent(MainActivity.this, DashboardActivity.class);
                                        //intent = new Intent(MainActivity.this, UpdateActivity.class);
                                        //intent.putExtra("userID", task.getResult().getUser().getUid());
                                        //intent.putExtra("userType", "user");
                                        startActivity(intent);
                                    }
                                    catch (Exception e) {
                                        Toast.makeText(MainActivity.this,"Korisnik nije prijavljen.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate using dialog themed context.
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                Context context = dialog.getContext();
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.dialog_forgot_password, null);
                EditText editTextEmail1 = view.findViewById(R.id.editTextEmail1);

                dialog.setTitle("Zaboravio/la si lozinku?");
                dialog.setView(view);
                dialog.setPositiveButton("Restartuj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        forgotPassword(editTextEmail1.getText().toString());
                    }
                }).setNegativeButton("Zatvori", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
    }

    private void forgotPassword(String email) {
         if(DataValidation.checkEmail(email)) {
             mFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful()) {
                         Toast.makeText(MainActivity.this, "Zahtev za promenu lozinka je poslat na mail: '" + email + "'.", Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }
         else {
             Toast.makeText(MainActivity.this, "Greška! Email nije validan.", Toast.LENGTH_SHORT).show();
         }
    }
}