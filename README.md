# AIDLDemo
实际使用AIDL进行进程间通信的小栗子

//Note: There is a very important thing to note is: the service side of aidl files and aidl file on the client to be consistent, and the package name must be the same.

01. First generation aidl file, as follows:
// IMyAidlInterface.aidl
package com.example.prx.aidldemo;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    //计算两个数的和
    int add(int num1 ,int num2);
}


02. Then edit the server-side code:


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


03. Last edited client code:


public class MainActivity extends AppCompatActivity
			  implements View.OnClickListener {

    private EditText etNum1,etNum2,etSum;
    private Button btnSum;
    private IMyAidlInterface myAidlInterface;
    private ServiceConnection conn = new ServiceConnection() {
        //绑定上服务的时候
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //拿到了远程服务
            Log.d("MainActivity", "拿到了远程服务");
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        //断开服务的时候
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //回收资源
            myAidlInterface = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //软件一启动就绑定服务
        Log.d("MainActivity", "软件一启动就绑定服务");
        bindToService();

    }

    private void initView() {
        etNum1 = (EditText) findViewById(R.id.etNum1);
        etNum2 = (EditText) findViewById(R.id.etNum2);
        etSum = (EditText) findViewById(R.id.etSum);
        btnSum = (Button) findViewById(R.id.btnSum);

        btnSum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int num1 = Integer.parseInt(etNum1.getText().toString());
        int num2 = Integer.parseInt(etNum2.getText().toString());

        Log.d("MainActivity", "num1:" + num1);
        Log.d("MainActivity", "num2:" + num2);

        try {
            //调用远程的服务的方法
            int sum = myAidlInterface.add(num1, num2);
            etSum.setText(sum + "");
        } catch (RemoteException e) {
            e.printStackTrace();
            etSum.setText("错误啦！");
        }
    }

    private void bindToService() {
        //获取到服务端
        Intent intent = new Intent();
        //新版本必须 显式Intent启动绑定服务
        intent.setComponent(new ComponentName("com.example.prx.aidldemo","com.example.prx.aidldemo.IRemoteService"));
        //自动创建服务
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}




