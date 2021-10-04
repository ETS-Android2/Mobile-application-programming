package paket.projekat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList imeKorisnika, prezimeKorisnika, korisnickoIme, korisnickiID, slikaURL;
    private Activity activity;

    CustomAdapter(Activity activity, Context context, ArrayList imeKorisnika, ArrayList prezimeKorisnika, ArrayList korisnickoIme, ArrayList korisnickiID, ArrayList slikaURL) {
        this.activity = activity;
        this.context = context;
        this.imeKorisnika = imeKorisnika;
        this.prezimeKorisnika = prezimeKorisnika;
        this.korisnickoIme = korisnickoIme;
        this.korisnickiID = korisnickiID;
        this.slikaURL = slikaURL;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_data, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        holder.imeKorisnika.setText(String.valueOf(imeKorisnika.get(holder.getAdapterPosition())));
        holder.prezimeKorisnika.setText(String.valueOf(prezimeKorisnika.get(holder.getAdapterPosition())));
        holder.korisnickoIme.setText(String.valueOf(korisnickoIme.get(holder.getAdapterPosition())));

        //Picasso.with(context).load(String.valueOf(slikaURL.get(holder.getAdapterPosition()))).into(holder.slikaURL);
        try {
            Picasso.with(context).load(String.valueOf(slikaURL.get(holder.getAdapterPosition()))).placeholder(R.drawable.ic_default_person_black).into(holder.slikaURL);
        }
        catch (Exception e) {
            Picasso.with(context).load(R.drawable.ic_default_person_black).into(holder.slikaURL);
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
//              intent.putExtra("imeRoditelja", String.valueOf(imeRoditelja.get(position)));
//              intent.putExtra("prezimeRoditelja", String.valueOf(prezimeRoditelja.get(position)));
                intent.putExtra("userID", String.valueOf(korisnickiID.get(holder.getAdapterPosition())));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return korisnickoIme.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView imeKorisnika, prezimeKorisnika, korisnickoIme;
        ImageView slikaURL;

        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imeKorisnika = itemView.findViewById(R.id.textViewFirstName);
            prezimeKorisnika = itemView.findViewById(R.id.textViewLastName);
            korisnickoIme = itemView.findViewById(R.id.textViewUsername);
            slikaURL = itemView.findViewById(R.id.imageView222);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
