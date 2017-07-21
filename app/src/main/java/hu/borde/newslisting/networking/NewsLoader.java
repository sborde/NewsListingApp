package hu.borde.newslisting.networking;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import hu.borde.newslisting.model.News;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * This member holds the request URL to fetch.
     */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<News> loadInBackground() {

        if (this.mUrl.isEmpty()) {
            return null;
        }

        List<News> returnList = null;

        try {
            returnList = RequestUtility.fetchNews(this.mUrl);
        } catch (IOException e) {
            Log.e("NewsLoader", "Error while book fetching" + e);
        }

        return returnList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
