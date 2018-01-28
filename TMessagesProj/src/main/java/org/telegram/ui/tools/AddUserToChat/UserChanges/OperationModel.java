package org.telegram.ui.tools.AddUserToChat.UserChanges;

public class OperationModel {
    private int date;
    private String operation;
    private int userId;

    public OperationModel(int uid, String op, int d) {
        this.userId = uid;
        this.operation = op;
        this.date = d;
    }

    public int getUser() {
        return this.userId;
    }

    public String getOperation() {
        return this.operation;
    }

    public int getDate() {
        return this.date;
    }
}
