package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import clases.Snapnote;

/**
 * Created by Argenis on 6/18/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "localReminders";

    // reminders table name
    private static final String TABLE_REMINDERS = "reminders";

    // reminders Table Columns names
    private static final String KEY_ID       = "id";
    private static final String KEY_DATE     = "reminder_date";
    private static final String KEY_DUE_DATE = "reminder_due_date";
    private static final String KEY_IMG      = "reminder_img";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_DUE_DATE + " TEXT," + KEY_IMG + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        // Create tables again
        onCreate(db);

    }

    // Adding new Snapnote
    public long addSnapnote(Snapnote note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, note.getDate()); // Note date
        values.put(KEY_DUE_DATE, note.getDuedate()); // Note due date
        values.put(KEY_IMG, note.getPhotoUrl()); // Note photo

        // Inserting Row
        long id = db.insert(TABLE_REMINDERS, null, values);

        db.close(); // Closing database connection

        return id;
    }

    // Getting single Snapnote
    public Snapnote getSnapnote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[] { KEY_ID,
                        KEY_DATE, KEY_DUE_DATE, KEY_IMG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Snapnote note = new Snapnote(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),  cursor.getString(3));

        return note;
    }

    // Getting All Snapnotes
    public List<Snapnote> getAllSnapnotes() {
        List<Snapnote> contactList = new ArrayList<Snapnote>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REMINDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Snapnote note = new Snapnote();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setDate(cursor.getString(1));
                note.setDuedate(cursor.getString(2));
                note.setPhotoUrl(cursor.getString(2));
                // Adding contact to list
                contactList.add(note);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting contacts Count
    public int getSnapnotesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
