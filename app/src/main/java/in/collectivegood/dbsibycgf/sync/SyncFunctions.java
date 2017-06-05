package in.collectivegood.dbsibycgf.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import static android.content.Context.ACCOUNT_SERVICE;

public class SyncFunctions {

    private static final String AUTHORITY = "in.collectivegood.dbsibycgf.provider";
    private static final String ACCOUNT_TYPE = "collectivegood.in";
    private static Account account = null;
    private static ContentResolver mResolver;

    public static void CreateSyncAccount(Context context, String ACCOUNT) {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            account = newAccount;

            ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, 60);
            ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        } else {
            account = accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
        }
        mResolver = context.getContentResolver();
        /*
         * Turn on periodic syncing
         */

        ContentResolver.setMasterSyncAutomatically(true);

    }

    public static void Sync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(account, AUTHORITY, settingsBundle);
    }
}
