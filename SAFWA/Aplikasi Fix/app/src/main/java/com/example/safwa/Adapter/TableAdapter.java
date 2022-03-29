package com.example.safwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safwa.Model.ModelDataRealTime;
import com.example.safwa.R;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableVH> {
    Context context;
    List<ModelDataRealTime> bData;

    public TableAdapter(Context context, List<ModelDataRealTime> bData) {
        this.context = context;
        this.bData = bData;
    }

    @NonNull
    @Override
    public TableVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table,parent,false);
        return new TableVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TableVH holder, int position) {
        holder.tv_kebocoran.setText(""+bData.get(position).getKebocoran());
        holder.tv_debit.setText(""+bData.get(position).getDebit());
        holder.tv_time.setText(bData.get(position).getTime());
        holder.tv_date.setText(""+bData.get(position).getDate());
        holder.tv_tagihan.setText(""+bData.get(position).getTagihan());
//        holder.tv_pres1.setText(bData.get(position).getPres1());
//        holder.tv_pres2.setText(bData.get(position).getPres2());

    }

    @Override
    public int getItemCount() {
        return bData.size();
    }

    public class TableVH extends RecyclerView.ViewHolder {
        TextView tv_time,tv_debit,tv_kebocoran, tv_date, tv_tagihan, tv_pres1, tv_pres2;
        public TableVH(@NonNull View itemView) {
            super(itemView);
//            tv_date = itemView.findViewById(R.id.tv_tanggal);
            tv_debit = itemView.findViewById(R.id.tv_debit);
            tv_kebocoran = itemView.findViewById(R.id.tv_kebocoran);
            tv_tagihan = itemView.findViewById(R.id.tv_tagihan);
//            tv_time = itemView.findViewById((R.id.))
        }
    }
}
