package com.android.hremote;

import android.app.Fragment;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class DiscoveryFragment extends Fragment {
    private static final String TAG = "HRemote";

    private ArrayList<String> mHostList = new ArrayList<String>();
    private NsdHelper mNsdHelper;
    private ArrayList<NsdServiceInfo> mServiceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.discovery_fragment, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, mHostList);

        mNsdHelper = new NsdHelper(this);

        mNsdHelper.discoveryServices();

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        return v;
    }

    public void addHost(String name) {
        mHostList.add(name);
    }

    public void clearHost() {
        mHostList.clear();
    }

    public void refresh()
    {
        mHostList.clear();
        mNsdHelper.discoveryServices();
    }
}
