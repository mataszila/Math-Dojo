package example.matasolutions.mathdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScoresActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<ScoreEntry> this_score_entries;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        setTitle("High Scores");

        ReadData(new MyHighScoresCallback() {
            @Override
            public void onHighScoresCallback(ArrayList<ScoreEntry> scoreEntries) {

                this_score_entries = scoreEntries;
                SetupActivity();
            }
        });



    }

    private void SetupBottomNavigationView(){

        bottomNavigationView = findViewById(R.id.high_scores_bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int menuId = menuItem.getItemId();

                switch(menuId){

                    case R.id.high_scores_navbar_most_points:
                        setTitle("High Scores:  Most points in a single game");
                        break;
                    case R.id.high_scores_navbar_most_XP:
                        setTitle("High Scores: Most XP");
                        break;
                }

                return false;
            }
        });

    }


    public void SetupActivity(){

        SortHighScores();

        SetupBottomNavigationView();

        RecyclerView recyclerView = findViewById(R.id.high_scores_recycler_view);

        MyAdapter myAdapter = new MyAdapter(this_score_entries,getApplicationContext());

        LinearLayoutManager  layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(myAdapter);

    }

    public void SortHighScores() {

        Collections.sort(this_score_entries, new Comparator<ScoreEntry>() {
            @Override
            public int compare(ScoreEntry lhs, ScoreEntry rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending

                double p1 = lhs.score;
                double p2 = rhs.score;


                return Double.compare(p2, p1);
            }
        });

    }

    void ReadData(final MyHighScoresCallback myCallback){

        myRef.child("high_scores").child("total_points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<ScoreEntry> scoreEntriesReturned =  ConvertSnapshot(dataSnapshot);

                myCallback.onHighScoresCallback(scoreEntriesReturned);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public ArrayList<ScoreEntry> ConvertSnapshot(DataSnapshot snapshot){

        ArrayList<ScoreEntry> list = new ArrayList<>();

        for(DataSnapshot snap : snapshot.getChildren()){

            list.add(snap.getValue(ScoreEntry.class));



        }


        return list;

    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<ScoreEntry> mDataset;
        private Context context;


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            public TextView place;
            public TextView name;
            public TextView score;
         //  public LinearLayout layout;



            public MyViewHolder(View v) {
                super(v);

                place =  v.findViewById(R.id.place);
                name =  v.findViewById(R.id.name);
                score =  v.findViewById(R.id.score);
               // layout = v.findViewById(R.id.productprice_recyclerview_layout);

            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<ScoreEntry> myDataset, Context context) {
            mDataset = myDataset;
            this.context = context;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View v = inflater.inflate(R.layout.high_scores_activity_recycler_view, parent, false);

            MyAdapter.MyViewHolder vh = new MyAdapter.MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final ScoreEntry thisEntry = mDataset.get(position);

            // Set item views based on your views and data model
            TextView place = holder.place;
            TextView name = holder.name;
            TextView score  = holder.score;

            if(position+1 < 10) {

                place.setText(" " + (position+1));
                name.setText(" " +thisEntry.name);
                score.setText(" " + String.valueOf(thisEntry.score));

            }
            else{
                place.setText(String.valueOf(position+1));
                name.setText(thisEntry.name);
                score.setText(String.valueOf(thisEntry.score));

            }

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

}


