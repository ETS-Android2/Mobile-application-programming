package paket.projekat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CustomAdapter5 extends RecyclerView.Adapter<CustomAdapter5.MyHolder> {
    Context context;
    List<UserData> userList;
    private HashMap<String, String> lastMessageMap;

    public CustomAdapter5(Context context, List<UserData> userList) {
        this.context = context;
        this.userList = userList;
        this.lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String hisUid = userList.get(position).getUserId();
        String hisUserProfilePicture = userList.get(position).getImageURL();
        String hisOnlineStatus = userList.get(position).getOnlineStatus();
        String hisFirstname = userList.get(position).getFirstName();
        String hisSecondname = userList.get(position).getSecondName();
        String lastMessage = lastMessageMap.get(hisUid);

        holder.textViewFirstAndLastName.setText(hisFirstname + " " + hisSecondname);

        if(lastMessage == null || lastMessage.equals("default")) {
            holder.textViewLastMessage.setVisibility(View.GONE);
        }
        else {
            holder.textViewLastMessage.setVisibility(View.VISIBLE);
            holder.textViewLastMessage.setText(lastMessage);
        }

        try {
            Picasso.with(context).load(hisUserProfilePicture).into(holder.imageViewUserProfilePicture);
        }
        catch (Exception e) {
            Picasso.with(context).load(R.drawable.ic_default_person_black).into(holder.imageViewUserProfilePicture);
        }

        if(hisOnlineStatus.equals("online")) {
            holder.imageViewOnlineStatus.setImageResource(R.drawable.circle_online);
        }
        else {
            holder.imageViewOnlineStatus.setImageResource(R.drawable.circle_offline);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userId", hisUid);  // hisUid
                context.startActivity(intent);
            }
        });
    }

    public void setLastMessageMap(String userId, String lastMessage) {
        lastMessageMap.put(userId, lastMessage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageViewUserProfilePicture, imageViewOnlineStatus;
        TextView textViewFirstAndLastName, textViewLastMessage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUserProfilePicture = itemView.findViewById(R.id.imageViewUserProfilePicture5);
            imageViewOnlineStatus = itemView.findViewById(R.id.imageViewOnlineStatus);
            textViewFirstAndLastName = itemView.findViewById(R.id.textViewFirstAndLastName5);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
        }
    }
}
