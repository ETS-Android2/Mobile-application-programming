package paket.projekat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter9 extends RecyclerView.Adapter<CustomAdapter9.MyHolder> {
    DatabaseReference userRef;

    Context context;
    List<CommentData> commentList;

    public CustomAdapter9(Context context, List<CommentData> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String userId = commentList.get(position).getUserId();

        setUserData(holder, userId);

        String comment = commentList.get(position).getComment();
        String commentId = commentList.get(position).getCommentId();
        String commentTime = commentList.get(position).getCommentTime();

        holder.textViewComment.setText(comment);
        holder.textViewTime.setText(commentTime);
    }

    private void setUserData(CustomAdapter9.MyHolder holder, String userId) {
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = snapshot.child("firstName").getValue(String.class);
                String secondName = snapshot.child("secondName").getValue(String.class);
                String userProfileImageUrl = snapshot.child("imageURL").getValue(String.class);

                holder.textViewFirstAndSecondName.setText(firstName + " " + secondName);
                try {
                    Picasso.with(context).load(userProfileImageUrl).placeholder(R.drawable.ic_default_person_black).into(holder.imageViewUserProfilePicture);
                }
                catch (Exception e) {
                    Picasso.with(context).load(R.drawable.ic_default_person_black).into(holder.imageViewUserProfilePicture);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageViewUserProfilePicture;
        TextView textViewFirstAndSecondName, textViewComment, textViewTime;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUserProfilePicture = itemView.findViewById(R.id.imageViewUserProfilePicture);
            textViewFirstAndSecondName = itemView.findViewById(R.id.textViewFirstAndLastName);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}
