package com.policyboss.policybossproelite.ultralaksha.ultralakshya.fragment.quote.content;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.policyboss.policybossproelite.R;

import magicfinmart.datacomp.com.finmartserviceapi.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class coverPage1Fragment extends Fragment {

    TextView txtName;

    public coverPage1Fragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cover_page1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtName = view.findViewById(R.id.txtName);

        if(getArguments().getString(Utility.ULTRA_LAKSHYA_HDR_NAME) !=null)
        {
            txtName.setText("" + getArguments().getString(Utility.ULTRA_LAKSHYA_HDR_NAME));
        }
    }
}
