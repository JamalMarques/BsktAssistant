package com.skynet.basketassistant.Activities;

import com.skynet.basketassistant.Fragments.Frag_expequip;
import com.skynet.basketassistant.Fragments.Frag_listaequip;
import com.skynet.basketassistant.Fragments.Frag_newteam;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.util.SystemUiHider;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class SelecTeamAct extends BaseActivity implements View.OnClickListener,Frag_listaequip.Callbacks {

    private SystemUiHider mSystemUiHider;

    private ImageButton ibagregarteam,iblogout;
    private Frag_listaequip frag_listaequipos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selec_team);

        TextView nomusr = (TextView)findViewById(R.id.tvuser);
        nomusr.setText(UserContainer.DameUser().getNombre());

        ibagregarteam = (ImageButton)findViewById(R.id.ibagregarteam);
        iblogout = (ImageButton)findViewById(R.id.iblogout);

        ibagregarteam.setOnClickListener(this);
        iblogout.setOnClickListener(this);

        frag_listaequipos = Frag_listaequip.getInstance();
        CambiarFrameLayoutLista(frag_listaequipos);
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
        //Frag_expequip frag = new Frag_expequip(equip);
        //get instance
        Frag_expequip frag = Frag_expequip.getInstance(equip.getId());
        CambiarFrameLayout(frag);
    }

    public void RefrescarListaEquipos(){  //se llama desde el fragment frag_newteam al hacer la insercion
                                        //aca se refrescara el framelayout con el equipo recien creado (como si lo ubiera seleccionado)
        frag_listaequipos.Refrescar();
    }

    public void RefrescarListaEquiposAdd(Equipo equip){
        frag_listaequipos.Refrescar(equip);
    }

    public void CambiarFrameLayout(Fragment frag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_contdetalle,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void CambiarFrameLayoutLista(Fragment frag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_lista,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

}

