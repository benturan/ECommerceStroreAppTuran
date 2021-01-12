package com.bilgeadam.ecommercestroreappturan.Adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bilgeadam.ecommercestroreappturan.Fragments.PageFragment;
import com.bilgeadam.ecommercestroreappturan.MVP.SliderResponse;

import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    public static int LOOPS_COUNT = 1000;
    private List<SliderResponse> sliderResponsesData;


    public MyPagerAdapter(FragmentManager manager, List<SliderResponse> sliderListResponsesData) {
        super(manager);
        this.sliderResponsesData = sliderListResponsesData;
    }


    @Override
    public Fragment getItem(int position) {
        if (sliderResponsesData != null && sliderResponsesData.size() > 0) {
            position = position % sliderResponsesData.size(); // use modulo for infinite cycling
            return PageFragment.newInstance(position);
        } else {
            return PageFragment.newInstance(0);
        }
    }


    @Override
    public int getCount() {
        if (sliderResponsesData != null && sliderResponsesData.size() > 0) {
            return sliderResponsesData.size() * LOOPS_COUNT; // simulate infinite by big number of products
        } else {
            return 1;
        }
    }
} 