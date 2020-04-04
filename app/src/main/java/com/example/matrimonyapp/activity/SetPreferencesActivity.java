package com.example.matrimonyapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.appyvet.materialrangebar.RangeBar;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.PopupFetcher;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;

public class SetPreferencesActivity extends AppCompatActivity {

    private com.appyvet.materialrangebar.RangeBar rangeBar_salaryRange, rangeBar_ageRange, rangeBar_heightRange;

    private View include_toolbar;

    private Context context;

    private TextView textView_toolbarHeader, textView_ageRange, textView_salaryRange, textView_heightRange,
            textView_qualificationId, textView_maritalStatusId, textView_familyTypeId, textView_familyValuesId,
            textView_colorId;

    private EditText editText_qualification, editText_maritalStatus, editText_familyType, editText_familyValues,
            editText_color;

    private RadioGroup radioGroup_diet;

    private CardView cardView_maritalStatus;

    private ImageView imageView_back;

    private String diet;

    UserModel userModel;

    PopupFetcher popupFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferences);

        //Initializations
        context = getApplicationContext();


        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(context).getUser();


        // Toolbar views initialization
        include_toolbar = findViewById(R.id.include_toolbar);
        textView_toolbarHeader = findViewById(R.id.textView_toolbarHeader);
        imageView_back = findViewById(R.id.imageView_back);


        editText_qualification = findViewById(R.id.editText_qualification);
        editText_maritalStatus = findViewById(R.id.editText_maritalStatus);
        radioGroup_diet = findViewById(R.id.radioGroup_diet);
        editText_familyType = findViewById(R.id.editText_familyType);
        editText_familyValues = findViewById(R.id.editText_familyValues);
        editText_color = findViewById(R.id.editText_color);
        rangeBar_ageRange = findViewById(R.id.rangeBar_ageRange);
        rangeBar_salaryRange = findViewById(R.id.rangeBar_salaryRange);
        rangeBar_heightRange= findViewById(R.id.rangeBar_heightRange);
        textView_ageRange = findViewById(R.id.textView_ageRange);
        textView_salaryRange = findViewById(R.id.textView_salaryRange);
        textView_heightRange = findViewById(R.id.textView_heightRange);

        cardView_maritalStatus = findViewById(R.id.cardView_maritalStatus);

        textView_qualificationId = findViewById(R.id.textView_qualificationId);
        textView_maritalStatusId = findViewById(R.id.textView_maritalStatusId);
        textView_familyTypeId = findViewById(R.id.textView_familyTypeId);
        textView_familyValuesId = findViewById(R.id.textView_familyValuesId);
        textView_colorId = findViewById(R.id.textView_colorId);


        include_toolbar.setBackgroundColor(Color.WHITE);
        textView_toolbarHeader.setText("Set Preferences");

        rangeBarChangeListener("Age",rangeBar_ageRange, textView_ageRange);
        rangeBarChangeListener("Salary",rangeBar_salaryRange, textView_salaryRange);
        rangeBarChangeListener("Height",rangeBar_heightRange, textView_heightRange);
        rangeBar_salaryRange.setRangePinsByValue(10,20);


        //rangeSeekBar_age.setBarColor(Color.GRAY);

        /*rangeSeekBar_age.setColorFilter(Color.BLACK);
        rangeSeekBar_age.setDrawingCacheBackgroundColor(Color.BLACK);*/

        /*rangeSeekBar_age.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

            }
        });*/

        popupFetcher = new PopupFetcher(context);

        editText_qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("SiblingRelation");
            }
        });




        popupMenuEditText(editText_maritalStatus,"MaritalStatus", textView_maritalStatusId.getText().toString());
        popupMenuEditText(editText_familyType, "FamilyType", textView_familyTypeId.getText().toString());
        popupMenuEditText(editText_familyValues, "FamilyValues", textView_familyValuesId.getText().toString());
        popupMenuEditText(editText_color, "Color", textView_colorId.getText().toString());

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void rangeBarChangeListener(final String bar, RangeBar rangeBar, final TextView textView)
    {
        rangeBar.setSelected(true);

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                if(bar.equalsIgnoreCase("Age")) {
                    textView.setText(leftPinValue+" - "+rightPinValue);
                }
                else if(bar.equalsIgnoreCase("Salary"))
                {
                    textView.setText(leftPinValue+"L - "+rightPinValue+"L");
                }
                else if(bar.equals("Height"))
                {
                    textView.setText(leftPinValue+"ft - "+rightPinValue+"ft");
                }
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });
    }

    public  void popupMenuEditText(final EditText editText, final String urlFor, String id)
    {


        AsyncTaskLoad runner = new AsyncTaskLoad();
        runner.execute(urlFor, id);




        /*Context wrapper = new ContextThemeWrapper(context,R.style.MyCustomPopupMenu);

        final PopupMenu popupMenu = new PopupMenu(wrapper, editText);

        popupMenu.getMenuInflater().inflate(menu,popupMenu.getMenu());

        editText.setText(popupMenu.getMenu().getItem(0).getTitle());


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        editText.setText(menuItem.getTitle());
                        return true;
                    }
                });

                popupMenu.show();

            }
        });
*/
    }



    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;




        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {

/*                if(params[0]=="Qualification")
                {
                    popupFetcher.loadList(URLs.URL_GET_+"Language="+userModel.getLanguage(),
                            "SiblingListId","SiblingListName",editText_qualification,
                            textView_relationId,context, R.style.MyCustomPopupMenu);
                }
                else */

                if(params[0]=="MaritalStatus")
                {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_MARITALSTATUS+"Language="+userModel.getLanguage(),
                            "MaritalStatusId","MaritalStatusName",editText_maritalStatus,
                            textView_maritalStatusId,context, R.style.MyCustomPopupMenu,id);
                }

                else if(params[0]=="FamilyType")
                {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_FAMILYTYPE+"Language="+userModel.getLanguage(),
                            "FamilyTypeId","FamilyTypeName",editText_familyType,
                            textView_familyTypeId,context, R.style.MyCustomPopupMenu,id);
                }

                else if(params[0]=="FamilyValues")
                {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_FAMILYVALUES+"Language="+userModel.getLanguage(),
                            "FamilyValuesId","FamilyValuesName",editText_familyValues,
                            textView_familyValuesId,context, R.style.MyCustomPopupMenu,id);
                }

                else if(params[0]=="Color")
                {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_SKINCOLOR+"Language="+userModel.getLanguage(),
                            "SkinColourId","SkinColourName",editText_color,
                            textView_colorId,context, R.style.MyCustomPopupMenu,id);
                }


            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }



}
