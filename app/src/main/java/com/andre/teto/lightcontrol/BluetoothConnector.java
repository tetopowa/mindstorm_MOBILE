package com.andre.teto.lightcontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class BluetoothConnector {

    private BluetoothAdapter localAdapter;
    private BluetoothSocket socket_nxt;
    private BluetoothDevice nxt;
    private OutputStreamWriter out;
    private final String nxtMAC = "00:16:53:09:17:0B";
    private Boolean isConnected;

    public BluetoothConnector() {

        this.localAdapter = BluetoothAdapter.getDefaultAdapter();
        this.setBluetoothON();
        this.isConnected = false;

    }

    public void setBluetoothON(){
        if (!this.localAdapter.isEnabled()) {
            this.localAdapter.enable();
            while(!(this.localAdapter.isEnabled()));
        }
    }
    public Boolean getIsConnected(){
        return this.isConnected;
    }

    public void setBluetoothOFF(){
        if (this.localAdapter.isEnabled()) {
            this.localAdapter.disable();
        }
    }

    public Boolean connectNXT(View v){
        this.nxt = this.localAdapter.getRemoteDevice(nxtMAC);
        try {
            this.socket_nxt = nxt.createRfcommSocketToServiceRecord(UUID
                    .fromString("00001101-0000-1000-8000-00805F9B34FB"));
            this.socket_nxt.connect();
            Snackbar.make(v, "Bluetooth: Connected to NXT", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            this.isConnected = true;
            return true;
        } catch (IOException e) {
            Snackbar.make(v, "Bluetooth: Device not found or cannot connect", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
    }


    public void sendMessage(int msg){

        try {
            this.out = new OutputStreamWriter(socket_nxt.getOutputStream());
            this.out.write((byte)msg);
            this.out.flush();
          //  Thread.sleep(500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectNXT(View v){
        try{
            this.out.close();
            this.socket_nxt.close();
            this.isConnected = false;
            Snackbar.make(v, "Bluetooth: Déconnecté", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } catch (IOException e){
            Snackbar.make(v, "Bluetooth: Erreur déconnection", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

}
