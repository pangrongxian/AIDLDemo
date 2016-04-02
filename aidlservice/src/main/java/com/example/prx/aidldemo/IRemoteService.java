package com.example.prx.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Administrator on 2016/4/1.
 */
public class IRemoteService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    private IBinder iBinder = new IMyAidlInterface.Stub() {
        @Override
        public int add(int num1, int num2) throws RemoteException {

            Log.d("IRemoteService", "收到了远程的请求，输入的参数是："+num1+"和"+num2);

            return num1 + num2;
        }
    };
}
