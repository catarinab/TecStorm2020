package com.example.cadeiraderodasautonoma;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Conexao extends AppCompatActivity {

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
            switch (message){
                case "parar":
                case "para":
                case "pare":
                case "parares":
                case "espera":
                case "esperar":
                case "cara":
                    this.mensagem = "S";
                    enviar(this.mensagem);
                    break;
                case "continua":
                case "continuar":
                case "anda":
                case "andares":
                case "andar":
                case "frente":
                case "move":
                case "adiante":
                case "mover":
                    this.mensagem = "F";
                    enviar(this.mensagem);
                    break;
                case("direita"):
                    this.mensagem = "R";
                    enviar(this.mensagem);
                    break;
                case("esquerda"):
                    this.mensagem = "L";
                    enviar(this.mensagem);
                    break;
                case("atrás"):
                case("para tras"):
                    this.mensagem = "B";
                    enviar(this.mensagem);
                    break;
                default:
                    getSpeechInput();
                    break;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
