package com.example.paindiary.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface PainRecordDao {
    @Query("SELECT * FROM PainRecord")
    LiveData<List<PainRecord>> getAll();
    @Query("SELECT * FROM PainRecord")
    List<PainRecord> getAllnolive();
    @Query("SELECT * FROM PainRecord WHERE id = :Id LIMIT 1")
    PainRecord findByID(int Id);
    @Query("SELECT * FROM PainRecord WHERE date = :Date and email= :email LIMIT 1")
    PainRecord findByDate(Date Date,String email);
    @Query("SELECT * FROM PainRecord WHERE email= :email LIMIT 1")
    List<PainRecord> findAllByEmail(String email);
    @Insert
    void insertAll(PainRecord... records);
    @Insert
    long insert(PainRecord record);
    @Delete
    void delete(PainRecord record);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCustomers(PainRecord... painRecords);
    @Query("DELETE FROM painrecord")
    void deleteAll();
    @Query("UPDATE PainRecord SET email=:email, painintensitylevel=:painintensitylevel, painlocation=:painlocation,moodlevel=:moodlevel,stepsaim=:stepsaim,steps=:steps,temperature=:temp,humidity=:humidity,pressure=:pressure WHERE date = :date")
    void updatebydate(Date date, String email,int painintensitylevel,String painlocation,int moodlevel,int stepsaim,int steps,float temp,float humidity,float pressure);
    //just for add test data in database inspector
    @Query("INSERT INTO PainRecord (email,painintensitylevel,painlocation,moodlevel,stepsaim,steps,date,temperature,humidity,pressure) values(:email,:painintensitylevel,:painlocation,:moodlevel,:stepsaim,:steps,:date,:temp,:humidity,:pressure)")
    void insertTest( String email,int painintensitylevel,String painlocation,int moodlevel,int stepsaim,int steps,Date date,float temp,float humidity,float pressure);
    //for PieChartLocation
    @Query(" select painlocation,count(*) as count from PainRecord where email=:email Group by painlocation ")
    List<PainRecordCount> countlocation(String email);
    //for LineChart
    @Query("select * from PainRecord where email=:email and  (date between :StartTime and :EndTime) ")
    List<PainRecord> findbydaterange(String email,Date StartTime,Date EndTime);



}
