package com.example.ejerciciocontactoscarmelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejerciciocontactoscarmelo.settings.SettingsActivity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {





////////////// DECLARACIONES DE VARIABLES ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final int NONE = -1;
    private static final int INTERN = 0; // radio id: interno
    private static final int PRIVATE = 1;
    private static final int PERMISO_LEER_ESCRIBIR = 4;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;

    private static final String TAG = "si";

    private String nombreFichero, valorFichero;

    private int tipo;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





////////////// DECLARACIONES DEL ARRAYLIST //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<Contacto> contactos;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





////////////// DECLARACIONES DE LAS VISTAS DEL LAYOUT ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Button btImportarContactos, btGuardarArchivo, btCargarArchivo;
    private RadioGroup rgOpcion;
    private TextView tvMostrar;
    private RadioButton rbInterno, rbPrivado;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





    // *** ============ Método onCreate ============================================================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initEvents();

    }

    //===============================================================================================================================================================================





    // *** ============ Método initComponents ============================================================================================================================================

        // ¿ Qué hace ? === Une los componentes del layout con los declarados en la clase

    public void initComponents(){

    rgOpcion = findViewById(R.id.rgOpcion);
    btImportarContactos = findViewById(R.id.btImportarContactos);
    tvMostrar = findViewById(R.id.tvCargarContactos);
    btGuardarArchivo = findViewById(R.id.btGuardarArchivo);
    btCargarArchivo = findViewById(R.id.btCargarArchivo);
    rbInterno = findViewById(R.id.rbInterno);
    rbPrivado = findViewById(R.id.rbPrivado);

    }

    //===============================================================================================================================================================================





    // *** ============ Método asignarEventos ============================================================================================================================================

        // ¿ Qué hace ? === Crea los eventos de los botones de importar contactos, de leer contactos y de escribir contactos en 1 archivo

    private void initEvents() {

        btImportarContactos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    // ¿ Qué hace ? === Cuando se haga click en el boton de importar contactos, llama al metodo que comprueba si tenemos permisos para leer los contactos, y se accede al metodo mostrar contactos

                comprobarPermisosContactos();
                msg ("Contactos importados");

            }

        });

        btGuardarArchivo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (contactos == null) {
                    msg ("Debes de importar los contactos antes");
                } else if(rbPrivado.isChecked()) {
                    intentGuardarActivity();
                        } else if (rbInterno.isChecked()) {
                            intentGuardarInternoActivity();
                                } 
                    }




        });

        btCargarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rbPrivado.isChecked()) {
                    intentCargarActivity();
                } else if (rbInterno.isChecked()) {
                    intentCargarInternoActivity();
                }


            }
        });

    }

    //===============================================================================================================================================================================



    private void intentGuardarActivity() {

        Intent intent = new Intent(this, GuardarContactosActivity.class);
        intent.putExtra("contactos", (Serializable) contactos);
        startActivity(intent);

    }

    private void intentGuardarInternoActivity() {

        Intent intent = new Intent(this, GuardarContactosInternoActivity.class);
        intent.putExtra("contactos", (Serializable) contactos);
        startActivity(intent);

    }


    private void intentCargarActivity() {
        Intent intent = new Intent(this, CargarArchivoActivity.class);
        startActivity(intent);
    }

    private void intentCargarInternoActivity() {
        Intent intent = new Intent(this, CargarArchivoInternoActivity.class);
        startActivity(intent);
    }




////////////////////////////////////////////////// IMPORTAR CONTACTOS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////// IMPORTAR CONTACTOS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////// IMPORTAR CONTACTOS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    // *** ============ Método getListaContactos ====================================================================================================================================

    public List<Contacto> getListaContactos(){

        String phoneNo = "";

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1","1"};
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int indiceNombre = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        // int indiceTelefono = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        List<Contacto> lista = new ArrayList<>();
        Contacto contacto;


        ContentResolver cr = getContentResolver();
        while(cursor.moveToNext()){
            contacto = new Contacto();
            contacto.setId(cursor.getLong(indiceId));
            contacto.setNombre(cursor.getString(indiceNombre));

            if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (pCur.moveToNext()) {
                    phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                pCur.close();
            }

            contacto.setTelefono(phoneNo);
            lista.add(contacto);
        }

        return lista;

    }

    //===============================================================================================================================================================================



    // *** ============ Método mostrarContactos ==========================================================================================================================================

    public void mostrarContactos(){

        contactos = getListaContactos();
        tvMostrar.setText(contactos.toString());

    }

    //===============================================================================================================================================================================



    // *** ============ Método comprobarPermisosContactos ==========================================================================================================================================

    private void comprobarPermisosContactos() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mostrarContactos();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                    Toast.makeText(this,R.string.razon,Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                }else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                mostrarContactos();
            }

        }

    }

    //===============================================================================================================================================================================



////////////////////////////////////////////////// IMPORTAR CONTACTOS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////// IMPORTAR CONTACTOS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////// IMPORTAR CONTACTOS ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////







    //============ Método obtenerTipoSeleccionado ===================================================================================================================================

    private static int getCheckedType (int item) {

        int tipo = NONE;

        switch(item) {
            case R.id.rbInterno:
                tipo = INTERN;
                break;

            case R.id.rbPrivado:
                tipo = PRIVATE;
                break;
        }

        return tipo;

    }

    //===============================================================================================================================================================================





    //============ Método getFile ===================================================================================================================================================

    private File getFile (int tipo) {
        File file = null;
        switch(tipo) {
            case INTERN:
                file = this.getFilesDir();
                break;
            case PRIVATE:
                file = this.getExternalFilesDir(null);
                break;
        }
        return file;
    }

    //===============================================================================================================================================================================





    //============ Método Toast (para comprobar) ====================================================================================================================================

    private void msg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    //===============================================================================================================================================================================





    /* ON CREATE OPTIONS MENU
    *
    * Coge el menu creado, y lo infla, de forma que aparezca y podamos utilizar nuestro menu :D
    *
     */

    //===============================================================================================================================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //===============================================================================================================================================================================





    //===============================================================================================================================================================================

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnSettings:
                showSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //===============================================================================================================================================================================





    //===============================================================================================================================================================================

    private void showSettings() {

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    //===============================================================================================================================================================================

}
