package com.example.projlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNotesActivity extends AppCompatActivity {

    EditText title, description;
    Button addNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        title = (EditText) findViewById(R.id.titlee);
        description = (EditText) findViewById(R.id.description);
        addNote = (Button) findViewById(R.id.addNote);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.length() != 0 && description.length() != 0){
                    DbHelper db = new DbHelper(AddNotesActivity.this);
                    db.addNotes(title.getText().toString(), description.getText().toString());

                    Intent intent = new Intent(AddNotesActivity.this, NoteActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }

                else
                {
                    if (title.length() == 0 ) Toast.makeText(AddNotesActivity.this,"Гарчиг заавал өгнүү", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(AddNotesActivity.this,"Ямар нэгэн тэмдэглэл бичнүү", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}