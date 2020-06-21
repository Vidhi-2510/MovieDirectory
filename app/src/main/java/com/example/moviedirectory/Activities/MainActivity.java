package com.example.moviedirectory.Activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.net.sip.SipSession;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedirectory.Data.MovieAdapter;
import com.example.moviedirectory.Model.Movie;
import com.example.moviedirectory.R;
import com.example.moviedirectory.Util.Constants;
import com.example.moviedirectory.Util.Prefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Downloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList;
    RequestQueue queue;
    private AlertDialog.Builder alBuilder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        queue= Volley.newRequestQueue(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        movieList=new ArrayList<>();
        Prefs prefs=new Prefs(this);
        String search=prefs.getSearch();
//        getMovies(search);
        movieList=getMovies(search);
        movieAdapter=new MovieAdapter(this,movieList);
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }

    public List<Movie> getMovies(String search) {
        movieList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL_LEFT + search + Constants.URL_RIGHT, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("Search");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject movieObj=jsonArray.getJSONObject(i);
                                     Movie movie=new Movie();

                                    movie.setTitle(movieObj.getString("Title"));
                                    movie.setYear(movieObj.getString("Year"));
                                    movie.setPoster(movieObj.getString("Poster"));
                                    movie.setMovieType(movieObj.getString("Type"));
                                    movie.setImdbId(movieObj.getString("imdbID"));
//                                Log.d("Movies",movie.getTitle());

                                movieList.add(movie);
                                movieAdapter.notifyDataSetChanged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);
        return movieList;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            getpoppup();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getpoppup() {
        alBuilder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.poppup,null);
        final EditText nsearch=(EditText)view.findViewById(R.id.searchedt);
        Button btn=(Button)view.findViewById(R.id.sbtbtn);
        alBuilder.setView(view);
        alertDialog=alBuilder.create();
        alertDialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs=new Prefs(MainActivity.this);
                if(!nsearch.getText().toString().isEmpty())
                {
                    prefs.setSearch(nsearch.getText().toString());
                    getMovies(nsearch.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });

    }
}
