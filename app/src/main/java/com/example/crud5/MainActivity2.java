package com.example.crud5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {
    EditText edtname,edtpass;
    CheckBox ckBox;
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        edtname = findViewById(R.id.editusename);
        edtpass = findViewById(R.id.editpass);
        ckBox = findViewById(R.id.checkBox1);
        btn = findViewById(R.id.button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    SharedPreferences srd = getSharedPreferences("demo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = srd.edit();
                    editor.putString("name",edtname.getText().toString());
                    editor.putString("pass",edtpass.getText().toString());
                    editor.apply();
                }
            }
        });
        SharedPreferences srd = getSharedPreferences("demo", MODE_PRIVATE);
        String name = srd.getString("name", "");
        String pass = srd.getString("pass", "");
        edtname.setText(name);
        edtpass.setText(pass);

    }



    public void onLogin(View view) {
        if (!ckBox.isChecked()){
            SharedPreferences srd = getSharedPreferences("demo",MODE_PRIVATE);
            SharedPreferences.Editor editor = srd.edit();
            editor.putString("name","");
            editor.putString("pass","");
            editor.apply();
        }
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}