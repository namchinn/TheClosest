package com.example.projlogin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }


    public class NotesActivity extends AppCompatActivity{

        RecyclerView recyclerView;
        FloatingActionButton fab;
        Adapter adapter;
        List<Model> noteList;
        DbHelper databaseClass;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_notes);

            recyclerView = findViewById(R.id.recycler_view);
            fab = findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotesActivity.this,AddNotesActivity.class);
                    startActivity(intent);
                }
            });

            noteList = new ArrayList<>();

            databaseClass = new DbHelper(this);

            fetchAllNotesFromDatabase();

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new Adapter(this, NotesActivity.this,noteList);
            recyclerView.setAdapter(adapter);
        }

        void fetchAllNotesFromDatabase(){
            Cursor cursor = databaseClass.readAllData();

            if(cursor.getCount() == 0){
                Toast.makeText(NotesActivity.this, "Хадгалсан тэмдэглал алга байна. Та тэмдэглэл оруулнуу", Toast.LENGTH_LONG).show();
            }
            else
            {
                while (cursor.moveToNext())
                {
                    noteList.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                }
            }

        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.options_menu, menu);
            MenuItem searchItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Тэмдэглэлээ эндээс хайнуу");

            SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            };

            searchView.setOnQueryTextListener(listener);

            return super.onCreateOptionsMenu(menu);

        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            if(item.getItemId() == R.id.delete_all){
                deleteAllNotes();
            }
            return super.onOptionsItemSelected(item);
        }

        private void deleteAllNotes()
        {
            DbHelper db = new DbHelper(NotesActivity.this);
            db.deleteAllNotes();
            recreate();
        }
    }
    }





