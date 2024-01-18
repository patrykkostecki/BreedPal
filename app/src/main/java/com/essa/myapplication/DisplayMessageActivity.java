package com.essa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView textView = findViewById(R.id.textView);
        textView.setText(message);

    }

    public void returnToMain(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void goToApp(View view){

        Intent intent = new Intent(this, ModelDisplay.class);
        startActivity(intent);

    }
}