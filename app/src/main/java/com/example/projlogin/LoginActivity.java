package com.example.projlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {

    ImageView ivw;


    Drawable drawable;
    Button login;
    EditText username1, password1;
    DbHelper DB;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ivw = (ImageView) findViewById(R.id.iv1);
        drawable = getResources().getDrawable(R.drawable.logo);
        ivw.setImageDrawable(drawable);
        login = (Button) findViewById(R.id.btnLogin1);
        username1 = (EditText) findViewById(R.id.username1);
        password1 = (EditText) findViewById(R.id.password1);
        DB = new DbHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = username1.getText().toString();
                String pass = password1.getText().toString();

                if(user.length() == 0 || pass.length() == 0)
                    Toast.makeText(LoginActivity.this,"Бүх талбараа бөглөнүү",Toast.LENGTH_LONG).show();
                else{
                    Boolean cheklog = DB.checkusernamepassword(user, pass);
                    if(cheklog){


                                Toast.makeText(LoginActivity.this,"Амжилттай нэвтэрлээ",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }


                    else
                        Toast.makeText(getApplicationContext(),"Нэвтрэх нэр эсвэл нууц үгээ дахин шалгануу",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}