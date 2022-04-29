package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final SQLiteOpenHelper helper;

    public PersistentTransactionDAO(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType,
                               double amount) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Transaction.COLUMN_NAME_DATE, date.getTime());
        values.put(DBContract.Transaction.COLUMN_NAME_ACCOUNT_NO, accountNo);
        values.put(DBContract.Transaction.COLUMN_NAME_EXPENSE_TYPE, expenseType.name());
        values.put(DBContract.Transaction.COLUMN_NAME_AMOUNT, amount);
        db.insert(DBContract.Transaction.TABLE_NAME, null, values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.query(DBContract.Transaction.TABLE_NAME, null, null, null, null, null, null);
        List<Transaction> transactions = new ArrayList<>();
        while (cursor.moveToNext()) {
            long time = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DBContract.Transaction.COLUMN_NAME_DATE));
            String accountNo = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Transaction.COLUMN_NAME_ACCOUNT_NO));
            String expenseType = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Transaction.COLUMN_NAME_EXPENSE_TYPE));
            double amount = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DBContract.Transaction.COLUMN_NAME_AMOUNT));
            transactions.add(new Transaction(new Date(time), accountNo,
                    ExpenseType.valueOf(expenseType), amount));
        }
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Transaction.TABLE_NAME, null, null, null, null, null,
                null, Integer.toString(limit));
        List<Transaction> transactions = new ArrayList<>();
        while (cursor.moveToNext()) {
            long time = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DBContract.Transaction.COLUMN_NAME_DATE));
            String accountNo = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Transaction.COLUMN_NAME_ACCOUNT_NO));
            String expenseType = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Transaction.COLUMN_NAME_EXPENSE_TYPE));
            double amount = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DBContract.Transaction.COLUMN_NAME_AMOUNT));
            transactions.add(new Transaction(new Date(time), accountNo,
                    ExpenseType.valueOf(expenseType), amount));
        }
        cursor.close();
        return transactions;
    }
}
