package com.example.paindiary;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.paindiary.room.PainRecord;
import com.example.paindiary.room.PainRecordDao;
import com.example.paindiary.room.PainRecordDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

public class UploadWorker extends Worker {
    private PainRecordDatabase database;
    private PainRecordDao dao;
    private DatabaseReference mDatabase;
    private String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
    public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy_MM_dd");
    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        mDatabase=FirebaseDatabase.getInstance("https://mobilepaindiary-7561f-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        database=PainRecordDatabase.getInstance(context);
        dao= database.PainRecordDao();
    }

    @Override
    public Result doWork() {

        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<PainRecord> painRecords = dao.getAllnolive();
                    //mDatabase.child("PainRecord").setValue(painRecords);
                    for(PainRecord p:painRecords)
                    {   String email=p.email;
                        String newString = email.replaceAll(regEx,"_");
                        Log.d("WorkManager","Email:"+p.email+",Date:"+p.date+" upload success!");
                        mDatabase.child("PainRecord").child(newString).child(sdf.format(p.date)).setValue(p);
                    }


                }
                catch (Exception e)
                {

                    throw e;
                }
            }
        });

        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }
}