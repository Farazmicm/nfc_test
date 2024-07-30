package com.example.nfc_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.nfc_test.R;
import com.example.nfc_test.models.ScannedUserDetailsResult;
import com.example.nfc_test.models.UserDetailsResult;

import java.util.Collections;
import java.util.List;


public class ScannedDataRecyclerViewBind extends RecyclerView.Adapter<ScannedDataViewHolder> {
    List<ScannedUserDetailsResult> list
            = Collections.emptyList();

    Context context;
    //ClickListener listener;

    public ScannedDataRecyclerViewBind(List<ScannedUserDetailsResult> list, Context context) {
        this.list = list;
        this.context = context;
        //this.listener = listener;
    }

    @NonNull
    @Override
    public ScannedDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View dataView = inflater.inflate(R.layout.scanned_item_data,parent, false);

        ScannedDataViewHolder viewHolder
                = new ScannedDataViewHolder(dataView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScannedDataViewHolder viewHolder, int position) {
        final int index = viewHolder.getAdapterPosition();
        ScannedUserDetailsResult scannedUserDetailsResult = list.get(position);
        UserDetailsResult userDetailsResult = scannedUserDetailsResult.userDetailsResult;
        viewHolder.sc_txtUserFullName.setText(userDetailsResult.getUserFullName());

        if(userDetailsResult.getClassName().isEmpty()){
            viewHolder.sc_txtClassDiv.setText("");
        }else{
            viewHolder.sc_txtClassDiv.setText(userDetailsResult.getClassName() + " - " + userDetailsResult.getDivisionName());
        }
        viewHolder.sc_txtCardNumbers.setText(scannedUserDetailsResult.scannedCard);
        viewHolder.sc_txtStatus.setText(scannedUserDetailsResult.isAttendanceTaken ? "Taken" : "Not Taken");
        viewHolder.sc_datetime.setText("Time: " + scannedUserDetailsResult.attendanceTakenTime);
        if(userDetailsResult.isDenied()) {
            viewHolder.sc_txtDeniedMsg.setText(userDetailsResult.getDeniedReason());
            viewHolder.sc_txtDeniedMsg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.sc_txtDeniedMsg.setText("");
            viewHolder.sc_txtDeniedMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }




}
