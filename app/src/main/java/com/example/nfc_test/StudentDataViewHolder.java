package com.example.nfc_test;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc_test.R;
import com.google.android.material.imageview.ShapeableImageView;

public class StudentDataViewHolder extends RecyclerView.ViewHolder{
    View view;
    TextView txtUserFullName,txtUserID,txtClassDiv,txtContactNumber,txtCardNumbers,txtDeniedMsg,tvCardNo;
    Button btnTapForAtt;
    ShapeableImageView usrImage;

    public StudentDataViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUserFullName = (TextView) itemView.findViewById(R.id.txtUserFullName);
        txtUserID = (TextView) itemView.findViewById(R.id.txtUserID);
        txtClassDiv = (TextView) itemView.findViewById(R.id.txtClassDiv);
        txtContactNumber = (TextView) itemView.findViewById(R.id.txtContactNumber);
        txtCardNumbers = (TextView) itemView.findViewById(R.id.txtCardNumbers);
        btnTapForAtt = (Button)itemView.findViewById(R.id.btnTapForAtt);
        usrImage = (ShapeableImageView)itemView.findViewById(R.id.usrImage);
        txtDeniedMsg = (TextView) itemView.findViewById(R.id.txtDeniedMsg);
        tvCardNo = (TextView) itemView.findViewById(R.id.txtCardNumber);
        view = itemView;
    }
}
