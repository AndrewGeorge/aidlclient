package com.example.administrator.aidlclient;
import com.example.administrator.aidltest.MyAidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView tv_res;
    private EditText num1;
    private EditText num2;
    private Button btn_add;
    private MyAidl aidl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();
        //软件一启动就绑定服务服务
        bindService();

    }

    //初始化视图
    private void initview() {
        num1 = (EditText) findViewById(R.id.edt_num1);
        num2 = (EditText) findViewById(R.id.edt_num2);
        btn_add = (Button) findViewById(R.id.btn_add);
        tv_res= (TextView) findViewById(R.id.tv_res);

        btn_add.setOnClickListener(this);
    }

    //获取到服务端
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                int n = Integer.parseInt(num1.getText().toString());
                int m = Integer.parseInt(num2.getText().toString());
                try {
                    //调用远程的服务
                    Log.d("TAG","n:"+n+"----m:"+m);
                   int res= aidl.add(n,m);
                    tv_res.setText(res+"");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    tv_res.setText("错误啦！");
                }

                break;
        }


    }

    private void bindService() {
        //新版本必须显示启动服务
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.administrator.aidltest", "com.example.administrator.aidltest.IremoteService"));
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        //当服务连接
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //拿到了远程的服务
            aidl = MyAidl.Stub.asInterface(service);
        }

        //当服务结束
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //回收资源
            aidl = null;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
