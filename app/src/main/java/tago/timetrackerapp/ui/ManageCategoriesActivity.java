package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tago.timetrackerapp.R;
import tago.timetrackerapp.model.Categories;
import tago.timetrackerapp.model.Category;
import tago.timetrackerapp.ui.managers.LocaleManager;

public class ManageCategoriesActivity extends AppCompatActivity {

    private final static String TAG = "ManageCategories";

    private RecyclerView recyclerView;

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Changes language to match settings, must be done before setting Content view
        LocaleManager.setLocale(this);
        setContentView(R.layout.activity_manage_categories);

        // Setup ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.edit_categories));

        // Floating add category button
        FloatingActionButton fab = findViewById(R.id.add_category);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddCategory();
            }
        });
        // Setup recycle view
        recyclerView = findViewById(R.id.categoryList);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Populate category list
        List<Category> categories = Categories.instance.load();
        if (categories != null)
            recyclerView.setAdapter(new CategoriesAdapter(categories));
    }

    private void startAddCategory() {
        Categories.instance.newCategory();
        Intent intent = new Intent(context, EditCategoryActivity.class);
       // intent.putExtra(EditCategoryActivity.RANDOM_COLOR_KEY, true);
        startActivity(intent);
    }

    public void startEditCategory(Category category) {
        Categories.instance.editCategory(category);
        Intent intent = new Intent(context, EditCategoryActivity.class);
        // passing Category as json
        //intent.putExtra(EditCategoryActivity.CATEGORY_KEY, new Gson().toJson(category));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Back button pressed
                finish();
        }
        return false;
    }

    private class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
        private final List<Category> items;

        public CategoriesAdapter(List<Category> items) {
            this.items = items;
            System.out.println("ITEMS: " + items);
        }

        @NonNull
        @Override
        public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_category, parent, false);
            return new CategoriesAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
            Category data = items.get(position);
            holder.name.setText(data.getName());
            holder.icon.setColorFilter(data.getColor());
            /* TODO: Config activitis */
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
            public TextView name;
            public TextView activities;
            public ImageView icon;

            public ViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                name = itemView.findViewById(R.id.name);
                activities = itemView.findViewById(R.id.name);
                icon = itemView.findViewById(R.id.icon);
            }

            @Override
            public void onClick(View v) {
                Category data = items.get(getAdapterPosition());
                startEditCategory(data);
            }
        }
    }
}