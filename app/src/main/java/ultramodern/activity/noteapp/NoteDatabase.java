package ultramodern.activity.noteapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=3;
    private static final String DATABASE_NAME="notes.db";
    private static final String DATABASE_TABLE="NOTES_TABLE";

    //COLUMNS
    private static final String KEY_ID="id";
    private static final String KEY_CONTENT="content";
    private static final String KEY_DATE="date";
    private static final String KEY_TIME="time";
    private static final String KEY_TITLE="title";

    NoteDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="CREATE TABLE "+ DATABASE_TABLE + " ("+ KEY_ID+" INT PRIMARY KEY,"+
        KEY_TITLE +" TEXT,"+
        KEY_CONTENT +" TEXT,"+
        KEY_DATE +" TEXT,"+
        KEY_TIME +" TEXT"+")";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        if (old_version>=new_version)
            return;
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE );
        onCreate(sqLiteDatabase);

    }
    public long AddNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, note.getTitle());
        cv.put(KEY_CONTENT, note.getContent());
        cv.put(KEY_DATE, note.getDate());
        cv.put(KEY_TIME, note.getTime());
        long ID=db.insert(DATABASE_TABLE,null,cv);
        Log.d("inserted","ID ->"+ ID);
        return ID;
    }
    public Note getNote(long id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(DATABASE_TABLE,new String[]{KEY_ID,KEY_TITLE,KEY_DATE,KEY_CONTENT,KEY_TIME},KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null
        );
        if (cursor !=null)
            cursor.moveToFirst();
        Note note = new Note(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        return note;
    }
    public List<Note> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();
        String query = "SELECT * FROM "+DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {
                Note note = new Note();
                note.setId(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));

                allNotes.add(note);
            }while (cursor.moveToNext());
        }
        return allNotes;
    }
}
