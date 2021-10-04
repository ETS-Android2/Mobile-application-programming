package paket.projekat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter4 extends RecyclerView.Adapter<CustomAdapter4.MyHolder> {
    DatabaseReference userRef;
    DatabaseReference likesRef;
    DatabaseReference postsRef;

    Context context;
    List<PostData> postList;

    String myUid;
    boolean mProcessLike = false;

    public CustomAdapter4(Context context, List<PostData> postList) {
        this.context = context;
        this.postList = postList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);

        return new CustomAdapter4.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter4.MyHolder holder, int position) {
        String userId = postList.get(holder.getAdapterPosition()).getUserId();

        setUserData(holder, userId);

        String postId = postList.get(holder.getAdapterPosition()).getPostId();
        String postTime = postList.get(holder.getAdapterPosition()).getPostTime();
        String postTitle = postList.get(holder.getAdapterPosition()).getPostTitle();
        String postDescription = postList.get(holder.getAdapterPosition()).getPostDescription();
        String postImageUrl = postList.get(holder.getAdapterPosition()).getPostImageUrl();
        //Integer postLikes = Integer.parseInt(postList.get(holder.getAdapterPosition()).getPostLikes());
        String postLikes = postList.get(holder.getAdapterPosition()).getPostLikes();
        String postComments = postList.get(holder.getAdapterPosition()).getPostComments();

        holder.textViewPostTime.setText(postTime);
        holder.textViewPostTitle.setText(postTitle);
        holder.textViewPostDescription.setText(postDescription);
        holder.textViewPostLikes.setText(postLikes + " Likes");
        holder.textViewPostComments.setText(postComments + " Comments");
        setLikes(holder, postId);

        if(postImageUrl.equals("noImage")) {
            holder.imageViewPostImage.setVisibility(View.GONE);
        }
        else {
            holder.imageViewPostImage.setVisibility(View.VISIBLE);
            try {
                Picasso.with(context).load(postImageUrl).into(holder.imageViewPostImage);
            } catch (Exception e) {
            }
        }

        if(!userId.equals(myUid)) {
            holder.imageViewMore.setVisibility(View.GONE);
        }

        holder.imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions(holder.imageViewMore, userId, myUid, postId, postImageUrl);
            }
        });

        holder.linearLayoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pLikes = Integer.parseInt(postList.get(holder.getAdapterPosition()).getPostLikes());
                mProcessLike = true;
                String pId = postList.get(holder.getAdapterPosition()).getPostId();
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mProcessLike) {
                            if(snapshot.child(pId).hasChild(myUid)) {
                                postsRef.child(pId).child("postLikes").setValue(""+(pLikes-1));
                                likesRef.child(pId).child(myUid).removeValue();
                                mProcessLike = false;
                            }
                            else {
                                postsRef.child(pId).child("postLikes").setValue(""+(pLikes+1));
                                likesRef.child(pId).child(myUid).setValue("Liked");
                                mProcessLike = false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.linearLayoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("postId", postId);
                intent.putExtra("hisUid", userId);
                context.startActivity(intent);
            }
        });

        holder.linearLayoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ThereProfileActivity.class);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });

        holder.textViewPostLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostLikedActivity.class);
                intent.putExtra("postId", postId);
                context.startActivity(intent);
            }
        });
    }

    private void setUserData(MyHolder holder, String userId) {
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = snapshot.child("firstName").getValue(String.class);
                String secondName = snapshot.child("secondName").getValue(String.class);
                String userProfileImageUrl = snapshot.child("imageURL").getValue(String.class);

                holder.textViewFirstAndLastName.setText(firstName + " " + secondName);
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

    private void setLikes(MyHolder holder, String postKey) {
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postKey).hasChild(myUid)){
                    holder.imageViewLike.setImageResource(R.drawable.ic_like_blue);
                    holder.textViewLike.setTextColor(Color.parseColor("#4B3BA0"));
                }
                else {
                    holder.imageViewLike.setImageResource(R.drawable.ic_like);
                    holder.textViewLike.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMoreOptions(ImageView imageViewMore, String userId, String myUid, String postId, String postImageUrl) {
        PopupMenu popupMenu = new PopupMenu(context, imageViewMore, Gravity.END);

        // svaki korisnik moze da brise samo svoje objaveP
        if(userId.equals(myUid)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Obriši objavu");
            popupMenu.getMenu().add(Menu.NONE, 1, 1, "Izmeni objavu");
        }
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Pregledaj objavu");

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
                    Intent intent = new Intent(context, EditPostActivity.class);
                    intent.putExtra("postId", postId);
                    context.startActivity(intent);
                }
                if(id == 2) {
                    // pregledanje objave
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("postId", postId);
                    intent.putExtra("hisUid", userId);
                    context.startActivity(intent);
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
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Brisanje...");
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("postId").equalTo(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().removeValue();
                    Toast.makeText(context, "Objava je uspešno obrisana.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteWithImage(String postId, String postImageUrl) {
        ProgressDialog progressDialog = new ProgressDialog(context);
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
                            Toast.makeText(context, "Objava je uspešno obrisana.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageViewUserProfilePicture, imageViewPostImage;
        TextView textViewFirstAndLastName, textViewPostTime, textViewPostTitle, textViewPostDescription, textViewPostLikes, textViewPostComments, textViewLike;
        LinearLayout linearLayoutLike, linearLayoutComment, linearLayoutProfile;
        ImageView imageViewMore, imageViewLike;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUserProfilePicture = itemView.findViewById(R.id.imageViewUserProfilePicture4);
            imageViewPostImage = itemView.findViewById(R.id.imageViewPostImage2);
            textViewFirstAndLastName = itemView.findViewById(R.id.textViewFirstAndLastName4);
            textViewPostTime = itemView.findViewById(R.id.textViewPostTime);
            textViewPostTitle = itemView.findViewById(R.id.textViewPostTitle);
            textViewPostDescription = itemView.findViewById(R.id.textViewPostDescription);
            textViewPostLikes = itemView.findViewById(R.id.textViewPostLikes);
            textViewPostComments = itemView.findViewById(R.id.textViewPostComments);
            linearLayoutLike = itemView.findViewById(R.id.linearLayoutLike);
            linearLayoutComment = itemView.findViewById(R.id.linearLayoutComment);
            linearLayoutProfile = itemView.findViewById(R.id.linearLayoutProfile);
            imageViewMore = itemView.findViewById(R.id.moreBtn);
            imageViewLike = itemView.findViewById(R.id.imageViewLike);
            textViewLike = itemView.findViewById(R.id.textViewLike);
        }
    }
}
