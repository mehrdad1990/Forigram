package org.telegram.ui.tools.AddUserToChat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.ui.Cells.LoadingCell;

public class AddUserAdapter extends Adapter {
    private int currentCount;
    private Context mContext;

    private class Holder extends ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    public AddUserAdapter(Context context) {
        this.mContext = context;
    }

    public boolean isDataSetChanged() {
        int current = this.currentCount;
        if (current != getItemCount() || current == 1) {
            return true;
        }
        return false;
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

    public int getItemCount() {
        int count = getDialogsArray().size();
        if (count == 0 && MessagesController.getInstance().loadingDialogs) {
            return 0;
        }
        if (!MessagesController.getInstance().dialogsEndReached) {
            count++;
        }
        this.currentCount = count;
        return count;
    }

    public TL_dialog getItem(int i) {
        ArrayList<TL_dialog> arrayList = getDialogsArray();
        if (i < 0 || i >= arrayList.size()) {
            return null;
        }
        return (TL_dialog) arrayList.get(i);
    }

    public void onViewAttachedToWindow(ViewHolder holder) {
        if (holder.itemView instanceof AddUserDialogCell) {
            ((AddUserDialogCell) holder.itemView).checkCurrentDialogIndex();
        }
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = new AddUserDialogCell(this.mContext);
        } else if (viewType == 1) {
            view = new LoadingCell(this.mContext);
        }
        view.setLayoutParams(new LayoutParams(-1, -2));
        return new Holder(view);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == 0) {
            AddUserDialogCell cell = viewHolder.itemView;
            cell.useSeparator = i != getItemCount() + -1;
            cell.setDialog(getItem(i), i);
        }
    }

    public int getItemViewType(int i) {
        if (i == getDialogsArray().size()) {
            return 1;
        }
        return 0;
    }
}
