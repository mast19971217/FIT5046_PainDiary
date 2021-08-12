package com.example.paindiary.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity
public class PainRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "painintensitylevel")
    public int painintensitylevel;
    @ColumnInfo(name = "painlocation")
    public String painlocation;
    @ColumnInfo(name = "moodlevel")
    public int moodlevel;
    @ColumnInfo(name = "stepsaim")
    public int stepsaim;
    @ColumnInfo(name = "steps")
    public int steps;
    @ColumnInfo(name = "date")
    public Date date;
    @ColumnInfo(name = "temperature")
    public float temp;
    @ColumnInfo(name = "humidity")
    public float humidity;
    @ColumnInfo(name = "pressure")
    public float pressure;
    public PainRecord(String email , int painintensitylevel, String painlocation,int moodlevel, int stepsaim,int steps,float temp,float humidity, float pressure ) {
        this.email=email;
        this.painlocation=painlocation;
        this.painintensitylevel=painintensitylevel;
        this.moodlevel=moodlevel;
        this.stepsaim=stepsaim;
        this.steps=steps;
        this.temp=temp;
        this.humidity=humidity;
        this.pressure=pressure;
        this.date=new Date(System.currentTimeMillis());
    }
    @Ignore
    public PainRecord(){}
    @Ignore
    public PainRecord(Date date,String email , int painintensitylevel, String painlocation,int moodlevel, int stepsaim,int steps,float temp,float humidity, float pressure ) {
        this.date=date;
        this.email=email;
        this.painlocation=painlocation;
        this.painintensitylevel=painintensitylevel;
        this.moodlevel=moodlevel;
        this.stepsaim=stepsaim;
        this.steps=steps;
        this.temp=temp;
        this.humidity=humidity;
        this.pressure=pressure;
        this.date=new Date(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }


}