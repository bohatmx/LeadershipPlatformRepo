package com.oneconnect.leadership.library.lists;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.CountryDTO;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.ArrayList;
import java.util.List;



public class EntityListFragment extends BaseListingFragment {

    public EntityListFragment() {
    }

    private List<BaseDTO> entities = new ArrayList<>();
    private BasicEntityAdapter adapter;
    private int type;
    private String stringTitle;
    private TextView txtTitle, txtCount;
    private ImageView iconAdd;
    private RecyclerView recyclerView;
    private BasicEntityAdapter.EntityListener mListener;

    public static EntityListFragment newInstance(ResponseBag bag) {
        EntityListFragment fragment = new EntityListFragment();
        Bundle args = new Bundle();
        args.putSerializable("bag", bag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setupEntities();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_category_list, container, false);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtCount = (TextView) view.findViewById(R.id.txtCount);
        iconAdd = (ImageView) view.findViewById(R.id.iconAdd);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        txtTitle.setText(stringTitle);
        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddEntity();
            }
        });
        setList();
        return view;
    }

    public void setListener(BasicEntityAdapter.EntityListener mListener) {
        this.mListener = mListener;
    }

    public void setList() {
        txtCount.setText(String.valueOf(entities.size()));
        adapter = new BasicEntityAdapter(entities, type, getActivity(), new BasicEntityAdapter.EntityListener() {
            @Override
            public void onAddEntity() {
                mListener.onAddEntity();
            }

            @Override
            public void onDeleteClicked(BaseDTO entity) {
                mListener.onDeleteClicked(entity);
            }

            @Override
            public void onUpdateClicked(BaseDTO entity) {
                mListener.onUpdateClicked(entity);
            }

            @Override
            public void onPhotoCaptureRequested(BaseDTO entity) {
                mListener.onPhotoCaptureRequested(entity);
            }

            @Override
            public void onVideoCaptureRequested(BaseDTO entity) {
                mListener.onVideoCaptureRequested(entity);
            }

            @Override
            public void onSomeActionRequired(BaseDTO entity) {
                mListener.onSomeActionRequired(entity);
            }

            @Override
            public void onMicrophoneRequired(BaseDTO entity) {
                mListener.onMicrophoneRequired(entity);
            }

            @Override
            public void onEntityClicked(BaseDTO entity) {
                mListener.onEntityClicked(entity);
            }
        });

        recyclerView.setAdapter(adapter);
    }


    private void setupEntities() {
        ResponseBag bag = (ResponseBag) getArguments().getSerializable("bag");
        if (bag != null) {
            type = bag.getType();
            switch (type) {
                case ResponseBag.CATEGORIES:
                    stringTitle = ResponseBag.DESC_CATEGORIES;
                    for (CategoryDTO c : bag.getCategories()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.COMPANIES:
                    stringTitle = ResponseBag.DESC_COMPANY;
                    for (CompanyDTO c : bag.getCompanies()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.COUNTRIES:
                    stringTitle = ResponseBag.DESC_COUNTRY;
                    for (CountryDTO c : bag.getCountries()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.DAILY_THOUGHTS:
                    stringTitle = ResponseBag.DESC_DAILY_THOUGHTS;
                    for (DailyThoughtDTO c : bag.getDailyThoughts()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.EBOOKS:
                    stringTitle = ResponseBag.DESC_EBOOKS;
                    for (EBookDTO c : bag.geteBooks()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.NEWS:
                    stringTitle = ResponseBag.DESC_NEWS;
                    for (NewsDTO c : bag.getNews()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PAYMENTS:
                    stringTitle = ResponseBag.DESC_PAYMENTS;
                    for (PaymentDTO c : bag.getPayments()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PHOTOS:
                    stringTitle = ResponseBag.DESC_PHOTOS;
                    for (PhotoDTO c : bag.getPhotos()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PODCASTS:
                    stringTitle = ResponseBag.DESC_PODCASTS;
                    for (PodcastDTO c : bag.getPodcasts()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PRICES:
                    stringTitle = ResponseBag.DESC_PRICE;
                    for (PriceDTO c : bag.getPrices()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.SUBSCRIPTIONS:
                    stringTitle = ResponseBag.DESC_SUBSCRIPTIONS;
                    for (SubscriptionDTO c : bag.getSubscriptions()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.USERS:
                    stringTitle = ResponseBag.DESC_USERS;
                    for (UserDTO c : bag.getUsers()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.VIDEOS:
                    stringTitle = ResponseBag.DESC_VIDEOS;
                    for (VideoDTO c : bag.getVideos()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.WEEKLY_MASTERCLASS:
                    stringTitle = ResponseBag.DESC_WEEKLY_MASTERCLASS;
                    for (WeeklyMasterClassDTO c : bag.getWeeklyMasterClasses()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.WEEKLY_MESSAGE:
                    stringTitle = ResponseBag.DESC_WEEKLY_MESSAGE;
                    for (WeeklyMessageDTO c : bag.getWeeklyMessages()) {
                        entities.add(c);
                    }
                    break;
            }
        }
    }

}
