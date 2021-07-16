package org.katolika.fihirana.lib.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.R;
import org.katolika.fihirana.lib.models.Sokajy;

public class SokajyAdapter extends ListAdapter<Sokajy, SokajyAdapter.SokajyHolder> {
    OnRowClickListener onRowClickListener;
    private static final DiffUtil.ItemCallback<Sokajy> DIFF_CALLBACK = new DiffUtil.ItemCallback<Sokajy>() {
        @Override
        public boolean areItemsTheSame(@NonNull  Sokajy oldItem, @NonNull  Sokajy newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull  Sokajy oldItem, @NonNull  Sokajy newItem) {
            return (oldItem.getId() == newItem.getId()) && (oldItem.getS_title().equals(newItem.getS_title()));
        }
    };
    public SokajyAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public SokajyAdapter.SokajyHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new SokajyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  SokajyAdapter.SokajyHolder holder, int position) {
        Sokajy sokajy = getItem(position);
        holder.cnt.setText(String.valueOf(sokajy.getCnt()));
        holder.s_title.setText(sokajy.getS_title());
        holder.s_description.setText(sokajy.getS_description());
    }

    class SokajyHolder extends RecyclerView.ViewHolder {
        TextView s_title;
        TextView s_description;
        TextView cnt;

        public SokajyHolder(@NonNull  View itemView) {
            super(itemView);
            s_description = itemView.findViewById(R.id.s_description);
            s_title = itemView.findViewById(R.id.s_title);
            cnt = itemView.findViewById(R.id.cnt);
            itemView.setOnClickListener(view -> {
                if(onRowClickListener != null) onRowClickListener.onClick(getItem(getAbsoluteAdapterPosition()));
            });
        }
    }

    public OnRowClickListener getOnRowClickListener() {
        return onRowClickListener;
    }

    public void setOnRowClickListener(OnRowClickListener onRowClickListener) {
        this.onRowClickListener = onRowClickListener;
    }

    public interface OnRowClickListener {
        void onClick(Sokajy sokajy);
    }
}
