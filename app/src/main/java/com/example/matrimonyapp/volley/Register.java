package com.example.matrimonyapp.volley;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class Register {


    String fullName, mobileNo, emailId, gender, address;

    AlertDialog.Builder builder;
    ProgressDialog progressDialog;


    public Register(String fullName, String mobileNo, String emailId, String gender, String address) {
        this.fullName = fullName;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.gender = gender;
        this.address = address;
    }


   /* private void register() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            if (!obj.getBoolean("error")) {


                                //Toast.makeText(getContext(),"Name - "+obj.getString("fullName"),Toast.LENGTH_LONG).show();



                            }
                            else{
                                builder.setMessage("User already exists with same Mobile No please Login")
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.setTitle("User already exists");
                                alert.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        *//*txt_signUp.setText("Sign Up");
                        txt_signUp.setEnabled(true);*//*
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        builder.setMessage("Something went wrong please try again !")
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle("Error");
                        alert.show();
                        *//*txt_signUp.setText("Sign Up");
                        txt_signUp.setEnabled(true);*//*
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("FullName", fullName);
                params.put("MobileNo", mobileNo);
                params.put("EmailId", emailId);
                params.put("Gender", gender);
                params.put("Address", address);
                return params;
            }
        };

        //VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }*/

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;


        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]) * 1000;
                //register();
                //Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            // finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            /*progressDialog = ProgressDialog.show(,
                    "Loading...",
                    "Wait for result..");*/
        }


        @Override
        protected void onProgressUpdate(String... text) {
            // finalResult.setText(text[0]);

        }

    }







    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



}
