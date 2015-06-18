package com.apps.argenis.snapnotes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapters.SnapAdapter;
import clases.Utils;
import clases.HidingScrollListener;
import clases.Snapnote;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements  TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton cnote; //button for a new snapnote
    FloatingActionButton tnote; //button for a new snapnote
    FloatingActionMenu actionMenu; //Menu for the main actions
    String mCurrentPhotoPath;     // Path for the new photo

    //variables for the recycler view

    RecyclerView recyclerView;

    //date and tiem vars
    private Calendar calendar; // calendar to set the reminder date
    private Calendar today; // calendar for the current day
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private static final String TIME_PATTERN = "HH:mm";

    private List<Snapnote> notes;

    // This method creates an ArrayList that has three  objects for example purpose

    private void initializeData(){
        notes = new ArrayList<>();
        notes.add(new Snapnote("Emma Wilson", "23 years old", R.mipmap.ic_launcher));
        notes.add(new Snapnote("Lavery Maiss", "25 years old", R.mipmap.ic_launcher));
        notes.add(new Snapnote("Lillie Watts", "35 years old", R.mipmap.ic_launcher));
        notes.add(new Snapnote("Emma Wilson", "23 years old", R.mipmap.ic_launcher));

    }



    static final int REQUEST_IMAGE_CAPTURE = 1;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.initializeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        actionMenu = (FloatingActionMenu) v.findViewById(R.id.fab_menu);
        cnote = (FloatingActionButton) v.findViewById(R.id.fab_cnote_new);
        tnote = (FloatingActionButton) v.findViewById(R.id.fab_tnote_new);

        cnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionMenu.close(true);

                //intent for taking the photo
                dispatchTakePictureIntent();

            }
        });

        tnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDateDialog();

            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.RecyclerViewNotes);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),3);
        //glm.setOrientation(LinearLayoutManager.VERTICAL);

        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

              int type = Utils.randomInt(1,3);

              int span = (3 - position % 3);

             /* switch (type)
              {
                 case 1:
                     span =  (3 - position % 3);
                     break;
                 case 2:
                     span = (3 - position % 3);
                     break;
                 case 3:
                     span =  1;
                     break;
              }*/

                return span;
            }
        });

        recyclerView.setLayoutManager(glm);

        SnapAdapter adapter = new SnapAdapter(notes);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        calendar = Calendar.getInstance();
        today    = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

    }

    //method to create a new iamge file to store the snaptno
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("Camera erro",ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case 0:
                Toast.makeText(getActivity().getBaseContext(),"Cancelled",Toast.LENGTH_LONG).show();
                break;

            case -1:
                Toast.makeText(getActivity().getBaseContext(),"Done",Toast.LENGTH_LONG).show();
                openDateDialog();
                break;
        }
    }

    private void hideViews()
    {
        actionMenu.close(true);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) actionMenu.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        actionMenu.animate().translationY(actionMenu.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
       // MainActivity.hideToolbar();
    }

    private void showViews() {
        // toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        actionMenu.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
       // MainActivity.showToolbar();
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        String reminderDay = String.valueOf(calendar.get(Calendar.YEAR)) +"-"+ String.valueOf(calendar.get(Calendar.MONTH)) +"-"+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        Log.d("Day set",reminderDay);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        openTimeDialog();
    }

    public void openDateDialog(){


        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(today);
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

    }

    public void openTimeDialog(){

        TimePickerDialog dpd = TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        dpd.show(getActivity().getFragmentManager(), "Timepickerdialog");

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
