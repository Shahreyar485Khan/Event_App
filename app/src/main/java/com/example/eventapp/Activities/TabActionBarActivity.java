/*
package com.example.eventapp.Activities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBar.Tab;
import android.app.Activity;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.eventapp.Fragments.CalendarFragment;
import com.example.eventapp.Fragments.EventFragment;
import com.example.eventapp.R;

public class TabActionBarActivity extends Activity {

    private class TabListener<T extends Fragment> implements
	    ActionBar.TabListener {
	private final Activity mActivity;
	private final Class<T> mClass;
	private Fragment mFragment;
	private final String mTag;

	*/
/**
	 * Constructor used each time a new tab is created.
	 * 
	 * @param activity
	 *            The host Activity, used to instantiate the fragment
	 * @param tag
	 *            The identifier tag for the fragment
	 * @param clz
	 *            The fragment's Class, used to instantiate the fragment
	 *//*

	public TabListener(Activity activity, String tag, Class<T> clz) {
	    mActivity = activity;
	    mTag = tag;
	    mClass = clz;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	    // User selected the already selected tab. Usually do nothing.
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	    // Check if the fragment is already initialized
	    Iterator<WeakReference<Fragment>> it = fragList.iterator();
	    while (it.hasNext()) {
		Fragment f = it.next().get();
		if (f.getTag().equals(mTag)) {

		    mFragment = f;
		} else {
		    ft.detach(f);
		}
	    }

	    if (mFragment == null) {
		// If not, instantiate and add it to the activity
		mFragment = Fragment.instantiate(mActivity, mClass.getName());
		ft.add(android.R.id.content, mFragment, mTag);
	    } else {
		// If it exists, simply attach it in order to show it
		ft.attach(mFragment);
	    }
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	    if (mFragment != null) {
		// Detach the fragment, because another one is being attached
		ft.detach(mFragment);
	    }
	}

    }

    List<WeakReference<Fragment>> fragList = new ArrayList<WeakReference<Fragment>>();

    @Override
    public void onAttachFragment(Fragment fragment) {
	fragList.add(new WeakReference<Fragment>(fragment));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ActionBar actionBar = getActionBar();

//	ActionBar actionBar1 = getActionBar();
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	String label1 = "new";
	Tab tab = actionBar.newTab();
	tab.setText(label1);

	TabListener<CalendarFragment> tl = new TabListener<CalendarFragment>(this,
		label1, CalendarFragment.class);
	tab.setTabListener(tl);
	actionBar.addTab(tab);

	String label2 = getResources().getString(R.string.label2);
	tab = actionBar.newTab();
	tab.setText(label2);

	TabListener<EventFragment> tl2 = new TabListener<EventFragment>(this,
		label2, EventFragment.class);
	tab.setTabListener(tl2);
	actionBar.addTab(tab);

    }

    public void replaceFragment(View v) {

	String label1;
	FragmentManager fm;
	FragmentTransaction ft;
	Iterator<WeakReference<Fragment>> it;
	int id = v.getId();

	switch (id) {
	case R.id.btn1:
	    label1 = getResources().getString(R.string.label1);
	    Fragment f3 = new Tab3Fragment();

	    fm = getFragmentManager();
	    ft = fm.beginTransaction();
	    ft.replace(android.R.id.content, f3, label1);
	    ft.commit();

	    it = fragList.iterator();
	    while (it.hasNext()) {
		WeakReference<Fragment> ref = it.next();
		Fragment f = ref.get();
		if (f instanceof Tab1Fragment) {
		    it.remove();
		}
	    }

	    break;
	case R.id.btn3:
	    label1 = getResources().getString(R.string.label1);
	    Fragment f1 = new Tab1Fragment();

	    fm = getFragmentManager();
	    ft = fm.beginTransaction();
	    ft.replace(android.R.id.content, f1, label1);
	    ft.commit();

	    it = fragList.iterator();
	    while (it.hasNext()) {
		WeakReference<Fragment> ref = it.next();
		Fragment f = ref.get();
		if (f instanceof Tab3Fragment) {
		    it.remove();
		}
	    }

	    break;
	default:
	    break;
	}

    }
}*/
