package com.example.cadeiraderodasautonoma;

import android.content.Intent;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Conexao extends AppCompatActivity {

    public static final String TAG = "conexaoAtividade";

    String mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_conexao);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            getSpeechInput();
            enviar(this.mensagem);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    private void enviar(String mens) throws IOException {
        if (mens.equals("")){
            return;
        }
        MainActivity.outp.write(mens.getBytes());
        System.out.println("enviou.");
        try {
            synchronized(this){
                wait(3000);
                getSpeechInput();
            }
        }
        catch(InterruptedException ex){
            getSpeechInput();
        }

    }

    public void getSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 100);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        }
        else{
            Toast.makeText(this, "Your device doesn't support Text Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && data != null && resultCode == RESULT_OK){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            createNewMove(result.get(0));
        }
    }

    public void createNewMove(String message){
        try {
            if (message.equals("parar") || message.equals("para") || message.equals("pare")) {
                this.mensagem = "S";
                enviar(this.mensagem);
            }
            else if (message.equals("continua") || message.equals("continuar") || message.equals("anda") || message.equals("andar")) {
                this.mensagem = "F";
                enviar(this.mensagem);
            }
            else if (message.equals("direita")) {
                this.mensagem = "R";
                enviar(this.mensagem);
            }
            else if (message.equals("esquerda")) {
                this.mensagem = "L";
                enviar(this.mensagem);
            }
            else if (message.equals("atrás")) {
                this.mensagem = "B";
                enviar(this.mensagem);
            }
            else System.out.println("nao deu");
            getSpeechInput();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
