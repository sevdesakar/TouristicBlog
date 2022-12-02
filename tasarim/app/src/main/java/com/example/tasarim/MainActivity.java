
package com.example.tasarim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toolbar;


import com.example.tasarim.Adapters.anasayfaAdapter;
import com.example.tasarim.Models.anasayfaModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    ImageButton account;
    EditText searchView;

    String sehirId;

    RecyclerView recyclerView;
    List<anasayfaModel> mAnasayfa;
    anasayfaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Intent intent = getIntent();
        sehirId = intent.getStringExtra("sehirId");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        account=findViewById(R.id.account);
        searchView=findViewById(R.id.searchView);

        recyclerView=findViewById(R.id.recyclerView);
        mAnasayfa=new ArrayList<>();
        adapter = new anasayfaAdapter(MainActivity.this,mAnasayfa);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        /// Searchview
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



        if (FirebaseAuth.getInstance().getCurrentUser() != null){

            account.setImageResource(R.drawable.ic_kapidan_cikis);

        }
        else {
            account.setImageResource(R.drawable.ic_baseline_account_circle_24);
        }


        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    void search(){


        if (searchView.length()>0){

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mAnasayfa.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        anasayfaModel post = snapshot1.getValue(anasayfaModel.class);
                        if (post.getIsim().toLowerCase().contains(searchView.getText().toString().toLowerCase()))
                            mAnasayfa.add(post);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mAnasayfa.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        anasayfaModel post = snapshot1.getValue(anasayfaModel.class);
                        mAnasayfa.add(post);
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

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent= new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_category:
                    Intent intent1= new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_profile:

                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        Intent intent2=new Intent(MainActivity.this,ProfileActivity.class);
                        startActivity(intent2);
                    }
                    else {
                        Intent intent2=new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent2);
                    }
                    /*Intent intent2 = new Intent(MainActivity.this,  ProfileActivity.class);
                    startActivity(intent2);*/
                    break;
            }
            return true;
        }
    };


}