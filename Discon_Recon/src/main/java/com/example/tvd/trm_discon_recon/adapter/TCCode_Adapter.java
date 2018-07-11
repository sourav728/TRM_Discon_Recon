package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.TC_Details2;
import com.example.tvd.trm_discon_recon.values.GetSetValues;
import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_UPDATE;

public class TCCode_Adapter extends RecyclerView.Adapter<TCCode_Adapter.TcHolder> {
    private ArrayList<GetSetValues> arraylist ;
    private Context context;
    private GetSetValues getSetValues = new GetSetValues();
    private TC_Details2 tc_details2;

    public TCCode_Adapter(Context context, ArrayList<GetSetValues> arraylist, TC_Details2 tcDetails2) {
        this.arraylist = arraylist;
        this.context = context;
        this.tc_details2 = tcDetails2;
    }

    @Override
    public TcHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tccode_adapter, null);
        return new TcHolder(view);
    }

    @Override
    public void onBindViewHolder(TcHolder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.tccode.setText(getSetValues.getTc_code());
        holder.tcir.setText(getSetValues.getTcir());
        holder.tcfr.setText(getSetValues.getTcfr());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class TcHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tccode,tcir,tcfr;
        public TcHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tccode = itemView.findViewById(R.id.txt_tccode);
            tcir = itemView.findViewById(R.id.txt_tcir);
            tcfr = itemView.findViewById(R.id.txt_tcfr);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            getSetValues = arraylist.get(position);
            tc_details2.show_tc_details_update_dialog2(TC_DETAILS_UPDATE, position, arraylist);
           /* Intent intent= new Intent(context, Tcdetails_Update.class);
            intent.putExtra("TCCODE",getSetValues.getTc_code());
            context.startActivity(intent);*/
        }
    }
}


