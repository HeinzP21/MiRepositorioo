package edu.uoc.android.restservice.ui.enter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.uoc.android.restservice.R;
import edu.uoc.android.restservice.rest.adapter.GitHubAdapter;
import edu.uoc.android.restservice.rest.model.Owner;

public class EnterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUser;
    private Button btnFollowers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_user);
        initViews();
    }



    private void initViews() {
        etUser = findViewById(R.id.enter_user_edit_text);
        btnFollowers = findViewById(R.id.enter_user_button);
        btnFollowers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnFollowers) {
            Intent infoUser = new Intent(this, InfoUserActivity.class); // Creación de intent para ir a la otra actividad
            infoUser.putExtra("loginName", etUser.getText().toString()); //Se envía como valor el nombre del usuaro de github a buscar
            startActivity(infoUser); // Se inicia la nueva actividad
        }
    }
}
