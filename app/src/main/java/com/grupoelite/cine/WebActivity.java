package com.grupoelite.cine;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

/**
 * Created by Mikel Gil on 28/04/2016.
 */

public class WebActivity extends Activity{

    //Variables
    public static final String EXTRA_PARAM_PELICULA = "asdf";

    private ImageView mImageView;
    private TextView mTitle;
    private TextView mDirector;
    private TextView mCast;
    private VideoView mTrailer;

    private Pelicula pelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        //Obtener la pelicula que se pasa de MainActivity
        pelicula = (Pelicula) getIntent().getSerializableExtra(EXTRA_PARAM_PELICULA);

        mImageView = (ImageView) findViewById(R.id.movieImage);
        mTitle = (TextView) findViewById(R.id.textView);
        mDirector = (TextView) findViewById(R.id.director);
        mCast = (TextView) findViewById(R.id.cast);
        mTrailer = (VideoView)findViewById(R.id.trailer);

        loadMovie();
    }

    //Metodo para cargar los datos de la pelicula, incluido su trailer
    private void loadMovie() {
        mTitle.setText(pelicula.getMovie());
        mDirector.setText("DIRECTOR: " + pelicula.getDirector());
        mCast.setText("CAST: " + pelicula.getCast());
        try {
            String link= pelicula.getTrailer();
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(mTrailer);
            Uri video = Uri.parse(link);
            mTrailer.setMediaController(mediaController);
            mTrailer.setVideoURI(video);
            //mTrailer.start();
        } catch (Exception e) {
            Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }

        //Cargar el poster de la pelicula utilizando la libreria de terceros Picasso
        Picasso.with(WebActivity.this).load(pelicula.getPoster()).into(mImageView);
    }
}
