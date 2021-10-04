package paket.projekat;

import android.accessibilityservice.GestureDescription;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomAdapter3 extends RecyclerView.Adapter<CustomAdapter3.MyHolder>{
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ChatData> chatList;
    String imageUrl;

    FirebaseUser firebaseUser;

    ProgressDialog progressDialog;

    String message = "";

    public CustomAdapter3(Context context, List<ChatData> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new MyHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String message = chatList.get(position).getMessage();

        String type = chatList.get(position).getType();
        String dateAndTime = chatList.get(position).getDateAndTime();
        String messageID = chatList.get(position).getMessageID();
        String senderID = chatList.get(position).getSender();
        String dateAndTimeUpdate = chatList.get(position).getDateAndTimeUpdate();
//        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
//        calendar.setTimeInMillis(Long.parseLong(timeStamp));
//        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        if(type.equals("text")) {
            holder.textViewMessage.setVisibility(View.VISIBLE);
            holder.textViewLastUpdate.setVisibility(View.VISIBLE);
            holder.imageViewMessageImage.setVisibility(View.GONE);
            holder.textViewMessage.setText(message);
        }
        else {
            holder.textViewMessage.setVisibility(View.GONE);
            holder.textViewLastUpdate.setVisibility(View.GONE);
            holder.imageViewMessageImage.setVisibility(View.VISIBLE);
            try {
                Picasso.with(context).load(message).into(holder.imageViewMessageImage);
            }
            catch (Exception e) {
                holder.imageViewMessageImage.setImageResource(R.drawable.ic_image_black);
            }
        }

//        holder.textViewTime.setText(dateTime);

        holder.textViewTime.setText(dateAndTime);

        holder.textViewLastUpdate.setText(dateAndTimeUpdate);

        if(holder.textViewLastUpdate.getText() != "") {
            holder.textViewLastUpdate.setText("Uređeno " + dateAndTimeUpdate);
            holder.textViewLastUpdate.setVisibility(View.VISIBLE);
        }

        try {
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_default_person).into(holder.imageViewProfilePicture);
        }
        catch (Exception e) {
            Picasso.with(context).load(R.drawable.ic_default_person).into(holder.imageViewProfilePicture);
        }

        if(position == chatList.size()-1) {
            if(chatList.get(position).isSeen()){
                holder.textViewIsSeen.setText("Seen");
            }
            else {
                holder.textViewIsSeen.setText("Delivered");
            }
        }
        else {
            holder.textViewIsSeen.setVisibility(View.GONE);
        }
        if(senderID.equals(firebaseUser.getUid())) {
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String options[];
                    if(type.equals("text")) {
                        options = new String[]{"Izmeni poruku", "Izbriši poruku"};
                    }
                    else {
                        options = new String[]{"Izbriši poruku"};
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    progressDialog = new ProgressDialog(context);
                    builder.setTitle("Izaberi opciju?");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0 && type.equals("text")) {
                                // Ažuriranje poruke
                                progressDialog.setMessage("Ažuriranje poruke");
                                showMessageDialog("0", messageID);
                            }
                            else if (which == 0 && type.equals("image")) {
                                // Brisanje poruke
                                progressDialog.setMessage("Brisanje poruke");
                                showMessageDialog("1", messageID);
                            }
                            else if (which == 1 && type.equals("text")) {
                                // Brisanje poruke
                                progressDialog.setMessage("Brisanje poruke");
                                showMessageDialog("1", messageID);
                            }
                        }
                    });
                    builder.create().show();
                }
            });
        }
    }

    private void showMessageDialog(String key, String messageID) {
        final String updateUserData[] = {"Ažuriranje poruke", "Brisanje poruke"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(updateUserData[Integer.parseInt(key)]);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        if (key == "0") {    // Azuriranje poruke
            EditText editText = new EditText(context);
            editText.setText(readMessage(messageID));
            linearLayout.addView(editText);
            builder.setView(linearLayout);
            updateMessage(builder, editText, messageID);
        }
        if (key == "1") {    // Brisanje poruke
            deleteMessage(builder, messageID);
        }
        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    progressDialog.dismiss();
                }
        });
        builder.create().show();
    }

    private String readMessage(String messageID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    ChatData chatData = ds.getValue(ChatData.class);
                    if(chatData.getMessageID().equals(messageID)) {
                        message = chatData.getMessage();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return message;
    }

    private void updateMessage(AlertDialog.Builder builder, EditText editText, String messageID) {
        builder.setPositiveButton("Ažuriraj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();

                if (!value.isEmpty()) {
                    progressDialog.show();

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Chats").child(messageID);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("message", value);
                    hashMap.put("dateAndTimeUpdate", dtf.format(now));

                    try {
                        databaseReference.updateChildren(hashMap);
                    } catch (Exception e) {
                        Toast.makeText(context, "Greška! Poruka nije ažurirana.", Toast.LENGTH_SHORT).show();
                    } finally {
                        progressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, "Greška! Popuni polje za poruku.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteMessage(AlertDialog.Builder builder, String messageID) {
        builder.setPositiveButton("Izbriši", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("Chats").child(messageID);

                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                        }
                        else {
                            Toast.makeText(context, "Greška! Poruka nije obrisana.", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfilePicture, imageViewMessageImage;
        TextView textViewMessage, textViewTime, textViewIsSeen, textViewLastUpdate;
        LinearLayout mainLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfilePicture = itemView.findViewById(R.id.imageViewUserProfilePicture3);
            imageViewMessageImage = itemView.findViewById(R.id.imageViewMessage);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewIsSeen = itemView.findViewById(R.id.textViewIsSeen);
            mainLayout = itemView.findViewById(R.id.mainLayout2);
            textViewLastUpdate = itemView.findViewById(R.id.textViewLastUpdate);
        }
    }


}
