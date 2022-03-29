package com.example.safwa.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safwa.Model.ModelDataHistory;
import com.example.safwa.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TabelHistoryAdapter extends RecyclerView.Adapter<TabelHistoryAdapter.TableVH>{
    Context context;
    ArrayList<ModelDataHistory> bData = new ArrayList<>();

    public TabelHistoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ModelDataHistory> bData){
        this.bData.clear();
        this.bData.addAll(bData);
        Collections.reverse(this.bData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.d("test", "getItemCount: " + bData.size());
        return bData.size();
    }

    @NonNull
    @Override
    public TableVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table,parent,false);
        return new TableVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TableVH holder, int position) {
        Log.d("pos", String.valueOf(position));

        holder.tv_tagihan.setText(""+bData.get(position).getTagihan());
        if(bData.get(position).getKebocoran() == "0"){
            holder.tv_kebocoran.setText("Tidak");
        }else{
            holder.tv_kebocoran.setText("Ya");
        }

        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
//        Calendar calendar = Calendar.getInstance();
//        long waktu = Long.parseLong(bData.get(position).getTimestamp()) * 1000L;
//        calendar.setTimeInMillis(waktu);
//        holder.tv_timestamp.setText(""+formatter.format(calendar.getTime()));

    }


    public class TableVH extends RecyclerView.ViewHolder {
        TextView tv_timestamp,tv_debit,tv_tagihan, tv_kebocoran;
        public TableVH(@NonNull View itemView) {
            super(itemView);
//            tv_timestamp = itemView.findViewById(R.id.tv_tanggal);
//            tv_debit = itemView.findViewById(R.id.tv_debit_history);
            tv_tagihan = itemView.findViewById(R.id.tv_tagihanTable);
            tv_kebocoran = itemView.findViewById(R.id.tv_kebocoran);
        }
    }
}
