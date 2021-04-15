package com.example.projlogin;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class CustomCalendarView  extends LinearLayout {
    DbHelper dbHelper;
    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    MyGridAdapter myGridAdapter;

    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        IntializeLayout();
        SetUpCalendar();
        PreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        NextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout, null);
            EditText EventName = addView.findViewById(R.id.eventname);
            TextView EventTime = addView.findViewById(R.id.eventtime);
            ImageButton SetTime = addView.findViewById(R.id.seteventtime);
            Button AddEvent = addView.findViewById(R.id.addevent);
            SetTime.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int hours = calendar.get(Calendar.HOUR_OF_DAY);
                    int minutes = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    Calendar c = Calendar.getInstance();
                                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    c.set(Calendar.MINUTE, minute);
                                    c.setTimeZone(TimeZone.getDefault());
                                    SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                                    String event_Time = hformate.format(c.getTime());
                                    EventTime.setText(event_Time);
                                }
                            },hours,minutes,false);
                    timePickerDialog.show();
                }
            });
            String date = eventDateFormate.format(dates.get(position));
            String month = monthFormat.format(dates.get(position));
            String year = yearFormat.format(dates.get(position));
            AddEvent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    SaveEvent(EventName.getText().toString(),EventTime.getText().toString(), date, month, year);
                    SetUpCalendar();
                    alertDialog.dismiss();

                }
            });
            builder.setView(addView);
            alertDialog = builder.create();
            alertDialog.show();

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormate.format(dates.get(position));


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);
                RecyclerView recyclerView =     showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecylcerAdapter eventRecylcerAdapter = new EventRecylcerAdapter(showView.getContext(), CollectEventByDate(date));
                recyclerView.setAdapter(eventRecylcerAdapter);
                eventRecylcerAdapter.notifyDataSetChanged();
                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();

                /*  TextView Events = showView.findViewById(R.id.eventname);
                TextView Time = showView.findViewById(R.id.eventime);
                TextView Date = showView.findViewById(R.id.eventdate); */


                return true;
            }
        });

    }

    private ArrayList<Events> CollectEventByDate(String date)
    {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.ReadEvents(date, database);
        while (cursor.moveToNext())
        {
            String event = cursor.getString(cursor.getColumnIndex(DbHelper.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DbHelper.TIME));
            String Date = cursor.getString(cursor.getColumnIndex(DbHelper.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DbHelper.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DbHelper.YEAR));
            Events events = new Events(event,time,Date,month,Year);
            arrayList.add(events);


        }
        cursor.close();
        dbHelper.close();
        return arrayList;
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void SaveEvent(String event, String time, String date, String month, String year)
    {
        DbHelper db = new DbHelper(context);
        SQLiteDatabase database = db.getWritableDatabase();
        db.SaveEvent(event, time, date, month, year, database);
        db.close();
        Toast.makeText(context, "Төлөвлөгөө хадгалагдлаа", Toast.LENGTH_SHORT).show();


    }

    private void IntializeLayout()
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        NextButton = view.findViewById(R.id.nextBtn);
        PreviousButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridView);
    }
    private void SetUpCalendar()
    {
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);

        CollectEventsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));


        while (dates.size() < MAX_CALENDAR_DAYS)
        {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        myGridAdapter = new MyGridAdapter(context,dates,calendar, eventsList);
        gridView.setAdapter(myGridAdapter);


    }

    private void CollectEventsPerMonth(String Month, String year)
    {
        eventsList.clear();
        dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.ReadEventsperMonth(Month,year,database);
        while (cursor.moveToNext())
        {
            String event = cursor.getString(cursor.getColumnIndex(DbHelper.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DbHelper.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DbHelper.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DbHelper.YEAR));
            Events events = new Events(event,time,date,month,Year);
            eventsList.add(events);
        }
        cursor.close();
        dbHelper.close();

    }

}
