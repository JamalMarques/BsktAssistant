package com.skynet.basketassistant.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.skynet.basketassistant.Otros.Manejo_Imagenes;
import com.skynet.basketassistant.R;


public class MainActivity extends BaseActivity implements View.OnClickListener{


    private TextView etnom;
    private TextView etpass;
    private ImageButton ibnext;
    private ImageButton ibadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InicializarManejoImagenes();

       etnom = (TextView)findViewById(R.id.etnom);
       etpass = (TextView)findViewById(R.id.etpass);
       ibnext = (ImageButton)findViewById(R.id.ibnext);
       ibadd = (ImageButton)findViewById(R.id.ibadd);

       ibnext.setOnClickListener(this);
       ibadd.setOnClickListener(this);

        UserContainer.DesasignarUser();   //DETACHING USER!
       // GenerarTesteo();
    }


    public void GenerarTesteo(){
        //------------ testeo
        DBUsuarios dbus = new DBUsuarios(this);
        dbus.Modoescritura();
        dbus.insertar("yamil","1234","yama_marques@hotmail.com");
        dbus.Cerrar();
        ///-----------------------
        DBCiudades dbc = new DBCiudades(this);
        dbc.Modoescritura();
        dbc.insertar("Mar del Plata","Buenos Aires");
        dbc.insertar("Buenos Aires","Buenos Aires");

        DBPartidos dbp = new DBPartidos(this);
        dbp.Modoescritura();
        dbp.insertar("2014-01-02","Miami",1,"Miami",89,68,19,20,25,25,15,18,21,14,0,0);
        dbp.insertar("2014-06-20","Spurs",1,"Spurs",39,68,19,20,25,25,15,18,21,14,10,5);
        dbp.insertar("2014-02-15","OKC",1,"OKC",109,68,19,20,25,25,15,18,21,14,18,13);
        dbp.insertar("2014-02-15","Bulls",1,"Bulls",99,68,19,20,25,25,15,18,21,14,10,10);
        dbp.insertar("2014-02-15","Raptors",1,"Raptors",59,68,19,20,25,25,15,18,21,14,20,20);
        dbp.insertar("2014-02-15","Local",1,"Quilmes",69,68,19,20,25,25,15,18,21,14,15,15);
        dbp.insertar("2014-02-15","Local",1,"Bahia Blanca",69,68,19,20,25,25,15,18,21,14,12,18);
        dbp.insertar("2014-02-15","Visita",1,"Dukes",89,68,19,20,25,25,15,18,21,14,1,20);
        dbp.insertar("2014-02-15","Local",1,"Lakers",100,68,19,20,25,25,15,18,21,14,9,14);
        dbp.insertar("2014-02-15","Local",1,"Fraks",67,68,19,20,25,25,15,18,21,14,23,29);
        dbp.Cerrar();

        DBJugadores dbj = new DBJugadores(this);
        dbj.Modoescritura();
        dbj.insertar("James","Lebron",2.03,97,6,"Ala-Privot",1,"empty");
        dbj.insertar("Chalmers","Mario",1.92,85,10,"Ayuda-Base",1,"empty");
        dbj.insertar("Marques","Yamil",1.92,87,27,"Alero",1,"empty");
        dbj.insertar("Michael","Jordan",1.98,87,23,"Alero",1,"empty");
        dbj.insertar("Briant","Kobe",1.98,83,24,"Alero",1,"empty");
        dbj.insertar("Howard","Dwayne",2.12,102,14,"Pivot",1,"empty");
        //dbj.Cerrar();

        DBEquipos asd = new DBEquipos(this);
        asd.Modoescritura();
        asd.insertar("Sporting",1,1,"Primera");
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
        }
        }
    }

    public void InicializarManejoImagenes(){
        Manejo_Imagenes.ImageNoPlayer = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher); //Cargo la imagen del noplayer
    }
}
