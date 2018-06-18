package com.example.tvd.trm_discon_recon.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.fragments.Discon_List;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCONNECTION_DIALOG;

public class Discon_List_Adapter extends RecyclerView.Adapter<Discon_List_Adapter.Discon_Holder> {
    ProgressDialog progressDialog;
    private SendingData sendingData = new SendingData();
    private GetSetValues getSetValues1 = new GetSetValues();
    private FunctionCall functionCall = new FunctionCall();
    /*private final Handler mhandler;
    {
        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DISCON_SUCCESS:
                        progressDialog.dismiss();
                        Toast.makeText(context, "Account Disconnected..", Toast.LENGTH_SHORT).show();
                        break;
                    case DISCON_FAILURE:
                        progressDialog.dismiss();
                        Toast.makeText(context, "Disconnection Failure!!", Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }*/

    private ArrayList<GetSetValues> arraylist = new ArrayList<>();
    private Context context;
    private GetSetValues getSetValues;
    private Discon_List_Adapter discon_list_adapter;
    private Discon_List discon_list;
    public Discon_List_Adapter(Context context, ArrayList<GetSetValues> arrayList, GetSetValues getSetValues, Discon_List discon_list) {
        this.context = context;
        this.arraylist = arrayList;
        this.getSetValues = getSetValues;
        this.discon_list = discon_list;
    }

    @Override
    public Discon_List_Adapter.Discon_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discon_adapter_layout, null);
        return new Discon_Holder(view);
    }

    @Override
    public void onBindViewHolder(Discon_List_Adapter.Discon_Holder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.accountid.setText(getSetValues.getAcc_id());
        //here %s%s meaning first string will set on first then space and then second string
        holder.arrears.setText(String.format("%s %s", context.getResources().getString(R.string.rupee), getSetValues.getArrears()));
        holder.prevraed.setText(getSetValues.getPrev_read());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class Discon_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView accountid, arrears, prevraed;
        LinearLayout lin;

        public Discon_Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            accountid = (TextView) itemView.findViewById(R.id.txt_account_id);
            arrears = (TextView) itemView.findViewById(R.id.txt_arrears);
            prevraed = (TextView) itemView.findViewById(R.id.txt_prevread);
            lin = (LinearLayout) itemView.findViewById(R.id.lin_layout);
            lin.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            discon_list.show_disconnection_dialog(DISCONNECTION_DIALOG, position, arraylist);
        }
    }

}
