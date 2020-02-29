package com.example.cadeiraderodasautonoma;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import android.content.Intent;
import android.widget.Toast;

import java.util.Locale;
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
        System.out.println("Comecar funcao");

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
        }
    }

    private void connect(String s) throws IOException {
        UUID uid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        for (BluetoothDevice div : escolha){
            if (div.getName().equals(s)){
                System.out.print("Este:");
                System.out.println(div.getUuids()[0].getUuid().toString());
                este = div;
                break;
            }

        }
        enca = este.createRfcommSocketToServiceRecord(uid);
        System.out.println("Encontrou");
        enca.connect();
        System.out.println("Conectou");
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
            System.out.println("Começa" + nomes);
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

    void desligar()throws IOException{
        bt.disable();
        li.setAdapter(null);
        tvxResult.setText("Desligado");
        if (outp != null) {
            outp.close();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
