package com.skynet.basketassistant.Activities;

import com.skynet.basketassistant.Datos.DBUsuarios;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NewUserAct extends BaseActivity implements View.OnClickListener{

    private SystemUiHider mSystemUiHider;

    private TextView etnom;
    private TextView etpass1;
    private TextView etpass2;
    private TextView etcorreo;
    private ImageButton ibok;
    private ImageButton ibback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_user);

        etnom = (TextView)findViewById(R.id.etnom);
        etpass1 = (TextView)findViewById(R.id.etpass1);
        etpass2 = (TextView)findViewById(R.id.etpass2);
        etcorreo = (TextView)findViewById(R.id.etcorreo);
        ibok =  (ImageButton)findViewById(R.id.ibok);
        ibback = (ImageButton)findViewById(R.id.ibback);


        ibok.setOnClickListener(this);
        ibback.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == ibok.getId()){
            if(!(etnom.getText().toString().equals("")) || !(etpass1.getText().toString().equals("")) || !(etpass1.getText().toString().equals(""))
                    || !(etcorreo.getText().toString().equals(""))){
                if(etpass1.getText().toString().equals(etpass2.getText().toString())){
                    //agregar usuario a base de datos!
                    DBUsuarios bdus = new DBUsuarios(this);
                    bdus.Modoescritura();
                    bdus.insertar(etnom.getText().toString(), etpass1.getText().toString(), etcorreo.getText().toString());
                    bdus.Modolectura();
                    UserContainer.AsignarUser(bdus.BuscarUsuario(etnom.getText().toString()));  //Attach user to the static parameter!!
                    Intent intent = new Intent(NewUserAct.this, SelecTeamAct.class);

                    startActivity(intent);
                }
            }
            else
                Toast.makeText(getApplicationContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(view.getId() == ibback.getId()){
                Intent intent = new Intent(NewUserAct.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
