package com.omnicode.baking_app;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.omnicode.baking_app.data.RecipeObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements RecipeDetailFragmentAdapter.StepItemClickListener {

    @BindView(R.id.tv_ingredients) TextView tvIngredients;
    @BindView(R.id.rv_steps) RecyclerView rvSteps;
    @BindView(R.id.tv_recipe_title) TextView tvRecipeTitle;

    private RecipeDetailFragmentAdapter recipeDetailFragmentAdapter;
    private RecipeObject recipeObj;

    //OnStepClickListener mCallback;

    public RecipeDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        bindViews(rootView);

        //DONE: aktuelles RecipeObject wird von RecipeDetail übergeben
        RecipeDetailViewModel viewModel = ViewModelProviders.of(getActivity()).get(RecipeDetailViewModel.class);
        recipeObj = viewModel.getRecipeObj();

        populateUI();

        /*rvSteps.setOnStepClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mCallback.onStepSelected(position);
            }
        });*/
        return rootView;
    }

    private void populateUI() {
        recipeDetailFragmentAdapter = new RecipeDetailFragmentAdapter(getActivity(), recipeObj, this);  //TODO CLickListener

        tvRecipeTitle.setText(recipeObj.getRecipeName());
        tvIngredients.setText(recipeObj.getIngredientsAsString());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvSteps.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        rvSteps.addItemDecoration(decoration);
        rvSteps.setHasFixedSize(true);
        rvSteps.setFocusable(false); //necessary as recyclerview gets focus and fragment will not start on top of page
        rvSteps.setAdapter(recipeDetailFragmentAdapter);
    }
    // *** libraries ***

    private void bindViews(View rootView) {
        // Bind the views
        ButterKnife.bind(this, rootView);
    }

    @Override
    public void onStepItemClickListener(int clickedItemIndex) {
        //Toast.makeText(getActivity(), "Step clicked "+recipeObj.getSteps().get(clickedItemIndex).getShortDesc(), Toast.LENGTH_SHORT).show();
        //TODO: REcipeStepDetail hier öffnen
        //TODO: evtl. auch eine Methode der activity aufrufen mit getActivity().openStepDetail(recipeObj, clickedItemIndex)
        /*Intent intent = new Intent(getActivity(), .class);
        intent.putExtra(getString(R.string.BUNDLE_KEY_RECIPE_OBJ), obj);
        startActivity(intent);*/
        ((RecipeDetail)getActivity()).openStepDetail(recipeObj, clickedItemIndex);
    }

    //CLick handler
   /* public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }*/
}
