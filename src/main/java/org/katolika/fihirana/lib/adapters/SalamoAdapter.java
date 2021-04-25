package org.katolika.fihirana.lib.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.DatabaseHelper;
import org.katolika.fihirana.lib.R;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.interfaces.OnLoadMoreListener;
import org.katolika.fihirana.lib.models.Hira;

import java.util.List;

public class SalamoAdapter extends RecyclerView.Adapter {
    List<Hira> hiraList;
    boolean loading = false;
    OnLoadMoreListener onLoadMoreListener;
    ItemClickListener onClickListener;

    class SalamoHolder extends RecyclerView.ViewHolder{
        TextView faha;
        TextView h_id;
        TextView h_title;
        TextView f_page;
        TextView f_title;
        public SalamoHolder(@NonNull View itemView) {
            super(itemView);
            faha = itemView.findViewById(R.id.faha);
            h_id = itemView.findViewById(R.id.h_id);
            h_title = itemView.findViewById(R.id.h_title);
            f_page = itemView.findViewById(R.id.f_page);
            f_title = itemView.findViewById(R.id.f_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickListener != null) onClickListener.onClick(view, getAdapterPosition());
                }
            });
        }

        void bind(Hira h) {
            faha.setText(String.valueOf(h.getFaha()));
            h_id.setText(String.valueOf(h.getId()));
            h_title.setText(h.getH_title());
            if(h.getF_page().get(0) > 0) f_page.setText(String.valueOf(h.getF_page().get(0)));
            f_title.setText(h.getF_title().get(0));
        }

    }

    public SalamoAdapter(List<Hira> hl, RecyclerView recyclerView) {
        hiraList = hl;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(!loading
                            && linearLayoutManager != null
                            && linearLayoutManager.findLastCompletelyVisibleItemPosition() == hiraList.size() - 1) {
                        if(onLoadMoreListener != null)
                        {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }

                }
            });
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_salamo, parent, false);
        return new SalamoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Hira hira = hiraList.get(position);
        ((SalamoHolder) holder).bind(hira);
    }

    @Override
    public int getItemCount() {
        return hiraList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded()
    {
        loading = false;
    }

    public void setOnClickListener(ItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
