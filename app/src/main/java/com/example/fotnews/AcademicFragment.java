package com.example.fotnews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AcademicFragment extends Fragment {
    public AcademicFragment() {
// Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academic, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<NewsItem> sampleData = new ArrayList<>();
        sampleData.add(new NewsItem(
                "University Seminar on AI",
                "A panel of experts discussed the future of AI in education...",
                System.currentTimeMillis() - 2 * 60 * 60 * 1000, // 2 hours ago
                R.drawable.ic_academic,
                R.drawable.sample_news));

        sampleData.add(new NewsItem(
                "Exam Timetable Released",
                "Check the faculty website to download your personal exam schedule.",
                System.currentTimeMillis() - 8 * 60 * 60 * 1000, // 8 hours ago
                R.drawable.ic_academic,
                R.drawable.sample_news));

        sampleData.add(new NewsItem(
                "Library Open Late",
                "University Library hours extended till 10 PM during exam week.",
                System.currentTimeMillis() - 24 * 60 * 60 * 1000, // 1 day ago
                R.drawable.ic_academic,
                R.drawable.sample_news));

        NewsAdapter adapter = new NewsAdapter(getContext(), sampleData);
        recyclerView.setAdapter(adapter);

        return view;
    }
}