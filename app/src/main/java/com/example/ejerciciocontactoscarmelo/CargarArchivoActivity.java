package com.example.ejerciciocontactoscarmelo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CargarArchivoActivity extends AppCompatActivity {

    EditText etNombreArchivo;
    Button btCargar;
    TextView tvCargarContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargar_archivo);

        initComponents();
        initEvents();

    }

    private void initComponents() {

        etNombreArchivo = findViewById(R.id.etNombreArchivo);
        btCargar = findViewById(R.id.btCargar);
        tvCargarContactos = findViewById(R.id.tvCargarContactos);

    }

    private void initEvents() {

        btCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCargarContactos.setText(cargarArchivo());
            }


        });

    }

    private void msg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    private String cargarArchivo() {
        String nombre = etNombreArchivo.getText().toString();

        File f = new File(getExternalFilesDir(null), nombre + ".csv");
        String texto = "";
        if(f.exists()) {
            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String linea = "";
                while((linea = br.readLine()) != null){
                    texto += linea + "\n";
                }
            } catch (IOException e) {
                msg("Error");
            }
        }
        return texto;
    }

}
