package com.example.notesandroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mohamad Jamous on 7/10/2021
 */

public class MainScreenActivity extends AppCompatActivity implements ActivityListener {


    private TextView tvCurrentDateSyntax;
    private AutoGridView mNotesGrid;
    private FloatingActionButton fbAdd;
    private Context mContext;
    private EditText editTextSubject, editTextContent;
    private TextView tvSwipeUp;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout linearLayout;
    private InputFilter filter;
    private DataBaseHelper mDataBaseHelper;
    private int shortAnimationDuration;
    private ArrayList<NoteObject> mDeletableNotes = new ArrayList<>();
    private ImageView btnDelete;
    private GridViewAdapter adapter;
    private int mIdsCount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));


        ImageButton imageButton = (ImageButton)view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intiViews();

        linearLayout = (LinearLayout) findViewById(R.id.rel_note_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

                tvSwipeUp.setVisibility(View.VISIBLE);
                fbAdd.setVisibility(View.INVISIBLE);

            }
        });


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        fbAdd.setVisibility(View.VISIBLE);
                        editTextSubject.getText().clear();
                        editTextContent.getText().clear();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                        String subject = editTextSubject.getText().toString();
                        String content = editTextContent.getText().toString();

                        System.out.println("subjectValue: " + subject);
                        System.out.println("contentValue: " + content);

                        String date = new SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(new Date());
                        String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

                        System.out.println("dateValue: " + date);
                        System.out.println("timeValue: " + time);


                        mIdsCount++;
                        System.out.println("mIdsCountN: " + mIdsCount);
                        NoteObject noteObject = new NoteObject(subject, content, time, date, mIdsCount);

                        Intent intent = new Intent(mContext, NewNoteActivity.class);
                        intent.putExtra("NoteObject", noteObject);
                        intent.putExtra("isPressed", false);
                        startActivity(intent);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        editTextContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String subject = editTextSubject.getText().toString();
                String content = editTextContent.getText().toString();

                System.out.println("subjectValue: " + subject);
                System.out.println("contentValue: " + content);

                String date = new SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(new Date());
                String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

                System.out.println("dateValue: " + date);
                System.out.println("timeValue: " + time);

                if (!(subject.equals("")) && !(content.equals("")))
                {
                    mIdsCount++;
                    System.out.println("mIdsCountValueI: " + mIdsCount);
                    NoteObject noteObject = new NoteObject(subject, content, time, date, mIdsCount);
                    if (mDataBaseHelper.InsertNote(noteObject))
                    {
                        Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                        populateItems();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                    }
                    tvSwipeUp.setVisibility(View.INVISIBLE);
                    editTextSubject.getText().clear();
                    editTextContent.getText().clear();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                else
                {
                    Toast.makeText(mContext, "Fill the fields", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });




        editTextSubject.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int wordsLength = countWords(s.toString());// words.length;
                // count == 0 means a new word is going to start
                if (count == 0 && wordsLength >= 10) {
                    setCharLimit(editTextSubject, editTextSubject.getText().length());
                } else {
                    removeFilter(editTextSubject);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(mDeletableNotes.isEmpty()))
                {
                    int size = mDeletableNotes.size();
                    System.out.println("sizeDeleted :" + size);
                    for (int i = 0; i < size; i++) {
                        System.out.println("DeletedNote: " + mDeletableNotes.get(i).getTitle());
                        mDataBaseHelper.DeleteNote(mDeletableNotes.get(i));

                    }

                    btnDelete.setVisibility(View.INVISIBLE);
                    populateItems();
                }

            }
        });

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);



    }

    private void intiViews()
    {

        mContext = this;
        tvCurrentDateSyntax = (TextView) findViewById(R.id.tv_current_date_syntax);
        mNotesGrid = (AutoGridView) findViewById(R.id.notes_grid);
        fbAdd = (FloatingActionButton) findViewById(R.id.adding_note_button);
        editTextSubject = (EditText) findViewById(R.id.et_subject);
        editTextContent = (EditText) findViewById(R.id.et_note);
        tvSwipeUp = (TextView) findViewById(R.id.tv_swipe_up);
        mDataBaseHelper = new DataBaseHelper(mContext);
        btnDelete = (ImageView) findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.INVISIBLE);


    }


    @Override
    protected void onStart() {
        tvSwipeUp.setVisibility(View.INVISIBLE);
        editTextSubject.getText().clear();
        editTextContent.getText().clear();
        btnDelete.setVisibility(View.INVISIBLE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mDeletableNotes = new ArrayList<>();

        populateItems();
        super.onStart();
    }




    @Override
    public void onBackPressed() {

        populateItems();
        super.onBackPressed();
    }


    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }



    private void setCharLimit(EditText et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[] { filter });
    }

    private void removeFilter(EditText et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }
    }


    private void populateItems() {

        Cursor data = mDataBaseHelper.FetchNoteTitle();
        ArrayList<NoteObject> mObjects = new ArrayList<>();

        ArrayList<String> titlesList = new ArrayList<>();
        ArrayList<String> contentsList = new ArrayList<>();
        ArrayList<String> timesList = new ArrayList<>();
        ArrayList<String> datesList = new ArrayList<>();
        ArrayList<Integer> idsList = new ArrayList<>();

        while(data.moveToNext())
        {
            titlesList.add(data.getString(0));
        }


        Cursor data1 = mDataBaseHelper.FetchNoteContent();
        while(data1.moveToNext())
        {
            contentsList.add(data1.getString(0));
        }


        Cursor data2 = mDataBaseHelper.FetchNoteTime();
        while(data2.moveToNext())
        {
            timesList.add(data2.getString(0));
        }


        Cursor data3 = mDataBaseHelper.FetchNoteDate();
        while(data3.moveToNext())
        {
            datesList.add(data3.getString(0));
        }

        Cursor data4 = mDataBaseHelper.FetchNoteId();
        while(data4.moveToNext())
        {
            idsList.add(data4.getInt(0));
        }

        for (int i = 0; i < titlesList.size(); i++)
        {
            mObjects.add(new NoteObject(titlesList.get(i), contentsList.get(i), timesList.get(i), datesList.get(i), idsList.get(i)));

            System.out.println("NoteTitleDisplay: " + titlesList.get(i));
            System.out.println("NoteIdDisplay: " + idsList.get(i));
        }

        if (mObjects.isEmpty())
        {
            mIdsCount = 0;
            tvCurrentDateSyntax.setText("");
        }
        else
        {
            mIdsCount = mObjects.get(mObjects.size() - 1).getId();
            System.out.println("mIdsCountValue: " + mIdsCount);
        }

        adapter = new GridViewAdapter(mObjects, mContext, this, this);
        mNotesGrid.setAdapter(adapter);


    }




    @Override
    public void isClicked(boolean clicked, NoteObject note) {
        if (clicked)
        {
            btnDelete.setVisibility(View.VISIBLE);
            mDeletableNotes.add(note);
        }
        else
        {
            mDeletableNotes.remove(note);
        }

        if (mDeletableNotes.isEmpty())
        {
            btnDelete.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void getCurrentDate(String date) {
        if (!(date.equals("")))
        {
            tvCurrentDateSyntax.setText(date);
        }
        else
        {
            tvCurrentDateSyntax.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.search_icon);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Here!");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });

        return true;
    }
}