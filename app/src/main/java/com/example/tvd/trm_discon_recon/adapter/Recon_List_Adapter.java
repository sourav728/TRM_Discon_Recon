package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

public class Recon_List_Adapter extends RecyclerView.Adapter<Recon_List_Adapter.ReconHolder>{
    private ArrayList<GetSetValues>arrayList = new ArrayList<>();
    private Context context;
    private GetSetValues getSetValues;
    private Recon_List_Adapter recon_list_adapter;
    public Recon_List_Adapter(Context context, ArrayList<GetSetValues>arrayList,GetSetValues getSetValues)
    {
        this.context = context;
        this.arrayList = arrayList;
        this.getSetValues = getSetValues;
    }
    @Override
    public Recon_List_Adapter.ReconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reconnection_adapter_layout, null);
        return new ReconHolder(view);
    }

    @Override
    public void onBindViewHolder(Recon_List_Adapter.ReconHolder holder, int position) {
        GetSetValues getSetValues = arrayList.get(position);
        holder.account_id.setText("");
        holder.date.setText("");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ReconHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView account_id,date;
        LinearLayout lin;
        public ReconHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            account_id = (TextView) itemView.findViewById(R.id.txt_account_id);
            date = (TextView) itemView.findViewById(R.id.txt_date);
            lin = (LinearLayout) itemView.findViewById(R.id.lin_layout);
            lin.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            final GetSetValues getSetValues = arrayList.get(pos);
            switch (v.getId())
            {
                case R.id.lin_layout:
            }
        }
    }
}
