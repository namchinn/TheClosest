package com.example.projlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class TalkActivity extends AppCompatActivity {
    TextView sedevv;
    Button sedevAvah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        sedevv = (TextView) findViewById(R.id.sedev) ;
         sedevAvah = (Button) findViewById(R.id.sedevAvah);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.talk);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.note:
                        Intent intent= new Intent(TalkActivity.this,NoteActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.talk:

                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        
        showRandomSedev();

        sedevAvah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomSedev();
            }
        });




    }

    public void showRandomSedev()
    {
        shuffle();
        sedevv.setText(sedevArray[0].getSedev());
    }

    Sedev s1 = new Sedev("Амралтаа өнгөрөөх хамгийн тохиромжтой арга юу вэ?");
    Sedev s2 = new Sedev("Та юунаас болж хүнд дургүй болдог вэ? Яагаад?");
    Sedev s3 = new Sedev("Өөрийгөө өөртөө итгэлтэй хүн гэж бодож байна уу? Яагаад?");
    Sedev s4 = new Sedev("Хамгийн их сэтгэлээс тань гардаггүй үйл явдал юу вэ?");
    Sedev s5 = new Sedev("Ямар кино хамгийн их сэтгэлд хүрж байсан бэ?");
    Sedev s6 = new Sedev("Ямар улиралд дуртай вэ?");
    Sedev s7 = new Sedev("Хэрвээ орчлон нэг зорилгын төлөө оршдог бол тэр нь юу байж болох вэ?");
    Sedev s8 = new Sedev("Өөрийнхөө ямар зан чанар, онцгой байдлаараа бахархдаг вэ?");
    Sedev s9 = new Sedev("Таны хамгийн сайн хувилбар танаас юугаараа илүү байх байсан бэ?");
    Sedev s10 = new Sedev("Ямар үед хамгийн их өөрөөрөө байж чаддаг вэ?");

    Sedev [] sedevArray = new Sedev[]
            {
                    s1, s2, s3, s4, s5, s6, s7, s8, s9, s10
            };

    public void shuffle()
    {
        Collections.shuffle(Arrays.asList(sedevArray));
    }


    }
