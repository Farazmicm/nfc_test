package com.example.nfc_test;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.nfc_test.R;
import com.example.nfc_test.models.ResponseModel;
import com.example.nfc_test.models.UserAttendanceExternalAPIVM;
import com.example.nfc_test.models.UserDetailsResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentDataAdapter extends ArrayAdapter<UserDetailsResult> {

    private Context context;
    private WebService webService;
    private View listDataView;
    ToastHandler mToastHandler;

    public StudentDataAdapter(Context context, int textViewResourceId, List<UserDetailsResult> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.webService = new WebService();
        mToastHandler = new ToastHandler(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.student_item_data, null);
        }

        UserDetailsResult item = getItem(position);
        if (item != null) {

            TextView txtUserFullName = (TextView) view.findViewById(R.id.txtUserFullName);

            if (txtUserFullName != null) {
                txtUserFullName.setText(item.getUserFullName());
            }

            TextView txtUserID = (TextView) view.findViewById(R.id.txtUserID);
            if (txtUserID != null) {
                txtUserID.setText(item.getUID());
            }

            TextView txtClassDiv = (TextView) view.findViewById(R.id.txtClassDiv);
            if (txtClassDiv != null) {
                txtClassDiv.setText(item.getClassName() + " - " + item.getDivisionName());
            }

            TextView txtContactNumber = (TextView) view.findViewById(R.id.txtContactNumber);
            if (txtContactNumber != null) {
                txtContactNumber.setText(item.getContactNo());
            }

            TextView txtCardNumbers = (TextView) view.findViewById(R.id.txtCardNumbers);
            if (txtCardNumbers != null) {
                txtCardNumbers.setText(item.getRFIDNumbers());
            }

            Button btnTapForAtt = (Button) view.findViewById(R.id.btnTapForAtt);
            if (btnTapForAtt != null) {
                btnTapForAtt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!item.getRFIDNumbers().isEmpty()) {
                            String[] numbers = new String[100];
                            numbers = item.getRFIDNumbers().split(",");
                            if (numbers.length > 0) {
                                if (customListner != null) {
                                    mToastHandler.showToast("position: " + position, Toast.LENGTH_LONG);
                                    customListner.onButtonClickListner(position, numbers[0]);
                                }
                                //doStudentVerification(numbers[0]);
                            }
                        }
                    }
                });
            }
        }
        return view;
    }

    public void doStudentVerification(String rfidCardNumber) {
        try {
            AsyncTask<String, Void, Void> runningTask;
            runningTask = new LongOperation();
            runningTask.execute(rfidCardNumber);
        } catch (Exception ex) {
            if (!MyVariables.IsProduction) {
                Log.v("Error", ex.toString());
            }
        }
    }

    private final class LongOperation extends AsyncTask<String, Void, Void> {
        ResponseModel responseModel1 = new ResponseModel();
        int viewPosition = -1;

        @Override
        protected Void doInBackground(String... params) {
            //String[] inputParam = params[0].split("##");

            String RfidNumber = params[0];
            //viewPosition = Integer.parseInt(inputParam[1]);

            Map<String, String> paramsList = new HashMap<>();
            List<UserAttendanceExternalAPIVM> externalAPIVMList = new ArrayList<UserAttendanceExternalAPIVM>();
            UserAttendanceExternalAPIVM externalAPIVM = new UserAttendanceExternalAPIVM(RfidNumber, "", MyVariables.getSystemDateTime(), MyVariables.getSystemDateTime(), "Default", "School", "", "", "10", true, false, "In", true, "manual");
            externalAPIVMList.add(externalAPIVM);
            Gson gson = new Gson();

            if (!MyVariables.IsProduction) {
                Log.v("JsonData", gson.toJson(externalAPIVMList));
            }
            paramsList.put("pRequestCriteria", "insert");
            paramsList.put("pRequestJsonData", gson.toJson(externalAPIVMList));
            webService.sendPostRequest("/ExternalAPI/UserAttendanceNFCMobileDataPush", paramsList, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    LongOperation.this.showData(result);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (listDataView != null) {

                TextView txtResult = (TextView) listDataView.findViewById(R.id.txtResult);
                if (txtResult != null) {
                    txtResult.setText(responseModel1.getResponseMessage());
                    txtResult.append("\n\n\n" + responseModel1.getResponseData());
                }
            }
        }

        protected void showData(String result) {
            Gson gson = new Gson();
            if (!MyVariables.IsProduction) {
                Log.v("Data", result);
            }
            ResponseModel responseModel = gson.fromJson(result, ResponseModel.class);
            if (!MyVariables.IsProduction) {
                Log.v("ResponseData", responseModel.getResponseData());
                Log.v("ResponseMessage", responseModel.getResponseMessage());
                Log.v("Success", "" + responseModel.isSuccess());
            }
            responseModel1 = responseModel;

            mToastHandler.showToast(responseModel.getResponseMessage(), Toast.LENGTH_LONG);
        }
    }

    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }
}



