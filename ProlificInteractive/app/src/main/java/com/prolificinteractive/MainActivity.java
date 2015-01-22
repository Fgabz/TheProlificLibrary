package com.prolificinteractive;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.prolificinteractive.fragments.LibraryFragment;
import com.prolificinteractive.utils.Globals;


public class MainActivity extends ActionBarActivity
{



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Globals g = Globals.getInstance();
        g.setUrl("http://prolific-interview.herokuapp.com/54bd7b72778b4a0008876e08");




        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, LibraryFragment.newInstance()).addToBackStack(null).commit();
    }


    @Override
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() > 1)
            getFragmentManager().popBackStack();
    }
}
