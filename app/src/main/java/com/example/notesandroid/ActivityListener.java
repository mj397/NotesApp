package com.example.notesandroid;

import java.util.ArrayList;
/**
 * Created by Mohamad Jamous on 24/10/2021
 */

public interface ActivityListener {

    void getCurrentDate(String date);

    void isClicked(boolean clicked, NoteObject note);
}
