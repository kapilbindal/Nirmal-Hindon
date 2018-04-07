package com.example.cheshta.nirmalhindan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LanguageActivity extends AppCompatActivity {
    Button btnEnglish,btnAdmin,btnUser;
    LinearLayout layoutUser,layoutLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        btnEnglish = findViewById(R.id.btnEnglish);
        btnAdmin = findViewById(R.id.btnAdmin);
        btnUser = findViewById(R.id.btnUser);
        layoutLanguage = findViewById(R.id.layoutLanguage);
        layoutUser = findViewById(R.id.layoutUser);

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutLanguage.setVisibility(View.GONE);
                layoutUser.setVisibility(View.VISIBLE);
            }
        });
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LanguageActivity.this,LoginActivity.class));
            }
        });
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LanguageActivity.this,LoginActivity.class));
            }
        });
    }
}
