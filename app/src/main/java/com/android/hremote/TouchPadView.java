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
                Message msg = mHandler.obtainMessage();

                Bundle bundle = new Bundle();
                bundle.putShort("absX",(short)(event.getX() - x));
                bundle.putShort("absY", (short)(event.getY() - y));

                msg.setData(bundle);

                mHandler.sendMessage(msg);
            }

            return true;
        }
    };

    class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

            short absX = bundle.getShort("absX");
            short absY = bundle.getShort("absY");
            short wheel = 0;

            try {
                DataOutputStream os = new DataOutputStream(NsdHelper.getSocket().getOutputStream());
                os.writeBoolean(false);
                os.writeBoolean(false);
                os.writeBoolean(false);
                os.writeShort(absX);
                os.writeShort(absY);
                os.writeShort(wheel);
            } catch (UnknownHostException e) {
            } catch (IOException e) {
            }
        }
    }
}
