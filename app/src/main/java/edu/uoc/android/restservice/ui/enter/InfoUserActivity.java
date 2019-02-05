package edu.uoc.android.restservice.ui.enter;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import edu.uoc.android.restservice.R;
import edu.uoc.android.restservice.rest.adapter.GitHubAdapter;
import edu.uoc.android.restservice.rest.model.Followers;
import edu.uoc.android.restservice.rest.model.Owner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoUserActivity extends AppCompatActivity {

    ArrayList<Owner> listaFollowers;
    RecyclerView recyclerViewFollowers;

    TextView textViewRepositories, textViewFollowing;
    ImageView imageViewProfile;

    ProgressDialog progressDialog, progressDialog2;  // Cuadro ce dialogo de progreso


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        textViewFollowing = findViewById(R.id.textViewFollowing);
        textViewRepositories = findViewById(R.id.textViewRepositories);
        imageViewProfile = (ImageView) findViewById(R.id.imageViewProfile);


        listaFollowers = new ArrayList<>();
        recyclerViewFollowers = (RecyclerView)findViewById(R.id.recyclerViewFollowers);

        recyclerViewFollowers.setLayoutManager(new LinearLayoutManager(this));

        String loginName = getIntent().getStringExtra("loginName");

        //initProgressBar();

        mostrarDatosBasicos(loginName);
    }

    TextView labelFollowing, labelRepositories, labelFollowers;
    private void initProgressBar()
    {
        //progressBar.setVisibility(View.VISIBLE);
        textViewFollowing.setVisibility(View.INVISIBLE);
        textViewRepositories.setVisibility(View.INVISIBLE);
        imageViewProfile.setVisibility(View.INVISIBLE);
        recyclerViewFollowers.setVisibility(View.INVISIBLE);

        labelFollowing = (TextView)findViewById(R.id.labelFollowing);
        labelFollowing.setVisibility(View.INVISIBLE);

        labelRepositories = (TextView) findViewById(R.id.labelRepositories);
        labelRepositories.setVisibility(View.INVISIBLE);

        labelFollowers = (TextView) findViewById(R.id.labelFollowers);
        labelFollowers.setVisibility(View.INVISIBLE);

    }

    private void mostrarDatosBasicos(String loginName){
        progressDialog = new ProgressDialog(this);  // Nueva insstancia de progressdialog
        progressDialog.setMessage("Buscando usuario");  // Mensaje a mostrar en cuadro de dialogo de  progreso
        progressDialog.show();  // Mostrar cuadro de dialogo de progreso
        GitHubAdapter adapter = new GitHubAdapter();
        Call<Owner> call = adapter.getOwner(loginName);
        call.enqueue(new Callback<Owner>() {

            @Override
            public void onResponse(Call<Owner> call, Response<Owner> response) {
                progressDialog.dismiss(); // Finalización de progressdialog
                Owner owner = response.body();
                if (owner != null) {    // Si hay resultados
                    textViewRepositories.setText(owner.getPublicRepos().toString()); // Se envia el numero de repositorios publicos al textview
                    textViewFollowing.setText(owner.getFollowing().toString()); // Se envia el numero de personas que sigue el usuario a un textview
                    Picasso.get().load(owner.getAvatarUrl()).into(imageViewProfile); // Se envia la imagen del usuario a un imageview
                }else { // si no hay resultados muestra mensaje de error
                    Toast.makeText(InfoUserActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Owner> call, Throwable t) {
                // Estado de fallo. Muestra un mensaje de error
                Toast.makeText(InfoUserActivity.this, "Ha ocurrido un error al realizar la petición", Toast.LENGTH_SHORT).show();
            }
        });

        // Nuevo progressdialog que aparecerá mientras busca los seguidores
        progressDialog2 = new ProgressDialog(this); // Nueva instancia a progressdialog
        progressDialog2.setMessage("Buscando seguidores"); // mensaje a mostrar
        progressDialog2.show(); // Mostrar progressdialog

        // Se hace una llamada a la lista de seguidores del usuario, enviando como parámetro el nombre de este
        Call<List<Followers>> callFollowers = new GitHubAdapter().getOwnerFollowers(loginName);

        callFollowers.enqueue(new Callback<List<Followers>>() {
            @Override
            public void onResponse(Call<List<Followers>> call, Response<List<Followers>> response) {
                progressDialog2.cancel();
                List<Followers> list = response.body(); // Se guarda en una lista el resultado obtenido
                if (list != null) { // Si hay contenido
                    // Se envía la lista al adaptador de seguidores para mostrar los datos
                    AdaptadorFollowers adapter = new AdaptadorFollowers(list);
                    recyclerViewFollowers.setAdapter(adapter);
                } else {
                    // Si el usuario ingresado no existe se muestra mensaje de error
                    Toast.makeText(InfoUserActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Followers>> call, Throwable t) {
                // Al ocurrir un fallo en la peticion muestra un error
                Toast.makeText(InfoUserActivity.this, "Ha ocurrido un error al realizar la petición", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
