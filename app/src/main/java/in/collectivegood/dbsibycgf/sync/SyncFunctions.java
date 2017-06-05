package in.collectivegood.dbsibycgf.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import static android.content.Context.ACCOUNT_SERVICE;


public class SyncFunctions {

    public static final String AUTHORITY = "in.collectivegood.dbsibycgf";
    public static final String ACCOUNT_TYPE = "CGF WASH MIS";
    public static Account account = null;

    public static void CreateSyncAccount(Context context, String ACCOUNT) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            account = newAccount;
            ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, 10 * 60);
        } else {
            account = null;
        }
    }
}
