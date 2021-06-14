package com.ammar.tawseel.notifecation;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageNotiy implements Parcelable {
private String from,msg_to,readed,reciver_type,type,sender_type,message,order_id;

    protected MessageNotiy(Parcel in) {
        from = in.readString();
        msg_to = in.readString();
        readed = in.readString();
        reciver_type = in.readString();
        type = in.readString();
        sender_type = in.readString();
        message = in.readString();
        order_id = in.readString();
    }

    public static final Creator<MessageNotiy> CREATOR = new Creator<MessageNotiy>() {
        @Override
        public MessageNotiy createFromParcel(Parcel in) {
            return new MessageNotiy(in);
        }

        @Override
        public MessageNotiy[] newArray(int size) {
            return new MessageNotiy[size];
        }
    };

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public MessageNotiy() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg_to() {
        return msg_to;
    }

    public void setMsg_to(String msg_to) {
        this.msg_to = msg_to;
    }

    public String getReaded() {
        return readed;
    }

    public void setReaded(String readed) {
        this.readed = readed;
    }

    public String getReciver_type() {
        return reciver_type;
    }

    public void setReciver_type(String reciver_type) {
        this.reciver_type = reciver_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(from);
        dest.writeString(msg_to);
        dest.writeString(readed);
        dest.writeString(reciver_type);
        dest.writeString(type);
        dest.writeString(sender_type);
        dest.writeString(message);
        dest.writeString(order_id);
    }
}
