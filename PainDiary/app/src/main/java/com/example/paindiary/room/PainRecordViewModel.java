package com.example.paindiary.room;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paindiary.CallBack;

import java.util.Date;
import java.util.List;

public class PainRecordViewModel extends ViewModel {

    private PainRecordRepository pRepository;
    private MutableLiveData<List<PainRecord>> allPainRecords;
    public PainRecordViewModel () {
        allPainRecords=new MutableLiveData<>();
    }
    public void setPainRecords(List<PainRecord> painRecords) { allPainRecords.setValue(painRecords); }
    public LiveData<List<PainRecord>> getAllPainRecords() {
        return pRepository.getAllPainRecords();
    }
    public void initalizeVars(Application application){
        pRepository = new PainRecordRepository(application);
    }
    public void insert(PainRecord painRecord) {
        pRepository.insert(painRecord);
    }
    public void insertAll(PainRecord painRecord) {
        pRepository.insertAll(painRecord);
    }
    public void deleteAll() {
        pRepository.deleteAll();
    }
    public void update(PainRecord... painRecords) {
        pRepository.updatePainRecords(painRecords);
    }
    public void updateBydate(Date date, String email,int painintensitylevel,String painlocation,int moodlevel,int stepsaim,int steps,float temp,float humidity,float pressure) {
        pRepository.updatePainRecordByDate(date, email,painintensitylevel,painlocation,moodlevel,stepsaim,steps,temp,humidity,pressure);
    }

    public void findByID(int painrecordId, CallBack<PainRecord> callback){ pRepository.findByID(painrecordId,callback); }
    public void findByDate(Date date,String email,CallBack<PainRecord> callback){pRepository.findBydate(date,email,callback);}
    public void countlocation(String email,CallBack<List<PainRecordCount>> callback){pRepository.countlocation(email,callback);}
    public void findBydaterange(String email,Date starttime,Date endtime,CallBack<List<PainRecord>> callBack){pRepository.findbydaterange(email,starttime,endtime,callBack);}
    public void findAllbyemail(String email,CallBack<List<PainRecord>> callBack ){pRepository.findAllbyemail(email,callBack);}
    public void getallnolive(CallBack<List<PainRecord>> callBack){pRepository.getallnolive(callBack);}

}

