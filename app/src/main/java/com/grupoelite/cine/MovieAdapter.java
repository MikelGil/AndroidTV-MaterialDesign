package com.grupoelite.cine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Mikel Gil on 28/04/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //Variables
    Peliculas peliculas;
    Context context;

    OnItemClickListener mItemClickListener;

    public MovieAdapter(Context context, Peliculas peliculas) {
        this.context = context;
        this.peliculas = peliculas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public RelativeLayout movieHolder;
        public TextView movieName;
        public ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);

            movieHolder = (RelativeLayout)itemView.findViewById(R.id.mainHolder);
            movieName = (TextView)itemView.findViewById(R.id.movie);
            movieImage = (ImageView)itemView.findViewById(R.id.image);

            movieHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    @Override
    public int getItemCount() {
        return peliculas.getData().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_lista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Pelicula pelicula = peliculas.getData().get(position);
        holder.movieName.setText(pelicula.getMovie());

        Picasso.with(context)
                .load(pelicula.getPoster())
                .into(holder.movieImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) holder.movieImage.getDrawable()).getBitmap();
                        // do your processing here....

                        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                int mutedLight = palette.getMutedColor(context.getResources().getColor(android.R.color.black));
                                //holder.movieNameHolder.setBackgroundColor(mutedLight);
                            }
                        });
                    }
                    @Override
                    public void onError() {
                        // reset your views to default colors, etc.
                    }
                });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
