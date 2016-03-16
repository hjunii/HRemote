package com.android.hremote;

import android.net.nsd.NsdServiceInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class DiscoveryFragment extends Fragment {

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

                NsdServiceInfo info = mNsdHelper.getServices().get(position);

                try {
                    class ConnectionThread extends Thread {
                        private InetAddress mHost;
                        private int mPort;
                        boolean mSuccess;

                        public ConnectionThread(InetAddress host, int port) {
                            mHost = host;
                            mPort = port;
                            mSuccess = false;
                        }

                        public void run() {
                            try {
                                NsdHelper.setSocket(new Socket(mHost, mPort));
                                mSuccess = true;
                            }
                            catch (IOException e) {
                            }
                        }

                        private boolean isSuccess() {
                            return mSuccess;
                        }
                    }

                    ConnectionThread thread = new ConnectionThread(info.getHost(), info.getPort());
                    thread.start();;
                    thread.join();
                    if (!thread.isSuccess())
                        return;

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, ((RemoteActivity) getActivity()).mMousePadFragment).commit();
                    if (getActivity() != null)
                    {
                        ((RemoteActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.connected));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mNsdHelper = new NsdHelper(this);
        mNsdHelper.discoveryServices();

        return v;
    }

    public void addHost(final String name)
    {
        if (getActivity() != null) {
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.add(name);
                        }
                    });
        }
    }

    public void removeHost(final String name)
    {
        if (getActivity() != null) {
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.remove(name);
                        }
                    });
        }
    }

    public void clearHost()
    {
        if (getActivity() != null) {
            getActivity().runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();
                        }
                    });
        }
    }

    public void refresh()
    {
        mNsdHelper.discoveryServices();
    }
}
