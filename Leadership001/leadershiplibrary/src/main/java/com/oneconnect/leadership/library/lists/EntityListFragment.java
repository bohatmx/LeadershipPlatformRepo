package com.oneconnect.leadership.library.lists;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.CountryDTO;
import com.oneconnect.leadership.library.data.DTOEntity;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryListener} interface
 * to handle interaction events.
 * Use the {@link EntityListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntityListFragment extends BaseListingFragment {

    public EntityListFragment() {
    }

    private List<DTOEntity> entities = new ArrayList<>();
    private BasicEntityAdapter adapter;
    private int type;
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
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        setList();
        return view;
    }

    public void setListener(BasicEntityAdapter.EntityListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void getData() {

    }

    @Override
    public void filterData() {

    }

    @Override
    public void setList() {
        adapter = new BasicEntityAdapter(entities, getActivity(), new BasicEntityAdapter.EntityListener() {
            @Override
            public void onDeleteClicked(DTOEntity entity) {
                mListener.onDeleteClicked(entity);
            }

            @Override
            public void onUpdateClicked(DTOEntity entity) {
                mListener.onUpdateClicked(entity);
            }

            @Override
            public void onPhotoCaptureRequested(DTOEntity entity) {
                mListener.onPhotoCaptureRequested(entity);
            }

            @Override
            public void onVideoCaptureRequested(DTOEntity entity) {
                mListener.onVideoCaptureRequested(entity);
            }

            @Override
            public void onLocationRequested(DTOEntity entity) {
                mListener.onLocationRequested(entity);
            }

            @Override
            public void onEntityClicked(DTOEntity entity) {
                mListener.onEntityClicked(entity);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void removeEntity(DTOEntity entity) {

    }

    @Override
    public void addEntity(DTOEntity entity) {

    }

    private void setupEntities() {
        ResponseBag bag = (ResponseBag) getArguments().getSerializable("bag");
        if (bag != null) {
            type = bag.getType();
            switch (type) {
                case ResponseBag.CATEGORIES:
                    for (CategoryDTO c : bag.getCategories()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.COMPANY:
                    for (CompanyDTO c : bag.getCompanies()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.COUNTRY:
                    for (CountryDTO c : bag.getCountries()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.DAILY_THOUGHTS:
                    for (DailyThoughtDTO c : bag.getDailyThoughts()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.EBOOKS:
                    for (EBookDTO c : bag.geteBooks()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.NEWS:
                    for (NewsDTO c : bag.getNews()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PAYMENTS:
                    for (PaymentDTO c : bag.getPayments()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PHOTOS:
                    for (PhotoDTO c : bag.getPhotos()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PODCASTS:
                    for (PodcastDTO c : bag.getPodcasts()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PRICE:
                    for (PriceDTO c : bag.getPrices()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.SUBSCRIPTIONS:
                    for (SubscriptionDTO c : bag.getSubscriptions()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.USERS:
                    for (UserDTO c : bag.getUsers()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.VIDEOS:
                    for (VideoDTO c : bag.getVideos()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.WEEKLY_MASTERCLASS:
                    for (WeeklyMasterClassDTO c : bag.getWeeklyMasterClasses()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.WEEKLY_MESSAGE:
                    for (WeeklyMessageDTO c : bag.getWeeklyMessages()) {
                        entities.add(c);
                    }
                    break;
            }
        }
    }

}
