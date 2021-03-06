package com.example.brendelsantos.jogosd.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.brendelsantos.jogosd.Adapter.CustomAdapterJogador;
import com.example.brendelsantos.jogosd.Model.Jogador;
import com.example.brendelsantos.jogosd.R;
import com.example.brendelsantos.jogosd.Tasks.ClienteTask;
import com.example.brendelsantos.jogosd.Tasks.SocketServerThread;
import com.example.brendelsantos.jogosd.Util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public final static String JOGADOR_HOST = "com.example.brendelsantos.jogosd.Activity.JogoActivity.JOGADOR";
    public final static  String IP_SERVER =  "192.168.1.10";
    public final static  int ID_JOGADOR =  2;

    private JsonUtil jsonUtil;
    private List jogadores;
    private ListView listaJogadores;
    private CustomAdapterJogador adapter;
    private SocketServerThread serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        jsonUtil = new JsonUtil();
        String json = "";
        ClienteTask myClientTask = new ClienteTask(
                IP_SERVER,
                6783, "" + "jogador#todos#"+ID_JOGADOR, json);
        try {
            json = myClientTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        jogadores = new ArrayList<Jogador>();
        jogadores = jsonUtil.gerarLista(Jogador.class, json);

        listaJogadores = (ListView) findViewById(R.id.lista_amigos);
        adapter = new CustomAdapterJogador(this.getApplicationContext(), jogadores);
        listaJogadores.setAdapter(adapter);
        listaJogadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Jogador jogadorSelecionado = (Jogador) listaJogadores.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), JogoActivity.class);
                intent.putExtra(JOGADOR_HOST, jogadorSelecionado.getHost());
                startActivity(intent);
            }
        });
        serverSocket =  new SocketServerThread(this, 8080);
        Thread socketServerThread = new Thread(serverSocket);
        socketServerThread.start();
    }

    public void abrirJogoActivity() {
        Intent intent = new Intent(getApplicationContext(), JogoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(), JogoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
