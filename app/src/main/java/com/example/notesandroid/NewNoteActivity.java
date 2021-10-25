package com.example.notesandroid;


import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Mohamad Jamous on 10/11/2021
 */

public class NewNoteActivity extends AppCompatActivity {


    private ImageView mActionClose;
    private Context mContext;
    private EditText editTextSubject, editTextContent;
    private InputFilter filter;
    private NoteObject mNote;
    private DataBaseHelper mDataBaseHelper;
    private boolean ifAlreadyExists;
    private int mIdsCount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        initViews();

        Intent intent = getIntent();

        if (intent != null)
        {
            mNote = (NoteObject) intent.getSerializableExtra("NoteObject");
            ifAlreadyExists = intent.getBooleanExtra("isPressed", false);
            System.out.println("ifAlreadyExistsValue: " + ifAlreadyExists);
            editTextSubject.setText(mNote.getTitle());
            editTextContent.setText(mNote.getContent());
            mIdsCount = mNote.getId();
            System.out.println("mIdsCountNew: " + mIdsCount);
        }


        if (!(editTextContent.getText().toString().equals("")))
        {
            editTextContent.setFocusable(true);
        }

        mActionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextSubject.setFocusable(false);
                editTextContent.setFocusable(false);
                String subject = editTextSubject.getText().toString();
                String content = editTextContent.getText().toString();

                System.out.println("subjectValue: " + subject);
                System.out.println("contentValue: " + content);

                String date = new SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(new Date());
                String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

                System.out.println("dateValue: " + date);
                System.out.println("timeValue: " + time);

                System.out.println("mIdsCountNL: " + mIdsCount);
                NoteObject noteObject = new NoteObject(subject, content, time, date, mIdsCount);

                if (!(subject.equals("")) && !(content.equals("")))
                {
                    if (ifAlreadyExists)
                    {
                        if (mDataBaseHelper.UpdateNote(noteObject)) {
                            Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "ErrorU", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if (mDataBaseHelper.InsertNote(noteObject)) {
                            System.out.println("noteObjectTitle: " + noteObject.getTitle());
                            Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "ErrorS", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Fill the fields", Toast.LENGTH_SHORT).show();
                }

                finish();

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
                    Toast.makeText(mContext, "Can't enter more than 10 words", Toast.LENGTH_SHORT).show();
                } else {
                    removeFilter(editTextSubject);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


    }

    private void initViews() {

        mContext = this;
        mActionClose = (ImageView) findViewById(R.id.action_close);
        editTextSubject = (EditText) findViewById(R.id.edit_subject);
        editTextContent = (EditText) findViewById(R.id.edit_content);
        mDataBaseHelper = new DataBaseHelper(mContext);


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


    @Override
    public void onBackPressed() {

        editTextSubject.setFocusable(false);
        editTextContent.setFocusable(false);
        String subject = editTextSubject.getText().toString();
        String content = editTextContent.getText().toString();

        System.out.println("subjectValue: " + subject);
        System.out.println("contentValue: " + content);

        String date = new SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        System.out.println("dateValue: " + date);
        System.out.println("timeValue: " + time);

        NoteObject noteObject = new NoteObject(subject, content, time, date, mIdsCount);

        if (!(subject.equals("")) && !(content.equals("")))
        {
            if (ifAlreadyExists)
            {
                if (mDataBaseHelper.UpdateNote(noteObject)) {
                    Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "ErrorU", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if (mDataBaseHelper.InsertNote(noteObject)) {
                    Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "ErrorS", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            Toast.makeText(mContext, "Fill the fields", Toast.LENGTH_SHORT).show();
        }

       super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}