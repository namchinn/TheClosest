 package com.example.projlogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static com.example.projlogin.R.layout.activity_note;

public class NoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Model> noteList;
    DbHelper databaseClass;
    RelativeLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(activity_note);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        coordinatorLayout = (RelativeLayout) findViewById(R.id.layout_notes);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        bottomNavigationView.setSelectedItemId(R.id.note);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.note:

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
                        startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this,AddNotesActivity.class);
                startActivity(intent);
            }
        });

        noteList = new ArrayList<>();

        databaseClass = new DbHelper(this);

        fetchAllNotesFromDatabase();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, NoteActivity.this,noteList);
        recyclerView.setAdapter(adapter);
    }

    void fetchAllNotesFromDatabase(){
        Cursor cursor = databaseClass.readAllData();

        if(cursor.getCount() == 0){
            Toast.makeText(NoteActivity.this, "Хадгалсан тэмдэглал алга байна. Та тэмдэглэл оруулнуу", Toast.LENGTH_LONG).show();
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
        MenuItem searchItem = (MenuItem) menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Тэмдэглэлээ эндээс хайнуу");

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
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
        DbHelper db = new DbHelper(NoteActivity.this);
        db.deleteAllNotes();
        recreate();
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Model item = adapter.getList().get(position);
            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Тэмдэглэл устгагдлаа",Snackbar.LENGTH_LONG).setAction("Устгахаа болих", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(item,position);
                    recyclerView.scrollToPosition(position);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);

                    if(!(event == DISMISS_EVENT_ACTION))
                    {
                        DbHelper db = new DbHelper(NoteActivity.this);
                        db.deleteSingleItem(item.getId());
                    }
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    };
}

