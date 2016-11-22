package com.wxdroid.basemodule.network;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskTicket implements Parcelable
{
    public int taskid;
    public String ticket;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskid);
        dest.writeString(this.ticket);
    }

    public TaskTicket() {
    }

    protected TaskTicket(Parcel in) {
        this.taskid = in.readInt();
        this.ticket = in.readString();
    }

    public static final Creator<TaskTicket> CREATOR = new Creator<TaskTicket>() {
        @Override
        public TaskTicket createFromParcel(Parcel source) {
            return new TaskTicket(source);
        }

        @Override
        public TaskTicket[] newArray(int size) {
            return new TaskTicket[size];
        }
    };

    @Override
    public String toString() {
        return "TaskTicket{" +
                "taskid=" + taskid +
                ", ticket='" + ticket + '\'' +
                '}';
    }
}
