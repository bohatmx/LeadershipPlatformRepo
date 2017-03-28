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
import com.oneconnect.leadership.library.data.PodcastDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PodcastListener} interface
 * to handle interaction events.
 * Use the {@link PodcastListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PodcastListFragment extends Fragment implements PageFragment{
    private PodcastListener mListener;
    public static final String TAG = PodcastListFragment.class.getSimpleName();
    public PodcastListFragment() {
        // Required empty public constructor
    }

    private List<PodcastDTO> podcasts;
    private View view;
    private RecyclerView recyclerView;

    public static PodcastListFragment newInstance(HashMap<String,PodcastDTO> list) {
        PodcastListFragment fragment = new PodcastListFragment();
        Bundle args = new Bundle();
        ResponseBag bag = new ResponseBag();
        bag.setPodcasts(new ArrayList<PodcastDTO>());
        for (PodcastDTO p: list.values()) {
            bag.getPodcasts().add(p);
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
            podcasts = bag.getPodcasts();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: .................");
        view =  inflater.inflate(R.layout.fragment_podcast_list, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PodcastListener) {
            mListener = (PodcastListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PodcastListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getTitle() {
        return "Podcasts";
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
    public interface PodcastListener {
        void onPodcastTapped(PodcastDTO podcast);
    }
}
