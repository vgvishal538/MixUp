package com.mixup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mixup.R;
import com.mixup.activity.DetailActivity;
import com.mixup.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vishu on 29/07/17.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    public List<Artist> artists_data;
    private Context context;

    public ArtistAdapter(Context context, List<Artist> artists_data) {
        this.artists_data = artists_data;
        this.context = context;
    }

    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_lay, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Artist artist = artists_data.get(position);
        holder.track_name.setText(artist.getTrackName());
        holder.artist_name.setText(artist.getArtistName());
        holder.cost.setText("Cost $"+String.valueOf(artist.getTrackPrice()));
        holder.duration.setText(String.valueOf(artist.getTrackTimeMillis()));
        Picasso.with(context).load(artist.getArtworkUrl100()).into(holder.artist_image);


    }

    @Override
    public int getItemCount() {
        return artists_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artist_image;
        TextView track_name,artist_name,genere,duration,cost;
        CardView card;
        Artist artist_d;

        public ViewHolder(final View view) {
            super(view);
            artist_image = (ImageView)view.findViewById(R.id.artist_image);
            track_name = (TextView)view.findViewById(R.id.track_name);
            artist_name = (TextView)view.findViewById(R.id.artist_name);
            genere = (TextView)view.findViewById(R.id.genere);
            duration = (TextView)view.findViewById(R.id.duration);
            cost = (TextView)view.findViewById(R.id.cost);
            card = (CardView)view.findViewById(R.id.card);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos  = getAdapterPosition();
                    artist_d = artists_data.get(pos);
                    String track_name = artist_d.getTrackName();
                    String artist_name = artist_d.getArtistName();
                    String genere = artist_d.getPrimaryGenreName();
                    String artist_image = artist_d.getArtworkUrl100();
                    String preview_url = artist_d.getPreviewUrl();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("track_name",track_name);
                    intent.putExtra("artist_name",artist_name);
                    intent.putExtra("genere",genere);
                    intent.putExtra("artist_image",artist_image);
                    intent.putExtra("preview_url",preview_url);
                    context.startActivity(intent);
                }
            });



            Typeface font_montserrat_regular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
            track_name.setTypeface(font_montserrat_regular);
            artist_name.setTypeface(font_montserrat_regular);
            genere.setTypeface(font_montserrat_regular);
            duration.setTypeface(font_montserrat_regular);
            cost.setTypeface(font_montserrat_regular);




        }

    }


}
