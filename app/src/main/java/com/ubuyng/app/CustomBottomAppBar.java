package com.ubuyng.app;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ubuyng.app.HelperClasses.BottomSheetListenersRoot;
import com.ubuyng.app.fragments.FragmentSixBottomSheet;

import java.util.List;

public class CustomBottomAppBar extends AppCompatActivity implements BottomSheetListenersRoot {

    private RecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton floatingActionButton;
    private FragmentSixBottomSheet fragmentSixBottomSheet;
    private boolean isFabAttachedAtCenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "TAP ON IMAGES AND MENU ICONS", Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.rv35);
        bottomAppBar = findViewById(R.id.bar);
        setSupportActionBar(bottomAppBar);

        floatingActionButton = findViewById(R.id.fab11);
        fragmentSixBottomSheet = new FragmentSixBottomSheet();
//        appBarAdapter = new AppBarAdapter(RecyclerData.recyclerViewData());
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(appBarAdapter);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name", "My Team");
                fragmentSixBottomSheet.setArguments(bundle);
                fragmentSixBottomSheet.show(getSupportFragmentManager(), "dialog");
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_projects:
                        Toast.makeText(getApplicationContext(),
                                "Projects activity",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_inbox:
                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_more:
                        Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClicked(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
