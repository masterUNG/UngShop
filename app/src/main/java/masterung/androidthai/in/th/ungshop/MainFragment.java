package masterung.androidthai.in.th.ungshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Login Controller
        loginController();


    }   // Main Method

    private void loginController() {
        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userEditText = getView().findViewById(R.id.edtUser);
                EditText passwordEditText = getView().findViewById(R.id.edtPassword);

                String userString = userEditText.getText().toString().trim();
                String passwordString = passwordEditText.getText().toString().trim();
                MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity());

//                Check Space
                if (userString.isEmpty() || passwordString.isEmpty()) {
//                    Have Space
                    myAlertDialog.normalDialog("Have Space", "Please Fill Every Blank");
                } else {
//                    No Space

                    try {

                        GetUserWhereName getUserWhereName = new GetUserWhereName(getActivity());
                        MyConstant myConstant = new MyConstant();

                        getUserWhereName.execute(userString, myConstant.getUrlGetUserWhereName());
                        String resultJSoN = getUserWhereName.get();
                        Log.d("25novV1", "resultJSoN ==> " + resultJSoN);

//                      Check User and Pass
                        if (resultJSoN.equals("null")) {
                            myAlertDialog.normalDialog("User False", "No " + userString + " in my Database");
                        } else {

                            JSONArray jsonArray = new JSONArray(resultJSoN);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            String truePassword = convertMD5toString(jsonObject.getString("pass"));
                            Log.d("25novV1", "truePassword ==> " + truePassword);

                            if (passwordString.equals(truePassword)) {
                                Intent intent = new Intent(getActivity(), ServiceActivity.class);
                                intent.putExtra("Login", resultJSoN);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                myAlertDialog.normalDialog("Password False", "Please Try Agains Password False" );
                            }

                        }   // if


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }   // if
            }
        });


    }

    private String convertMD5toString(String md5String) {

        String resultString = "";

        try {

            MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");
            messageDigest.update(md5String.getBytes());
            byte[] bytes = messageDigest.digest();

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < bytes.length; i += 1) {
                String hexString = Integer.toHexString(0xFF & bytes[i]);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                    stringBuffer.append(hexString);
                }   // while
            }   // for
            resultString = stringBuffer.toString();
            return resultString;


        } catch (Exception e) {
            e.printStackTrace();
            return resultString;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

}
