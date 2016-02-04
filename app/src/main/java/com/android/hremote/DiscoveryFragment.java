package com.android.hremote;

import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class DiscoveryFragment extends Fragment {
    private static final String TAG = "HRemote";

    private ArrayAdapter<String> mAdapter;
    private NsdHelper mNsdHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.discovery_fragment, container, false);

        mAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, ((RemoteActivity) getActivity()).mMousePadFragment).commit();
            }
        });

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
                    }
                });
    }

    public void refresh()
    {
        mNsdHelper.discoveryServices();
    }
}
