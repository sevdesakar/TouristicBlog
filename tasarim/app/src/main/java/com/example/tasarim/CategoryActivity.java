package com.example.tasarim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.tasarim.Adapters.anasayfaAdapter;
import com.example.tasarim.Adapters.categoryAdapter;
import com.example.tasarim.Models.anasayfaModel;
import com.example.tasarim.Models.categoryModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<categoryModel> mCategory;
    categoryAdapter adapter;

    EditText searchView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        searchView=findViewById(R.id.searchView);
        search();
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView=findViewById(R.id.recyclerView);
        mCategory=new ArrayList<>();
        adapter = new categoryAdapter(CategoryActivity.this, mCategory);

        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);



    }


    void search(){


        if (searchView.length()>0){

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("sehir");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mCategory.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        categoryModel post = snapshot1.getValue(categoryModel.class);
                        if (post.getSehir().toLowerCase().contains(searchView.getText().toString().toLowerCase()))
                            mCategory.add(post);
                        mCategory.add(post);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        }
        else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("sehir");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mCategory.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        categoryModel post = snapshot1.getValue(categoryModel.class);
                        mCategory.add(post);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }



    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent= new Intent(CategoryActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_category:
                    Intent intent1= new Intent(CategoryActivity.this, CategoryActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_profile:
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        Intent intent2=new Intent(CategoryActivity.this,ProfileActivity.class);
                        startActivity(intent2);
                    }
                    else {
                        Intent intent2=new Intent(CategoryActivity.this,LoginActivity.class);
                        startActivity(intent2);
                    }
                    break;
            }
            return true;
        }
    };
}