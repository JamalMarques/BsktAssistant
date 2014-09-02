package com.skynet.basketassistant;

import com.skynet.basketassistant.Fragments.Frag_expequip;
import com.skynet.basketassistant.Fragments.Frag_listaequip;
import com.skynet.basketassistant.Fragments.Frag_newteam;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class SelecTeamAct extends Activity implements View.OnClickListener,Frag_listaequip.Callbacks {

    private SystemUiHider mSystemUiHider;

    Button ibagregarteam,iblogout;
    //Bundle bun_usr;    //bun_usr.getString("User") ---> nombre del usuario!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selec_team);

        //bun_usr = getIntent().getExtras();
        //bun_usr.getString("User") ---> nombre del usuario!

        TextView nomusr = (TextView)findViewById(R.id.tvuser);
        nomusr.setText(UserContainer.DameUser().getNombre());

        ibagregarteam = (Button)findViewById(R.id.ibagregarteam);
        iblogout = (Button)findViewById(R.id.iblogout);

        ibagregarteam.setOnClickListener(this);
        iblogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == ibagregarteam.getId()){  //Add new team!

            Frag_newteam frag = new Frag_newteam();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout_contdetalle,frag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();

        }else
            if(view.getId() == iblogout.getId() ){  //Redirect to the Login Activity!
                Intent intent = new Intent(SelecTeamAct.this, MainActivity.class);
                startActivity(intent);
            }

    }

    @Override
    public void onSelecciondeItemEquipo(Equipo equip) {  //Aca va a venir luego del onItemClick() del Fragment1
        Frag_expequip frag = new Frag_expequip(equip);
        CambiarFrameLayout(frag);
    }

    public void RefrescarListaFrag1(){  //se llama desde el fragment frag_newteam al hacer la insercion
                                        //aca se refrescara el framelayout con el equipo recien creado (como si lo ubiera seleccionado)
        Frag_listaequip fraglista = (Frag_listaequip)getFragmentManager().findFragmentById(R.id.fragment);
        fraglista.Refrescar();
    }

    public void CambiarFrameLayout(Fragment frag){
        //IDEA! usar este metodo como generico para cambiar el framelayout segun nuestra voluntad. el fragment
        // enviado debe estar totalmente cargado y listo para solo ser mostrado.
        //frag.setArguments(bun_usr);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_contdetalle,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

}

