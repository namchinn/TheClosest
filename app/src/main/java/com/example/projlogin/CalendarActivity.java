package com.example.projlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalendarActivity extends AppCompatActivity {
    CustomCalendarView customCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        customCalendarView = (CustomCalendarView) findViewById(R.id.custom_calendar_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.calendar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.note:
                        Intent intent= new Intent(CalendarActivity.this,NoteActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.talk:
                        startActivity(new Intent(getApplicationContext(), TalkActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.calendar:

                        return true;
                }
                return false;
            }
        });
    }
}