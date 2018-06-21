package com.example.tvd.trm_discon_recon.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.fragments.Recon_List;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECONNECTION_DIALOG;

public class Recon_List_Adapter extends RecyclerView.Adapter<Recon_List_Adapter.Recon_Holder> {

    private ArrayList<GetSetValues> arraylist;
    private Context context;
    private Recon_List recon_list;

    public  Recon_List_Adapter(Context context,ArrayList<GetSetValues>arraylist,Recon_List recon_list)
    {
        this.context = context;
        this.arraylist = arraylist;
        this.recon_list = recon_list;
    }
    @Override
    public Recon_List_Adapter.Recon_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recon_adapter_layout, null);
        return  new Recon_Holder(view);
    }

    @Override
    public void onBindViewHolder(Recon_List_Adapter.Recon_Holder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.accountid.setText(getSetValues.getRecon_acc_id());
        Log.d("Debug","Recon_Acc id"+getSetValues.getRecon_acc_id());
        //here %s%s meaning first string will set on first then space and then second string
        holder.prevraed.setText(getSetValues.getRecon_prevread());
        Log.d("Debug","Recon_PrevRead"+getSetValues.getRecon_prevread());
        if (StringUtils.startsWithIgnoreCase(getSetValues.getRecon_flag(),"Y"))
            holder.reconnected.setVisibility(View.VISIBLE);
        else holder.reconnected.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class Recon_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView accountid, prevraed, reconnected;
        LinearLayout lin;

        public Recon_Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            accountid = (TextView) itemView.findViewById(R.id.txt_account_id);
            prevraed = (TextView) itemView.findViewById(R.id.txt_prevread);
            reconnected = (TextView) itemView.findViewById(R.id.txt_reconnected);
            lin = (LinearLayout) itemView.findViewById(R.id.lin_layout);
            lin.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            GetSetValues getSetValues = arraylist.get(position);
            /*****Comment should be removed from here************/
            if (StringUtils.startsWithIgnoreCase(getSetValues.getRecon_flag(),"Y"))
                Toast.makeText(context, "Account ID Already Reconnected!!", Toast.LENGTH_SHORT).show();
            else recon_list.show_reconnection_dialog(RECONNECTION_DIALOG, position, arraylist);
        }
    }
}
