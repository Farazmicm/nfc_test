package com.example.nfc_test;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.nfc_test.R;
import com.example.nfc_test.models.UserDetailsResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class StudentDataRecyclerViewBind extends RecyclerView.Adapter<StudentDataViewHolder> {
    List<UserDetailsResult> list
            = Collections.emptyList();

    Context context;
    ClickListener listener;

    public StudentDataRecyclerViewBind(List<UserDetailsResult> list, Context context, ClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout

        View dataView = inflater.inflate(R.layout.student_item_data, parent, false);

        StudentDataViewHolder viewHolder
                = new StudentDataViewHolder(dataView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDataViewHolder viewHolder, int position) {
        final int index = viewHolder.getAdapterPosition();
        UserDetailsResult userDetailsResult = list.get(position);
        try {
            if (userDetailsResult.getUserFullName() == null && userDetailsResult.getUserFullName().isEmpty()) {
                viewHolder.txtUserFullName.setVisibility(View.GONE);
            } else {
                viewHolder.txtUserFullName.setVisibility(View.VISIBLE);
                viewHolder.txtUserFullName.setText(userDetailsResult.getUserFullName());
            }
            if (userDetailsResult.getContactNo() == null && userDetailsResult.getContactNo().isEmpty()) {
                viewHolder.txtContactNumber.setVisibility(View.GONE);
            } else {
                viewHolder.txtContactNumber.setVisibility(View.VISIBLE);
                viewHolder.txtContactNumber.setText(Html.fromHtml("<font color=#000000><b>Contact no : </b></font>" + userDetailsResult.getContactNo()));
            }
            if (userDetailsResult.getClassName() == null && userDetailsResult.getDivisionName() == null) {
                viewHolder.txtClassDiv.setText("");
                viewHolder.txtClassDiv.setVisibility(View.GONE);
            } else {
                viewHolder.txtClassDiv.setVisibility(View.VISIBLE);
                viewHolder.txtClassDiv.setText(Html.fromHtml("<font color=#000000><b>Class Division : </b></font>" + userDetailsResult.getClassName() + " - " + userDetailsResult.getDivisionName()));
            }
            viewHolder.txtUserID.setText(Html.fromHtml("<font color=#000000><b>User Id : </b></font>" + userDetailsResult.getUserID()));

            List<String> cardNumbers = Arrays.asList(userDetailsResult.getRFIDNumbers().split(","));
            String cardN = "";
            if (cardNumbers.size() > 0) {
                for (int i = 0; i < cardNumbers.size(); i++) {
                    if (cardNumbers.get(i).isEmpty()) {

                    } else {
                        cardN += "- " + cardNumbers.get(i) + "\n";
                    }
                }
                viewHolder.txtCardNumbers.setText(cardN);
                viewHolder.txtCardNumbers.setVisibility(View.VISIBLE);
                viewHolder.tvCardNo.setVisibility(View.VISIBLE);
            } else {
                viewHolder.txtCardNumbers.setVisibility(View.GONE);
                viewHolder.tvCardNo.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){}

        /*if(!userDetailsResult.getUserProfileImage().isEmpty()){
            String imgUrl = MyVariables.SCHOOL_WEB_URL + "/" + MyVariables.SCHOOL_GROUP_CODE + "?imageFileHostedPath="+userDetailsResult.getUserProfileImage();
            try {
                Log.e("ImageURL",imgUrl);
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Drawable d = MyVariables.LoadImageFromWebOperations(imgUrl);
                            this.setImage(d);
                        }catch (Exception e){
                            Log.e("LoadImage",e.toString());
                        }
                    }

                    protected void setImage(Drawable d){
                        viewHolder.usrImage.setImageDrawable(d);
                    }
                });
                th.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        */
        if (userDetailsResult.isDenied()) {
            viewHolder.txtDeniedMsg.setText(userDetailsResult.getDeniedReason());
            viewHolder.txtDeniedMsg.setVisibility(View.VISIBLE);
            viewHolder.btnTapForAtt.setVisibility(View.GONE);
        } else {

            viewHolder.btnTapForAtt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.click(index, userDetailsResult);
                }
            });
            viewHolder.btnTapForAtt.setVisibility(View.VISIBLE);
            viewHolder.txtDeniedMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
