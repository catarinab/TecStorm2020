package com.example.cadeiraderodasautonoma;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import android.content.Intent;
import java.util.*;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.bluetooth.BluetoothAdapter;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private TextView tvxResult;
    BluetoothAdapter bt;
    Set<BluetoothDevice> escolha;
    BluetoothDevice este;
    BluetoothSocket enca;
    public static OutputStream outp;
    ListView li;

    public final static String EXTRA_MESSAGE = "com.example.cadeiraderodasautonoma.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvxResult = (TextView) findViewById(R.id.tvxResult);
        li = (ListView)findViewById(R.id.lis);
        li.setEnabled(false);
        li.setClickable(true);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ligarBT();

        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    String no = (String) li.getItemAtPosition(position);
                    String[] dispo = no.split("\n");
                    connect(dispo[0]);
                } catch (IOException e){System.out.println(e);}
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    void ligarBT () {

        bt = BluetoothAdapter.getDefaultAdapter();

        if (bt == null) {
            tvxResult.setText("Nao tem BT");
            return;
        }
        if (bt.isEnabled()){
            listar();
        }

        if (!bt.isEnabled()) {
            Intent ativ = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativ, 5);
            listar();
        }
    }

    private void connect(String s) throws IOException {
        UUID uid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        for (BluetoothDevice div : escolha){
            if (div.getName().equals(s)){
                este = div;
                break;
            }

        }
        enca = este.createRfcommSocketToServiceRecord(uid);
        enca.connect();
        outp = enca.getOutputStream();
        Intent muda = new Intent(this, Conexao.class);
        muda.putExtra(EXTRA_MESSAGE, s);
        startActivity(muda);
    }

    void listar (){
        if(bt != null && bt.isEnabled()){
            Set<BluetoothDevice> paired = bt.getBondedDevices();
            escolha = paired;
            List<String> nomes = new ArrayList<String>();
            if (paired.size() > 0) {
                for (BluetoothDevice disp : paired) {
                    nomes.add(disp.getName() + "\n" + disp.getAddress());

                }
                li.setEnabled(true);
                ArrayAdapter<String> arra = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nomes);
                li.setAdapter(arra);
            } else {
                System.out.println("Nao ha dispositivo!");
            }
            System.out.println("Fim" + nomes);
        } else {
            System.out.println("Nao deu!");
        }
    }
}
