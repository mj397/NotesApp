package com.example.notesandroid;


import java.io.Serializable;

/**
 * Created by Mohamad Jamous on 7/10/2021
 */
public class NoteObject implements Serializable {

    private String noteTitle;
    private String noteContent;
    private String noteTime;
    private String noteDate;
    private int noteId;

    public NoteObject(String noteTitle, String noteContent, String noteTime, String noteDate) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteTime = noteTime;
        this.noteDate = noteDate;
    }

    public NoteObject(String noteTitle, String noteContent, String noteTime, String noteDate, int noteId) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteTime = noteTime;
        this.noteDate = noteDate;
        this.noteId = noteId;
    }

    public String getTitle() {
        return noteTitle;
    }

    public String getContent() {
        return noteContent;
    }

    public String getTime() {
        return noteTime;
    }

    public String getDate()
    {
        return noteDate;
    }

    public int getId()
    {
        return noteId;
    }



}
