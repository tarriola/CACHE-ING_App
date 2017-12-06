package group2.tcss450.uw.edu.cache_ing_app.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import group2.tcss450.uw.edu.cache_ing_app.R;

/**
 * Created by Vincent on 11/22/2017.
 *
 * Class used to hold all the information displayed as a list.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private final ArrayList<JSONObject> mValues;

    public LogAdapter (ArrayList<JSONObject> list) {
        mValues = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject temp = mValues.get(position);
        try {
            String fullname = temp.getString("firstname") + " " + temp.getString("lastname");
            holder.name.setText(fullname);
            holder.nickname.setText(temp.getString("nickname"));
            holder.comment.setText(temp.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    /**
     * A class used to hold the information of each log
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView nickname;
        public final TextView comment;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.log_name);
            nickname = itemView.findViewById(R.id.log_nickname);
            comment = itemView.findViewById(R.id.log_comment);

        }
    }
}