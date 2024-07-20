package com.example.crud5;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edId,edName,edFee,edDate;
    Spinner spinner;
    ListView listView;
    String[] Course;
    String[] course1= {"MCA","MBA","CS","CA"};
    ArrayAdapter adapter;
    String sCourse;
    dbHelper dbhelper;
    ContentValues cv;
    SQLiteDatabase db;

    ArrayList recordList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner1);
        edId = findViewById(R.id.edtid);
        edName = findViewById(R.id.edtname);
        edDate = findViewById(R.id.editTextDate);
        edFee = findViewById(R.id.autoCompleteTextView);
        listView = findViewById(R.id.list1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        display();

        Course = getResources().getStringArray(R.array.course);
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,Course);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,course1);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sCourse = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String record = (String) recordList.get(i);

                String[] cols =record.split(" : ");
                edId.setText(cols[0]);
                edName.setText(cols[1]);
                edDate.setText(cols[3]);
                edFee.setText(cols[4]);
            }
        });
    }
    public void datepick(){
        DatePickerDialog dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                edDate.setText(i2 +"/"+ i1 +"/"+ i);
            }
        }, 2024, 0, 0);
        dp.show();
    }

    public void onInsert(View view) {
        dbhelper = new dbHelper(this);
        db = dbhelper.getWritableDatabase();
        cv = new ContentValues();

        cv.put("id",edId.getText().toString());
        cv.put("name",edName.getText().toString());
        cv.put("course",sCourse);
        cv.put("doj",edDate.getText().toString());
        cv.put("fee",edFee.getText().toString());

        long rows = db.insert("stud",null,cv);
        if (rows > 0){
            display();
            Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdate(View view) {
        dbhelper = new dbHelper(this);
        db = dbhelper.getWritableDatabase();
        cv = new ContentValues();

        cv.put("id",edId.getText().toString());
        cv.put("name",edName.getText().toString());
        cv.put("course",sCourse);
        cv.put("doj",edDate.getText().toString());
        cv.put("fee",edFee.getText().toString());

        String whereClause = "id= ?";
        String[] whereArgs = {edId.getText().toString()};
        int row = db.update("stud",cv,whereClause,whereArgs);
        if (row > 0){
            display();
            Toast.makeText(this, "Update Sucessful", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDelete(View view) {
        dbhelper = new dbHelper(this);
        db = dbhelper.getWritableDatabase();
        cv = new ContentValues();

        String whereClause = "id= ?";
        String[] whereArgs = {edId.getText().toString()};
        int row = db.delete("stud",whereClause,whereArgs);
        if (row > 0){
            display();
            Toast.makeText(this, "Deleted Sucessful", Toast.LENGTH_SHORT).show();
        }
    }

    public void onShow(View view) {
        dbhelper = new dbHelper(this);
        db = dbhelper.getWritableDatabase();
        cv = new ContentValues();

        recordList.clear();

        Cursor cursor = db.rawQuery("select * from stud where name= ?",new String[]{edName.getText().toString()});

        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String course = cursor.getString(cursor.getColumnIndexOrThrow("course"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("doj"));
                String fee = cursor.getString(cursor.getColumnIndexOrThrow("fee"));

                String record = id +" : "+name+" : "+course+" : "+date+" : "+fee;
                recordList.add(record);

            }
        }else {
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,recordList);
        listView.setAdapter(adapter);

    }
    public void display(){
        dbhelper = new dbHelper(this);
        db = dbhelper.getWritableDatabase();
        cv = new ContentValues();
        recordList.clear();
        Cursor cursor = db.rawQuery("select * from stud",null);
        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String course = cursor.getString(cursor.getColumnIndexOrThrow("course"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("doj"));
            String fee = cursor.getString(cursor.getColumnIndexOrThrow("fee"));

            String record = id +" : "+name+" : "+course+" : "+date+" : "+fee;
            recordList.add(record);
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,recordList);
        listView.setAdapter(adapter);
    }

    public void datepick(View view) {
        datepick();
    }

}