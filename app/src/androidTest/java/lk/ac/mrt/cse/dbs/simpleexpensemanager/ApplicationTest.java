/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ApplicationTest {
    private static ExpenseManager expenseManager;
    private static final Account testAccount = new Account("123", "ABC Bank", "John", 5.5);
    private static final Calendar calendar = Calendar.getInstance();

    static {
        calendar.set(2022, 3, 2);
    }

    private static final Transaction testTransaction =
            new Transaction(calendar.getTime(), "12345A", ExpenseType.EXPENSE, 100.0);

    @BeforeClass
    public static void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        assertEquals("lk.ac.mrt.cse.dbs.simpleexpensemanager", context.getPackageName());
        expenseManager = new PersistentExpenseManager(context);
    }

    @Test
    public void testAddAccount() {
        assertEquals(2, expenseManager.getAccountNumbersList().size());
        expenseManager.addAccount(testAccount.getAccountNo(), testAccount.getBankName(),
                testAccount.getAccountHolderName(), testAccount.getBalance());
        List<String> accountNumbers = expenseManager.getAccountNumbersList();
        assertTrue(accountNumbers.contains(testAccount.getAccountNo()));
    }

    @Test
    public void testAddTransaction() {
        assertEquals(0, expenseManager.getTransactionLogs().size());
        try {
            expenseManager.updateAccountBalance(
                    testTransaction.getAccountNo(), calendar.get(Calendar.DATE),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR),
                    testTransaction.getExpenseType(), Double.toString(testTransaction.getAmount()));
        } catch (InvalidAccountException e) {
            fail();
        }
        List<Transaction> logs = expenseManager.getTransactionLogs();
        Transaction transaction = logs.get(0);

        Calendar actualCalendar = Calendar.getInstance();
        actualCalendar.setTime(transaction.getDate());
        assertEquals(calendar.get(Calendar.YEAR), actualCalendar.get(Calendar.YEAR));
        assertEquals(calendar.get(Calendar.MONTH), actualCalendar.get(Calendar.MONTH));
        assertEquals(calendar.get(Calendar.DATE), actualCalendar.get(Calendar.DATE));

        assertEquals(testTransaction.getAccountNo(), transaction.getAccountNo());
        assertEquals(testTransaction.getExpenseType(), transaction.getExpenseType());
        assertEquals(testTransaction.getAmount(), transaction.getAmount(), 0.01);
    }
}
