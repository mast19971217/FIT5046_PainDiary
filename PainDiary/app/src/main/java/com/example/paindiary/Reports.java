package com.example.paindiary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.paindiary.databinding.FragmentReportsBinding;
import com.google.android.material.tabs.TabLayout;


public class Reports extends Fragment {

    private FragmentReportsBinding Binding;
    private     TabLayout tabLayout;
    private         ViewPager2 viewPager;
    public Reports(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        Binding = FragmentReportsBinding.inflate(inflater, container, false);
        View view = Binding.getRoot();
        viewPager = Binding.viewPager;
        tabLayout = Binding.tabs;
        viewPager.setAdapter(new ViewPagerAdapter(getActivity()));
        tabLayout.addTab(tabLayout.newTab().setText("PainLocation"));
        tabLayout.addTab(tabLayout.newTab().setText("Steps"));
        tabLayout.addTab(tabLayout.newTab().setText("Pain&Weather"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("tag",tab.getPosition()+"");
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
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
class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 3;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull @Override public Fragment createFragment(int position) {
        if(position==0)
        {

            PieChartLocation location=new PieChartLocation();
            return location;
        }
        else if(position==1)
        {
            PieChartStep step=new PieChartStep();
            return step;
        }
        else
        {
            LineChart lineChart=new LineChart();
            return lineChart;
        }



    }
    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}