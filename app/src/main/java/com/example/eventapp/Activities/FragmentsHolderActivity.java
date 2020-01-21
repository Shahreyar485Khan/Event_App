package com.example.eventapp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Adapters.ViewPagerAdapter;
import com.example.eventapp.Fragments.CalendarFragment;
import com.example.eventapp.Fragments.EventFragment;
import com.example.eventapp.Fragments.RequestsFragment;
import com.example.eventapp.Fragments.SearchFragment;
import com.example.eventapp.R;
import com.example.eventapp.Utils.Constants;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.example.eventapp.Utils.ViewPagerNoSwipe;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FragmentsHolderActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener {


    private Toolbar toolbar;
    AlertDialog.Builder builder;
    private TabLayout tabLayout;
    ViewPagerNoSwipe viewPager;
    PhpMethodsUtils phpMethodsUtils;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments_holder);

       // toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        phpMethodsUtils = new PhpMethodsUtils(this);

        viewPager = new ViewPagerNoSwipe(this, R.styleable.MyViewPager_swipeable);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        mAuth = FirebaseAuth.getInstance();

        Constants.currentUser = mAuth.getCurrentUser();
        phpMethodsUtils.getCurrentDevice();








        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            //    Toast.makeText(FragmentsHolderActivity.this, "onPageSelected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

             //   Toast.makeText(FragmentsHolderActivity.this, "onTabSelected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }


    private void setupViewPager(ViewPagerNoSwipe viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CalendarFragment(), "Calender");
        adapter.addFragment(new EventFragment(), "Event");
        adapter.addFragment(new RequestsFragment(), "Requests");
        adapter.addFragment(new SearchFragment(), "Search");
        viewPager.setAdapter(adapter);
    }


    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Calender");
        tabOne.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tabOne.setTextSize(12);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Events");
        tabTwo.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tabTwo.setTextSize(12);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Requests");
        tabThree.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tabThree.setTextSize(12);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setText("Search");
        tabfour.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tabfour.setTextSize(12);
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class DeactivatedViewPager extends ViewPager {

        public DeactivatedViewPager(Context context) {
            super(context);
        }

        public DeactivatedViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean canScrollHorizontally(int direction) {
            return false;
        }
    }

}
