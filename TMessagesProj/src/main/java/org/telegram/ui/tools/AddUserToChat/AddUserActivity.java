package org.telegram.ui.tools.AddUserToChat;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C0859R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.ItemAnimator;
import org.telegram.messenger.support.widget.RecyclerView.OnScrollListener;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.HintDialogCell;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.VideoPlayer;
import org.telegram.ui.StickersActivity.TouchHelperCallback;

public class AddUserActivity extends BaseFragment implements NotificationCenterDelegate {
    private AddUserAdapter dialogsAdapter;
    private LinearLayout emptyView;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ProgressBar progressView;
    private int userId;

    /* renamed from: org.telegram.ui.Apogram.AddUserToChat.AddUserActivity.1 */
    class C12141 implements Runnable {
        final /* synthetic */ Context val$context;

        C12141(Context context) {
            this.val$context = context;
        }

        public void run() {
            Theme.loadRecources(this.val$context);
        }
    }

    /* renamed from: org.telegram.ui.Apogram.AddUserToChat.AddUserActivity.2 */
    class C12152 extends ActionBarMenuOnItemClick {
        C12152() {
        }

        public void onItemClick(int id) {
            if (id == -1) {
                AddUserActivity.this.finishFragment();
            } else if (id != 1) {
            }
        }
    }

    /* renamed from: org.telegram.ui.Apogram.AddUserToChat.AddUserActivity.3 */
    class C12163 extends LinearLayoutManager {
        C12163(Context x0) {
            super(x0);
        }

        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }

    /* renamed from: org.telegram.ui.Apogram.AddUserToChat.AddUserActivity.4 */
    class C12174 implements OnItemClickListener {
        C12174() {
        }

        public void onItemClick(View view, int position) {
            if (AddUserActivity.this.listView != null && AddUserActivity.this.listView.getAdapter() != null && AddUserActivity.this.listView.getAdapter() == AddUserActivity.this.dialogsAdapter) {
                TL_dialog dialog = AddUserActivity.this.dialogsAdapter.getItem(position);
                if (dialog != null) {
                    MessagesController.getInstance().addUserToChat(-((int) dialog.id), MessagesController.getInstance().getUser(Integer.valueOf(AddUserActivity.this.userId)), null, 0, null, null);
                    Toast toast = Toast.makeText(AddUserActivity.this.getParentActivity(), LocaleController.getString("Done", C0859R.string.Done), 1);
                    ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    toast.show();
                    AddUserActivity.this.finishFragment();
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.Apogram.AddUserToChat.AddUserActivity.5 */
    class C12185 implements OnTouchListener {
        C12185() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

    /* renamed from: org.telegram.ui.Apogram.AddUserToChat.AddUserActivity.6 */
    class C12196 extends OnScrollListener {
        C12196() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    }

    public AddUserActivity(Bundle args) {
        super(args);
    }

    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        if (getArguments() != null) {
            this.userId = this.arguments.getInt("userId", 0);
        }
        return true;
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    public View createView(Context context) {
        AndroidUtilities.runOnUIThread(new C12141(context));
        this.actionBar.setTitle(LocaleController.getString("SelectChat", C0859R.string.SelectChat));
        this.actionBar.setBackButtonImage(C0859R.drawable.ic_ab_back);
        this.actionBar.setActionBarMenuOnItemClick(new C12152());
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        this.listView = new RecyclerListView(context);
        this.listView.setVerticalScrollBarEnabled(true);
        this.listView.setItemAnimator(null);
        this.listView.setInstantClick(true);
        this.listView.setLayoutAnimation(null);
        this.listView.setTag(Integer.valueOf(4));
        this.layoutManager = new C12163(context);
        this.layoutManager.setOrientation(1);
        this.listView.setLayoutManager(this.layoutManager);
        this.listView.setVerticalScrollbarPosition(LocaleController.isRTL ? 1 : 2);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, GroundOverlayOptions.NO_DIMENSION));
        this.listView.setOnItemClickListener(new C12174());
        this.emptyView = new LinearLayout(context);
        this.emptyView.setOrientation(1);
        this.emptyView.setVisibility(8);
        this.emptyView.setGravity(17);
        frameLayout.addView(this.emptyView, LayoutHelper.createFrame(-1, GroundOverlayOptions.NO_DIMENSION));
        this.emptyView.setOnTouchListener(new C12185());
        TextView textView = new TextView(context);
        textView.setText(LocaleController.getString("NoChats", C0859R.string.NoChats));
        textView.setTextColor(-6974059);
        textView.setGravity(17);
        textView.setTextSize(1, 20.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyView.addView(textView, LayoutHelper.createLinear(-2, -2));
        textView = new TextView(context);
        String help = LocaleController.getString("NoChatsHelp", C0859R.string.NoChatsHelp);
        if (AndroidUtilities.isTablet() && !AndroidUtilities.isSmallTablet()) {
            help = help.replace('\n', ' ');
        }
        textView.setText(help);
        textView.setTextColor(-6974059);
        textView.setTextSize(1, 15.0f);
        textView.setGravity(17);
        textView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(8.0f), 0);
        textView.setLineSpacing((float) AndroidUtilities.dp(2.0f), TouchHelperCallback.ALPHA_FULL);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyView.addView(textView, LayoutHelper.createLinear(-2, -2));
        this.progressView = new ProgressBar(context);
        this.progressView.setVisibility(8);
        frameLayout.addView(this.progressView, LayoutHelper.createFrame(-2, -2, 17));
        this.listView.setOnScrollListener(new C12196());
        this.dialogsAdapter = new AddUserAdapter(context);
        this.listView.setAdapter(this.dialogsAdapter);
        if (MessagesController.getInstance().loadingDialogs && MessagesController.getInstance().dialogs.isEmpty()) {
            this.emptyView.setVisibility(8);
            this.listView.setEmptyView(this.progressView);
        } else {
            this.progressView.setVisibility(8);
            this.listView.setEmptyView(this.emptyView);
        }
        return this.fragmentView;
    }

    public void onResume() {
        super.onResume();
        if (this.dialogsAdapter != null) {
            this.dialogsAdapter.notifyDataSetChanged();
        }
    }

    protected void onDialogDismiss(Dialog dialog) {
        super.onDialogDismiss(dialog);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            int a = 0;
            while (a < permissions.length) {
                if (grantResults.length > a && grantResults[a] == 0) {
                    String str = permissions[a];
                    Object obj = -1;
                    switch (str.hashCode()) {
                        case 1365911975:
                            if (str.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                                int i = 1;
                                break;
                            }
                            break;
                        case 1977429404:
                            if (str.equals("android.permission.READ_CONTACTS")) {
                                obj = null;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case VideoPlayer.TRACK_DEFAULT /*0*/:
                            ContactsController.getInstance().readContacts();
                            break;
                        case VideoPlayer.TYPE_AUDIO /*1*/:
                            ImageLoader.getInstance().checkMediaPaths();
                            break;
                        default:
                            break;
                    }
                }
                a++;
            }
        }
    }

    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.dialogsNeedReload) {
            if (this.dialogsAdapter != null) {
                if (this.dialogsAdapter.isDataSetChanged()) {
                    this.dialogsAdapter.notifyDataSetChanged();
                } else {
                    updateVisibleRows(TLRPC.MESSAGE_FLAG_HAS_BOT_ID);
                }
            }
            if (this.listView != null) {
                try {
                    if (MessagesController.getInstance().loadingDialogs && MessagesController.getInstance().dialogs.isEmpty()) {
                        this.emptyView.setVisibility(8);
                        this.listView.setEmptyView(this.progressView);
                    } else {
                        this.progressView.setVisibility(8);
                        this.listView.setEmptyView(this.emptyView);
                    }
                } catch (Throwable e) {
                    FileLog.m18e("tmessages", e);
                }
            }
        } else if (id == NotificationCenter.emojiDidLoaded) {
            updateVisibleRows(0);
        } else if (id == NotificationCenter.updateInterfaces) {
            updateVisibleRows(((Integer) args[0]).intValue());
        } else if (id != NotificationCenter.appDidLogout) {
            if (id == NotificationCenter.encryptedChatUpdated) {
                updateVisibleRows(0);
            } else if (id == NotificationCenter.contactsDidLoaded) {
                updateVisibleRows(0);
            } else if (id != NotificationCenter.openedChatChanged) {
                if (id == NotificationCenter.notificationsSettingsUpdated) {
                    updateVisibleRows(0);
                } else if (id == NotificationCenter.messageReceivedByAck || id == NotificationCenter.messageReceivedByServer || id == NotificationCenter.messageSendError) {
                    updateVisibleRows(ItemAnimator.FLAG_APPEARED_IN_PRE_LAYOUT);
                }
            }
        }
        if (id == NotificationCenter.didLoadedReplyMessages) {
            updateVisibleRows(0);
        }
    }

    private ArrayList<TL_dialog> getDialogsArray() {
        ArrayList<TL_dialog> allDialogs = new ArrayList();
        allDialogs.addAll(MessagesController.getInstance().dialogs);
        ArrayList<TL_dialog> dialogs = new ArrayList();
        for (int i = 0; i < allDialogs.size(); i++) {
            TL_dialog dialog = (TL_dialog) allDialogs.get(i);
            int lower_id = (int) dialog.id;
            boolean isChat = lower_id < 0 && ((int) (dialog.id >> 32)) != 1;
            Chat chat = MessagesController.getInstance().getChat(Integer.valueOf(-lower_id));
            if (isChat && (chat.creator || chat.editor)) {
                dialogs.add(dialog);
            }
        }
        return dialogs;
    }

    private void updateVisibleRows(int mask) {
        if (this.listView != null) {
            int count = this.listView.getChildCount();
            for (int a = 0; a < count; a++) {
                View child = this.listView.getChildAt(a);
                if (child instanceof DialogCell) {
                    DialogCell cell = (DialogCell) child;
                    if ((mask & TLRPC.MESSAGE_FLAG_HAS_BOT_ID) != 0) {
                        cell.checkCurrentDialogIndex();
                    } else if ((mask & TLRPC.USER_FLAG_UNUSED3) == 0) {
                        cell.update(mask);
                    }
                } else if (child instanceof UserCell) {
                    ((UserCell) child).update(mask);
                } else if (child instanceof ProfileSearchCell) {
                    ((ProfileSearchCell) child).update(mask);
                } else if (child instanceof RecyclerListView) {
                    RecyclerListView innerListView = (RecyclerListView) child;
                    int count2 = innerListView.getChildCount();
                    for (int b = 0; b < count2; b++) {
                        View child2 = innerListView.getChildAt(b);
                        if (child2 instanceof HintDialogCell) {
                            ((HintDialogCell) child2).checkUnreadCounter(mask);
                        }
                    }
                }
            }
        }
    }
}
