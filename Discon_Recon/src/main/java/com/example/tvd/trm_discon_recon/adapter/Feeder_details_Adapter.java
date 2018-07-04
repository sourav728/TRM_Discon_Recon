package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.FeederDetails;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.FEEDER_DETAILS_UPDATE_DIALOG;

public class Feeder_details_Adapter extends RecyclerView.Adapter<Feeder_details_Adapter.FeederHolder> {
    private ArrayList<GetSetValues> arraylist ;
    private Context context;
    private GetSetValues getSetValues;
    private FeederDetails feederDetails;
    public Feeder_details_Adapter(ArrayList<GetSetValues>arrayList, Context context, GetSetValues getSetValues,FeederDetails feederDetails)
    {
        this.arraylist = arrayList;
        this.context = context;
        this.getSetValues = getSetValues;
        this.feederDetails = feederDetails;
    }
    @Override
    public Feeder_details_Adapter.FeederHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeder_details_adapter_layout, null);
        return new FeederHolder(view);
    }

    @Override
    public void onBindViewHolder(Feeder_details_Adapter.FeederHolder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.fdr_code.setText(getSetValues.getFdr_code());
        holder.fdr_ir.setText(getSetValues.getFdr_ir());
        holder.fdr_fr.setText(getSetValues.getFdr_fr());
        holder.fdr_mf.setText(getSetValues.getFdr_mf());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class FeederHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView fdr_code,fdr_ir,fdr_fr,fdr_mf;
        public FeederHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            fdr_code = itemView.findViewById(R.id.txt_feeder_code);
            fdr_ir = itemView.findViewById(R.id.txt_fdrir);
            fdr_fr = itemView.findViewById(R.id.txt_fdrfr);
            fdr_mf = itemView.findViewById(R.id.txt_fdr_mf);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            GetSetValues getSetValues = arraylist.get(position);
            feederDetails.show_fdr_details_update_dialog(FEEDER_DETAILS_UPDATE_DIALOG,position,arraylist);
        }
    }
}
