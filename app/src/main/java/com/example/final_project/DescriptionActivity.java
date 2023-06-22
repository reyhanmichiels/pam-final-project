package com.example.final_project;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DescriptionActivity extends AppCompatActivity {
    TextView tvName, tvTaskCount, tvTitle, tvDescription;
    ImageView back;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        tvName = findViewById(R.id.tv_name_description);
        tvTaskCount = findViewById(R.id.tv_taskCount_description);
        tvTitle = findViewById(R.id.tv_title_description);
        tvDescription = findViewById(R.id.tv_description_description);
        back = findViewById(R.id.btn_back_description);

        Intent getIntent = getIntent();
        tvName.setText(firebaseUser.getDisplayName().toString());
        tvTaskCount.setText(getIntent.getStringExtra("taskCount"));

        Task task = getIntent.getParcelableExtra("task");
        tvTitle.setText(task.getTitle());
        tvDescription.setText(task.getDescription());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(DescriptionActivity.this, HomeActivity.class);
                finish();
//                startActivity(it);
            }
        });

    }
}
