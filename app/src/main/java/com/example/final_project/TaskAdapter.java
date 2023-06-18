package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> listTask;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public TaskAdapter(List<Task> listTask) {
        this.listTask = listTask;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View noteView = inflater.inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(noteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Task task = listTask.get(position);

        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference();

                databaseReference.child("tasks").child(firebaseAuth.getUid()).child(listTask.get(holder.getAdapterPosition()).getId()).removeValue();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent it = new Intent(context, DescriptionActivity.class);
                it.putExtra("task", listTask.get(holder.getAdapterPosition()));
                it.putExtra("taskCount", String.valueOf(listTask.size()) + " task");
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        Button imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title_item);
            tvDescription = itemView.findViewById(R.id.tv_description_item);
            imgDelete = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}
