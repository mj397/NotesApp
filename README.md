# NotesApp
Notes app that uses SQLite database methods for deleting, updating, inserting, and fetching data from database.



- Inserting new note to database:


```
  public boolean InsertNote(NoteObject note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTE_TITLE, note.getTitle());
        contentValues.put(NOTE_TIME, note.getTime());
        contentValues.put(NOTE_CONTENT, note.getContent());
        contentValues.put(NOTE_DATE, note.getDate());
        contentValues.put(NOTE_ID, note.getId());


        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        //if date is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
```
    
    
 
    
- Deleting note from database:


```
public void DeleteNote(NoteObject note){

SQLiteDatabase db = this.getWritableDatabase();

String query =
        NOTE_TITLE + "='" + note.getTitle() +
        "' AND " + NOTE_CONTENT + "='" + note.getContent() +
        "' AND " + NOTE_TIME + "='" + note.getTime() +
        "' AND " + NOTE_DATE + "='" + note.getDate() +
        "' AND " + NOTE_ID + "='" + note.getId() + "'";

System.out.println("queryValue: " + query);

db.delete(TABLE_NAME, query, null);

db.close();
}
```
    
    
    
    
- Update note: 

```
public boolean UpdateNote(NoteObject note){
  SQLiteDatabase db = this.getWritableDatabase();

  ContentValues values = new ContentValues();

  values.put(NOTE_TITLE, note.getTitle());
  values.put(NOTE_CONTENT, note.getContent());
  values.put(NOTE_TIME, note.getTime());
  values.put(NOTE_DATE, note.getDate());
  values.put(NOTE_ID, note.getId());

  String whereClause = NOTE_ID + "=?";
  String whereArgs[] = {String.valueOf(note.getId())};

  int result = db.update(TABLE_NAME, values, whereClause, whereArgs);
  System.out.println("updatedRows: " + result);

  //returns number of rows affected.
  if (result == 0) {
      return false;
  } else {
      return true;
  }
}
```
    
    
- Fetching note title:


```
 public Cursor FetchNoteTitle(){
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor data = db.rawQuery("SELECT " + NOTE_TITLE + " FROM " + TABLE_NAME, null);
    return data;
    }
```
    
    
- Used AutoGridView class that helps with displaying notes correctly and not overlapping each other
here is the link to github project: (https://github.com/JJdeGroot/AutoGridView);
    
    
    
    
<img src="https://user-images.githubusercontent.com/63315306/138689092-6d98f6b0-02cf-4b77-b120-5f02c33ba52d.jpg" align="left" height="500" width="250">
<img src="https://user-images.githubusercontent.com/63315306/138689206-84c7d062-6af2-49af-a124-12bf1069fab9.jpg" align="right" height="500" width="250">
<img src="https://user-images.githubusercontent.com/63315306/138689225-cf7fcb0c-0e52-40d1-907c-4279dad6197c.jpg" align="right" height="500" width="250">
<img src="https://user-images.githubusercontent.com/63315306/138689272-962fa925-81ad-4dd2-8d07-f4ccff1710b4.jpg" align="left" height="500" width="250">
<img src="https://user-images.githubusercontent.com/63315306/138689281-8c3e5ba5-1039-419d-aea2-4a69614875f9.jpg" align="right" height="500" width="250">
<img src="https://user-images.githubusercontent.com/63315306/138689338-41c140bd-e748-4102-b56b-b13e11965c1f.jpg" align="left" height="500" width="250">



