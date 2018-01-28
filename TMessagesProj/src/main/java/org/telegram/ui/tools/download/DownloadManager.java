/*
 * This is the source code of Telegram for Android v. 2.0.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2016.
 */

package org.telegram.ui.tools.download;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import com.arash.telegram.lavegram.R;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Adapters.BaseFragmentAdapter;
import org.telegram.ui.Cells.TextInfoCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.GroupCreateActivity;
import org.telegram.ui.ProfileActivity;

import java.util.ArrayList;

public class DownloadManager extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    public interface PrivacyActivityDelegate {
        void didUpdatedUserList(ArrayList<Integer> ids, boolean added);
    }

    private ListView listView;
    private ListAdapter listViewAdapter;
    private int selectedUserId;

    private boolean isGroup;

    private ArrayList<Integer> uidArray;
    private boolean isAlwaysShare;

    private PrivacyActivityDelegate delegate;

    private final static int block_user = 1;

    public DownloadManager(ArrayList<Integer> users, boolean group, boolean always) {
        super();
        uidArray = users;
        isAlwaysShare = always;
        isGroup = group;
    }

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance().addObserver(this, NotificationCenter.updateInterfaces);
        return true;
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.updateInterfaces);
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        if (isGroup) {
            if (isAlwaysShare) {
                actionBar.setTitle(LocaleController.getString("AlwaysAllow", R.string.AlwaysAllow));
            } else {
                actionBar.setTitle(LocaleController.getString("NeverAllow", R.string.NeverAllow));
            }
        } else {
            if (isAlwaysShare) {
                actionBar.setTitle(LocaleController.getString("AlwaysShareWithTitle", R.string.AlwaysShareWithTitle));
            } else {
                actionBar.setTitle(LocaleController.getString("NeverShareWithTitle", R.string.NeverShareWithTitle));
            }
        }
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == block_user) {
                    Bundle args = new Bundle();
                    args.putBoolean(isAlwaysShare ? "isAlwaysShare" : "isNeverShare", true);
                    args.putBoolean("isGroup", isGroup);
                    GroupCreateActivity fragment = new GroupCreateActivity(args);
                    fragment.setDelegate(new GroupCreateActivity.GroupCreateActivityDelegate() {
                        @Override
                        public void didSelectUsers(ArrayList<Integer> ids) {
                            for (Integer id : ids) {
                                if (uidArray.contains(id)) {
                                    continue;
                                }
                                uidArray.add(id);
                            }
                            listViewAdapter.notifyDataSetChanged();
                            if (delegate != null) {
                                delegate.didUpdatedUserList(uidArray, true);
                            }
                        }
                    });
                    presentFragment(fragment);
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        menu.addItem(block_user, R.drawable.plus);

        fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        TextView emptyTextView = new TextView(context);
        emptyTextView.setTextColor(0xff808080);
        emptyTextView.setTextSize(20);
        emptyTextView.setGravity(Gravity.CENTER);
        emptyTextView.setVisibility(View.INVISIBLE);
        emptyTextView.setText(LocaleController.getString("NoContacts", R.string.NoContacts));
        frameLayout.addView(emptyTextView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) emptyTextView.getLayoutParams();
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        layoutParams.height = LayoutHelper.MATCH_PARENT;
        layoutParams.gravity = Gravity.TOP;
        emptyTextView.setLayoutParams(layoutParams);
        emptyTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        listView = new ListView(context);
        listView.setEmptyView(emptyTextView);
        listView.setVerticalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setAdapter(listViewAdapter = new ListAdapter(context));
        listView.setVerticalScrollbarPosition(LocaleController.isRTL ? ListView.SCROLLBAR_POSITION_LEFT : ListView.SCROLLBAR_POSITION_RIGHT);
        frameLayout.addView(listView);
        layoutParams = (FrameLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.width = LayoutHelper.MATCH_PARENT;
        layoutParams.height = LayoutHelper.MATCH_PARENT;
        listView.setLayoutParams(layoutParams);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < uidArray.size()) {
                    Bundle args = new Bundle();
                    args.putInt("user_id", uidArray.get(i));
                    presentFragment(new ProfileActivity(args));
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < 0 || i >= uidArray.size() || getParentActivity() == null) {
                    return true;
                }
                selectedUserId = uidArray.get(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                CharSequence[] items = new CharSequence[]{LocaleController.getString("Delete", R.string.Delete)};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            uidArray.remove((Integer) selectedUserId);
                            listViewAdapter.notifyDataSetChanged();
                            if (delegate != null) {
                                delegate.didUpdatedUserList(uidArray, false);
                            }
                        }
                    }
                });
                showDialog(builder.create());
                return true;
            }
        });

        return fragmentView;
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        if (id == NotificationCenter.updateInterfaces) {
            int mask = (Integer)args[0];
            if ((mask & MessagesController.UPDATE_MASK_AVATAR) != 0 || (mask & MessagesController.UPDATE_MASK_NAME) != 0) {
                updateVisibleRows(mask);
            }
        }
    }

    private void updateVisibleRows(int mask) {
        if (listView == null) {
            return;
        }
        int count = listView.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = listView.getChildAt(a);
            if (child instanceof UserCell) {
                ((UserCell) child).update(mask);
            }
        }
    }

    public void setDelegate(PrivacyActivityDelegate privacyActivityDelegate) {
        delegate = privacyActivityDelegate;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listViewAdapter != null) {
            listViewAdapter.notifyDataSetChanged();
        }
    }

    private class ListAdapter extends BaseFragmentAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i) {
            return i != uidArray.size();
        }

        @Override
        public int getCount() {
            if (uidArray.isEmpty()) {
                return 0;
            }
            return uidArray.size() + 1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            if (type == 0) {
                if (view == null) {
                    view = new UserCell(mContext, 1, 0, false);
                }
                TLRPC.User user = MessagesController.getInstance().getUser(uidArray.get(i));
                ((UserCell)view).setData(user, null, user.phone != null && user.phone.length() != 0 ? PhoneFormat.getInstance().format("+" + user.phone) : LocaleController.getString("NumberUnknown", R.string.NumberUnknown), 0);
            } else if (type == 1) {
                if (view == null) {
                    view = new TextInfoCell(mContext);
                    ((TextInfoCell) view).setText(LocaleController.getString("RemoveFromListText", R.string.RemoveFromListText));
                }
            }
            return view;
        }

        @Override
        public int getItemViewType(int i) {
            if(i == uidArray.size()) {
                return 1;
            }
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEmpty() {
            return uidArray.isEmpty();
        }
    }

    /**
     * Created by Binh on 4/4/2015.
     */
    @EBean
    public static class DownloadManager {
        DisplayImageOptions options;

        @RootContext
        MainActivity mContext;

        ImageLoader imageLoader;
        RPCListener listener;
        ProductModel productModel;
        ProgressDialog mDialog;
        int total = 0;
        int count = 0;
        AlertDialog alertDialogReload;

        public DownloadManager() {
            imageLoader = ImageLoader.getInstance();

            options = new DisplayImageOptions.Builder()
                    //.showImageForEmptyUri(R.drawable.ic_emtry)
                    //.showImageOnFail(R.drawable.ic_error)
                    //.resetViewBeforeLoading(true)
                    //.cacheInMemory(false)
                    .cacheOnDisk(true)
                            //.imageScaleType(ImageScaleType.EXACTLY)
                            //.bitmapConfig(Bitmap.Config.RGB_565)
                            //.considerExifParams(true)
                            //.displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }

        /**
         * Download document
         *
         * @param productModel
         * @param listener
         */
        public void downloadDocument(final ProductModel productModel, final RPCListener listener) {
            if (productModel != null)
                this.productModel = productModel;

            if (listener != null)
                this.listener = listener;

            total = productModel.numberPages;
            count = 0;

            if (total > 0) {
                if (productModel == null)
                    return;

                showLoading();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        imageLoader.loadImageSync(productModel.mobileThumbnail);
                        for (int i = 0; i < total; i++) {
                            final String imageUrl = String.format(AppConstant.URL_LIST_DATA_READ, productModel.documentId, i);
                            imageLoader.loadImage(imageUrl, options, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    if (alertDialogReload != null && alertDialogReload.isShowing())
                                        return;

                                    if (mDialog != null && mDialog.isShowing())
                                        mDialog.dismiss();

                                    stopLoadImage();

                                    showReload();
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    countLoading();
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {
                                    if (mDialog != null && mDialog.isShowing())
                                        mDialog.dismiss();
                                }
                            });
                        }
                    }
                }).start();
            } else {
                mContext.showMessageBox(mContext.getString(R.string.msg_not_update_data), null);
            }
        }

        @UiThread
        void showReload() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle(mContext.getString(R.string.msg_download_false));
            alertDialogBuilder.setMessage(mContext.getString(R.string.msg_check_connect_or_memory));
            alertDialogBuilder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialogBuilder.setPositiveButton(mContext.getString(R.string.msg_continue_download), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    downloadDocument(productModel, listener);
                }
            });

            alertDialogReload = alertDialogBuilder.create();

            alertDialogBuilder.show();
        }

        @UiThread
        void showLoading() {
            mDialog = DialogUtil.showDialogDownload(mContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    cancelDownload();
                }
            });

            mDialog.show();
        }

        @UiThread
        void countLoading() {
            count++;
            float per = ((float) count / total) * 100f;
            mDialog.setMessage(mContext.getString(R.string.msg_downloading) + (int) per + "%");

            if (count == total) {
                if (mDialog != null && mDialog.isShowing())
                    mDialog.dismiss();

                if (listener != null)
                    listener.onSuccess(true);
            }

        }

        @UiThread
        @Background
        public void cancelDownload() {
            if (imageLoader == null)
                return;

            if (alertDialogReload != null && alertDialogReload.isShowing())
                alertDialogReload.dismiss();

            if (mDialog != null && mDialog.isShowing())
                mDialog.dismiss();

            stopLoadImage();

            imageLoader.getDiskCache().remove(productModel.mobileThumbnail);

            for (int i = 0; i < total; i++) {
                final String imageUrl = String.format(AppConstant.URL_LIST_DATA_READ, productModel.documentId, i);
                if (imageLoader != null)
                    imageLoader.getDiskCache().remove(imageUrl);
            }

            count = 0;
            total = 0;
        }

        public void stopLoadImage() {
            if (imageLoader == null)
                return;

            imageLoader.stop();
        }

        public boolean isDownSuccess() {
            if (count == total)
                return true;
            else
                return false;
        }
    }
}
