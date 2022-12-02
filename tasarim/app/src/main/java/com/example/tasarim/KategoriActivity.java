package com.example.tasarim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

public class KategoriActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<anasayfaModel> mAnasayfa;
    anasayfaAdapter adapter;

    String sehirId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView=findViewById(R.id.recyclerView);
        mAnasayfa=new ArrayList<>();
        adapter=new anasayfaAdapter(KategoriActivity.this, mAnasayfa);

        recyclerView.setLayoutManager(new LinearLayoutManager(KategoriActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        sehirId = intent.getStringExtra("sehirId");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mAnasayfa.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    if (snapshot1.child("sehirId").getValue(String.class).equals(sehirId)){
                        anasayfaModel post = snapshot1.getValue(anasayfaModel.class);
                        mAnasayfa.add(post);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent= new Intent(KategoriActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_category:
                    Intent intent1= new Intent(KategoriActivity.this, CategoryActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.navigation_profile:
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        Intent intent2=new Intent(KategoriActivity.this,ProfileActivity.class);
                        startActivity(intent2);
                    }
                    else {
                        Intent intent2=new Intent(KategoriActivity.this,LoginActivity.class);
                        startActivity(intent2);
                    }
                    break;
            }
            return true;
        }
    };
}


 /*recyclerView=findViewById(R.id.recyclerView);
        mAnasayfa=new ArrayList<>();
        adapter = new anasayfaAdapter(KategoriActivity.this,mAnasayfa);

        recyclerView.setLayoutManager(new LinearLayoutManager(KategoriActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        sehirId = intent.getStringExtra("sehirId");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("post").child(sehirId);
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
        }); */
