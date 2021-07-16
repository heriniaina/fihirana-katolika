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
import org.katolika.fihirana.lib.entities.Favorite;


public class FavoriteAdapter extends ListAdapter<Favorite, FavoriteAdapter.FavoriteHolder> {

    OnRowClickListener onRowClickListener;
    private static final DiffUtil.ItemCallback<Favorite> DIFF_CALLBACK = new DiffUtil.ItemCallback<Favorite>() {
        @Override
        public boolean areItemsTheSame(@NonNull Favorite oldItem, @NonNull  Favorite newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull  Favorite oldItem, @NonNull  Favorite newItem) {
            return (oldItem.getId() == newItem.getId()) && (oldItem.getH_title().equals(newItem.getH_title()));
        }
    };
    public FavoriteAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hira, parent, false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  FavoriteAdapter.FavoriteHolder holder, int position) {
        Favorite favorite = getItem(position);
        holder.h_title.setText(favorite.getH_title());
        if(favorite.getF_title() != null && !favorite.getF_title().isEmpty()) {
            holder.f_title.setText(favorite.getF_title());
            holder.f_page.setText(String.valueOf(favorite.getF_page()));
        } else
        {
            holder.f_title.setText(R.string.favorite_fihirana_not_set);
            holder.f_page.setText("");
        }
    }

    class FavoriteHolder extends RecyclerView.ViewHolder {
        TextView h_title;
        TextView f_title;
        TextView f_page;

        public FavoriteHolder(@NonNull  View itemView) {
            super(itemView);
            h_title = itemView.findViewById(R.id.h_title);
            f_title = itemView.findViewById(R.id.f_title);
            f_page = itemView.findViewById(R.id.f_page);
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
        void onClick(Favorite favorite);
    }
}
