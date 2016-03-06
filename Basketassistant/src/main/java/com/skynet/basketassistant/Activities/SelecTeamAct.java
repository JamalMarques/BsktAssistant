package com.skynet.basketassistant.Activities;

import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Fragments.FragDialog_YesNo;
import com.skynet.basketassistant.Fragments.Frag_expequip;
import com.skynet.basketassistant.Fragments.Frag_listaequip;
import com.skynet.basketassistant.Fragments.Frag_newteam;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.UserContainer;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class SelecTeamAct extends BaseActivity implements View.OnClickListener,Frag_listaequip.Callbacks,FragDialog_YesNo.OnCompleteYesNoDialogListener {

    private ImageButton IBAddTeam, IBLogout;
    private Frag_listaequip teamListFragment;
    private Frag_expequip actualDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selec_team);

        TextView nomusr = (TextView)findViewById(R.id.tvuser);
        nomusr.setText(UserContainer.DameUser().getNombre());

        IBAddTeam = (ImageButton)findViewById(R.id.ibagregarteam);
        IBLogout = (ImageButton)findViewById(R.id.iblogout);

        IBAddTeam.setOnClickListener(this);
        IBLogout.setOnClickListener(this);

        teamListFragment = Frag_listaequip.getInstance();
        CambiarFrameLayoutLista(teamListFragment);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == IBAddTeam.getId()){  //Add new team!
            addTeamFragmentBehavior();
        }else
            if(view.getId() == IBLogout.getId() ){  //Redirect to the Login Activity!
                FragDialog_YesNo frag = FragDialog_YesNo.getInstance(getString(R.string.logout_message), Constants.YES_NO_LOG_OUT);
                frag.show(getSupportFragmentManager(),"YES_NO_MESSAGE");
                //onBackPressed();
            }

    }

    private void addTeamFragmentBehavior(){
        Frag_newteam addFragment = (Frag_newteam) getSupportFragmentManager().findFragmentByTag(getString(R.string.new_team_fragment_TAG));
        if( addFragment == null || !addFragment.isVisible() ) {
            Frag_newteam frag = new Frag_newteam();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.framelayout_contdetalle, frag, getString(R.string.new_team_fragment_TAG));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }

    @Override
    public void onSelecciondeItemEquipo(Equipo equip) {  //Aca va a venir luego del onItemClick() del Fragment1
        actualDetailFragment = Frag_expequip.getInstance(equip.getId());
        CambiarFrameLayout(actualDetailFragment);
    }

    @Override
    public void onSelectionNewTeam() {
        addTeamFragmentBehavior();
    }

    public void refreshTeams(){  //se llama desde el fragment frag_newteam al hacer la insercion
                                        //aca se refrescara el framelayout con el equipo recien creado (como si lo ubiera seleccionado)
        teamListFragment.Refrescar();
    }

    public void RefrescarListaEquiposAdd(Equipo equip){
        teamListFragment.Refrescar(equip);
    }

    public void CambiarFrameLayout(Fragment frag){
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_contdetalle,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void CambiarFrameLayoutLista(Fragment frag){
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_lista, frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void ClearDetailFragment(Fragment fragment){
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    //*---------------------------- DELETE TEAMS BEHAVIUOR --------------------------------
    private void deleteTeam(int teamId){
        DBEquipos dbe = new DBEquipos(this);
        dbe.eliminar(teamId, DBEquipos.CN_ID);
        dbe.Cerrar();
        refreshTeams();
        ClearDetailFragment(actualDetailFragment);
    }

    @Override
    public void onCompleteYesNoDialog(int response, int whocall) {
        if(whocall == Constants.YES_NO_LOG_OUT && response == Constants.YES){
            UserContainer.DesloguearUser(this);
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            if(whocall == Constants.YES_NO_DELETE_TEAM && response == Constants.YES ){
                deleteTeam(Frag_listaequip.idToDelete);
            }
            Frag_listaequip.idToDelete = null;
        }
    }
    //*-----------------------------------------------------------------------------------

}

