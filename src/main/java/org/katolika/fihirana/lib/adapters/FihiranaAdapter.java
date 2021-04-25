package org.katolika.fihirana.lib.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.katolika.fihirana.lib.R;
import org.katolika.fihirana.lib.interfaces.ItemClickListener;
import org.katolika.fihirana.lib.models.Fihirana;

import java.util.List;

public class FihiranaAdapter extends RecyclerView.Adapter {

    private static final String TAG = "FihiranaAdapter";
    private List<Fihirana> fihiranaList;
    private ItemClickListener clickListener;

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
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null) clickListener.onClick(view, getAdapterPosition());
                }
            });
        }

        void bind(Fihirana fihirana)
        {
            f_description.setText(fihirana.getDescription());
            f_title.setText(fihirana.getName());
            f_id.setText(String.valueOf(fihirana.getId()));
            cnt.setText(String.valueOf(fihirana.getCnt()));
        }

    }

    public FihiranaAdapter(List<Fihirana> fl) {
        fihiranaList = fl;

    }

    @NonNull
    @Override
    public FihiranaAdapter.FihiranaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fihirana, parent, false);
        return new FihiranaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FihiranaHolder)
        {
            Fihirana fihirana = fihiranaList.get(position);
            ((FihiranaHolder) holder).bind(fihirana);
        }

    }



    @Override
    public int getItemCount() {

        return fihiranaList.size();
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
