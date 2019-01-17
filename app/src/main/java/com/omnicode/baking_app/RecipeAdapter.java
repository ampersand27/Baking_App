package com.omnicode.baking_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.omnicode.baking_app.data.RecipeObject;

import java.util.List;

import timber.log.Timber;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final RecipeItemClickListener mOnClickListener;
    private List<RecipeObject> recipeList;

    public RecipeAdapter(RecipeItemClickListener clickListener) {
        mOnClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_grid_item; //im Moment nur ein Frame mit einer TextView
        LayoutInflater inflater =LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Timber.d("#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        //return mNumberMovies;
        if (null == recipeList)
            return 0;
        return recipeList.size();
    }

    /* ClickListener */
    public interface RecipeItemClickListener {
        void onRecipeItemClickListener(int clickedItemIndex);
    }

    /* ***** Viewholder Class ***** */
    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mRecipeTextView;
        ImageView mRecipeImageView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            //mRecipeImageView = (ImageView) itemView.findViewById(R.id.img_recipe);
            mRecipeTextView = itemView.findViewById(R.id.tv_title_recipe);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            RecipeObject obj = recipeList.get(listIndex);
            mRecipeTextView.setText(obj.getRecipeName());
          //DONE Bilder und Videos laden - keine Bilder vorhanden
            /*String path = NetworkUtils.buildUrlLoadImage(obj.getImagePath()).toString();
            if (path!=null && !path.equals("")) {
                Picasso
                        .with(itemView.getContext())
                        .load(path)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(mRecipeImageView);
            }*/
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeItemClickListener(clickedPosition);
        }
    }

    public void setRecipeData(List<RecipeObject> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }
}
