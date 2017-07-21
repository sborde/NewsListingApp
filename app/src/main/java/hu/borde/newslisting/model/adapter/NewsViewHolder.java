package hu.borde.newslisting.model.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import hu.borde.newslisting.R;

/**
 * Created by borde on 2017.07.06..
 */

public class NewsViewHolder {

    private TextView author;
    private TextView title;
    private TextView publishingDate;
    private TextView section;
    private Context context;

    public NewsViewHolder(View parent, Context context) {
        this.context = context;
        this.author = (TextView)parent.findViewById(R.id.author_field);
        this.title = (TextView)parent.findViewById(R.id.title_field);
        this.publishingDate = (TextView)parent.findViewById(R.id.published_field);
        this.section = (TextView)parent.findViewById(R.id.topic_field);
    }

    public void setAuthor(String author) {
        if (author == null) {
            this.author.setText(context.getString(R.string.unknown_author));
        } else {
            this.author.setText(author);
        }
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setPublishingDate(String date) {

        if (date == null) {
            this.publishingDate.setText(context.getString(R.string.unknown_publishing_date));
        } else {
            this.publishingDate.setText(date);
        }
    }

    public void setSection(String section) {
        this.section.setText(section);
    }
}
