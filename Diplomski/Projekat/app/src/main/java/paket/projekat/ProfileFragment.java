package paket.projekat;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    ImageView imageViewUserImage;
    TextView textViewFirstNameAndLastName, textViewGender, textViewDateOfBirth, textViewNumbOfTelephone;
    FloatingActionButton floatingActionButtonEditUser;
    ProgressDialog progressDialog;
    View view;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String firstName = "", secondName = "", userName = "", dateOfBirth = "", password = "",
            day = "", month = "", year = "", numbOfTelephone = "", gender = "", email = "", userId = "", imageURL = "";

    Uri pickedImgUri;

    static int PReqCode = 1;
    static int REQUESCODE = 1;

    RecyclerView recyclerViewListOfPosts;

    List<PostData> postList;
    CustomAdapter4 adapterPosts;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        imageViewUserImage = view.findViewById(R.id.imageViewUserImage3);
        textViewFirstNameAndLastName = view.findViewById(R.id.textViewFirstAndLastName);
        textViewGender = view.findViewById(R.id.textViewGender2);
        textViewDateOfBirth = view.findViewById(R.id.textViewDataOfBirth2);
        textViewNumbOfTelephone = view.findViewById(R.id.textViewNumbOfTelephone2);
        floatingActionButtonEditUser = view.findViewById(R.id.floatingActionButtonEditUser);
        recyclerViewListOfPosts = view.findViewById(R.id.recyclerViewListOfPosts2);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(firebaseUser.getUid());

        postList = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());

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

        floatingActionButtonEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        if(firebaseUser == null) {
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

        initializationFields();
        //loadMyPosts();

        return view;
    }

    private void initializationFields() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userId").getValue(String.class) != null)
                    userId = snapshot.child("userId").getValue(String.class);
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
                    Picasso.with(getContext()).load(imageURL).into(imageViewUserImage);
                    //imageViewUserImage.setPadding(0, 0, 0, 0);
                    imageViewUserImage.setBackgroundColor(0);
                }
                catch (Exception e) {
                    Picasso.with(getContext()).load(R.drawable.ic_acc_circle).into(imageViewUserImage);
                }
                loadMyPosts();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadMyPosts() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // prvo prikazujemo novije postove, pa starije
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerViewListOfPosts.setLayoutManager(layoutManager);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = dbRef.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PostData myPosts = ds.getValue(PostData.class);

                    postList.add(myPosts);

                    adapterPosts = new CustomAdapter4(getActivity(), postList);

                    recyclerViewListOfPosts.setAdapter(adapterPosts);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchMyPosts(String searchQuery) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // prvo prikazujemo novije postove, pa starije
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerViewListOfPosts.setLayoutManager(layoutManager);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = dbRef.orderByChild("userId").equalTo(userId);

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

                    adapterPosts = new CustomAdapter4(getActivity(), postList);

                    recyclerViewListOfPosts.setAdapter(adapterPosts);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getContext(),"Prihvatite traženu dozvolu",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            imageViewUserImage.setImageURI(pickedImgUri);

            try {
                uploadImage(pickedImgUri, userName);
            }
            catch (Exception e) {
                Toast.makeText(getContext(), "Greška! Korisnička slika nije ažurirana.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(Uri pickedImgUri, String userName) {
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
                            databaseReference.updateChildren(hashMap);
                            Toast.makeText(getContext(), "Korisnička slika je uspešno ažurirana.", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Toast.makeText(getContext(),"Greška! Korisnička slika nije ažurirana." + e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showEditProfileDialog() {
        String options[] = {"Izmeni ime", "Izmeni prezime", "Izmeni pol", "Izmeni datum rođenja", "Izmeni broj telefona"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Izaberi opciju");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    // Promena imena
                    progressDialog.setMessage("Ažuriranje imena");
                    showUserDataDialog("0");
                }
                else if(which == 1) {
                    // Promena prezimena
                    progressDialog.setMessage("Ažuriranje prezimena");
                    showUserDataDialog("1");
                }
                else if(which == 2) {
                    // Promena pola
                    progressDialog.setMessage("Ažuriranje pola");
                    showUserDataDialog("2");
                }
                else if(which == 3) {
                    // Promena datuma rodjenja
                    progressDialog.setMessage("Ažuriranje datuma rodjenja");
                    showUserDataDialog("3");
                }
                else if(which == 4) {
                    // Promena broja telefona
                    progressDialog.setMessage("Ažuriranje broja telefona");
                    showUserDataDialog("4");
                }
            }
        });
        builder.create().show();
    }

    private void showUserDataDialog(String key) {
        final String updateUserData[] = {"Ažuriranje imena", "Ažuriranje prezimena", "Ažuriranje pola", "Ažuriranje datuma rođenja", "Ažuriranje broja telefona"};
        final String enterUserData[] = {"Unesi ime", "Unesi prezime", "Unesi pol", "Unesi datum rođenja", "Unesi broj telefona"};
        final String hashKeys[] = {"firstName", "secondName", "gender", "dateOfBirth", "numbOfTelephone"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(updateUserData[Integer.parseInt(key)]);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        EditText editText = new EditText(getActivity());
        editText.setHint(enterUserData[Integer.parseInt(key)]);

        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Ažuriraj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();
                boolean state = false;
                String message = "", day = "", month = "", year = "";

                if(!value.isEmpty()) {
                    progressDialog.show();

                    Map<String, Object> hashMap = new HashMap<>();

                    switch (key) {
                        case "0":
                            if(DataValidation.checkFirstName(value)) {
                                state = true;
                            }
                            else {
                                message = "Ime mora da sadrži samo slova.\n";
                            }
                            break;
                        case "1":
                            if(DataValidation.checkSecondName(value)) {
                                state = true;
                            }
                            else {
                                message = "Prezime mora da sadrži samo slova.\n";
                            }
                            break;
                        case "2":
                            if(DataValidation.checkGender(value)) {
                                state = true;
                            }
                            else {
                                message = "Pol mora da sadrži samo m ili z/ž.\n";
                            }
                            break;
                        case "3":
                            int k = 0;
                            for(int i = 0; i < value.length(); i++) {
                                if(value.charAt(i) != '.') {
                                    switch(k) {
                                        case 0:
                                            day += value.charAt(i);
                                            break;
                                        case 1:
                                            month += value.charAt(i);
                                            break;
                                        case 2:
                                            year += value.charAt(i);
                                            break;
                                    }
                                }
                                if(value.charAt(i) == '.') { k++; }
                            }
                            if(DataValidation.checkDay(day) && DataValidation.checkMonth(month) && DataValidation.checkYear(year)) {
                                state = true;
                            }
                            else {
                                message += "\nDan mora biti u opsegu od 1 do 31.\n";
                                message += "Mesec mora biti u opsegu od 1 do 12.\n";
                                message += "Godina mora biti u opsegu od 1900 do "+ Calendar.getInstance().get(Calendar.YEAR) +".\n";
                            }
                            break;
                        case "4":
                            if(DataValidation.checkNumberOfTelephone(value)) {
                                state = true;
                            }
                            else {
                                message = "Telefon mora da sadrži samo cifre i da ima minimalnu dužinu od 6 cifara.\n";
                            }
                            break;
                    }

                    if(state == true) {
                        hashMap.put(hashKeys[Integer.parseInt(key)], value);

                        try {
                            databaseReference.updateChildren(hashMap);
                            Toast.makeText(getContext(), "Korisničke informacije su uspešno ažurirane.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Greška! Korisničke informacije nisu ažurirane.", Toast.LENGTH_SHORT).show();
                        } finally {
                            progressDialog.dismiss();
                        }
                    }
                    else {
                        Toast.makeText(view.getContext(),"Greška! " + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else {
                    Toast.makeText(view.getContext(),"Greška! Popuni polje.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);    // za prikazivanje meni opcija u fragmentu

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.isEmpty()) {
                    searchMyPosts(query);
                }
                else {
                    loadMyPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    searchMyPosts(newText);
                }
                else {
                    loadMyPosts();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout) {
            offlineStatus();
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        if(id == R.id.action_add_post) {
            startActivity(new Intent(getActivity(), AddPostActivity.class));
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