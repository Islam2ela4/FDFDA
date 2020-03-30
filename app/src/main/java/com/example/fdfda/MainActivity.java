package com.example.fdfda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.fdfda.Adapters.GridAdapter;
import com.example.fdfda.Model.Card_details;
import com.example.fdfda.Repository.DataAcess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    GridView gridView;
    FloatingActionButton fab;

    GridAdapter adapter;
    ArrayList<Card_details> list;

    // File
    DataAcess acess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // end toolbar

        // dynamic gridview
        gridView = findViewById(R.id.mainGrid);

        acess = new DataAcess(getApplicationContext());
        // start read from file first 3 lines
        list = acess.read_from_file_first_3_lines();

        list.add(0, new Card_details("", "", ""));

        //end  read from file first 3 lines
        Collections.reverse(list);
        adapter = new GridAdapter(MainActivity.this, list);
        gridView.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start create folder and file in it for each conversation
                String[] format = acess.formatting();
                acess.write_in_file_daytimedate(format, 1);
                // end create folder and file in it for each conversation

                list.add(0, new Card_details(format[0], format[1], format[2]));
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
                //end  read from file first 3 lines

                Snackbar.make(view, "Added new one !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    // menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.close:
                finish();
                moveTaskToBack(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
