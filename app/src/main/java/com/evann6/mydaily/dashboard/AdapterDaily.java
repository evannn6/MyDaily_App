package com.evann6.mydaily.dashboard;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.evann6.mydaily.databinding.AdapterDailyBinding;
import com.evann6.mydaily.databinding.EditDailyBinding;
import com.evann6.mydaily.model.ModelDaily;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterDaily extends RecyclerView.Adapter<AdapterDaily.AdapterHolder> {

    Context context;
    ArrayList<ModelDaily> arrayList;
    AdapterDailyBinding binding;
    String keyID = "";
    String date;
    Update daily;

    public AdapterDaily(Context context, ArrayList<ModelDaily> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        daily = (Update) context;
    }

    @NonNull
    @Override
    public AdapterDaily.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new AdapterHolder(AdapterDailyBinding.inflate(layoutInflater));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder,int position) {

        ModelDaily modelDaily = arrayList.get(position);
        String title = modelDaily.getTitle();
        String description = modelDaily.getDescription();
        String date = modelDaily.getDate();

        holder.binding.title.setText(title);
        holder.binding.description.setText(description);
        holder.binding.date.setText(date);

        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily.deleteDaily(modelDaily);
            }
        });

        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelDaily modelDaily1 = arrayList.get(holder.getAdapterPosition());
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                EditDailyBinding binding = EditDailyBinding.inflate(layoutInflater);
                editDaily(modelDaily1, binding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    } // Untuk menentukan jumlah data yang akan ditampilkan

    public static class AdapterHolder extends RecyclerView.ViewHolder { // Untuk menghubungkan antara adapter dengan layout
        AdapterDailyBinding binding;
        public AdapterHolder(@NonNull AdapterDailyBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    private void editDaily(ModelDaily md,EditDailyBinding editBinding) {
        Dialog dialog = new Dialog(context);
        View view = editBinding.getRoot();
        dialog.setContentView(view);
        dialog.show();

        editBinding.title.setText(md.getTitle());
        editBinding.description.setText(md.getDescription());
        keyID = md.getUserid();

        editBinding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();

                date = format.format(calendar.getTime());

                String title = editBinding.title.getText().toString();
                String description = editBinding.description.getText().toString();

                ModelDaily mdEdit = new ModelDaily(title, description, date, keyID);
                daily.updateDaily(mdEdit);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("EDIT BERHASIL", "EDIT BERHASIL");
                    }
                }, 200);
            }
        });
    }
    private void deleteDaily(ModelDaily md) {

        daily.deleteDaily(md);
    }


}