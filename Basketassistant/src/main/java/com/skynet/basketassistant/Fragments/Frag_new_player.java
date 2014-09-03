package com.skynet.basketassistant.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Activities.JugadoresAct;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 05/07/14.
 */
public class Frag_new_player extends Fragment implements View.OnClickListener{

    private Equipo equipo;

    private ImageButton b_back,b_add;
    private EditText et_apellido,et_nombre,et_altura,et_peso,et_numero;
    private TextView tv_hidden_url;
    private Spinner spinner_roles;
    private ImageView iv_photoplayer;

    /*private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private Intent intent;
    private int codigo;*/

    public Frag_new_player(Equipo eq){
        equipo = eq;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.frag_new_player,container,false);

        b_back = (ImageButton)view.findViewById(R.id.b_back);
        b_add = (ImageButton)view.findViewById(R.id.b_add);
        b_back.setOnClickListener(this);
        b_add.setOnClickListener(this);

        et_apellido = (EditText)view.findViewById(R.id.et_apellido);
        et_nombre = (EditText)view.findViewById(R.id.et_nombre);
        et_altura = (EditText)view.findViewById(R.id.et_altura);
        et_peso = (EditText)view.findViewById(R.id.et_peso);
        et_numero = (EditText)view.findViewById(R.id.et_numero);
        spinner_roles = (Spinner)view.findViewById(R.id.spinner_roles);
        tv_hidden_url = (TextView)view.findViewById(R.id.tv_hidden_url);

        ArrayAdapter<CharSequence> adap_cat = ArrayAdapter.createFromResource(getActivity(),R.array.roles, R.layout.spinner_item);
        spinner_roles.setAdapter(adap_cat);

        return view;
    }

    @Override
    public void onClick(View view) {

        if( view.getId() == b_back.getId() ){
            ((JugadoresAct)getActivity()).VaciarFragment2(this);
        }
        else
            if( view.getId() == b_add.getId() ){
                if( et_apellido.getText().toString().equals("") || et_numero.getText().toString().equals("") || et_nombre.getText().toString().equals("") || et_altura.getText().toString().equals("") || et_peso.getText().toString().equals("") || et_numero.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Complete todos los campos",Toast.LENGTH_SHORT).show();
                }else
                    if ( et_apellido.getText().toString().length() < 4 || et_nombre.getText().toString().length() < 4){
                        Toast.makeText(getActivity(),"El apellido y el nombre deben tener minimo 4 caracteres",Toast.LENGTH_SHORT).show();
                    }else{
                        //Creo el jugador!
                        DBJugadores dbj = new DBJugadores(getActivity());
                        dbj.Modoescritura();
                        dbj.insertar(et_apellido.getText().toString(),et_nombre.getText().toString(),Integer.valueOf(et_altura.getText().toString()),Integer.valueOf(et_peso.getText().toString()),Integer.valueOf(et_numero.getText().toString()),
                                        spinner_roles.getSelectedItem().toString(),equipo.getId(),tv_hidden_url.getText().toString());
                        dbj.Cerrar();
                    }
            }
            /*else
                if(view.getId() == iv_photoplayer.getId()){
                    CharSequence opciones[] = new CharSequence[]{"Seleccionar de Galeria","Sacar Foto"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Seleccione Opcion");
                    builder.setItems(opciones,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                if( which == 0 ){  //Seleccionar de la Galeria
                                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                    codigo = SELECT_PICTURE;
                                }else{  //Sacar foto
                                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    codigo = TAKE_PICTURE;
                                    Uri output = Uri.fromFile(new File(Manejo_Imagenes.Url+jugador.getApellido()+String.valueOf(jugador.getId())+".jpg"));  //ej: Marques27.jpg
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,output);
                                }

                                startActivityForResult(intent, codigo);
                        }
                    });
                    builder.show();
                }*/
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
