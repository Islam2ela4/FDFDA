package com.example.fdfda.Adapters;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.fdfda.Model.Card_details;
import com.example.fdfda.FdfdaActivity;
import com.example.fdfda.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Card_details> list;

    public GridAdapter(Context context, ArrayList<Card_details> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list.size() != 1)
            return list.size()-1;
        else return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        Card_details details_ = list.get(0);
        if (!details_.getDay().equals("") || !details_.getTime().equals("") || !details_.getDate().equals("")) {
            convertView = inflater.inflate(R.layout.added_card_view, null, true);
            final TextView day = convertView.findViewById(R.id.day);
            final TextView time = convertView.findViewById(R.id.time);
            final TextView date = convertView.findViewById(R.id.date);
            final CardView cardView = convertView.findViewById(R.id.card);

            Card_details details = list.get(position);
            day.setText(details.getDay());
            time.setText(details.getTime());
            date.setText(details.getDate());

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(R.layout.custom_alertdialog);
                    builder.setCancelable(true);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String day_ = day.getText().toString();
                            String time_ = time.getText().toString();
                            String date_ = date.getText().toString();
                            String file_name = "F" + day_ + time_ + date_ + ".txt";
                            String Folder_Path = context.getFilesDir().getAbsolutePath() + File.separator + "FDFDA";
                            File file = new File(Folder_Path + "/" + file_name);
                            file.getAbsoluteFile().delete();

                            // override equal method to compare object to other objects in Arraylist to remove it
                            list.remove(new Card_details(day_, time_, date_));
                            notifyDataSetChanged();

                            Snackbar.make(v, "One item is deleted", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar.make(v, "You canceled deleting the item", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            dialog.cancel();
                        }
                    });
                    builder.show();

                    return true;
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FdfdaActivity.class);
                    intent.putExtra("day", day.getText().toString());
                    intent.putExtra("time", time.getText().toString());
                    intent.putExtra("date", date.getText().toString());
                    context.startActivity(intent);
                }
            });
        }else {
            convertView = inflater.inflate(R.layout.add_cardview, null, true);
        }

        return convertView;
    }

}
