package com.example.paindiary.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class PainRecordCount {
    @ColumnInfo(name = "painlocation")
    public String painlocation;
    @ColumnInfo(name = "count")
    public int count;
    public PainRecordCount(String painlocation,int count)
    {
        this.painlocation=painlocation;
        this.count=count;
    }
}
