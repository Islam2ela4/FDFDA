package com.example.fdfda.Repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.fdfda.Model.Card_details;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataAcess {

    Context context;

    public DataAcess(Context context){
        this.context = context;
    }

    public void write_in_file_daytimedate(String[] day_time_date_sms, int statue){
        // create folder if it is not exist
        File folderDir = create_folder();
        // create file inside folder
        File file = new File(folderDir, "F"+day_time_date_sms[0]+day_time_date_sms[1]+day_time_date_sms[2]+".txt");
        // write inside file
        FileOutputStream outputStreamWrite = null;
        FileOutputStream outputStreamAppend = null;
        if (statue == 2){
            try {
                outputStreamAppend = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] smsInByte = day_time_date_sms[3].concat("\n").getBytes();
            try {
                outputStreamAppend.write(smsInByte);
                outputStreamAppend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                outputStreamWrite = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                byte[] txt1InByte = day_time_date_sms[0].concat("\n").getBytes();
                outputStreamWrite.write(txt1InByte);
                outputStreamWrite.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStreamAppend = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (int i = 1; i < day_time_date_sms.length; i++) {
                    byte[] txtInByte = day_time_date_sms[i].concat("\n").getBytes();
                try {
                    outputStreamAppend.write(txtInByte);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                outputStreamAppend.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String Folder_Name = "FDFDA";
    public File create_folder(){
        String Folder_Path = context.getFilesDir().getAbsolutePath() + File.separator + Folder_Name;
        File folderDir = new File(Folder_Path);
        if (!folderDir.exists()) {
            folderDir.mkdirs();
        }
        return folderDir;
    }

    public String[] read_file(File file){
        int read = -1;
        String[] tokens = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuffer CharactersBuffer = new StringBuffer();
        StringBuffer BinaryBuffer = new StringBuffer();

        try {
            read = inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (read == -1) {
            Toast.makeText(context, "File is Empty", Toast.LENGTH_SHORT).show();
        } else {
            while (read != -1) {
                CharactersBuffer.append((char) read);
                BinaryBuffer.append(read).append("\n");
                try {
                    read = inputStream.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String allStringBufferContent = CharactersBuffer.toString();
            String allBinaryBufferContent = BinaryBuffer.toString();
            Log.d("2ela4", "allStringBufferContent " + allStringBufferContent);
            Log.d("2ela4", "allBinaryBufferContent " + allBinaryBufferContent);
            tokens = allStringBufferContent.split("\n");
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tokens;
    }

    public ArrayList<Card_details> read_from_file_first_3_lines(){
        // create folder if it is not exist
        File folderDir = create_folder();
        // get all files
        File[] files = folderDir.listFiles();
        Log.d("Files", "Size: "+ files.length);
        ArrayList<Card_details> list = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            File file = new File(folderDir, files[i].getName());
            // read first 3 lines from file
            String[] tokens = read_file(file);
            list.add(new Card_details(tokens[0], tokens[1], tokens[2]));
        }
        return list;
    }


    public String[] formatting(){
        Date dNow = new Date( );
        SimpleDateFormat ft1 = new SimpleDateFormat ("E");
        SimpleDateFormat ft2 = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat ft3 = new SimpleDateFormat("dd.MM.yyyy");

        String[] day_time_datelist = new String[3];
        day_time_datelist[0] = ft1.format(dNow);
        day_time_datelist[1] = ft2.format(dNow);
        day_time_datelist[2] = ft3.format(dNow);
        return day_time_datelist;
    }
}
