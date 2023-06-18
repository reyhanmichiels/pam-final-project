package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateActivity extends AppCompatActivity {
    EditText etTitle, etDescription;
    Button btnAdd, btnCancel;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Task task;
    TextView tvName, tvTaskCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        etTitle = findViewById(R.id.et_title_create);
        etDescription = findViewById(R.id.et_description_create);
        btnAdd = findViewById(R.id.btn_add_create);
        btnCancel = findViewById(R.id.btn_cancel_create);
        tvName = findViewById(R.id.tv_name_create);
        tvTaskCount = findViewById(R.id.tv_taskCount_create);

        Intent getIt = getIntent();
        tvName.setText(getIt.getStringExtra("name"));
        tvTaskCount.setText(getIt.getStringExtra("taskCount"));

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        task = new Task();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CreateActivity.this, HomeActivity.class);
                finish();
                startActivity(it);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(CreateActivity.this, HomeActivity.class);
        finish();
        startActivity(it);
    }

    public void addData(){
        if (!validateForm()){
            return;
        }
        String title = etTitle.getText().toString();
        String desc = etDescription.getText().toString();
        Task baru = new Task(title, desc);
        String key = databaseReference.child("tasks").push().getKey();
        baru.setId(key);
        databaseReference.child("tasks").child(firebaseAuth.getUid()).child(key).setValue(baru).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(CreateActivity.this, "Add data", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(CreateActivity.this, HomeActivity.class);
                finish();
                startActivity(it);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateActivity.this, "Failed to Add data", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(CreateActivity.this, HomeActivity.class);
                finish();
                startActivity(it);
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etTitle.getText().toString())) {
            etTitle.setError("Required");
            result = false;
        } else {
            etTitle.setError(null);
        }
        if (TextUtils.isEmpty(etDescription.getText().toString())) {
            etDescription.setError("Required");
            result = false;
        } else {
            etDescription.setError(null);
        }
        return result;
    }
}
