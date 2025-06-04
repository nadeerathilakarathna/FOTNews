package com.example.fotnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<NewsItem> newsList;

    public static long convertFormattedToMillis(String formattedTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            Date date = sdf.parse(formattedTime);
            return date != null ? date.getTime() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getCustomRelativeTime(String unixTimestampStr) {
        try {


            long pastTimeMillis = convertFormattedToMillis(unixTimestampStr);
            long now = System.currentTimeMillis();
            long diff = now - pastTimeMillis;

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long months = days / 30;
            long years = days / 365;

            if (seconds < 60) {
                return seconds + "s";
            } else if (minutes < 60) {
                return minutes + " min";
            } else if (hours < 24) {
                return hours + "h";
            } else if (days < 30) {
                return days + "d";
            } else if (months < 12) {
                return months + " mo";
            } else {
                return years + "y";
            }
        } catch (NumberFormatException e) {
            return "Invalid";
        }
    }


    public NewsAdapter(Context context, List<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem item = newsList.get(position);
        holder.title.setText(item.title);
        holder.content.setText(item.content);
        holder.category.setImageResource(item.category);

        String relativeTime = getCustomRelativeTime(item.timestamp);
        holder.time.setText(relativeTime);

        Glide.with(context).load(item.image_url).placeholder(R.drawable.example_news).error(R.drawable.example_news).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, time;
        ImageView image;
        ImageView category;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            content = itemView.findViewById(R.id.newsContent);
            time = itemView.findViewById(R.id.newsTime);
            category = itemView.findViewById(R.id.newsCategory);
            image = itemView.findViewById(R.id.newsImage);
        }
    }
}


