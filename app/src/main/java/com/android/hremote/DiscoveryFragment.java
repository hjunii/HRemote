package com.android.hremote;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class DiscoveryFragment extends Fragment {

    ArrayList<String> mHostList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.discovery_fragment, container, false);

        mHostList = new ArrayList<String>();
        mHostList.add("Hello");
        mHostList.add("World");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, mHostList);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                return v;
    }
}
