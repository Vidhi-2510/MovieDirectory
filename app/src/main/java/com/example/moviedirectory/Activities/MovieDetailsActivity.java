package com.example.moviedirectory.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedirectory.Model.Movie;
import com.example.moviedirectory.R;
import com.example.moviedirectory.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {
    private Movie movie;
    private TextView title;
    private TextView release;
    private TextView category;
    private ImageView img;
    private TextView runtime;
    private TextView director;
    private TextView actor;
    private TextView ratings;
    private TextView plot;
    private TextView writer;
    RequestQueue queue ;
    private String movieId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        queue= Volley.newRequestQueue(this);
        movie=(Movie)getIntent().getSerializableExtra("movie");
        movieId=movie.getImdbId();
        setUpUI();
        getMoviesDetails(movieId);

    }

    private void getMoviesDetails(String id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL_L + id + Constants.URL_RIGHT, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("Ratings");
                            String source=null;
                            String value=null;
                            if(jsonArray.length()>0){
                                JSONObject mRatings=jsonArray.getJSONObject(jsonArray.length()-1);
                                ratings.setText("Source" +mRatings.getString("Source") +" Value" +mRatings.getString("Value"));
                            }else{
                                ratings.setText("Ratings : N/A");
                            }
                            title.setText("Title " +response.getString("Title"));
                            runtime.setText("Runtime " +response.getString("Runtime"));
                            release.setText("Released " +response.getString("Released"));
                            category.setText("Genre " +response.getString("Genre"));
                            director.setText("Director " +response.getString("Director"));
                            writer.setText("Writer " +response.getString("Writer"));
                            plot.setText("Plot " +response.getString("Plot"));
                            actor.setText("Actors " +response.getString("Actors"));
                            Picasso.with(getApplicationContext()).load(response.getString("Poster")).into(img);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void setUpUI() {
        title=(TextView)findViewById(R.id.TitleDet);
        img=(ImageView)findViewById(R.id.movieimgDet);
        release=(TextView)findViewById(R.id.releaseDet);
        category=(TextView)findViewById(R.id.catDet);
        runtime=(TextView)findViewById(R.id.runtimeDet);
        ratings=(TextView)findViewById(R.id.RatingDet);
        plot=(TextView)findViewById(R.id.PlotDet);
        actor=(TextView)findViewById(R.id.ActorDet);
        director=(TextView)findViewById(R.id.DirectorDet);
        writer=(TextView)findViewById(R.id.WriterDet);
    }
}
