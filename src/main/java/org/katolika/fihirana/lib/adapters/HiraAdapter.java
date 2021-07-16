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

import org.katolika.fihirana.lib.R;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.interfaces.OnLoadMoreListener;
import org.katolika.fihirana.lib.models.Fihirana;
import org.katolika.fihirana.lib.models.Hira;
import org.katolika.fihirana.lib.models.HiraInfo;

import java.util.List;

public class HiraAdapter extends ListAdapter<HiraInfo, HiraAdapter.HiraHolder> {

    boolean loading = false;
    OnLoadMoreListener onLoadMoreListener;
    OnRowClickListener onClickListener;

    private static final DiffUtil.ItemCallback<HiraInfo> DIFF_CALLBACK = new DiffUtil.ItemCallback<HiraInfo>() {
        @Override
        public boolean areItemsTheSame(@NonNull  HiraInfo oldItem, @NonNull HiraInfo newItem) {
            return  oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull  HiraInfo oldItem, @NonNull  HiraInfo newItem) {
            return ( oldItem.getId() == newItem.getId()) && (oldItem.getF_page() == newItem.getF_page()) && (oldItem.getF_title().equals(newItem.getF_title()));
        }
    };


    class HiraHolder extends RecyclerView.ViewHolder{
        TextView h_title;
        TextView f_page;
        TextView f_title;
        public HiraHolder(@NonNull View itemView) {
            super(itemView);
            h_title = itemView.findViewById(R.id.h_title);
            f_page = itemView.findViewById(R.id.f_page);
            f_title = itemView.findViewById(R.id.f_title);
            itemView.setOnClickListener(view -> {
                if(onClickListener != null) onClickListener.onClick(getItem(getAbsoluteAdapterPosition()));
            });
        }

        void bind(HiraInfo h) {
            h_title.setText(h.getH_title());
            if(h.getF_page() > 0) {
                f_page.setText(String.valueOf(h.getF_page()));
                f_title.setText(h.getF_title());
            } else {
                f_title.setText(R.string.str_hira_not_in_fihirana);
                f_page.setText("");
            }

        }

    }

    public HiraAdapter () {
        super(DIFF_CALLBACK);


    }
    @NonNull
    @Override
    public HiraAdapter.HiraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_hira, parent, false);
        return new HiraHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HiraAdapter.HiraHolder holder, int position) {
        holder.bind(getItem(position));

    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded()
    {
        loading = false;
    }

    public void setOnClickListener(OnRowClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnRowClickListener {
        void onClick(HiraInfo hira);
    }
}
