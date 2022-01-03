package com.example.adunik_krishi.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adunik_krishi.R;
import com.example.adunik_krishi.constant.Constant;
import com.example.adunik_krishi.models.Question;
import com.example.adunik_krishi.screens.ProductDetailsActivity;
import com.example.adunik_krishi.screens.ProfileActivity;
import com.example.adunik_krishi.screens.QuestionDetailsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    List<Question> questions;
    Context context;
    int id;

    public QuestionAdapter(List<Question> questions, Context context, int id) {
        this.questions = questions;
        this.context = context;
        this.id = id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_design_quesntion,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questions.get(position);

        holder.question_IDTV.setText("প্রশ্নঃ "+(questions.size() - position));
        holder.question_TitleTV.setText(question.getqTitle());
        holder.question_DetailsTV.setText(question.getqDetails());
        holder.question_DateTV.setText("প্রকাশের তারিখঃ "+question.getqDate());

        if(id == 0){
            holder.deleteIV.setVisibility(View.GONE);
            holder.editIV.setVisibility(View.GONE);
        }
        else {
            holder.deleteIV.setVisibility(View.VISIBLE);
            holder.editIV.setVisibility(View.VISIBLE);
        }

        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Warning!!!")
                        .setMessage("আপনি কি এই প্রশ্নটি ডিলিট করতে চান ?")
                        .setPositiveButton("হ্যা", (dialog, which) -> {
                            DatabaseReference mPostReference = FirebaseDatabase.getInstance(Constant.DATABASE_REFERENCE).getReference().child("questions").child(question.getqID());
                            mPostReference.removeValue();
                            notifyDataSetChanged();
                            context.startActivity(new Intent(context, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        })
                        .setNegativeButton("না", null)
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuestionDetailsActivity.class);
                intent.putExtra("qID",question.getqID());
                intent.putExtra("phoneNumber",question.getqPhone());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView question_IDTV,question_TitleTV,question_DetailsTV,question_DateTV;
        ImageView editIV,deleteIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question_IDTV = itemView.findViewById(R.id.model_questionIDTV);
            question_TitleTV = itemView.findViewById(R.id.model_question_titleTV);
            question_DetailsTV = itemView.findViewById(R.id.model_question_detailsTV);
            question_DateTV = itemView.findViewById(R.id.model_question_dateTV);
            editIV = itemView.findViewById(R.id.model_editIV);
            deleteIV = itemView.findViewById(R.id.model_deleteIV);
        }
    }
}
