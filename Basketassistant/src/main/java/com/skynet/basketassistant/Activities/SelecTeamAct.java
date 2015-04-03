package com.skynet.basketassistant.Activities;

import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Fragments.FragDialog_YesNo;
import com.skynet.basketassistant.Fragments.Frag_expequip;
import com.skynet.basketassistant.Fragments.Frag_listaequip;
import com.skynet.basketassistant.Fragments.Frag_newteam;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.R;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class SelecTeamAct extends BaseActivity implements View.OnClickListener,Frag_listaequip.Callbacks,FragDialog_YesNo.OnCompleteYesNoDialogListener {

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
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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
        Frag_expequip frag = Frag_expequip.getInstance(equip.getId());
        CambiarFrameLayout(frag);
    }

    public void refreshTeams(){  //se llama desde el fragment frag_newteam al hacer la insercion
                                        //aca se refrescara el framelayout con el equipo recien creado (como si lo ubiera seleccionado)
        frag_listaequipos.Refrescar();
    }

    public void RefrescarListaEquiposAdd(Equipo equip){
        frag_listaequipos.Refrescar(equip);
    }

    public void CambiarFrameLayout(Fragment frag){
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_contdetalle,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void CambiarFrameLayoutLista(Fragment frag){
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_lista,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    //*---------------------------- DELETE TEAMS BEHAVIUOR --------------------------------
    private void deleteTeam(int teamId){
        DBEquipos dbe = new DBEquipos(this);
        dbe.eliminar(teamId,DBEquipos.CN_ID);
        dbe.Cerrar();
    }

    @Override
    public void onCompleteYesNoDialog(int response, int whocall) {  //whoCall in this case contains the team's ID that the app uses to delete it from database
        deleteTeam(whocall);
        refreshTeams();
    }
    //*-----------------------------------------------------------------------------------
}

