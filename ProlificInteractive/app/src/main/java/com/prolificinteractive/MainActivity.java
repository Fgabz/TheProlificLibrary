package com.prolificinteractive;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.prolificinteractive.fragments.LibraryFragment;
import com.prolificinteractive.models.BaseResponse;
import com.prolificinteractive.services.IDeleteLibraryService;
import com.prolificinteractive.utils.Globals;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity
{

    private IDeleteLibraryService mDeleteLibraryService;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Globals g = Globals.getInstance();
        g.setUrl("http://prolific-interview.herokuapp.com/54bd7b72778b4a0008876e08");

        mDeleteLibraryService = new RestAdapter.Builder()
            .setEndpoint(g.getUrl())
            .build()
            .create(IDeleteLibraryService.class);


        getFragmentManager()
                .beginTransaction()
                .add(R.id.container, LibraryFragment.newInstance()).addToBackStack(null).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete)
        {

            SnackBar snackbar = new SnackBar(MainActivity.this,
                    "Are you sure that you want to delete all your library ?",
                    "YES", new View.OnClickListener() {
                @Override public void onClick(View v)
                {
                    mDeleteLibraryService.deleteLibrary(new Callback<BaseResponse>() {
                        @Override public void success(BaseResponse baseResponse, Response response)
                        {
                            Toast.makeText(MainActivity.this, "Library Deleted", Toast.LENGTH_SHORT).show();
                        }

                        @Override public void failure(RetrofitError error)
                        {
                            Toast.makeText(MainActivity.this, " Error Library Deleted :(",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            snackbar.show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() > 1)
            getFragmentManager().popBackStack();
    }
}
