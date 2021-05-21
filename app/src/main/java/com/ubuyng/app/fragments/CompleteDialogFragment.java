package com.ubuyng.app.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.alexvasilkov.gestures.commons.circle.CircleImageView;
import com.squareup.picasso.Picasso;
import com.ubuyng.app.EditProfileActivity;
import com.ubuyng.app.InternetActivity;
import com.ubuyng.app.ProjectBidActivity;
import com.ubuyng.app.R;
import com.ubuyng.app.ubuyapi.Models.ItemBids;
import com.ubuyng.app.ubuyapi.retrofit.UbuyApi;
import com.ubuyng.app.ubuyapi.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CompleteDialogFragment extends DialogFragment {

    private Toolbar toolbar;
    private ImageButton button;
    private String taskName, project_id, pro_name, pro_amount, pro_image, bid_id;
    private TextView pro_name_txt, task_name_txt, message_alert, bid_amount_txt;
    ImageButton mark_done_btn;
    private CircleImageView avi_pro_image;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complete_alert_full, container, false);
        toolbar = view.findViewById(R.id.trans_tool2);

        toolbar.setBackground(ContextCompat.getDrawable(getActivity(), android.R.drawable.dialog_holo_light_frame));
        toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.transparent));
        toolbar.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),android.R.color.transparent)));



//        bundle to get pass ids
        assert getArguments() != null;
         taskName = getArguments().getString("taskName");
         project_id = getArguments().getString("task_id");
         pro_name = getArguments().getString("bidder_name");
         pro_amount = getArguments().getString("bidder_amount");
         pro_image = getArguments().getString("bidder_image");
         bid_id = getArguments().getString("bid_id");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //You can change it as needed
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });
//        text area
        pro_name_txt = view.findViewById(R.id.pro_name_txt);
        pro_name_txt.setText(pro_name);

        task_name_txt = view.findViewById(R.id.task_name_txt);
        task_name_txt.setText(taskName);

        bid_amount_txt = view.findViewById(R.id.bid_amount_txt);
        bid_amount_txt.setText(pro_amount);

        message_alert = view.findViewById(R.id.message_alert);
        message_alert.setText("Are you satisfied with "+pro_name+" task?");

//        image view
        avi_pro_image = view.findViewById(R.id.avi_pro_image);
        Picasso.get().load(pro_image)
                .resize(150, 150)
                .error(R.drawable.loading_placeholder)
                .into(avi_pro_image);

        button = view.findViewById(R.id.mark_done_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCompletion();
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;


    }



    private void loadCompletion() {
        ProgressDialog pDialog;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UbuyApi.JSONGETURL+UbuyApi.JSONGETVERSION)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        UbuyApi ubuyapi = retrofit.create(UbuyApi.class);
        Call<String> call = ubuyapi.markComplete(bid_id, project_id);
        Log.i("onEmptyResponse", "test network");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("please wait ...");
        pDialog.setCancelable(false);
        pDialog.show();
        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                HttpUrl main_url = response.raw().request().url();
                Log.i("debugger", String.valueOf(main_url));
                if (response.body() != null) {
                    Log.i("onSuccess", response.body().toString());

                    String jsonresponse = response.body().toString();
                    getBidsJson(jsonresponse);

                } else {
                    Log.i("onEmptyResponse", "Returned empty ");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }
                if (response.isSuccessful()){
                    Log.i("onEmptyResponse", "Returned success");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Intent intent = new Intent(getActivity(), InternetActivity.class);
                startActivity(intent);
                Log.i("onEmptyResponse", "error occured");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getBidsJson(String jsonresponse) {
        try {
            JSONObject mainJson = new JSONObject(jsonresponse);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
            JSONObject objJson;
            for (int i = 0; i < jsonArray.length(); i++) {
                objJson = jsonArray.getJSONObject(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("DATA_TESTER", "NO DATA FOUND ON THIS URL");


        }
        displayData();
    }

    private void displayData() {
        showToast("Project completed");
        Objects.requireNonNull(getActivity()).onBackPressed();

    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }


}
