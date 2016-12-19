package com.wxdroid.microcodor.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by jinchun on 2016/11/30.
 */

public class BaseFragment extends Fragment {
    public Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

    }

}
