package com.example.bhdi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.content.Intent;

import android.view.View;
import android.widget.Button;

public class Card extends AppCompatActivity {

    CardView blog, map, classify, bmi, qna, about;

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

        blog.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Post_Page.class)));
        //map.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Maps.class)));
        classify.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MachineLearning.class)));
        bmi.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Youtube_Video_Embedded.class)));
        qna.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RatingApp.class)));
        about.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserProfile_Page.class)));

    }
}