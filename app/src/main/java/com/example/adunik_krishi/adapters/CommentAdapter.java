package com.example.adunik_krishi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adunik_krishi.R;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.Comment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    List<Comment> comments;
    Context context;

    DatabaseReference databaseReference;

    public CommentAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_design_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.commentDetails.setText(comment.getComment());

        databaseReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference("users").child(comment.getPhone()).child("name");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.commentAdmin.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentAdmin,commentDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commentAdmin = itemView.findViewById(R.id.comment_adminTV);
            commentDetails = itemView.findViewById(R.id.comment_detailsTV);
        }
    }
}
