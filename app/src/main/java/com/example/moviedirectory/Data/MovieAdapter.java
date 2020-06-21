package com.example.moviedirectory.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedirectory.Activities.MovieDetailsActivity;
import com.example.moviedirectory.Model.Movie;
import com.example.moviedirectory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movielist;

    public MovieAdapter(Context context, List<Movie> movielist) {
        this.context = context;
        this.movielist = movielist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie=movielist.get(position);
        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getMovieType());
        holder.year.setText(movie.getYear());
        String posterlink=movie.getPoster();
        Picasso.with(context)
                .load(posterlink)
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(holder.poster);



    }

    @Override
    public int getItemCount() {
        return movielist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView poster;
        TextView type;
        TextView year;
        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context=ctx;
            title=(TextView)itemView.findViewById(R.id.movieTitleID);
            poster=(ImageView)itemView.findViewById(R.id.movieimgID);
            type=(TextView)itemView.findViewById(R.id.moviecatID);
            year=(TextView)itemView.findViewById(R.id.moviereleaseID);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie=movielist.get(getAdapterPosition());
                    Intent intent=new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra("movie",movie);
                    ctx.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
