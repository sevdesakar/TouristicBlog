package com.example.tasarim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tasarim.Adapters.CommentAdapter;
import com.example.tasarim.Adapters.anasayfaAdapter;
import com.example.tasarim.Models.yorum_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    EditText edt_yorum_ekle;
    ImageView deneme, geri;
    TextView txt_gonder;

    String postid;

    CommentAdapter adapter;
    RecyclerView recyclerView;
    List<yorum_model> mYorum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        geri=findViewById(R.id.geri);
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        mYorum = new ArrayList<>();


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CommentAdapter(CommentActivity.this, mYorum);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        postid = intent.getStringExtra("postId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yorumlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edt_yorum_ekle = findViewById(R.id.edt_yorumEkle);
        deneme = findViewById(R.id.deneme);
        txt_gonder = findViewById(R.id.txt_gonder);


        txt_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_yorum_ekle.getText().toString().equals("")) {
                    Toast.makeText(CommentActivity.this, "Boş yorum göndeeremezzsiniz", Toast.LENGTH_LONG).show();
                } else {
                    yorumekle();
                }
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("yorumlar").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mYorum.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    yorum_model yorum = snapshot1.getValue(yorum_model.class);
                    mYorum.add(yorum);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void yorumekle() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("yorumlar").child(postid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("yorum", edt_yorum_ekle.getText().toString());
        hashMap.put("gonderen", FirebaseAuth.getInstance().getUid());
        hashMap.put("postid", postid);
        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CommentActivity.this, "Yorum Gönderildi", Toast.LENGTH_LONG).show();
                    edt_yorum_ekle.setText("");
                } else Toast.makeText(CommentActivity.this, "Başarısız ", Toast.LENGTH_LONG).show();
            }
        });

    }


}