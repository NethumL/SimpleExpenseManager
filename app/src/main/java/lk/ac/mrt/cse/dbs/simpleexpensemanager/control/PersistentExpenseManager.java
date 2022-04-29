package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBContract;

public class PersistentExpenseManager extends ExpenseManager {
    public static final String DATABASE_NAME = "190349K";
    public static final int DATABASE_VERSION = 1;
    private static final String[] SQL_CREATE_COMMANDS = {
            String.format("CREATE TABLE %s(%s text PRIMARY KEY, %s text, %s text, %s real);",
                    DBContract.Account.TABLE_NAME, DBContract.Account.COLUMN_NAME_ACCOUNT_NO,
                    DBContract.Account.COLUMN_NAME_BANK_NAME,
                    DBContract.Account.COLUMN_NAME_HOLDER_NAME,
                    DBContract.Account.COLUMN_NAME_BALANCE),
            String.format(
                    "CREATE TABLE %s(%s int PRIMARY KEY, %s timestamp, %s text, %s text, %s real, FOREIGN KEY (%s) REFERENCES %s(%s));",
                    DBContract.Transaction.TABLE_NAME, DBContract.Transaction._ID,
                    DBContract.Transaction.COLUMN_NAME_DATE, DBContract.Transaction.COLUMN_NAME_ACCOUNT_NO,
                    DBContract.Transaction.COLUMN_NAME_EXPENSE_TYPE,
                    DBContract.Transaction.COLUMN_NAME_AMOUNT,
                    DBContract.Transaction.COLUMN_NAME_ACCOUNT_NO, DBContract.Account.TABLE_NAME,
                    DBContract.Account.COLUMN_NAME_ACCOUNT_NO),
    };
    private static final String[] SQL_DELETE_COMMANDS = {
            "DROP TABLE IF EXISTS " + DBContract.Account.TABLE_NAME,
            "DROP TABLE IF EXISTS " + DBContract.Transaction.TABLE_NAME};
    private final DatabaseHelper helper;

    public PersistentExpenseManager(Context context) {
        helper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        setup();
    }

    @Override
    public void setup() {
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String databaseName, CursorFactory cursorFactory,
                              int databaseVersion) {
            super(context, databaseName, cursorFactory, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (String command : SQL_CREATE_COMMANDS) {
                db.execSQL(command);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for (String command : SQL_DELETE_COMMANDS) {
                db.execSQL(command);
            }
            onCreate(db);
        }
    }
}
