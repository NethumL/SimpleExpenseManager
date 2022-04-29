package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract(){}

    public static class Account implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_ACCOUNT_NO = "account_no";
        public static final String COLUMN_NAME_BANK_NAME = "bank_name";
        public static final String COLUMN_NAME_HOLDER_NAME = "holder_name";
        public static final String COLUMN_NAME_BALANCE = "balance";
    }

    public static class Transaction implements BaseColumns {
        public static final String TABLE_NAME = "transac";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ACCOUNT_NO = "account_no";
        public static final String COLUMN_NAME_EXPENSE_TYPE = "expense_type";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }
}
