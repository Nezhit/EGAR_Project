package com.example.EgarProject.exceptions;

public class RoleException extends RuntimeException{
    private int errorCode;
    private String errorMessage;
   public RoleException(int errorCode){
        super();
        this.errorCode=errorCode;

        switch (errorCode){
            case 1:
                this.errorMessage="Роль User не найдена";
                break;
            case 2:
                this.errorMessage="Роль Admin не найдена";
                break;
            case 3:
                this.errorMessage="Роль Moderator не найдена";
                break;
            default:
                this.errorMessage="Проблема с системой ролей";
                break;
        }
    }
    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
