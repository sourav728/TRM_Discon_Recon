package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

public class Discon_List_Adapter extends RecyclerView.Adapter<Discon_List_Adapter.Discon_Holder>{
    private ArrayList<GetSetValues>arraylist = new ArrayList<>();
    private Context context;
    private GetSetValues getSetValues;
    private Discon_List_Adapter discon_list_adapter;
    public Discon_List_Adapter(Context context, ArrayList<GetSetValues> arrayList, GetSetValues getSetValues)
    {
        this.context = context;
        this.arraylist = arrayList;
        this.getSetValues = getSetValues;
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
        holder.arrears.setText(String.format("%s %s", context.getResources().getString(R.string.rupee),getSetValues.getArrears()));
        holder.prevraed.setText(getSetValues.getPrev_read());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class Discon_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView accountid,arrears,prevraed;
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
            int pos = getAdapterPosition();
            final GetSetValues getSetValues = arraylist.get(pos);
            switch (v.getId())
            {
                case R.id.lin_layout:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Disconnection");
                    dialog.setCancelable(false);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.discon_layout, null);
                    dialog.setView(view);
                    //Button myButton = (Button) view.findViewById( R.id.myButton );
                    final TextView accno = (TextView) view.findViewById(R.id.txt_account_no);
                    final TextView arrears = (TextView) view.findViewById(R.id.txt_arrears);
                    final TextView prevread = (TextView) view.findViewById(R.id.txt_prevread);
                    dialog.setPositiveButton("Disconnect",null);
                    dialog.setNegativeButton("Cancel",null);
                    final AlertDialog discon_dialog = dialog.create();
                    discon_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button positive = discon_dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            Button negative = discon_dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                            accno.setText(getSetValues.getAcc_id());
                            arrears.setText(String.format("%s %s", context.getResources().getString(R.string.rupee),getSetValues.getArrears()));
                            prevread.setText(getSetValues.getPrev_read());
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                            negative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    discon_dialog.dismiss();
                                }
                            });
                        }
                    });
                    discon_dialog.show();
                    ((AlertDialog)discon_dialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
                    ((AlertDialog)discon_dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                    break;
            }
        }
    }
}
