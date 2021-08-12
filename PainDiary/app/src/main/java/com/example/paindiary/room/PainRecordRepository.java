package com.example.paindiary.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.paindiary.CallBack;

import java.util.Date;
import java.util.List;

public class PainRecordRepository {
    private PainRecordDao dao;
    private LiveData<List<PainRecord>> allPainRecords;


    public PainRecordRepository(Application application) {
        PainRecordDatabase db = PainRecordDatabase.getInstance(application);
        dao = db.PainRecordDao();

    }

    public LiveData<List<PainRecord>> getAllPainRecords() {
        allPainRecords = dao.getAll();
        return allPainRecords;
    }

    public void insert(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(painRecord);
            }
        });
    }

    public void deleteAll() {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public void delete(final PainRecord painRecord) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(painRecord);
            }
        });
    }

    public void insertAll(final PainRecord... painRecords) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(painRecords);
            }
        });
    }

    public void updatePainRecords(final PainRecord... painRecords) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateCustomers(painRecords);
            }
        });
    }

    public void updatePainRecordByDate(Date date, String email,int painintensitylevel,String painlocation,int moodlevel,int stepsaim,int steps,float temp,float humidity,float pressure) {
       PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updatebydate(date, email,painintensitylevel,painlocation,moodlevel,stepsaim,steps,temp,humidity,pressure);
            }
        });
    }
    public void findAllbyemail(final String email,CallBack<List<PainRecord>> callBack) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<PainRecord> runpainrecord = dao.findAllByEmail(email);

                    if (callBack != null) {
                        callBack.onSucess(runpainrecord);
                    }
                }
                catch (Exception e)
                {
                    if(callBack!=null)
                    {
                        callBack.onFailure();
                    }
                    throw e;
                }
            }
        });


    }


    public void findByID(final int painrecordId,CallBack<PainRecord> callBack) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    PainRecord runpainrecord = dao.findByID(painrecordId);

                    // Log.d("abc12",runpainrecord.email);
                    if (callBack != null) {
                        callBack.onSucess(runpainrecord);
                    }
                }
                catch (Exception e)
                {
                    if(callBack!=null)
                    {
                        callBack.onFailure();
                    }
                    throw e;
                }
            }
        });


    }
    public  void findbydaterange(String email,Date starttime,Date endtime,CallBack<List<PainRecord>> callBack)
    {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<PainRecord> runpainrecord = dao.findbydaterange(email,starttime,endtime);


                    if (callBack != null) {
                        callBack.onSucess(runpainrecord);
                    }
                }
                catch (Exception e)
                {
                    if(callBack!=null) {
                        callBack.onFailure();
                    }
                    throw e;
                }
            }
        });
    }
    public  void getallnolive(CallBack<List<PainRecord>> callBack)
    {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<PainRecord> runpainrecord = dao.getAllnolive();


                    if (callBack != null) {
                        callBack.onSucess(runpainrecord);
                    }
                }
                catch (Exception e)
                {
                    if(callBack!=null) {
                        callBack.onFailure();
                    }
                    throw e;
                }
            }
        });
    }
    public void findBydate(final Date date, String email,CallBack<PainRecord> callBack) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    PainRecord runpainrecord = dao.findByDate(date,email);

                    //Log.d("abc123",runpainrecord.email);

                    if (callBack != null) {
                        callBack.onSucess(runpainrecord);
                    }
                }
                catch (Exception e)
                {
                    if(callBack!=null) {
                        callBack.onFailure();
                    }
                    throw e;
                }
            }
        });

    }

    public void  countlocation(final String email,CallBack<List<PainRecordCount>> callBack) {
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    List<PainRecordCount> res = dao.countlocation(email);

                    if (callBack != null) {
                        callBack.onSucess(res);
                    }
                }
                catch (Exception e)
                {
                    if(callBack!=null)
                    {
                        callBack.onFailure();
                    }
                    throw e;
                }
            }
        });

    }


}