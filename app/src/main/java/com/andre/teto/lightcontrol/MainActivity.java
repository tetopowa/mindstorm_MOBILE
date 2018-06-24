package com.andre.teto.lightcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button bluetoothConnect = (Button) findViewById(R.id.connect);
        final Switch switchLampe = (Switch) findViewById(R.id.switchLampe);
        final Switch switchPlafonnier = (Switch) findViewById(R.id.switchPlafonnier);
        final Switch switchAutomatique = (Switch) findViewById(R.id.switchAutomatique);
        final EditText intensitePlafonnier = (EditText) findViewById(R.id.intensitePlafonnier);
        final int valeurIntensitePlafonnierBase = 50;

        final BluetoothConnector bluetoothConnector = new BluetoothConnector();


        bluetoothConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(ProgressBar)findViewById(R.id.progressBar).;
                //findViewById(R.id.progressBar).animate();
                if(bluetoothConnector.getIsConnected()){
                    bluetoothConnector.disconnectNXT(v);
                    bluetoothConnect.setText("Se connecter");
                } else {

                    if(bluetoothConnector.connectNXT(v)){
                        bluetoothConnect.setText("Se déconnecter");
                    };

                }

            }
        });

        switchLampe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bluetoothConnector.sendMessage(1);
            }
        });

        switchPlafonnier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bluetoothConnector.sendMessage(2);
            }
        });

        switchAutomatique.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    switchLampe.setClickable(false);
                    switchPlafonnier.setClickable(false);
                    intensitePlafonnier.setFocusable(false);
                } else {
                    switchLampe.setClickable(true);
                    switchPlafonnier.setClickable(true);
                    intensitePlafonnier.setFocusableInTouchMode(true);
                }
                bluetoothConnector.sendMessage(3);
            }
        });

        intensitePlafonnier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() !=0){
                    int valeurIntensitePlafonnier = Integer.parseInt(s.toString());
                    if (switchPlafonnier.isChecked()){
                        bluetoothConnector.sendMessage(Integer.valueOf("10"+s.toString()));
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
