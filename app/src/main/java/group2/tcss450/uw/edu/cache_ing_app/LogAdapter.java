package group2.tcss450.uw.edu.cache_ing_app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Vincent on 11/22/2017.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private final ArrayList<JSONObject> mValues;

    public LogAdapter (ArrayList<JSONObject> list) {
        mValues = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject temp = mValues.get(position);

        try {
            holder.firstName.setText((String)temp.get("first"));
            holder.lastName.setText((String)temp.get("last"));
            if (temp.has("nick")) {
                holder.nickName.setText((String) temp.get("nick"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView firstName;
        public final TextView lastName;
        public final TextView nickName;

        public ViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.log_first_name);
            lastName = itemView.findViewById(R.id.log_last_name);
            nickName = itemView.findViewById(R.id.log_nick_name);

        }
    }
}
