package com.example.paindiary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paindiary.databinding.FragmentDailyRecordBinding;
import com.example.paindiary.room.PainRecord;
import com.example.paindiary.room.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class DailyRecord extends Fragment {

    private FragmentDailyRecordBinding Binding;
    private FirebaseAuth mAuth;
    private List<PainRecord> List = null;
    private PainRecordViewModel painRecordViewModel;
    public DailyRecord(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Binding = FragmentDailyRecordBinding.inflate(inflater, container, false);
        View view = Binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView = Binding.recycleview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        painRecordViewModel  = new
                ViewModelProvider(this).get(PainRecordViewModel.class);
        painRecordViewModel.initalizeVars(getActivity().getApplication());
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new
                Observer<List<PainRecord>>() {
                    @Override
                    public void onChanged(@Nullable final List<PainRecord> painRecords)
                    {

                        List=new ArrayList<PainRecord>() ;
                        for (PainRecord temp : painRecords) {
                            if(mAuth.getCurrentUser().getEmail().equals(temp.email))
                            {List.add(temp);}
                        }
                        Log.d("wwww",List.size()+"");
                        PainRecordAdapter adapter = new PainRecordAdapter(List);
                        recyclerView.setAdapter(adapter);

                    }
                });


        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Binding = null;
    }

}
class PainRecordHolder extends RecyclerView.ViewHolder{
    TextView info;
    public PainRecordHolder(View root) {
        super(root);
        info=(TextView) root.findViewById(R.id.info);
    }
}
class PainRecordAdapter extends RecyclerView.Adapter<PainRecordHolder>{
    private List<PainRecord> painRecords;

    public PainRecordAdapter(List<PainRecord> painRecords){
        this.painRecords = painRecords;
    }

    public void setPainRecords(List<PainRecord> painRecordss) {
        this.painRecords = painRecords;
    }

    @Override
    public PainRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        PainRecordHolder holder = new PainRecordHolder(view);
        //Log.d("tag","???");
        return holder;
    }
    private enum moodlevel {
        VeryLow,Low,Averge,Good,VeryGood
    }
    @Override
    public void onBindViewHolder(PainRecordHolder holder, int position) {
        PainRecord painRecord = painRecords.get(position);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        holder.info.setText("Date:"+sdf.format(painRecord.date)+"\n"
        +"Pain Intensity Level:"+painRecord.painintensitylevel+"\n"
        +"Pain Location:"+painRecord.painlocation+"\n"
                +"MoodLevel:"+moodlevel.values()[painRecord.moodlevel]+"\n"
                +"Goal of Steps："+painRecord.stepsaim+"\n"
        +"Steps："+painRecord.steps+"\n"
        +"Temperature:"+painRecord.temp+"°C\n"
                +"Humidity:"+painRecord.humidity+"%\n"
                +"Atmospheric Pressure:"+painRecord.pressure+"hpa\n");
        //Log.d("tag","???");
    }

    @Override
    public int getItemCount() {
        return painRecords.size();
    }
}