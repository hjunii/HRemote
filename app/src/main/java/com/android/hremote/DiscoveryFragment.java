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

    private ArrayAdapter<String> mAdapter;
    private NsdHelper mNsdHelper;
    private ArrayList<NsdServiceInfo> mServiceList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.discovery_fragment, container, false);

        mAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mNsdHelper = new NsdHelper(this);
        mNsdHelper.discoveryServices();

        return v;
    }

    public void addHost(final String name)
    {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.add(name);
                        //mAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void removeHost(final String name)
    {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.remove(name);
                        //mAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void clearHost()
    {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        //mAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void refresh()
    {
        mNsdHelper.discoveryServices();
    }
}
