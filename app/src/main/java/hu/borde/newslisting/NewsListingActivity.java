package hu.borde.newslisting;

import android.content.Context;
import android.app.LoaderManager;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import hu.borde.newslisting.model.News;
import hu.borde.newslisting.model.adapter.NewsListAdapter;
import hu.borde.newslisting.networking.NewsLoader;

public class NewsListingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = "BookListing";

    /**
     * Adapter for the listview.
     */
    private NewsListAdapter mAdapter;

    /**
     * Base API query string.
     */
    private static final String URL_STRING = "http://content.guardianapis.com/search?tag=technology/technology&from-date=2014-01-01&api-key=test";

    /**
     * Query term written in search field.
     */
    private String queryString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_listing);

        ListView newsList = (ListView) findViewById(R.id.news_list);
        newsList.setEmptyView(findViewById(R.id.empty_view));

        this.mAdapter = new NewsListAdapter(this, new ArrayList<News>());

        newsList.setAdapter(this.mAdapter);
        findViewById(R.id.empty_view_text).setVisibility(View.GONE);
        hideStatusIcon();

        if (hasNetworkAccess()) {
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            getLoaderManager().initLoader(0, null, NewsListingActivity.this);
        } else {
            showNoNetworkStatus();
        }




    }

    public NewsListAdapter getAdapter() {
        return this.mAdapter;
    }

    /**
     * Set status icon and error message according to no network state.
     */
    private void showNoNetworkStatus() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.empty_view_text)).setText(R.string.no_internet_found);
        ((ImageView)findViewById(R.id.empty_view_status_icon)).setImageResource(R.drawable.wifi_off);
        showStatusIcon();
    }

    /**
     * Checks if app has network access.
     * @return true if has network access
     */
    private boolean hasNetworkAccess() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Hide empty list status icon.
     */
    private void hideStatusIcon() {
        ImageView sadFace = (ImageView) findViewById(R.id.empty_view_status_icon);
        sadFace.setVisibility(View.GONE);
    }

    /**
     * Show empty list status icon.
     */
    private void showStatusIcon() {
        ImageView sadFace = (ImageView) findViewById(R.id.empty_view_status_icon);
        sadFace.setVisibility(View.VISIBLE);
    }


    @Override
    public android.content.Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(NewsListingActivity.this, URL_STRING);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> data) {

        mAdapter.clear();

        findViewById(R.id.progress_bar).setVisibility(View.GONE);

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {

            ((TextView) findViewById(R.id.empty_view_text)).setText(R.string.no_result_found);
            ((ImageView)findViewById(R.id.empty_view_status_icon)).setImageResource(R.drawable.emoticon_sad);
            showStatusIcon();

        }

        ((ListView)findViewById(R.id.news_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((News)parent.getItemAtPosition(position)).getWebUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
