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
import com.oneconnect.leadership.library.data.EBookDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EBookListener} interface
 * to handle interaction events.
 * Use the {@link EBookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EBookListFragment extends Fragment implements PageFragment{
    private EBookListener mListener;
    public static final String TAG = EBookListFragment.class.getSimpleName();
    public EBookListFragment() {
        // Required empty public constructor
    }

    private List<EBookDTO> eBooks;
    private View view;
    private RecyclerView recyclerView;

    public static EBookListFragment newInstance(HashMap<String, EBookDTO> list) {
        EBookListFragment fragment = new EBookListFragment();
        Bundle args = new Bundle();
        ResponseBag bag = new ResponseBag();
        bag.seteBooks(new ArrayList<EBookDTO>());
        for (EBookDTO v: list.values()) {
            bag.geteBooks().add(v);
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
            eBooks = bag.geteBooks();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: #################");
        view =  inflater.inflate(R.layout.fragment_ebook_list, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EBookListener) {
            mListener = (EBookListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EBookListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getTitle() {
        return "EBooks";
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
    public interface EBookListener {
        void onEBookTapped(EBookDTO eBook);
    }
}
