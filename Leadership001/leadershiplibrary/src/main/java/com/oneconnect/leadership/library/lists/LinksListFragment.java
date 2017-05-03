package com.oneconnect.leadership.library.lists;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UrlListener} interface
 * to handle interaction events.
 * Use the {@link LinksListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinksListFragment extends Fragment implements PageFragment{
    public static final String TAG = LinksListFragment.class.getSimpleName();
    private UrlListener mListener;

    public LinksListFragment() {
        // Required empty public constructor
    }

    private List<UrlDTO> urls;
    private View view;
    private RecyclerView recyclerView;

    public static LinksListFragment newInstance(HashMap<String, UrlDTO> list) {
        LinksListFragment fragment = new LinksListFragment();
        Bundle args = new Bundle();
        ResponseBag bag = new ResponseBag();
        bag.setUrls(new ArrayList<UrlDTO>());
        for (UrlDTO u: list.values()) {
            bag.getUrls().add(u);
        }
        args.putSerializable("bag", bag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResponseBag  bag = (ResponseBag) getArguments().getSerializable("bag");
            urls = bag.getUrls();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: *****************");
        view =  inflater.inflate(R.layout.fragment_links_list, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UrlListener) {
            mListener = (UrlListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UrlListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    String pageTitle;

    @Override
    public String getTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface UrlListener {
        void onUrlTapped(UrlDTO url);
    }
}
