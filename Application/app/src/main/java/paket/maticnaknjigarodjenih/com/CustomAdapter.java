package paket.maticnaknjigarodjenih.com;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList imeDeteta, prezimeDeteta, jmbgDeteta;

    private Activity activity;

    CustomAdapter(Activity activity, Context context, ArrayList imeDeteta, ArrayList prezimeDeteta, ArrayList jmbgDeteta) {
        this.context = context;
        this.imeDeteta = imeDeteta;
        this.prezimeDeteta = prezimeDeteta;
        this.jmbgDeteta = jmbgDeteta;
        this.activity = activity;
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
        holder.imeDeteta.setText(String.valueOf(imeDeteta.get(holder.getAdapterPosition())));
        holder.prezimeDeteta.setText(String.valueOf(prezimeDeteta.get(holder.getAdapterPosition())));
        holder.jmbgDeteta.setText(String.valueOf(jmbgDeteta.get(holder.getAdapterPosition())));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("imeDeteta", String.valueOf(imeDeteta.get(holder.getAdapterPosition())));
                intent.putExtra("prezimeDeteta", String.valueOf(prezimeDeteta.get(holder.getAdapterPosition())));
                intent.putExtra("jmbgDeteta", String.valueOf(jmbgDeteta.get(holder.getAdapterPosition())));

                activity.startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return jmbgDeteta.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView imeDeteta, prezimeDeteta, jmbgDeteta;

        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imeDeteta = itemView.findViewById(R.id.textViewIme);
            prezimeDeteta = itemView.findViewById(R.id.textViewPrezime);
            jmbgDeteta = itemView.findViewById(R.id.textViewJMBG);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
