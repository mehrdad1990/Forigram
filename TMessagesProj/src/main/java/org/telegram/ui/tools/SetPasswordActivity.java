package org.telegram.ui.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C0859R;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.BaseFragmentAdapter;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.DialogsActivity;

public class SetPasswordActivity extends BaseFragment implements NotificationCenterDelegate {
    private static final int change_Pass = 3;
    private static final int disable_hidden = 2;
    private static final int done_button = 1;
    private static final int enable_hidden = 1;
    private static final int unlock_dialogs = 4;
    private int changeenablePasscodeRow;
    private int enablePasscodeRow;
    private String firstPassword;
    private ListAdapter listAdapter;
    private ListView listView;
    private int passcodeDetailRow;
    private int passcodeSetStep;
    private EditText passwordEditText;
    private int rowCount;
    private TextView titleTextView;
    private int type;

    /* renamed from: org.telegram.ui.Apogram.SetPasswordActivity.1 */
    class C12951 extends ActionBarMenuOnItemClick {
        C12951() {
        }

        public void onItemClick(int id) {
            if (id == -1) {
                if (SetPasswordActivity.this.type == SetPasswordActivity.unlock_dialogs) {
                    SetPasswordActivity.this.presentFragment(new DialogsActivity(null), true);
                } else {
                    SetPasswordActivity.this.finishFragment();
                }
            } else if (id != SetPasswordActivity.enable_hidden) {
            } else {
                if (SetPasswordActivity.this.passcodeSetStep == 0) {
                    SetPasswordActivity.this.processNext();
                } else if (SetPasswordActivity.this.passcodeSetStep == SetPasswordActivity.enable_hidden) {
                    SetPasswordActivity.this.processDone();
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.Apogram.SetPasswordActivity.2 */
    class C12962 implements OnEditorActionListener {
        C12962() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (SetPasswordActivity.this.passcodeSetStep == 0) {
                SetPasswordActivity.this.processNext();
                return true;
            } else if (SetPasswordActivity.this.passcodeSetStep != SetPasswordActivity.enable_hidden) {
                return false;
            } else {
                SetPasswordActivity.this.processDone();
                return true;
            }
        }
    }

    /* renamed from: org.telegram.ui.Apogram.SetPasswordActivity.3 */
    class C12973 implements TextWatcher {
        C12973() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (SetPasswordActivity.this.passwordEditText.length() != SetPasswordActivity.unlock_dialogs) {
                return;
            }
            if (SetPasswordActivity.this.type != SetPasswordActivity.enable_hidden) {
                SetPasswordActivity.this.processDone();
            } else if (SetPasswordActivity.this.passcodeSetStep == 0) {
                SetPasswordActivity.this.processNext();
            } else if (SetPasswordActivity.this.passcodeSetStep == SetPasswordActivity.enable_hidden) {
                SetPasswordActivity.this.processDone();
            }
        }
    }

    /* renamed from: org.telegram.ui.Apogram.SetPasswordActivity.4 */
    class C12984 implements OnCreateContextMenuListener {
        C12984() {
        }

        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            menu.clear();
        }
    }

    /* renamed from: org.telegram.ui.Apogram.SetPasswordActivity.5 */
    class C12995 implements Callback {
        C12995() {
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }
    }

    /* renamed from: org.telegram.ui.Apogram.SetPasswordActivity.6 */
    class C13006 implements OnItemClickListener {
        C13006() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == SetPasswordActivity.this.changeenablePasscodeRow) {
                SetPasswordActivity.this.presentFragment(new SetPasswordActivity(SetPasswordActivity.change_Pass));
                SetPasswordActivity.this.finishFragment();
            } else if (i == SetPasswordActivity.this.enablePasscodeRow) {
                TextCheckCell cell = (TextCheckCell) view;
                if (ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getString("chat_password", TtmlNode.ANONYMOUS_REGION_ID).length() != 0) {
                    SetPasswordActivity.this.presentFragment(new SetPasswordActivity(SetPasswordActivity.disable_hidden));
                } else {
                    SetPasswordActivity.this.presentFragment(new SetPasswordActivity(SetPasswordActivity.enable_hidden));
                }
                SetPasswordActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.Apogram.SetPasswordActivity.7 */
    class C13017 implements Runnable {
        C13017() {
        }

        public void run() {
            if (SetPasswordActivity.this.passwordEditText != null) {
                SetPasswordActivity.this.passwordEditText.requestFocus();
                AndroidUtilities.showKeyboard(SetPasswordActivity.this.passwordEditText);
            }
        }
    }

    /* renamed from: org.telegram.ui.Apogram.SetPasswordActivity.8 */
    class C13028 implements OnPreDrawListener {
        C13028() {
        }

        public boolean onPreDraw() {
            SetPasswordActivity.this.listView.getViewTreeObserver().removeOnPreDrawListener(this);
            return true;
        }
    }

    private class ListAdapter extends BaseFragmentAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        public boolean areAllItemsEnabled() {
            return false;
        }

        public boolean isEnabled(int i) {
            String chatPassword = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getString("chat_password", TtmlNode.ANONYMOUS_REGION_ID);
            if (i == SetPasswordActivity.this.enablePasscodeRow || (chatPassword.length() != 0 && i == SetPasswordActivity.this.changeenablePasscodeRow)) {
                return true;
            }
            return false;
        }

        public int getCount() {
            return SetPasswordActivity.this.rowCount;
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean hasStableIds() {
            return false;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            boolean z = false;
            int viewType = getItemViewType(i);
            String chatPassword;
            if (viewType == 0) {
                if (view == null) {
                    view = new TextCheckCell(this.mContext);
                    view.setBackgroundColor(-1);
                }
                TextCheckCell textCell = (TextCheckCell) view;
                if (i == SetPasswordActivity.this.enablePasscodeRow) {
                    chatPassword = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getString("chat_password", TtmlNode.ANONYMOUS_REGION_ID);
                    String string = LocaleController.getString("EnablePass", C0859R.string.EnablePass);
                    if (chatPassword.length() > 0) {
                        z = true;
                    }
                    textCell.setTextAndCheck(string, z, true);
                }
            } else if (viewType == SetPasswordActivity.enable_hidden) {
                if (view == null) {
                    view = new TextSettingsCell(this.mContext);
                    view.setBackgroundColor(-1);
                }
                TextSettingsCell textCell2 = (TextSettingsCell) view;
                if (i == SetPasswordActivity.this.changeenablePasscodeRow) {
                    chatPassword = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getString("chat_password", TtmlNode.ANONYMOUS_REGION_ID);
                    textCell2.setText(LocaleController.getString("ChangePass", C0859R.string.ChangePass), false);
                    textCell2.setTextColor(chatPassword.length() == 0 ? -3750202 : Theme.MSG_TEXT_COLOR);
                }
            } else if (viewType == SetPasswordActivity.disable_hidden) {
                if (view == null) {
                    view = new TextInfoPrivacyCell(this.mContext);
                }
                if (i == SetPasswordActivity.this.passcodeDetailRow) {
                    ((TextInfoPrivacyCell) view).setText(LocaleController.getString("ChangePassInfo", C0859R.string.ChangePassInfo));
                    view.setBackgroundResource(C0859R.drawable.greydivider_bottom);
                }
            }
            return view;
        }

        public int getItemViewType(int i) {
            if (i == SetPasswordActivity.this.enablePasscodeRow) {
                return 0;
            }
            if (i == SetPasswordActivity.this.changeenablePasscodeRow) {
                return SetPasswordActivity.enable_hidden;
            }
            if (i == SetPasswordActivity.this.passcodeDetailRow) {
                return SetPasswordActivity.disable_hidden;
            }
            return 0;
        }

        public int getViewTypeCount() {
            return SetPasswordActivity.change_Pass;
        }

        public boolean isEmpty() {
            return false;
        }
    }

    public SetPasswordActivity(int type) {
        this.passcodeSetStep = 0;
        this.type = type;
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    public View createView(Context context) {
        if (this.type != 5) {
            this.actionBar.setBackButtonImage(C0859R.drawable.ic_ab_back);
        }
        this.actionBar.setAllowOverlayTitle(false);
        this.actionBar.setActionBarMenuOnItemClick(new C12951());
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = this.fragmentView;
        LayoutParams layoutParams;
        if (this.type != 0) {
            this.actionBar.createMenu().addItemWithWidth(enable_hidden, C0859R.drawable.ic_done, AndroidUtilities.dp(56.0f));
            this.titleTextView = new TextView(context);
            this.titleTextView.setTextColor(Theme.ATTACH_SHEET_TEXT_COLOR);
            if (this.type != enable_hidden) {
                this.titleTextView.setText(LocaleController.getString("EnterCurrentPasscode", C0859R.string.EnterCurrentPasscode));
            } else if (ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getString("chat_password", TtmlNode.ANONYMOUS_REGION_ID).length() != 0) {
                this.titleTextView.setText(LocaleController.getString("EnterNewPasscode", C0859R.string.EnterNewPasscode));
            } else {
                this.titleTextView.setText(LocaleController.getString("EnterNewFirstPasscode", C0859R.string.EnterNewFirstPasscode));
            }
            this.titleTextView.setTextSize(enable_hidden, 18.0f);
            this.titleTextView.setGravity(enable_hidden);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            frameLayout.addView(this.titleTextView);
            layoutParams = (LayoutParams) this.titleTextView.getLayoutParams();
            layoutParams.width = -2;
            layoutParams.height = -2;
            layoutParams.gravity = enable_hidden;
            layoutParams.topMargin = AndroidUtilities.dp(38.0f);
            this.titleTextView.setLayoutParams(layoutParams);
            this.passwordEditText = new EditText(context);
            this.passwordEditText.setTextSize(enable_hidden, 20.0f);
            this.passwordEditText.setTextColor(Theme.MSG_TEXT_COLOR);
            this.passwordEditText.setMaxLines(enable_hidden);
            this.passwordEditText.setLines(enable_hidden);
            this.passwordEditText.setInputType(disable_hidden);
            this.passwordEditText.setGravity(enable_hidden);
            this.passwordEditText.setSingleLine(true);
            if (this.type == enable_hidden) {
                this.passcodeSetStep = 0;
                this.passwordEditText.setImeOptions(5);
            } else {
                this.passcodeSetStep = enable_hidden;
                this.passwordEditText.setImeOptions(6);
            }
            this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.passwordEditText.setTypeface(Typeface.DEFAULT);
            AndroidUtilities.clearCursorDrawable(this.passwordEditText);
            frameLayout.addView(this.passwordEditText);
            layoutParams = (LayoutParams) this.passwordEditText.getLayoutParams();
            layoutParams.topMargin = AndroidUtilities.dp(90.0f);
            layoutParams.height = AndroidUtilities.dp(36.0f);
            layoutParams.leftMargin = AndroidUtilities.dp(40.0f);
            layoutParams.gravity = 51;
            layoutParams.rightMargin = AndroidUtilities.dp(40.0f);
            layoutParams.width = -1;
            this.passwordEditText.setLayoutParams(layoutParams);
            this.passwordEditText.setOnEditorActionListener(new C12962());
            this.passwordEditText.addTextChangedListener(new C12973());
            if (VERSION.SDK_INT < 11) {
                this.passwordEditText.setOnCreateContextMenuListener(new C12984());
            } else {
                this.passwordEditText.setCustomSelectionActionModeCallback(new C12995());
            }
            if (this.type == change_Pass || this.type == unlock_dialogs) {
                this.actionBar.setTitle(LocaleController.getString("Authentication", C0859R.string.Authentication));
            } else {
                this.actionBar.setTitle(LocaleController.getString("SetPass", C0859R.string.SetPass));
            }
        } else {
            this.actionBar.setTitle(LocaleController.getString("HideChats", C0859R.string.HideChats));
            frameLayout.setBackgroundColor(Theme.ACTION_BAR_MODE_SELECTOR_COLOR);
            this.listView = new ListView(context);
            this.listView.setDivider(null);
            this.listView.setDividerHeight(0);
            this.listView.setVerticalScrollBarEnabled(false);
            this.listView.setDrawSelectorOnTop(true);
            frameLayout.addView(this.listView);
            layoutParams = (LayoutParams) this.listView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -1;
            layoutParams.gravity = 48;
            this.listView.setLayoutParams(layoutParams);
            ListView listView = this.listView;
            android.widget.ListAdapter listAdapter = new ListAdapter(context);
            this.listAdapter = listAdapter;
            listView.setAdapter(listAdapter);
            this.listView.setOnItemClickListener(new C13006());
        }
        return this.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
        if (this.type != 0) {
            AndroidUtilities.runOnUIThread(new C13017(), 200);
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.didSetPasscode && this.type == 0) {
            updateRows();
            if (this.listAdapter != null) {
                this.listAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = this.rowCount;
        this.rowCount = i + enable_hidden;
        this.enablePasscodeRow = i;
        i = this.rowCount;
        this.rowCount = i + enable_hidden;
        this.changeenablePasscodeRow = i;
        i = this.rowCount;
        this.rowCount = i + enable_hidden;
        this.passcodeDetailRow = i;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.listView != null) {
            this.listView.getViewTreeObserver().addOnPreDrawListener(new C13028());
        }
    }

    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen && this.type != 0) {
            AndroidUtilities.showKeyboard(this.passwordEditText);
        }
    }

    private void processNext() {
        if (this.passwordEditText.getText().length() == 0) {
            onPasscodeError();
            return;
        }
        this.titleTextView.setText(LocaleController.getString("ReEnterYourPasscode", C0859R.string.ReEnterYourPasscode));
        this.firstPassword = this.passwordEditText.getText().toString();
        this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
        this.passcodeSetStep = enable_hidden;
    }

    private void processDone() {
        boolean z = false;
        if (this.passwordEditText.getText().length() == 0) {
            onPasscodeError();
        } else if (this.type == enable_hidden) {
            if (this.firstPassword.equals(this.passwordEditText.getText().toString())) {
                editor = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).edit();
                editor.putString("chat_password", this.firstPassword);
                editor.putBoolean("chat_unlocked", true);
                editor.commit();
                restartApp();
                this.passwordEditText.clearFocus();
                AndroidUtilities.hideKeyboard(this.passwordEditText);
                return;
            }
            try {
                Toast.makeText(getParentActivity(), LocaleController.getString("PasscodeDoNotMatch", C0859R.string.PasscodeDoNotMatch), 0).show();
            } catch (Throwable e) {
                FileLog.m18e("tmessages", e);
            }
            AndroidUtilities.shakeView(this.titleTextView, 2.0f, 0);
            this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
        } else if (this.type == disable_hidden) {
            preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            if (preferences.getString("chat_password", TtmlNode.ANONYMOUS_REGION_ID).equals(this.passwordEditText.getText().toString())) {
                editor = preferences.edit();
                editor.putString("chat_password", TtmlNode.ANONYMOUS_REGION_ID);
                editor.commit();
                this.passwordEditText.clearFocus();
                AndroidUtilities.hideKeyboard(this.passwordEditText);
                restartApp();
                return;
            }
            this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
            onPasscodeError();
        } else if (this.type == change_Pass) {
            if (ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getString("chat_password", TtmlNode.ANONYMOUS_REGION_ID).equals(this.passwordEditText.getText().toString())) {
                this.passwordEditText.clearFocus();
                AndroidUtilities.hideKeyboard(this.passwordEditText);
                presentFragment(new SetPasswordActivity(enable_hidden), true);
                return;
            }
            this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
            onPasscodeError();
        } else if (this.type == unlock_dialogs) {
            preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            if (preferences.getString("chat_password", TtmlNode.ANONYMOUS_REGION_ID).equals(this.passwordEditText.getText().toString())) {
                boolean chatUnlocked = preferences.getBoolean("chat_unlocked", false);
                editor = preferences.edit();
                String str = "chat_unlocked";
                if (!chatUnlocked) {
                    z = true;
                }
                editor.putBoolean(str, z);
                editor.commit();
                this.passwordEditText.clearFocus();
                AndroidUtilities.hideKeyboard(this.passwordEditText);
                presentFragment(new DialogsActivity(null), true);
                return;
            }
            this.passwordEditText.setText(TtmlNode.ANONYMOUS_REGION_ID);
            onPasscodeError();
        }
    }

    private void restartApp() {
        Context context = getParentActivity().getBaseContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        if (VERSION.SDK_INT >= 11) {
            intent.addFlags(TLRPC.MESSAGE_FLAG_EDITED);
        }
        ((AlarmManager) context.getSystemService(NotificationCompatApi21.CATEGORY_ALARM)).set(enable_hidden, System.currentTimeMillis() + 1, PendingIntent.getActivity(context, 0, intent, 268435456));
        System.exit(disable_hidden);
    }

    private void onPasscodeError() {
        if (getParentActivity() != null) {
            Vibrator v = (Vibrator) getParentActivity().getSystemService("vibrator");
            if (v != null) {
                v.vibrate(200);
            }
            AndroidUtilities.shakeView(this.titleTextView, 2.0f, 0);
        }
    }
}
