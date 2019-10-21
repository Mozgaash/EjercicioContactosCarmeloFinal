package com.example.ejerciciocontactoscarmelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GuardarContactosInternoActivity extends AppCompatActivity {


    TextView tvMostrar;
    EditText etNombreArchivo;
    Button btGuardar;
    List<Contacto> contactos;
    String nombre;
    SharedPreferences sharedPreferences;
    SharedPreferences config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_contactos_interno);

        Intent i = getIntent();
        contactos = (List)i.getSerializableExtra("contactos");

        initComponents();
        initEvents();

    }

    private void msg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    private void initComponents() {

        tvMostrar = findViewById(R.id.tvCargarContactos);
        etNombreArchivo = findViewById(R.id.etNombreArchivo);
        btGuardar = findViewById(R.id.btGuardar);
        sharedPreferences = getSharedPreferences("storedValues", Context.MODE_PRIVATE);
        config = PreferenceManager.getDefaultSharedPreferences(this);

    }

    private void initEvents() {

        mostrarContactos();
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombreArchivo.getText().toString() != "") {
                    almacenarArchivo();
                    if(config.getBoolean("recordar", true)) {
                        storeValues();
                    }
                }
            }
        });
        if(config.getBoolean("recordar", true)) {
            loadValues();
        }

    }



    private void loadValues() {

        etNombreArchivo.setText(sharedPreferences.getString("guardarArchivo", ""));

    }



    private void storeValues() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("guardarArchivo", nombre);
        editor.commit();

    }



    private void mostrarContactos() {

        tvMostrar.setText(contactos.toString());

    }

    //File f2 = new File(getFilesDir(), nombre + ".csv");

    private void almacenarArchivo() {
        nombre = etNombreArchivo.getText().toString();
        File f = new File(getFilesDir(), nombre + ".csv");

        // getFilesDir()
        try{
            FileWriter fw = new FileWriter(f);
            fw.write(contactosToCSV());
            fw.flush();
            fw.close();
            msg("Guardado con exito en: " + f.getAbsolutePath());
        }catch(IOException e){
            tvMostrar.setText(e.getMessage());
            msg(e.getMessage());
        }

    }



    private String contactosToCSV(){
        String cadena = "id,nombre,numero\n";
        for (int i = 0; i < contactos.size(); i++){
            cadena += contactos.get(i).toCSV() + "\n";
        }
        return cadena;
    }

}
