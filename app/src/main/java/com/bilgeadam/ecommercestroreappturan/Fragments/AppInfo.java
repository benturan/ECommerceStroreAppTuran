package com.bilgeadam.ecommercestroreappturan.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bilgeadam.ecommercestroreappturan.Activities.MainActivity;
import com.bilgeadam.ecommercestroreappturan.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppInfo extends Fragment {

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.termsLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.termsLayout:
                ((MainActivity) getActivity()).loadFragment(new TermsAndConditions(), true);
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.title.setText("Uygulama Hakkında");
       // Config.getCartList(getActivity(), true);
    }
}
