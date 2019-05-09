package example.matasolutions.mathdojo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileAdapter extends RecyclerView.Adapter<MyProfileAdapter.MyViewHolder> {
    private ArrayList<Level> mDataset;
    private Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView currentXP;
        public TextView next_level_XP;
        public TextView current_level;
        public TextView next_level;
        public ProgressBar progressBar;
        public CircleImageView image;



        public MyViewHolder(View v) {
            super(v);

            currentXP = v.findViewById(R.id.profile_currentXP);
            next_level_XP = v.findViewById(R.id.profile_next_level_XP);
            current_level = v.findViewById(R.id.profile_current_level);
            next_level = v.findViewById(R.id.profile_next_level);
            image  = v.findViewById(R.id.profile_stats_circle_image_view);
            progressBar = v.findViewById(R.id.determinate_Bar);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyProfileAdapter(ArrayList<Level> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyProfileAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(R.layout.profile_stats_recyclerview, parent, false);

        MyProfileAdapter.MyViewHolder vh = new MyProfileAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyProfileAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Level thisEntry = mDataset.get(position);

        // Set item views based on your views and data model
        TextView currentXP = holder.currentXP;
        TextView next_level_XP = holder.next_level_XP;
        TextView current_level = holder.current_level;
        TextView next_level = holder.next_level;
        CircleImageView image = holder.image;
        ProgressBar progress_bar = holder.progressBar;

        currentXP.setText(String.valueOf(thisEntry.totalXP) + "XP");
        next_level_XP.setText(String.valueOf(thisEntry.totalXP_until_next_level) + "XP");
        current_level.setText("Level" + String.valueOf(thisEntry.levelNumber));
        next_level.setText("Level" + String.valueOf(thisEntry.levelNumber+1));
        image.setImageResource(setLevelTypeImageResource(thisEntry.levelType));

        Double currentProgress = thisEntry.totalXP - thisEntry.previous_totalXP_until_next_level;
        Double totalProgress  = thisEntry.totalXP_until_next_level;

        Double progressPercentage = (currentProgress/totalProgress) * 100;
        int progress =progressPercentage.intValue();

        progress_bar.setProgress(progress);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private int setLevelTypeImageResource(LevelType type){

        switch (type){

            case OVERALL:
                return R.drawable.ic_baseline_star_24px;
            case SKILL_ADD:
                return R.drawable.ic_plus_svgrepo_com;
            case SKILL_SUBTRACT:
                return R.drawable.ic_subtract_svgrepo_com;
            case SKILL_MULTIPLICATION:
                return R.drawable.ic_multiply_mathematical_sign_svgrepo_com;

        }

        return R.drawable.ic_baseline_star_24px;

    }




}
