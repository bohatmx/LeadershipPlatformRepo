package com.oneconnect.leadership.library.lists;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoListener} interface
 * to handle interaction events.
 * Use the {@link VideoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoListFragment extends Fragment implements PageFragment {
    private VideoListener mListener;
    public static final String TAG = VideoListFragment.class.getSimpleName();

    public VideoListFragment() {
        // Required empty public constructor
    }

    private List<VideoDTO> videos;
    private View view;
    private RecyclerView recyclerView;

    public static VideoListFragment newInstance(HashMap<String, VideoDTO> list) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        ResponseBag bag = new ResponseBag();
        bag.setVideos(new ArrayList<VideoDTO>());
        for (VideoDTO v: list.values()) {
            bag.getVideos().add(v);
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
            videos = bag.getVideos();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: %%%%%%%%%%%%%%%%%%%%%%");
        view =  inflater.inflate(R.layout.fragment_video_list, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VideoListener) {
            mListener = (VideoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement VideoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getTitle() {
        return "Videos";
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
    public interface VideoListener {
        void onVideoTapped(VideoDTO video);
    }
}
