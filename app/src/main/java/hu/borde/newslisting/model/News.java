package hu.borde.newslisting.model;

import android.content.res.Resources;

import java.text.SimpleDateFormat;
import java.util.List;

import hu.borde.newslisting.R;


public class News {

    /**
     * Title of a given news.
     */
    private String mTitle;

    /**
     * URL of this news on the web.
     */
    private String mWebUrl;

    /**
     * Name of the topic.
     */
    private String mTopic;

    /**
     * Name of the author.
     */
    private String mAuthor;

    /**
     * Publishing date.
     */
    private long mPublishDate;


    public News(String title, String author, String url, String topic, long pubDate) {
        this.mTitle = title;
        this.mWebUrl = url;
        this.mTopic = topic;
        this.mPublishDate = pubDate;
        this.mAuthor = author;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getTopic() {
        return mTopic;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getAuthor() {

        if (this.mAuthor == null) {
            return null;
        }

        return this.mAuthor;
    }

    /**
     * Converts the publishing date to string.
     * @return publishing date as string
     */
    public String getPublishDateAsString() {

        if (this.mPublishDate == -1) {
            return null;
        }

        SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
        return sbf.format(this.mPublishDate);
    }


}
