package com.example.bhdi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class Card extends AppCompatActivity {

    CardView blog, map, classify, bmi, qna, about;
    ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //set the title

        blog = findViewById(R.id.blog);
        map = findViewById(R.id.map);
        classify = findViewById(R.id.classify);
        bmi = findViewById(R.id.bmi);
        qna = findViewById(R.id.qna);
        about = findViewById(R.id.about_me);
        btn = findViewById(R.id.lg);

        blog.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Post_Page.class)));
        map.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MapTextData.class)));
        classify.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MachineLearning.class)));
        bmi.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Youtube_Video_Embedded.class)));
        qna.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RatingApp.class)));
        about.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserProfile_Page.class)));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("isLoggedin");
                editor.commit();
                //Toast.makeText(UserProfile_Page.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Card.this, Login_Page.class));
                finish();
            }
        });

    }
}