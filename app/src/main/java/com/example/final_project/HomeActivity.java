package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    TextView tvName, tvTaskCount;
    Button btnAddTask;
    RecyclerView rv;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    List<Task> listTask = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);

        tvName = findViewById(R.id.tv_name_create);
        tvTaskCount = findViewById(R.id.tv_taskCount_create);
        btnAddTask = findViewById(R.id.btn_addTask);
        rv = findViewById(R.id.rv);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        tvName.setText(firebaseUser.getDisplayName().toString());

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(HomeActivity.this, CreateActivity.class);
                it.putExtra("name", firebaseUser.getDisplayName().toString());
                it.putExtra("taskCount", String.valueOf(listTask.size()) + " task");
                startActivity(it);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!listTask.isEmpty()) {
                    listTask.clear();

                }

                for (DataSnapshot taskSnapshot : snapshot.child("tasks").child(firebaseAuth.getUid()).getChildren()) {
                    Task note = taskSnapshot.getValue(Task.class);
                    listTask.add(note);
                }

                TaskAdapter adapter = new TaskAdapter(listTask);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                tvTaskCount.setText(String.valueOf(listTask.size()) + " task");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast("Error occurred");
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
