package com.example.notesandroid;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohamad Jamous on 7/10/2021
 */
public class GridViewAdapter extends BaseAdapter implements Filterable {

    private ArrayList<NoteObject> mNotesList;
    private Context mContext;
    private Activity activity;
    private DataBaseHelper mDataBaseHelper;
    private ArrayList<NoteObject> mDeletedNotes = new ArrayList<>();
    private ActivityListener listener;
    private ArrayList<NoteObject> listFiltered  = new ArrayList<>();



    public GridViewAdapter(ArrayList<NoteObject> list, Context mContext, Activity activity, ActivityListener listener) {
        this.mNotesList = list;
        this.mContext = mContext;
        this.activity = activity;
        this.listener = listener;
        this.listFiltered = list;

        mDataBaseHelper = new DataBaseHelper(mContext);
    }


    @Override
    public int getCount() {
        return listFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return listFiltered.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {

            view = LayoutInflater.from(mContext).inflate(R.layout.note_item_layout, viewGroup, false);
        }

        TextView tvNoteTime = (TextView) view.findViewById(R.id.tv_note_time);
        TextView tvNoteTitle = (TextView) view.findViewById(R.id.tv_note_title);
        TextView tvNoteContent = (TextView) view.findViewById(R.id.tv_note_content);
        TextView tvNoteDate = (TextView) view.findViewById(R.id.tv_note_date);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.check_box);

        NoteObject currentNote = (NoteObject) getItem(i);

        listener.getCurrentDate(currentNote.getDate());

        tvNoteTime.setText(listFiltered.get(i).getTime());
        tvNoteDate.setText(listFiltered.get(i).getDate());
        tvNoteTitle.setText(listFiltered.get(i).getTitle());
        String content = listFiltered.get(i).getContent();

        System.out.println("countWords(content)Value: " + countWords(content));
        if (countWords(content) > 30)
        {
            String trimContent = content.substring(0, Math.min(content.length(), 30));
            trimContent += "....";
            tvNoteContent.setText(trimContent);
        }
        else
        {
            tvNoteContent.setText(content);
        }



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NoteObject currentNote = listFiltered.get(i);
                System.out.println("currentNoteTitle: " + currentNote.getTitle());

                Intent intent = new Intent(mContext, NewNoteActivity.class);
                intent.putExtra("NoteObject", currentNote);
                intent.putExtra("isPressed", true);
                activity.startActivity(intent);

            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked)
                {
                    listener.isClicked(true, listFiltered.get(i));
                }
                else
                {
                    listener.isClicked(false, listFiltered.get(i));
                }
            }
        });



        return view;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    //no constraint given, just return all the data. (no search)
                    results.count = mNotesList.size();
                    results.values = mNotesList;
                } else {//do the search
                    ArrayList<NoteObject> resultsData = new ArrayList<>();
                    String searchStr = constraint.toString().toUpperCase();
                    for (NoteObject s : mNotesList)
                        if (s.getTitle().toUpperCase().contains(searchStr)) resultsData.add(s);
                    results.count = resultsData.size();
                    results.values = resultsData;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFiltered = (ArrayList<NoteObject>) results.values;
                notifyDataSetChanged();
            }
        };
    }



    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }



}
