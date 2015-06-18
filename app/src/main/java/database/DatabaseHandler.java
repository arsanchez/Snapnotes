package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                + KEY_DUE_DATE + " TEXT," + KEY_IMG + " TEXT," + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        // Create tables again
        onCreate(db);

    }

    // Getting single Snapnote
    Snapnote getSnapnote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

      /*  Snapnote contact = new Snapnote(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));*/
        // return contact
        return contact;
    }
}
