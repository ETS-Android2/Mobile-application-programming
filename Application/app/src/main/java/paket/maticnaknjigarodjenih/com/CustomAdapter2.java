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

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> {

    private Context context;
    private ArrayList imeRoditelja, prezimeRoditelja, jmbgRoditelja;

    private Activity activity;

    CustomAdapter2(Activity activity, Context context, ArrayList imeRoditelja, ArrayList prezimeRoditelja, ArrayList jmbgRoditelja) {
        this.context = context;
        this.imeRoditelja = imeRoditelja;
        this.prezimeRoditelja = prezimeRoditelja;
        this.jmbgRoditelja = jmbgRoditelja;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CustomAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_data2, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter2.MyViewHolder holder, int position) {
        holder.imeRoditelja.setText(String.valueOf(imeRoditelja.get(position)));
        holder.prezimeRoditelja.setText(String.valueOf(prezimeRoditelja.get(position)));
        holder.jmbgRoditelja.setText(String.valueOf(jmbgRoditelja.get(position)));

        holder.mainLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity2.class);
//                    intent.putExtra("imeRoditelja", String.valueOf(imeRoditelja.get(position)));
//                    intent.putExtra("prezimeRoditelja", String.valueOf(prezimeRoditelja.get(position)));
                intent.putExtra("jmbgRoditelja", String.valueOf(jmbgRoditelja.get(position)));

                activity.startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return jmbgRoditelja.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView imeRoditelja, prezimeRoditelja, jmbgRoditelja;

        LinearLayout mainLayout2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imeRoditelja = itemView.findViewById(R.id.textViewImeRoditelja);
            prezimeRoditelja = itemView.findViewById(R.id.textViewPrezimeRoditelja);
            jmbgRoditelja = itemView.findViewById(R.id.textViewJMBGRoditelja);

            mainLayout2 = itemView.findViewById(R.id.mainLayout2);
        }
    }
}
