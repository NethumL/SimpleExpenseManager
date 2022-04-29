package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final SQLiteOpenHelper helper;

    public PersistentAccountDAO(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Account.TABLE_NAME,
                new String[]{DBContract.Account.COLUMN_NAME_ACCOUNT_NO}, null,
                null, null, null, null);
        List<String> accountNumbers = new ArrayList<>();
        while (cursor.moveToNext()) {
            String accountNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_ACCOUNT_NO));
            accountNumbers.add(accountNumber);
        }
        cursor.close();
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Account.TABLE_NAME, null, null, null, null, null, null);
        List<Account> accounts = new ArrayList<>();
        while (cursor.moveToNext()) {
            String accountNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_ACCOUNT_NO));
            String bankName = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_BANK_NAME));
            String accountHolderName = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_HOLDER_NAME));
            double balance = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_BALANCE));
            accounts.add(new Account(accountNumber, bankName, accountHolderName, balance));
        }
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Account.TABLE_NAME, null,
                DBContract.Account.COLUMN_NAME_ACCOUNT_NO + " = ?",
                new String[]{accountNo}, null, null, null);
        while (cursor.moveToNext()) {
            String accountNumber = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_ACCOUNT_NO));
            String bankName = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_BANK_NAME));
            String accountHolderName = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_HOLDER_NAME));
            double balance = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(DBContract.Account.COLUMN_NAME_BALANCE));
            cursor.close();
            return new Account(accountNumber, bankName, accountHolderName, balance);
        }
        cursor.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Account.COLUMN_NAME_ACCOUNT_NO, account.getAccountNo());
        values.put(DBContract.Account.COLUMN_NAME_BANK_NAME, account.getBankName());
        values.put(DBContract.Account.COLUMN_NAME_HOLDER_NAME, account.getAccountHolderName());
        values.put(DBContract.Account.COLUMN_NAME_BALANCE, account.getBalance());
        db.insert(DBContract.Account.TABLE_NAME, null, values);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        this.getAccount(accountNo);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DBContract.Account.TABLE_NAME, DBContract.Account.COLUMN_NAME_ACCOUNT_NO + " = ?",
                new String[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount)
            throws InvalidAccountException {
        SQLiteDatabase db = helper.getWritableDatabase();

        Account account = this.getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }

        ContentValues values = new ContentValues();
        values.put(DBContract.Account.COLUMN_NAME_BALANCE, account.getBalance());

        db.update(DBContract.Account.TABLE_NAME, values,
                DBContract.Account.COLUMN_NAME_ACCOUNT_NO + " = ?", new String[]{accountNo});
    }
}
