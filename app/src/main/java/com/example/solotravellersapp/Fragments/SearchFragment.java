package com.example.solotravellersapp.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solotravellersapp.Activities.AddToCalendarActivity;
import com.example.solotravellersapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView card4txt1;

    Button addtocalendar_btn;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_search, container, false);

         //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize your CardViews and BottomNavigationView
        card4txt1 = view.findViewById(R.id.card4txt1);

        card4txt1.setOnClickListener(v -> {
            _dialogdetailspage1();
            showToast("You clicked the louvre museum!");
        });

        return view;
    }

    private void _dialogdetailspage1() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.detailspage_dialog, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        addtocalendar_btn = dialogView.findViewById(R.id.addtocalendar_btn);

        addtocalendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Close the dialog
                alertDialog.dismiss();

                // Start the ConfirmationActivity
                Intent intent = new Intent(getActivity(), AddToCalendarActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}