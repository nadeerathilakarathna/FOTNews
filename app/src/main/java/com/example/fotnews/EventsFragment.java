package com.example.fotnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {
    public EventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        LinearLayout progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = view.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<NewsItem> sampleData = new ArrayList<>();
        NewsAdapter adapter = new NewsAdapter(getContext(), sampleData);
        recyclerView.setAdapter(adapter);

        FirebaseHelper.loadNews(1, sampleData, getContext(), adapter, RunnableHelper.progressbar_stop_loader(progressBar));

        return view;
    }
}