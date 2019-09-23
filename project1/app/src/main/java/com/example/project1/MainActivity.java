package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project1.R;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    Button record;
    Button result;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record = (Button)findViewById(R.id.record);
        result = (Button)findViewById(R.id.result);
        icon = (ImageView)findViewById(R.id.icon);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, RResultActivity.class);
                startActivity(intent);

            }
        });

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"그냥 이런 기능 까먹지 않게 넣어나 두자",Toast.LENGTH_SHORT).show();
            }
        });

    }
}