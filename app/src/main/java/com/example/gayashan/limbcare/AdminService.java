package com.example.gayashan.limbcare;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapters.NoticeAdapter;
import adapters.ServiceAdapter;

public class AdminService extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ServiceCard> serviceCardList;


    DatabaseHelper mHelper;
    TextView topic, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceCardList = new ArrayList<>();

        adapter = new ServiceAdapter(serviceCardList, this);

        recyclerView.setAdapter(adapter);

        FloatingActionButton fbtnservice = findViewById(R.id.fab);

        fbtnservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminService.this, AdminServiceAdd.class));
            }
        });

        mHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor cursor = retrieveAllData();
        while (cursor.moveToNext()) {

            serviceCardList.add(new ServiceCard(cursor.getString(0), cursor.getString(1),cursor.getBlob(2)));
        }
        cursor.close();
    }

    private Cursor retrieveAllData() {
        SQLiteDatabase db = mHelper.getReadableDatabase();


        String[] projection1 = {
                DatabaseHelper.SERVICE_TOPIC,
                DatabaseHelper.SERVICE_DESCRIPTION,
                DatabaseHelper.SERVICE_PHOTO,

        };

        Cursor cursor1 = db.query(
                DatabaseHelper.SERVICE,   // The table to query
                projection1,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        return cursor1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.close();
    }
}
