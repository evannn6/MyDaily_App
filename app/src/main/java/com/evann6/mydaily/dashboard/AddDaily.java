package com.evann6.mydaily.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.evann6.mydaily.databinding.AddDailyBinding;
import com.evann6.mydaily.model.ModelDaily;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDaily extends AppCompatActivity {

    AddDailyBinding binding;
    SharedPreferences preferences;
    String uniqueID;
    String keyUnique;
    DatabaseReference reference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddDailyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dialog = new ProgressDialog(this);
        preferences = getSharedPreferences("userdiary", MODE_PRIVATE);
        uniqueID = preferences.getString("unique", "");
        reference = FirebaseDatabase.getInstance().getReference("DataUser").child(uniqueID);

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.title.getText().toString();
                String description = binding.description.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = Calendar.getInstance();
                String fixDate = dateFormat.format(calendar.getTime());

                dialog.setMessage("Please wait...");
                dialog.show();

                keyUnique = reference.push().getKey();
                ModelDaily modelDaily = new ModelDaily(title, description, fixDate, keyUnique);
                reference.child(keyUnique).setValue(modelDaily).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddDaily.this, "Daily Saved", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }
                        else {
                            Toast.makeText(AddDaily.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}