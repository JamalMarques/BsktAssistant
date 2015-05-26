package com.skynet.basketassistant.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBCiudades;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.Datos.DBUsuarios;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.Modelo.Usuario;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.Otros.Manejo_Imagenes;
import com.skynet.basketassistant.R;


public class MainActivity extends BaseActivity implements View.OnClickListener{


    private TextView etnom;
    private TextView etpass;
    private Button ibnext;
    private Button ibadd;
    private ImageButton bWearable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InicializarManejoImagenes();

        etnom = (TextView) findViewById(R.id.etnom);
        etpass = (TextView) findViewById(R.id.etpass);
        ibnext = (Button) findViewById(R.id.ibnext);
        ibadd = (Button) findViewById(R.id.ibadd);
        bWearable = (ImageButton) findViewById(R.id.bWearable);

        ibnext.setOnClickListener(this);
        ibadd.setOnClickListener(this);
        bWearable.setOnClickListener(this);

        UserContainer.DesasignarUser();   //DETACHING USER!

        if (Constants.TESTING_MODE) {
            GenerateTestingMode();
            Constants.TESTING_MODE = false;
        }
    }


    public void GenerateTestingMode(){
        //------------ testeo
        DBUsuarios dbus = new DBUsuarios(this);
        dbus.Modoescritura();
        dbus.insertar("Yamil","1234","yama_marques@hotmail.com");
        dbus.Cerrar();
        ///-----------------------
        DBCiudades dbc = new DBCiudades(this);
        dbc.Modoescritura();
        dbc.insertar("Mar del Plata", "Buenos Aires");
        dbc.insertar("Buenos Aires", "Buenos Aires");

        DBPartidos dbp = new DBPartidos(this);
        dbp.Modoescritura();
        dbp.insertar("2014-01-02","Visita",1,"Miami",89,68,19,20,25,25,15,18,21,14,0,0);
        dbp.insertar("2014-06-20","Visita",1,"Spurs",39,68,19,20,25,25,15,18,21,14,10,5);
        dbp.insertar("2014-02-15","Visita",1,"OKC",109,68,19,20,25,25,15,18,21,14,18,13);
        dbp.insertar("2014-02-15","Local",1,"Bulls",99,68,19,20,25,25,15,18,21,14,10,10);
        dbp.insertar("2014-02-15","Local",1,"Raptors",59,68,19,20,25,25,15,18,21,14,20,20);
        dbp.insertar("2014-02-15","Visita",1,"Quilmes",69,68,19,20,25,25,15,18,21,14,15,15);
        dbp.insertar("2014-02-15","Local",1,"Bahia Blanca",69,68,19,20,25,25,15,18,21,14,12,18);
        dbp.insertar("2014-02-15","Visita",1,"Dukes",89,68,19,20,25,25,15,18,21,14,1,20);
        dbp.insertar("2014-02-15","Local",1,"Lakers",100,68,19,20,25,25,15,18,21,14,9,14);
        dbp.insertar("2014-02-15","Local",1,"Fraks",67,68,19,20,25,25,15,18,21,14,23,29);
        dbp.Cerrar();

        DBJugadores dbj = new DBJugadores(this);
        dbj.Modoescritura();
        dbj.insertar("James", "Lebron", 203, 97, 6, "Ala-Privot", 1, "empty");
        dbj.insertar("Gasol","Paul",192,85,10,"Ayuda-Base",1,"empty");
        dbj.insertar("Wade","Dwyane",192,87,27,"Alero",1,"empty");
        dbj.insertar("Michael","Jordan",198,87,23,"Alero",1,"empty");
        dbj.insertar("Bryant","Kobe",198,83,24,"Alero",1,"empty");
        dbj.insertar("Howard","Dwight",212,102,14,"Pivot",1,"empty");
        dbj.insertar("Rondo","Rajon",185,84,9,"Base",1,"empty");
        dbj.insertar("Pierce","Paul",201,106,34,"Ala-Pivot",1,"empty");
        dbj.insertar("Oneal","Shaquille",216,147,32,"Pivot",1,"empty");
        dbj.insertar("Rose","Derrick",191,86,1,"Base",1,"empty");
        dbj.insertar("Harden","James",196,102,13,"Alero",1,"empty");
        dbj.insertar("Duncan","Tim",211,117,21,"Pivot",1,"empty");
        dbj.insertar("Leonard","Kawhi",201,104,2,"Alero",1,"empty");
        //dbj.Cerrar();

        DBEquipos asd = new DBEquipos(this);
        asd.Modoescritura();
        asd.insertar("Dream Team",1,1,"Primera");
        asd.insertar("Union",1,1,"Juveniles");
        asd.insertar("Harlem Globetrotters",1,1,"Juveniles");
        asd.insertar("Miami Heat",1,1,"Juveniles");
        asd.insertar("Spurs",1,1,"Juveniles");
        asd.insertar("Bulls",1,1,"Juveniles");
        asd.insertar("Thunder",1,1,"Juveniles");
        asd.insertar("Speeders",1,1,"Juveniles");
        asd.insertar("Toon Squad",1,1,"Juveniles");
        //asd.Cerrar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) { //checkear despues como hacerlo en case

        //PRESIONA NEXT
        if(view.getId() == ibnext.getId()){

            if(etnom.getText().toString().equals("") == false || etpass.getText().toString().equals("") == false){
                DBUsuarios dbus = new DBUsuarios(this);
                dbus.Modolectura();
                Usuario usr = dbus.BuscarUsuario(etnom.getText().toString());
                dbus.Cerrar();
                if(usr != null) {
                    if (usr.getPass().toString().equals(etpass.getText().toString())) {
                        //redirigir a la nueva activity
                        Intent intent = new Intent(MainActivity.this, SelecTeamAct.class);
                        UserContainer.AsignarUser(usr);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(),"Password erroneo...", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"El usuario no existe",Toast.LENGTH_SHORT).show();
            }
            else
            {Toast.makeText(getApplicationContext(),"Complete los campos requeridos",Toast.LENGTH_SHORT).show();}
        }
        else{
            if(view.getId() == ibadd.getId()){
               Intent intent = new Intent(MainActivity.this, NewUserAct.class);
               startActivity(intent);
            }else{
                if(view == bWearable){
                    Intent intent = new Intent(MainActivity.this, WearableConfigurationActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void InicializarManejoImagenes(){
        Manejo_Imagenes.ImageNoPlayer = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher); //Cargo la imagen del noplayer
    }
}
