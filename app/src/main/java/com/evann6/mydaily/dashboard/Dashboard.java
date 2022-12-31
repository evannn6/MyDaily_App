package com.evann6.mydaily.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import static android.content.Context.MODE_PRIVATE;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.evann6.mydaily.databinding.ActivityDashboardBinding;
import com.evann6.mydaily.model.ModelDaily;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements Update {

    ActivityDashboardBinding binding; // --> untuk mengakses view
    ArrayList<ModelDaily> diaries; //membuat arraylist
    DatabaseReference reference; //firebase
    SharedPreferences preferences; // --> untuk menyimpan data sementara
    SharedPreferences.Editor editor; // --> untuk mengedit data sementara
    LinearLayoutManager linearLayoutManager; // --> untuk membuat layout linear
    AdapterDaily dapterDaily; // --> untuk mengakses adapter
    ModelDaily modelDaily; // --> untuk mengakses model
    ProgressDialog dialog; // --> untuk membuat loading
    String unique; // --> untuk menyimpan data sementara


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences("userdaily", MODE_PRIVATE);
        unique = preferences.getString("unique", "");
        reference = FirebaseDatabase.getInstance().getReference("DataUser").child(unique);
        diaries = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        dialog = new ProgressDialog(Dashboard.this);

        binding.rvDaily.setLayoutManager(linearLayoutManager);
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, AddDaily.class));
            }
        });
        showDaily();
    }

        private void showDaily(){
            dialog.setMessage("Loading...");
            dialog.show();
            diaries.clear();
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()){
                        modelDaily = ds.getValue(ModelDaily.class);
                        diaries.add(modelDaily);
                    }
                    AdapterDaily adapterDaily = new AdapterDaily(Dashboard.this, diaries);
                    binding.rvDaily.setAdapter(adapterDaily);
                    binding.noItem.setVisibility(View.GONE);
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.noItem.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void updateDaily(ModelDaily Daily) {
            reference.child(modelDaily.getUserid()).setValue(Daily).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Dashboard.this, "Update Success", Toast.LENGTH_SHORT).show();
                        showDaily();
                    }
                    else {
                        Toast.makeText(Dashboard.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }

        @Override
        public void deleteDaily(ModelDaily Daily) {
            reference.child(modelDaily.getUserid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Dashboard.this, "Delete Success", Toast.LENGTH_SHORT).show();
                        showDaily();
                    }
                    else {
                        Toast.makeText(Dashboard.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        protected void onRestart() {
            super.onRestart();
            showDaily();
        }
}