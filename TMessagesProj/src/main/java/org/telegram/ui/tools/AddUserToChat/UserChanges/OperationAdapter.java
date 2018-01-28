package org.telegram.ui.tools.AddUserToChat.UserChanges;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.Apogram.SpecialContacts.SpecialContactCell;

public class OperationAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<OperationModel> specialContactsModel;

    public OperationAdapter(Context context) {
        this.specialContactsModel = new ArrayList();
        this.mContext = context;
        getSpecialContactsArray();
    }

    private void getSpecialContactsArray() {
        for (int i = 0; i < new SC_DBHelper(ApplicationLoader.applicationContext).getAllSContacts().size(); i++) {
        }
    }

    public int getCount() {
        return this.specialContactsModel.size();
    }

    public Object getItem(int position) {
        if (this.specialContactsModel != null) {
            return this.specialContactsModel.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new SpecialContactCell(this.mContext, 10);
        }
        OperationModel modelP = (OperationModel) this.specialContactsModel.get(position);
        return convertView;
    }
}
