package com.al264087.chillyrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.al264087.chillyrace.Level1.Level1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Level1Game(View view)
    {
        Intent intent = new Intent(this, Level1.class);
        startActivity(intent);
    }

}
