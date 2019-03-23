package com.example.grievanceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.grievanceapp.Model.Complaint;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ComplaintViewHolder> {
    private List<Object> rvComplaints;
    private Context rvContext;
    private List<Object> rvStatus;

    public RecyclerViewAdapter(List<Object> rvComps, List<Object> rvStatus, Context context) {
        this.rvComplaints = rvComps;
        this.rvStatus = rvStatus;
        this.rvContext = context;
    }

    @NonNull
    @Override
    public ComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(rvContext)
                .inflate(R.layout.complaint_list_item, viewGroup, false);
        ComplaintViewHolder holder = new ComplaintViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintViewHolder holder, final int pos) {
        holder.cvhComplaint.setText(rvComplaints.get(pos).toString());
        holder.cvhStatus.setText(rvStatus.get(pos).toString());
        holder.cvhParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rvContext, ViewTaskActivity.class);
                intent.putExtra("complaints", pos);
                rvContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(rvComplaints == null) {
            return 0;
        }
        return rvComplaints.size();
    }

    public class ComplaintViewHolder extends  RecyclerView.ViewHolder {

        private TextView cvhComplaint;
        private TextView cvhStatus;
        private RelativeLayout cvhParent;

        public ComplaintViewHolder(@NonNull View itemView) {
            super(itemView);
            cvhComplaint = (TextView) itemView.findViewById(R.id.list_complaint);
            cvhStatus = (TextView) itemView.findViewById(R.id.list_complaint_status);
            cvhParent = (RelativeLayout) itemView.findViewById(R.id.comp_list);
        }
    }
}
