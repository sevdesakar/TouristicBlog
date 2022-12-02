 package com.example.tasarim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
   ImageButton geri;
   TextView tw_register;
   EditText et_sifre, et_mail;
   Button btngiris;
   FirebaseAuth girisyetkisi;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        girisyetkisi=FirebaseAuth.getInstance();
        et_mail=findViewById(R.id.et_mail);
        et_sifre=findViewById(R.id.et_sifre);
        btngiris=findViewById(R.id.btngiris);
        btngiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pdgiris=new ProgressDialog(LoginActivity.this);
                pdgiris.setMessage("Giriş Yapılıyor");
                pdgiris.show();

                String str_emailgiris=et_mail.getText().toString();
                String str_sifregiris=et_sifre.getText().toString();

                if (TextUtils.isEmpty(str_emailgiris) || TextUtils.isEmpty(str_sifregiris))
                {
                    Toast.makeText(LoginActivity.this, "Bütün alanları doldurun.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    girisyetkisi.signInWithEmailAndPassword(str_emailgiris,str_sifregiris)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {
                                        DatabaseReference yolgiris= FirebaseDatabase.getInstance().getReference().child("kullanicilar").child(girisyetkisi.getUid());
                                        yolgiris.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                Intent intent= new Intent(LoginActivity.this,
                                                        MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                                pdgiris.dismiss();
                                            }
                                        });
                                    }
                                    else
                                    {

                                        pdgiris.dismiss();
                                    Toast.makeText(LoginActivity.this, "E mailinizi veya şifrenizi kontrol edin!", Toast.LENGTH_LONG).show();}

                                }
                            });
                }
            }
        });

        tw_register=findViewById(R.id.tw_register);
        tw_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }


        });
        geri=findViewById(R.id.geri);
       geri.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent= new Intent(LoginActivity.this, MainActivity.class);
               startActivity(intent);
           }
       });

           }



     }