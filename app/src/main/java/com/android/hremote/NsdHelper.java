package com.android.hremote;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.util.ArrayList;

public class NsdHelper {

    private static final String TAG = "HRemote";
    private static final String SERVICE_TYPE = "_http._tcp.";
    private String mServiceName = "HRemoteService";
    private NsdManager.ResolveListener mResolveListener;

    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private ArrayList<NsdServiceInfo> mServices;

    private DiscoveryFragment mContext;

    public NsdHelper(DiscoveryFragment context) {
        mNsdManager = (NsdManager) context.getContext().getSystemService(Context.NSD_SERVICE);
        initializeResolveListener();
        mServices = new ArrayList<NsdServiceInfo>();
        mContext = context;
    }

    public  void discoveryServices() {
        stopDiscovery();
        initializeDiscoveryListener();
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    private void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }
            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
                mServices.add(serviceInfo);

                ((DiscoveryFragment) mContext).clearHost();
                for (int i = 0; i < mServices.size(); ++i) {
                    ((DiscoveryFragment) mContext).addHost(mServices.get(i).getHost().toString());
                }
            }
        };
    }

    private void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }
            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().contains(mServiceName)){
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }
            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                mServices.remove(service);

                ((DiscoveryFragment) mContext).clearHost();
                for (int i = 0; i < mServices.size(); ++i) {
                    ((DiscoveryFragment) mContext).addHost(mServices.get(i).getHost().toString());
                }
            }
            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            }
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            }
        };
    }

    private void stopDiscovery() {
        mServices.clear();
        if (mDiscoveryListener != null) {
            try {
                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            } finally {
            }
            mDiscoveryListener = null;
        }
    }
}
