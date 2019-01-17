package com.omnicode.baking_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omnicode.baking_app.data.RecipeObject;
import com.omnicode.baking_app.data.StepObject;

import java.util.List;

public class RecipeDetailFragmentAdapter extends RecyclerView.Adapter<RecipeDetailFragmentAdapter.RecipeDetailViewHolder> {

    private final StepItemClickListener mOnClickListener;

    private RecipeObject obj;
    private Context context;
    private List<StepObject> stepsList;

    public RecipeDetailFragmentAdapter(Context context, RecipeObject obj, StepItemClickListener clickListener) {
        this.context = context;
        mOnClickListener = clickListener;
        this.obj = obj;
        stepsList = obj.getSteps();
    }

    @NonNull
    @Override
    public RecipeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_detail_fragment_step_item; //DONE neues Griditem für Step
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeDetailViewHolder viewHolder = new RecipeDetailViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == stepsList)
            return  0;
        return stepsList.size();
    }

    class RecipeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvStep;

        public RecipeDetailViewHolder(View itemView) {
            super(itemView);
            tvStep = (TextView) itemView.findViewById(R.id.tv_step);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex) {
            StepObject stepObj = stepsList.get(listIndex);
            String stepLabel = String.valueOf(stepObj.getStepsId()) + " - " + stepObj.getShortDesc();
            tvStep.setText(stepLabel);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onStepItemClickListener(clickedPosition);

        }
    }

    //ClickLIstener
    public interface StepItemClickListener {
        void onStepItemClickListener(int clickedItemIndex);
    }

}
