package org.katolika.fihirana.lib.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.DatabaseHelper;
import org.katolika.fihirana.lib.R;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.interfaces.OnLoadMoreListener;
import org.katolika.fihirana.lib.models.Hira;
import org.katolika.fihirana.lib.models.Salamo;

import java.util.List;
import java.util.Locale;

public class SalamoAdapter extends ListAdapter<Salamo, SalamoAdapter.SalamoHolder> {

    boolean loading = false;
    OnLoadMoreListener onLoadMoreListener;
    OnRowClickListener onRowClickListener;


    private static final DiffUtil.ItemCallback<Salamo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Salamo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Salamo oldItem, @NonNull  Salamo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Salamo oldItem, @NonNull Salamo newItem) {
            return (oldItem.getId() == newItem.getId()) && (oldItem.getH_title().equals(newItem.getH_title())) && oldItem.getFaha() == newItem.getFaha();
        }
    };

    class SalamoHolder extends RecyclerView.ViewHolder{
        TextView faha;
        TextView h_title;
        TextView f_page;
        TextView f_title;
        public SalamoHolder(@NonNull View itemView) {
            super(itemView);
            faha = itemView.findViewById(R.id.faha);
            h_title = itemView.findViewById(R.id.h_title);
            f_page = itemView.findViewById(R.id.f_page);
            f_title = itemView.findViewById(R.id.f_title);
            itemView.setOnClickListener(view -> {
                if(onRowClickListener != null) onRowClickListener.onClick(getItem(getAbsoluteAdapterPosition()));
            });
        }

        void bind(Salamo h) {
            faha.setText(String.valueOf(h.getFaha()));
            h_title.setText(h.getH_title());
            f_title.setText(String.format(Locale.FRANCE, "%s p.%d", h.getF_title(), h.getF_page()));
        }

    }

    public SalamoAdapter() {
        super(DIFF_CALLBACK);

    }
    @NonNull
    @Override
    public SalamoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_salamo, parent, false);
        return new SalamoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SalamoAdapter.SalamoHolder holder, int position) {
        Salamo salamo = getItem(position);
        holder.bind(salamo);
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded()
    {
        loading = false;
    }


    public OnRowClickListener getOnRowClickListener() {
        return onRowClickListener;
    }

    public void setOnRowClickListener(OnRowClickListener onRowClickListener) {
        this.onRowClickListener = onRowClickListener;
    }

    public interface OnRowClickListener {
        void onClick(Salamo salamo);
    }
}
