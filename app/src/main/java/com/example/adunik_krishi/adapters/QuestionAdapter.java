package com.example.adunik_krishi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adunik_krishi.R;
import com.example.adunik_krishi.models.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    List<Question> questions;
    Context context;

    public QuestionAdapter(List<Question> questions, Context context) {
        this.questions = questions;
        this.context = context;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView question_IDTV,question_TitleTV,question_DetailsTV,question_DateTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question_IDTV = itemView.findViewById(R.id.model_questionIDTV);
            question_TitleTV = itemView.findViewById(R.id.model_question_titleTV);
            question_DetailsTV = itemView.findViewById(R.id.model_question_detailsTV);
            question_DateTV = itemView.findViewById(R.id.model_question_dateTV);
        }
    }
}
