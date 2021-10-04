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

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter8 extends RecyclerView.Adapter<CustomAdapter8.MyHolder> {
    Context context;
    List<UserData> listOfUsers;
    List<FriendsData> friendsList;

    DatabaseReference databaseReference;

    public CustomAdapter8(Context context, List<UserData> listOfUsers) {
        this.context = context;
        this.listOfUsers = listOfUsers;
        friendsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String hisUid = listOfUsers.get(position).getUserId();
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

