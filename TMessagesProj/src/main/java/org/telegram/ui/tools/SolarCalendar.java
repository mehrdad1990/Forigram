package org.telegram.ui.tools;

import java.util.Calendar;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.C0859R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.exoplayer.upstream.NetworkLock;
import org.telegram.messenger.volley.Request.Method;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.VideoPlayer;

public class SolarCalendar {
    private Calendar calendar;
    private int date;
    private int month;
    private int weekDay;
    private int year;

    public SolarCalendar() {
        this.calendar = Calendar.getInstance();
        calSolarCalendar();
    }

    public SolarCalendar(Calendar calendar) {
        this.calendar = calendar;
        calSolarCalendar();
    }

    private void calSolarCalendar() {
        int georgianYear = this.calendar.get(1);
        int georgianMonth = this.calendar.get(2) + 1;
        int georgianDate = this.calendar.get(5);
        this.weekDay = this.calendar.get(7) - 1;
        int[] buf1 = new int[]{0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        int[] buf2 = new int[]{0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
        int ld;
        if (georgianYear % 4 != 0) {
            this.date = buf1[georgianMonth - 1] + georgianDate;
            if (this.date > 79) {
                this.date -= 79;
                if (this.date <= 186) {
                    switch (this.date % 31) {
                        case VideoPlayer.TRACK_DEFAULT /*0*/:
                            this.month = this.date / 31;
                            this.date = 31;
                            break;
                        default:
                            this.month = (this.date / 31) + 1;
                            this.date %= 31;
                            break;
                    }
                    this.year = georgianYear - 621;
                    return;
                }
                this.date -= 186;
                switch (this.date % 30) {
                    case VideoPlayer.TRACK_DEFAULT /*0*/:
                        this.month = (this.date / 30) + 6;
                        this.date = 30;
                        break;
                    default:
                        this.month = (this.date / 30) + 7;
                        this.date %= 30;
                        break;
                }
                this.year = georgianYear - 621;
                return;
            }
            if (georgianYear <= 1996 || georgianYear % 4 != 1) {
                ld = 10;
            } else {
                ld = 11;
            }
            this.date += ld;
            switch (this.date % 30) {
                case VideoPlayer.TRACK_DEFAULT /*0*/:
                    this.month = (this.date / 30) + 9;
                    this.date = 30;
                    break;
                default:
                    this.month = (this.date / 30) + 10;
                    this.date %= 30;
                    break;
            }
            this.year = georgianYear - 622;
            return;
        }
        this.date = buf2[georgianMonth - 1] + georgianDate;
        if (georgianYear >= 1996) {
            ld = 79;
        } else {
            ld = 80;
        }
        if (this.date > ld) {
            this.date -= ld;
            if (this.date <= 186) {
                switch (this.date % 31) {
                    case VideoPlayer.TRACK_DEFAULT /*0*/:
                        this.month = this.date / 31;
                        this.date = 31;
                        break;
                    default:
                        this.month = (this.date / 31) + 1;
                        this.date %= 31;
                        break;
                }
                this.year = georgianYear - 621;
                return;
            }
            this.date -= 186;
            switch (this.date % 30) {
                case VideoPlayer.TRACK_DEFAULT /*0*/:
                    this.month = (this.date / 30) + 6;
                    this.date = 30;
                    break;
                default:
                    this.month = (this.date / 30) + 7;
                    this.date %= 30;
                    break;
            }
            this.year = georgianYear - 621;
            return;
        }
        this.date += 10;
        switch (this.date % 30) {
            case VideoPlayer.TRACK_DEFAULT /*0*/:
                this.month = (this.date / 30) + 9;
                this.date = 30;
                break;
            default:
                this.month = (this.date / 30) + 10;
                this.date %= 30;
                break;
        }
        this.year = georgianYear - 622;
    }

    public String getWeekDay() {
        String strWeekDay = TtmlNode.ANONYMOUS_REGION_ID;
        switch (this.weekDay) {
            case VideoPlayer.TRACK_DEFAULT /*0*/:
                return LocaleController.getString("Sunday", C0859R.string.Sunday);
            case VideoPlayer.TYPE_AUDIO /*1*/:
                return LocaleController.getString("Monday", C0859R.string.Monday);
            case VideoPlayer.STATE_PREPARING /*2*/:
                return LocaleController.getString("Tuesday", C0859R.string.Tuesday);
            case VideoPlayer.STATE_BUFFERING /*3*/:
                return LocaleController.getString("Wednesday", C0859R.string.Wednesday);
            case VideoPlayer.STATE_READY /*4*/:
                return LocaleController.getString("Thursday", C0859R.string.Thursday);
            case VideoPlayer.STATE_ENDED /*5*/:
                return LocaleController.getString("Friday", C0859R.string.Friday);
            case Method.TRACE /*6*/:
                return LocaleController.getString("Saturday", C0859R.string.Saturday);
            default:
                return strWeekDay;
        }
    }

    public String getMonth() {
        String strMonth = TtmlNode.ANONYMOUS_REGION_ID;
        switch (this.month) {
            case VideoPlayer.TYPE_AUDIO /*1*/:
                return LocaleController.getString("Farvardin", C0859R.string.Farvardin);
            case VideoPlayer.STATE_PREPARING /*2*/:
                return LocaleController.getString("Ordibehesht", C0859R.string.Ordibehesht);
            case VideoPlayer.STATE_BUFFERING /*3*/:
                return LocaleController.getString("Khordad", C0859R.string.Khordad);
            case VideoPlayer.STATE_READY /*4*/:
                return LocaleController.getString("Tir", C0859R.string.Tir);
            case VideoPlayer.STATE_ENDED /*5*/:
                return LocaleController.getString("Mordad", C0859R.string.Mordad);
            case Method.TRACE /*6*/:
                return LocaleController.getString("Shahrivar", C0859R.string.Shahrivar);
            case Method.PATCH /*7*/:
                return LocaleController.getString("Mehr", C0859R.string.Mehr);
            case TLRPC.USER_FLAG_USERNAME /*8*/:
                return LocaleController.getString("Aban", C0859R.string.Aban);
            case C0859R.styleable.YearPicker_dp_yearMax /*9*/:
                return LocaleController.getString("Azar", C0859R.string.Azar);
            case NetworkLock.DOWNLOAD_PRIORITY /*10*/:
                return LocaleController.getString("Dey", C0859R.string.Dey);
            case C0859R.styleable.YearPicker_dp_yearTextSize /*11*/:
                return LocaleController.getString("Bahman", C0859R.string.Bahman);
            case Atom.FULL_HEADER_SIZE /*12*/:
                return LocaleController.getString("Esfand", C0859R.string.Esfand);
            default:
                return strMonth;
        }
    }

    public String getDesDate() {
        StringBuilder describedDateFormat = new StringBuilder();
        describedDateFormat.append(String.valueOf(this.date)).append(" ").append(getMonth()).append(" ").append(String.valueOf(this.year)).append(" ").append(LocaleController.getString("Saat", C0859R.string.Saat)).append(" ").append(getTime());
        return String.valueOf(describedDateFormat);
    }

    public String getShortDesDateTime() {
        StringBuilder describedDateFormat = new StringBuilder();
        describedDateFormat.append(String.valueOf(this.date)).append(" ").append(getMonth()).append(" ").append(LocaleController.getString("Saat", C0859R.string.Saat)).append(" ").append(getTime());
        return String.valueOf(describedDateFormat);
    }

    public String getShortDesDate() {
        StringBuilder describedDateFormat = new StringBuilder();
        describedDateFormat.append(String.valueOf(this.date)).append(" ").append(getMonth()).append(" ");
        return String.valueOf(describedDateFormat);
    }

    public String getNumDateTime() {
        StringBuilder numericDateFormat = new StringBuilder();
        numericDateFormat.append(String.valueOf(this.year)).append("/").append(String.valueOf(this.month)).append("/").append(String.valueOf(this.date)).append(" ").append(LocaleController.getString("Saat", C0859R.string.Saat)).append(" ").append(getTime());
        return String.valueOf(numericDateFormat);
    }

    public String getNumDate() {
        StringBuilder numericDateFormat = new StringBuilder();
        numericDateFormat.append(String.valueOf(this.year)).append("/").append(String.valueOf(this.month)).append("/").append(String.valueOf(this.date)).append(" ");
        return String.valueOf(numericDateFormat);
    }

    public String getTime() {
        boolean is24HourFormat = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0).getBoolean("enable24HourFormat", false);
        int h = this.calendar.get(11);
        int m = this.calendar.get(12);
        StringBuilder time = new StringBuilder();
        if (is24HourFormat) {
            time.append(h).append(":").append(m);
        } else {
            int i = h < 12 ? h : h == 12 ? 12 : h - 12;
            time.append(i).append(":").append(m < 10 ? "0" + m : Integer.valueOf(m)).append(h < 12 ? " " + LocaleController.getString("AM", C0859R.string.AM) : " " + LocaleController.getString("PM", C0859R.string.PM));
        }
        return String.valueOf(time);
    }

    public long getTimeInMillis() {
        return this.calendar.getTimeInMillis();
    }

    public String toString() {
        return getDesDate();
    }

    public static void main(String[] args) {
    }
}
