package com.grupoelite.cine;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by Mikel Gil on 28/04/2016.
 */
public class MainActivity extends Activity implements View.OnClickListener{

    Peliculas peliculas;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se crea un layout de 2 filas y con scroll horizontal
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        //Se crea la vista con el layout
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        //Obtener los datos de un archivo JSON o XML
        Consulta();
    }
    @Override
    public void onClick(View v){
        Toast.makeText(this, "pulsado " + v.getId(), Toast.LENGTH_SHORT).show();
    }

    //Metodo que pasandole una url hace una consulta del archivo JSON o XML
    public void Consulta(){
        ProcessJSON consulta = new ProcessJSON();
        consulta.execute("http://www.sotoan.com/Curso/cine.JSON");
    }

    //Metodo para procesar el contenido del archivo JSON o XML
    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);
            // Return the data from specified url
            return stream;
        }
        protected void onPostExecute(String stream){
            //..........Process JSON DATA................
            if(stream !=null){
                //Se crea la lista de objetos de peliculas desde el JSON utilizando la libreria de terceros Gson
                final Gson gson = new Gson();
                peliculas = gson.fromJson(stream, Peliculas.class);

                //Se crea un adaptador personalizado llamado MovieAdapter pasandole la lista de peliculas
                MovieAdapter adaptador = new MovieAdapter(MainActivity.this, peliculas);

                //Se crea la vista pasandole el adaptador
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(adaptador);
                //Se pone un "escuchador" para cargar una pelicula en concreto al darle click
                adaptador.setOnItemClickListener(onItemClickListener);
            }
        }
    }

    //Metodo que llama a otro activity y le pasa la pelicula para ver los detalles de esta
    MovieAdapter.OnItemClickListener onItemClickListener = new MovieAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Pelicula miPelicula = peliculas.getData().get(position);
            Intent i = new Intent(MainActivity.this, WebActivity.class);
            i.putExtra(WebActivity.EXTRA_PARAM_PELICULA, miPelicula);

            startActivity(i);
        }
    };
}
