package com.example.fdfda;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fdfda.Adapters.RecycleAdapter;
import com.example.fdfda.Repository.DataAcess;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class FdfdaActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView back;

    RecyclerView recyclerView;
    RecycleAdapter mAdapter;

    ArrayList<String> list;

    LinearLayout send;
    LinearLayout record;
    EditText text;

    TextView fdfda_day, fdfda_time, fdfda_date;

    DataAcess dataAcess;

    String fdfda_day_ ,fdfda_time_, fdfda_date_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fdfda);

        // start toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // end toolbar

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FdfdaActivity.this, MainActivity.class));
                finish();
            }
        });

        fdfda_day = findViewById(R.id.fdfda_day);
        fdfda_time = findViewById(R.id.fdfda_time);
        fdfda_date = findViewById(R.id.fdfda_date);

        fdfda_day_ = getIntent().getStringExtra("day");
        fdfda_time_ = getIntent().getStringExtra("time");
        fdfda_date_ = getIntent().getStringExtra("date");

        fdfda_day.setText(fdfda_day_);
        fdfda_time.setText(fdfda_time_);
        fdfda_date.setText(fdfda_date_);

        // start of recyclerview configuration
        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<>();

        dataAcess = new DataAcess(getApplicationContext());
        File folderDir = dataAcess.create_folder();
        String file_name = "F"+fdfda_day_+fdfda_time_+fdfda_date_+".txt";
        File file = new File(folderDir, file_name);
        String[] sms = dataAcess.read_file(file);
        if (sms.length > 3) {
            for (int i = 3; i < sms.length; i++) {
                list.add(sms[i]);
            }
        }


        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(FdfdaActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecycleAdapter(FdfdaActivity.this, list, fdfda_day_, fdfda_time_, fdfda_date_);
        recyclerView.setAdapter(mAdapter);

        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());


        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.scrollBy(0, oldBottom - bottom);
                }
            }
        });
        // end of recyclerview configuration

        send = findViewById(R.id.send);
        record = findViewById(R.id.record);
        text = findViewById(R.id.edtxt);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = text.getText().toString();
                if (!string.isEmpty()){
                    list.add(string);

                    String[] day_time_date_sms = {fdfda_day_, fdfda_time_, fdfda_date_, string};
                    dataAcess.write_in_file_daytimedate(day_time_date_sms, 2);

                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                    text.setText("");
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, 10);
                }else {
                    Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10:
                if (resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text.setText(result.get(0));
                }
                break;
        }
    }
}
