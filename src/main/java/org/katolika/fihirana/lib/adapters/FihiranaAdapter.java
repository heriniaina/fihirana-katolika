package org.katolika.fihirana.lib.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.R;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.models.Fihirana;

import java.util.List;

public class FihiranaAdapter extends ListAdapter<Fihirana, FihiranaAdapter.FihiranaHolder> {

    private static final String TAG = "FihiranaAdapter";
    private OnRowClickListener clickListener;

    private static final DiffUtil.ItemCallback<Fihirana> DIFF_CALLBACK = new DiffUtil.ItemCallback<Fihirana>() {
        @Override
        public boolean areItemsTheSame(@NonNull  Fihirana oldItem, @NonNull  Fihirana newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull  Fihirana oldItem, @NonNull  Fihirana newItem) {
            return (oldItem.getId() == newItem.getId()) && (oldItem.getF_title().equals(newItem.getF_title()));
        }
    };

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class FihiranaHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView f_title;
        TextView f_id;
        TextView f_description;
        TextView cnt;

        FihiranaHolder(View v) {
            super(v);
            f_title = v.findViewById(R.id.f_title);
            f_id = v.findViewById(R.id.f_id);
            f_description = v.findViewById(R.id.f_description);
            cnt = v.findViewById(R.id.cnt);
            v.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if(clickListener != null) clickListener.onClick(getItem(position));
            });
        }

        void bind(Fihirana fihirana)
        {
            Log.d(TAG, "bind: f_title " + fihirana.getF_title());
            f_description.setText(fihirana.getF_description());
            f_title.setText(fihirana.getF_title());
            f_id.setText(String.valueOf(fihirana.getId()));
            cnt.setText(String.valueOf(fihirana.getCnt()));
        }

    }

    public FihiranaAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public FihiranaAdapter.FihiranaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fihirana, parent, false);
        return new FihiranaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FihiranaAdapter.FihiranaHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setClickListener(OnRowClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnRowClickListener {
        void onClick(Fihirana fihirana);
    }
}
