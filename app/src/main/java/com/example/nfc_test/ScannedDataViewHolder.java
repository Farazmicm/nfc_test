package com.example.nfc_test;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.nfc_test.R;
import com.google.android.material.imageview.ShapeableImageView;

public class ScannedDataViewHolder extends RecyclerView.ViewHolder{
    View view;
    TextView sc_txtUserFullName,sc_txtClassDiv,sc_txtCardNumbers,sc_txtDeniedMsg,sc_datetime,sc_txtStatus;
    ShapeableImageView sc_usrImage;

    public ScannedDataViewHolder(@NonNull View itemView) {
        super(itemView);
        sc_txtUserFullName = (TextView) itemView.findViewById(R.id.sc_txtUserFullName);
        sc_txtClassDiv = (TextView) itemView.findViewById(R.id.sc_txtClassDiv);
        sc_txtCardNumbers = (TextView) itemView.findViewById(R.id.sc_txtCardNumbers);
        sc_datetime= (TextView) itemView.findViewById(R.id.sc_datetime);
        sc_usrImage = (ShapeableImageView)itemView.findViewById(R.id.sc_usrImage);
        sc_txtDeniedMsg = (TextView) itemView.findViewById(R.id.sc_txtDeniedMsg);
        sc_txtStatus = (TextView) itemView.findViewById(R.id.sc_txtStatus);
        view = itemView;
    }
}
