package org.telegram.ui.tools;

import android.content.SharedPreferences;
import java.util.ArrayList;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.User;

public class DialogsLoader {
    ArrayList<TL_dialog> allDialogs;
    boolean chatUnlocked;
    int defaulTab;
    ArrayList<TL_dialog> dialogs;
    boolean isTabsEnabled;
    SharedPreferences preferences;
    int selectedTab;

    public DialogsLoader() {
        this.preferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
        this.chatUnlocked = this.preferences.getBoolean("chat_unlocked", false);
        this.isTabsEnabled = this.preferences.getBoolean("tabs", true);
        this.defaulTab = this.preferences.getInt("defaul_tab", 5);
        this.selectedTab = this.preferences.getInt("selected_tab", this.defaulTab);
        this.allDialogs = new ArrayList();
        this.dialogs = new ArrayList();
    }

    public ArrayList<TL_dialog> getDialogsArray() {
        this.allDialogs.addAll(MessagesController.getInstance().dialogs);
        int i;
        TL_dialog dialog;
        int lower_id;
        int high_id;
        boolean isChat;
        User user;
        boolean isBot;
        if (this.chatUnlocked) {
            if (!this.isTabsEnabled) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                        this.dialogs.add(dialog);
                    }
                }
                return this.dialogs;
            } else if (this.selectedTab == 7) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (dialog.unread_count > 0 && this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                        this.dialogs.add(dialog);
                    }
                }
                return this.dialogs;
            } else if (this.selectedTab == 6) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                        this.dialogs.add(dialog);
                    }
                }
                return this.dialogs;
            } else if (this.selectedTab == 5) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (this.preferences.contains("hide_" + String.valueOf(dialog.id)) && this.preferences.contains("fav_" + String.valueOf(dialog.id))) {
                        this.dialogs.add(dialog);
                    }
                }
                return this.dialogs;
            } else if (this.selectedTab == 4) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                        lower_id = (int) dialog.id;
                        high_id = (int) (dialog.id >> 32);
                        if (!DialogObject.isChannel(dialog)) {
                            if (lower_id >= 0 || high_id == 1) {
                                isChat = false;
                            } else {
                                isChat = true;
                            }
                            if (!(isChat || lower_id <= 0 || high_id == 1)) {
                                user = MessagesController.getInstance().getUser(Integer.valueOf(lower_id));
                                if (user == null || !user.bot) {
                                    isBot = false;
                                } else {
                                    isBot = true;
                                }
                                if (!isBot) {
                                    this.dialogs.add(dialog);
                                }
                            }
                        }
                    }
                }
                return this.dialogs;
            } else if (this.selectedTab == 3) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                        high_id = (int) (dialog.id >> 32);
                        if (((int) dialog.id) >= 0 || high_id == 1) {
                            isChat = false;
                        } else {
                            isChat = true;
                        }
                        if (!DialogObject.isChannel(dialog) && isChat) {
                            this.dialogs.add(dialog);
                        }
                    }
                }
                return this.dialogs;
            } else if (this.selectedTab == 2) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                        lower_id = (int) dialog.id;
                        if (DialogObject.isChannel(dialog) && MessagesController.getInstance().getChat(Integer.valueOf(-lower_id)).megagroup) {
                            this.dialogs.add(dialog);
                        }
                    }
                }
                return this.dialogs;
            } else if (this.selectedTab == 1) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                        lower_id = (int) dialog.id;
                        if (DialogObject.isChannel(dialog) && !MessagesController.getInstance().getChat(Integer.valueOf(-lower_id)).megagroup) {
                            this.dialogs.add(dialog);
                        }
                    }
                }
                return this.dialogs;
            } else if (this.selectedTab == 0) {
                for (i = 0; i < this.allDialogs.size(); i++) {
                    dialog = (TL_dialog) this.allDialogs.get(i);
                    if (this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                        lower_id = (int) dialog.id;
                        high_id = (int) (dialog.id >> 32);
                        if (!DialogObject.isChannel(dialog)) {
                            if (lower_id >= 0 || high_id == 1) {
                                isChat = false;
                            } else {
                                isChat = true;
                            }
                            user = null;
                            if (!(isChat || lower_id <= 0 || high_id == 1)) {
                                user = MessagesController.getInstance().getUser(Integer.valueOf(lower_id));
                            }
                            if (user == null || !user.bot) {
                                isBot = false;
                            } else {
                                isBot = true;
                            }
                            if (isBot) {
                                this.dialogs.add(dialog);
                            }
                        }
                    }
                }
                return this.dialogs;
            }
        } else if (!this.isTabsEnabled) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                    this.dialogs.add(dialog);
                }
            }
            return this.dialogs;
        } else if (this.selectedTab == 7) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id)) && dialog.unread_count > 0) {
                    this.dialogs.add(dialog);
                }
            }
            return this.dialogs;
        } else if (this.selectedTab == 6) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                    this.dialogs.add(dialog);
                }
            }
            return this.dialogs;
        } else if (this.selectedTab == 5) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id)) && this.preferences.contains("fav_" + String.valueOf(dialog.id))) {
                    this.dialogs.add(dialog);
                }
            }
            return this.dialogs;
        } else if (this.selectedTab == 4) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                    lower_id = (int) dialog.id;
                    high_id = (int) (dialog.id >> 32);
                    if (!DialogObject.isChannel(dialog)) {
                        if (lower_id >= 0 || high_id == 1) {
                            isChat = false;
                        } else {
                            isChat = true;
                        }
                        if (!(isChat || lower_id <= 0 || high_id == 1)) {
                            user = MessagesController.getInstance().getUser(Integer.valueOf(lower_id));
                            if (user == null || !user.bot) {
                                isBot = false;
                            } else {
                                isBot = true;
                            }
                            if (!isBot) {
                                this.dialogs.add(dialog);
                            }
                        }
                    }
                }
            }
            return this.dialogs;
        } else if (this.selectedTab == 3) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                    high_id = (int) (dialog.id >> 32);
                    if (((int) dialog.id) >= 0 || high_id == 1) {
                        isChat = false;
                    } else {
                        isChat = true;
                    }
                    if (!DialogObject.isChannel(dialog) && isChat) {
                        this.dialogs.add(dialog);
                    }
                }
            }
            return this.dialogs;
        } else if (this.selectedTab == 2) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                    lower_id = (int) dialog.id;
                    if (DialogObject.isChannel(dialog) && MessagesController.getInstance().getChat(Integer.valueOf(-lower_id)).megagroup) {
                        this.dialogs.add(dialog);
                    }
                }
            }
            return this.dialogs;
        } else if (this.selectedTab == 1) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                    lower_id = (int) dialog.id;
                    if (DialogObject.isChannel(dialog) && !MessagesController.getInstance().getChat(Integer.valueOf(-lower_id)).megagroup) {
                        this.dialogs.add(dialog);
                    }
                }
            }
            return this.dialogs;
        } else if (this.selectedTab == 0) {
            for (i = 0; i < this.allDialogs.size(); i++) {
                dialog = (TL_dialog) this.allDialogs.get(i);
                if (!this.preferences.contains("hide_" + String.valueOf(dialog.id))) {
                    lower_id = (int) dialog.id;
                    high_id = (int) (dialog.id >> 32);
                    if (!DialogObject.isChannel(dialog)) {
                        if (lower_id >= 0 || high_id == 1) {
                            isChat = false;
                        } else {
                            isChat = true;
                        }
                        user = null;
                        if (!(isChat || lower_id <= 0 || high_id == 1)) {
                            user = MessagesController.getInstance().getUser(Integer.valueOf(lower_id));
                        }
                        if (user == null || !user.bot) {
                            isBot = false;
                        } else {
                            isBot = true;
                        }
                        if (isBot) {
                            this.dialogs.add(dialog);
                        }
                    }
                }
            }
            return this.dialogs;
        }
        return null;
    }
}
