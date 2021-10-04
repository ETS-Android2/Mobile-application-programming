package paket.projekat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyHolder> {
    Context context;
    List<UserData> listOfUsers;

    public CustomAdapter2(Context context, List<UserData> listOfUsers) {
        this.context = context;
        this.listOfUsers = listOfUsers;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String userId = listOfUsers.get(position).getUserId();
        String userImageURL = listOfUsers.get(position).getImageURL();
        String firstName = listOfUsers.get(position).getFirstName();
        String secondName = listOfUsers.get(position).getSecondName();

        holder.firstName.setText(firstName);
        holder.secondName.setText(secondName);

        try {
            Picasso.with(context).load(userImageURL).placeholder(R.drawable.ic_default_person).into(holder.imageViewUserProfilePicture);
        }
        catch (Exception e) {
            Picasso.with(context).load(R.drawable.ic_default_person).into(holder.imageViewUserProfilePicture);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(new String[]{"Pogledaj profil", "Pošalji poruku", "Ukloni iz liste prijatelja"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            // klik na profil
                            Intent intent = new Intent(context, ThereProfileActivity.class);
                            intent.putExtra("userId", userId);
                            context.startActivity(intent);
                        }
                        if(which == 1) {
                            // klin na caskanje
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("userId", userId);
                            context.startActivity(intent);
                        }
                        if(which == 2) {
                            // ukloniti korisnika iz liste prijatelja
                            String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Friends").child("user_" + myUid).child(userId);
                            ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot ds : snapshot.getChildren()) {
                                        ds.getRef().removeValue();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Friends").child("user_" + userId).child(myUid);
                            ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot ds : snapshot.getChildren()) {
                                        ds.getRef().removeValue();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfUsers.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageViewUserProfilePicture;
        TextView firstName, secondName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUserProfilePicture = itemView.findViewById(R.id.imageViewUserProfilePicture);
            firstName = itemView.findViewById(R.id.textViewFirstName2);
            secondName = itemView.findViewById(R.id.textViewSecondName2);
        }
    }
}
