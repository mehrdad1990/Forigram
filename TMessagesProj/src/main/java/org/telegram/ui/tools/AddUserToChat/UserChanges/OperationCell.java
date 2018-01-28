package org.telegram.ui.tools.AddUserToChat.UserChanges;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.C0859R;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.exoplayer.C0927C;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Apogram.SolarCalendar;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.LayoutHelper;

public class OperationCell extends FrameLayout {
    private AvatarDrawable avatarDrawable;
    private BackupImageView avatarImageView;
    private CheckBox checkBox;
    private SimpleTextView dateTextView;
    private ImageView imageView;
    private String mDate;
    private String mOperation;
    private User mUser;
    private SimpleTextView nameTextView;
    private int newValueColor;
    private int oldValueColor;
    private OperationModel operationModel;
    private SimpleTextView operationTextView;

    @SuppressLint({"RtlHardcoded"})
    public OperationCell(Context context, int padding) {
        int i;
        int i2;
        int i3;
        int i4 = 5;
        super(context);
        this.mUser = null;
        this.oldValueColor = -5723992;
        this.newValueColor = -12876608;
        this.avatarDrawable = new AvatarDrawable();
        this.avatarImageView = new BackupImageView(context);
        this.avatarImageView.setRoundRadius(AndroidUtilities.dp(24.0f));
        View view = this.avatarImageView;
        if (LocaleController.isRTL) {
            i = 5;
        } else {
            i = 3;
        }
        addView(view, LayoutHelper.createFrame(48, 48.0f, i | 48, LocaleController.isRTL ? 0.0f : (float) (padding + 7), 8.0f, LocaleController.isRTL ? (float) (padding + 7) : 0.0f, 0.0f));
        this.nameTextView = new SimpleTextView(context);
        this.nameTextView.setTextColor(Theme.STICKERS_SHEET_TITLE_TEXT_COLOR);
        this.nameTextView.setTextSize(17);
        SimpleTextView simpleTextView = this.nameTextView;
        if (LocaleController.isRTL) {
            i2 = 5;
        } else {
            i2 = 3;
        }
        simpleTextView.setGravity(i2 | 48);
        this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        view = this.nameTextView;
        if (LocaleController.isRTL) {
            i3 = 5;
        } else {
            i3 = 3;
        }
        addView(view, LayoutHelper.createFrame(-1, 20.0f, i3 | 48, LocaleController.isRTL ? 28.0f : (float) (padding + 68), 11.5f, LocaleController.isRTL ? (float) (padding + 68) : 28.0f, 0.0f));
        this.operationTextView = new SimpleTextView(context);
        this.operationTextView.setTextSize(14);
        simpleTextView = this.operationTextView;
        if (LocaleController.isRTL) {
            i2 = 5;
        } else {
            i2 = 3;
        }
        simpleTextView.setGravity(i2 | 48);
        this.operationTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        view = this.operationTextView;
        if (LocaleController.isRTL) {
            i3 = 5;
        } else {
            i3 = 3;
        }
        addView(view, LayoutHelper.createFrame(-1, 20.0f, i3 | 48, LocaleController.isRTL ? 28.0f : (float) (padding + 68), 34.5f, LocaleController.isRTL ? (float) (padding + 68) : 28.0f, 0.0f));
        this.dateTextView = new SimpleTextView(context);
        this.dateTextView.setTextSize(14);
        simpleTextView = this.dateTextView;
        if (LocaleController.isRTL) {
            i2 = 3;
        } else {
            i2 = 5;
        }
        simpleTextView.setGravity(i2 | 48);
        this.dateTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        view = this.dateTextView;
        if (LocaleController.isRTL) {
            i3 = 3;
        } else {
            i3 = 5;
        }
        addView(view, LayoutHelper.createFrame(-1, 20.0f, i3 | 48, LocaleController.isRTL ? (float) (padding + 5) : 28.0f, 60.5f, LocaleController.isRTL ? 28.0f : (float) (padding + 10), 0.0f));
        this.imageView = new ImageView(context);
        this.imageView.setScaleType(ScaleType.CENTER);
        this.imageView.setVisibility(8);
        View view2 = this.imageView;
        if (LocaleController.isRTL) {
            i2 = 5;
        } else {
            i2 = 3;
        }
        addView(view2, LayoutHelper.createFrame(-2, -2.0f, i2 | 16, LocaleController.isRTL ? 0.0f : 16.0f, 0.0f, LocaleController.isRTL ? 16.0f : 0.0f, 0.0f));
        this.checkBox = new CheckBox(context, C0859R.drawable.round_check2);
        this.checkBox.setVisibility(4);
        View view3 = this.checkBox;
        if (!LocaleController.isRTL) {
            i4 = 3;
        }
        addView(view3, LayoutHelper.createFrame(22, 22.0f, i4 | 48, LocaleController.isRTL ? 0.0f : (float) (padding + 37), 38.0f, LocaleController.isRTL ? (float) (padding + 37) : 0.0f, 0.0f));
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(104.0f), C0927C.ENCODING_PCM_32BIT));
    }

    public void setChecked(boolean z, boolean z2) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(z, z2);
    }

    public void setData(OperationModel model) {
        if (model == null) {
            this.nameTextView.setText(TtmlNode.ANONYMOUS_REGION_ID);
            this.avatarImageView.setImageDrawable(null);
        }
        this.mUser = MessagesController.getInstance().getUser(Integer.valueOf(model.getUser()));
        this.mOperation = model.getOperation();
        this.mDate = String.valueOf(model.getDate());
        update();
    }

    public void update() {
        if (this.mUser != null) {
            TLObject tLObject = null;
            if (this.mUser.photo != null) {
                tLObject = this.mUser.photo.photo_small;
            }
            this.avatarDrawable.setInfo(this.mUser);
            this.avatarImageView.setImage(tLObject, "50_50", this.avatarDrawable);
            this.nameTextView.setText(ContactsController.formatName(this.mUser.first_name, this.mUser.last_name));
            this.operationTextView.setText(this.mOperation);
            this.operationTextView.setTextColor(this.oldValueColor);
            Date date = new Date(this.mDate);
            String stringDate = getStringDate(date);
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            this.dateTextView.setText(m24a(instance.get(11), 2) + ":" + m24a(instance.get(12), 2));
        }
    }

    public static String getStringDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new SolarCalendar(cal).getShortDesDate();
    }

    public static String m24a(int var0, int var1) {
        char[] var2 = new char[var1];
        Arrays.fill(var2, '0');
        return new DecimalFormat(String.valueOf(var2)).format((long) var0);
    }
}
