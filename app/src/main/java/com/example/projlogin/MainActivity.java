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



public class MainActivity extends AppCompatActivity {

    EditText username , password, repassword;
    ImageView ivw;
    Drawable drawable;
    Button lgn, lgp;
    DbHelper DB;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        ivw = (ImageView) findViewById(R.id.iv);
        drawable = getResources().getDrawable(R.drawable.logo);
        ivw.setImageDrawable(drawable);
        lgn = (Button) findViewById(R.id.btnLogin);
        lgp = (Button) findViewById(R.id.btnLogup);

        DB = new DbHelper(this);


        lgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(user.length() == 0 || pass.length()== 0 || repass.length()== 0) {
                    Toast.makeText(MainActivity.this,"Бүх талбарыг бөглөнүү!",Toast.LENGTH_LONG).show();
                }
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(!checkuser){
                            Boolean insert = DB.insertData(user, pass);
                                if(insert){

                                            Toast.makeText(MainActivity.this,"Та амжилттай бүртгүүллээ",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }


                                else Toast.makeText(getApplicationContext(),"Бүртгэл амжилтгүй боллоо",Toast.LENGTH_LONG).show();


                        }
                        else
                            Toast.makeText(MainActivity.this,"Хэрэглэгчийн нэр бүртгэлтэй байна",Toast.LENGTH_LONG).show();
                    }else
                    Toast.makeText(MainActivity.this,"Нууц үгээ зөв давтаж оруулнуу",Toast.LENGTH_LONG).show();


                }


            }
        });

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });

    }


}