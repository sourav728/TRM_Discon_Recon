package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.TcDetails2;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_UPDATE_DIALOG;

public class TcDetailsAdapter extends RecyclerView.Adapter<TcDetailsAdapter.TcHolder> {
    private ArrayList<GetSetValues> arraylist ;
    private Context context;
    private GetSetValues getSetValues;
    private TcDetails2 tcDetails2;
    public TcDetailsAdapter(ArrayList<GetSetValues>arrayList,Context context,GetSetValues getSetValues, TcDetails2 tcDetails2)
    {
        this.arraylist = arrayList;
        this.context = context;
        this.getSetValues = getSetValues;
        this.tcDetails2 = tcDetails2;
    }
    @Override
    public TcDetailsAdapter.TcHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tc_details_adapter_layout, null);
        return new TcHolder(view);
    }

    @Override
    public void onBindViewHolder(TcDetailsAdapter.TcHolder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.tccode.setText(getSetValues.getTc_code());
        holder.tcir.setText(getSetValues.getTcir());
        holder.tcfr.setText(getSetValues.getTcfr());
        holder.tcmf.setText(getSetValues.getTcmf());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class TcHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tccode,tcir,tcfr,tcmf;
        public TcHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tccode = itemView.findViewById(R.id.txt_tccode);
            tcir = itemView.findViewById(R.id.txt_tcir);
            tcfr = itemView.findViewById(R.id.txt_tcfr);
            tcmf = itemView.findViewById(R.id.txt_tcmf);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            GetSetValues getSetValues = arraylist.get(position);
            tcDetails2.show_tc_details_update_dialog(TC_DETAILS_UPDATE_DIALOG,position,arraylist);
        }
    }
}
