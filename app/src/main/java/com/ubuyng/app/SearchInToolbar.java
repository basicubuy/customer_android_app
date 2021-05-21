package com.ubuyng.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.tabs.TabLayout;
import com.ubuyng.app.fragments.FragmentEventsCat;
import com.ubuyng.app.fragments.FragmentHomeCat;
import com.ubuyng.app.fragments.FragmentMoreCat;
import com.ubuyng.app.fragments.FragmentWebCat;
import com.ubuyng.app.ubuyapi.Models.ItemProject;
import com.ubuyng.app.ubuyapi.adapters.SearchSubCatAdapter;
import com.ubuyng.app.ubuyapi.util.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class SearchInToolbar extends AppCompatActivity {

    private Toolbar toolbar;

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private String sub_id, sub_Title, locate_string, user_address, user_city, user_lat, user_lng;
    TextView cat_tip, location_tip, warning_text;
    Button enter_question;

    private static final String TAG = "SearchInToolbar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_search);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        sub_id = intent.getStringExtra("Sub_Id");
        sub_Title = intent.getStringExtra("Sub_Title");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(sub_Title);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Tools.setSystemBarColor(this);

        location_tip = findViewById(R.id.location_tip);
        cat_tip = findViewById(R.id.cat_tip);
        enter_question = findViewById(R.id.enter_question);
        warning_text = findViewById(R.id.warning_text);

        locate_string = "Tell us your location so we can find the nearest \"" + sub_Title + "\" expert near you ";
        location_tip.setText(locate_string);
        cat_tip.setText(sub_Title);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.map_key_api), Locale.US);
        }
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS
        ));
        autocompleteFragment.setCountry("NG");
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: " + place.getName() + " city is " + Objects.requireNonNull(place.getAddressComponents()).asList().get(3).getShortName() + " state is " + place.getAddressComponents().asList().get(4).getShortName() +", " + place.getId() + " The Lat is " + Objects.requireNonNull(place.getLatLng()).latitude + "and Lng is" + place.getLatLng().longitude);
                user_address = place.getName();


//                try {
//                    Log.i(TAG, "Place_tracker: " + place.getAddress()+ " place name is " + place.getName() + " state is " + place.getAddressComponents().asList().get(4).getShortName());
//                    user_city =
//                } catch (NullPointerException e) {
////                    System.out.print("Caught the NullPointerException");
//                    Log.i(TAG, "Place_tracker: bug caught");
//                }


                user_lat = String.valueOf(place.getLatLng().latitude);
                user_lng = String.valueOf(place.getLatLng().longitude);
//               TODO: make " find verified pros " button visible
                enter_question.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SearchInToolbar.this, TaskerActivity.class);
                intent.putExtra("sub_id", sub_id);
                intent.putExtra("sub_title", sub_Title);
                intent.putExtra("user_address", user_address);
                intent.putExtra("user_city", user_city);
                intent.putExtra("user_lat", user_lat);
                intent.putExtra("user_lng", user_lng);
                startActivity(intent);
            }
// place.getAddressComponents().asList().get(1).getShortName()
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        enter_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchInToolbar.this, TaskerActivity.class);
                intent.putExtra("sub_id", sub_id);
                intent.putExtra("sub_title", sub_Title);
                intent.putExtra("user_address", user_address);
                intent.putExtra("user_city", user_city);
                intent.putExtra("user_lat", user_lat);
                intent.putExtra("user_lng", user_lng);
                startActivity(intent);
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i(TAG, status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//                Log.i(TAG, "Places closed: ");
//            }
//        }
//    }
//
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
        finish();
    } else {
        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
    }
    return super.onOptionsItemSelected(item);
}



}
