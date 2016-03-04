package com.android.hremote;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

public class TouchPadView extends View {

    private static final short EV_SYN = 0x00;
    private static final short EV_KEY = 0x01;
    private static final short EV_REL = 0x02;
    private static final short EV_ABS = 0x03;

    private static final short ABS_X = 0x00;
    private static final short ABS_Y = 0x01;

    private static final short REL_X = 0x00;
    private static final short REL_Y = 0x01;

    private int x;
    private int y;

    private MsgHandler mHandler;

    public TouchPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHandler = new MsgHandler();
        setOnTouchListener(mTouchEvent);
    }

    private OnTouchListener mTouchEvent = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x = (int) event.getX();
                y = (int) event.getY();
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                Message msgX = mHandler.obtainMessage();
                Message msgY = mHandler.obtainMessage();
                Message msgSYN = mHandler.obtainMessage();

                Bundle bundleX = new Bundle();
                bundleX.putShort("type", EV_REL);
                bundleX.putShort("code", REL_X);
                bundleX.putInt("value", (int)event.getX() - x);
                Bundle bundleY = new Bundle();
                bundleY.putShort("type", EV_REL);
                bundleY.putShort("code", REL_Y);
                bundleY.putInt("value", (int)event.getY() - y);
                Bundle bundleSYN = new Bundle();
                bundleSYN.putShort("type", EV_SYN);
                bundleSYN.putShort("code", (short) 0);
                bundleSYN.putInt("value", 0);

                msgX.setData(bundleX);
                msgY.setData(bundleY);
                msgSYN.setData(bundleSYN);

                mHandler.sendMessage(msgX);
                mHandler.sendMessage(msgY);
                mHandler.sendMessage(msgSYN);
            }

            return true;
        }
    };

    class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

            short type = bundle.getShort("type");
            short code = bundle.getShort("code");
            int value = bundle.getInt("value");

            try {
                DataOutputStream os = new DataOutputStream(NsdHelper.getSocket().getOutputStream());
                os.writeShort(type);
                os.writeShort(code);
                os.writeInt(value);
            } catch (UnknownHostException e) {
            } catch (IOException e) {
            }
        }
    }
}
