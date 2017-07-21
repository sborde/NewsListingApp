package hu.borde.newslisting.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hu.borde.newslisting.R;
import hu.borde.newslisting.model.News;


public class NewsListAdapter extends ArrayAdapter<News> {


    public NewsListAdapter(@NonNull Context context, @NonNull List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        News currentItem = getItem(position);

        NewsViewHolder vh = new NewsViewHolder(convertView, getContext());

        vh.setTitle(currentItem.getTitle());
        vh.setSection(currentItem.getTopic());
        vh.setPublishingDate(currentItem.getPublishDateAsString());
        vh.setAuthor(currentItem.getAuthor());


        return convertView;
    }
}
