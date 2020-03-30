package com.example.fdfda.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fdfda.Repository.DataAcess;
import com.example.fdfda.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    ArrayList<String> list;
    Context context;
    String fdfda_day, fdfda_time, fdfda_date;

    public RecycleAdapter(Context context, ArrayList<String> list, String fdfda_day, String fdfda_time, String fdfda_date){
        this.context = context;
        this.list = list;
        this.fdfda_day = fdfda_day;
        this.fdfda_time = fdfda_time;
        this.fdfda_date = fdfda_date;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.conver_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.txt.setText(list.get(position));
        holder.txt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setView(R.layout.custom_alertdialog);
                }
                builder.setCancelable(true);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String file_name = "F" + fdfda_day + fdfda_time + fdfda_date + ".txt";
                        String Folder_Path = context.getFilesDir().getAbsolutePath() + File.separator + "FDFDA";
                        File file = new File(Folder_Path + "/" + file_name);
                        DataAcess acess = new DataAcess(context);
                        String[] tokens = acess.read_file(file);
                        ArrayList<String> list_ = new ArrayList<>();
                        for (int x = 0; x < tokens.length; x++){
                            list_.add(tokens[x]);
                        }
                        for (int i = 0; i < list_.size(); i++){
                            if (list_.get(i).contentEquals(holder.txt.getText())){
                                list_.remove(i);
                            }
                        }
                        String[] tokens_ = new String[list_.size()];
                        for (int x = 0; x < list_.size(); x++){
                            tokens_[x] = list_.get(x);
                        }
                        acess.write_in_file_daytimedate(tokens_, 3);
                        list.remove(holder.txt.getText());
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
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txt;
        public MyViewHolder(View v) {
            super(v);
            txt = v.findViewById(R.id.txt);
        }
    }
}