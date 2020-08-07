package com.techno.aidl_walkthrough;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.techno.aidl_walkthrough.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private IAddInterface iAddInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Intent intent = new Intent(this, AdditionService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        activityMainBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(activityMainBinding.edtNum1.getText().toString().trim().length() == 0 ||
                        activityMainBinding.edtNum2.getText().toString().trim().length() == 0)) {
                    int x = Integer.parseInt(activityMainBinding.edtNum1.getText().toString());
                    int y = Integer.parseInt(activityMainBinding.edtNum2.getText().toString());

                    int result = 0;
                    try {
                        result = iAddInterface.add(x, y);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    activityMainBinding.txtResult.setText("Result is : " + result);
                } else {
                    Toast.makeText(MainActivity.this, "Please provide Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iAddInterface = IAddInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}