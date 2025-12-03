package com.example.dzienniktreningowy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TrainingAdapter adapter;
    private ArrayList<Training> trainingList;
    private TrainingDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TrainingDbHelper(this);
        trainingList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TrainingAdapter(trainingList);
        recyclerView.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.buttonAddTraining);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTrainingActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        trainingList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TrainingDbHelper.TABLE_NAME, null, null, null, null, null, TrainingDbHelper.COLUMN_ID + " DESC");

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TrainingDbHelper.COLUMN_NAME));
            int reps = cursor.getInt(cursor.getColumnIndexOrThrow(TrainingDbHelper.COLUMN_REPS));

            trainingList.add(new Training(name, reps));
        }
        cursor.close();

        if (trainingList.isEmpty()){
            Toast.makeText(this, "Brak zapisanych treningów. Dodaj pierwszy!", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }
}