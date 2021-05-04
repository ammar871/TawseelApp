package com.ammar.tawseel.pojo.response;

import android.service.autofill.UserData;

import com.ammar.tawseel.pojo.data.DataChat;
import com.ammar.tawseel.pojo.data.DataFatora;
import com.ammar.tawseel.pojo.data.DataFinshOrder;
import com.ammar.tawseel.pojo.data.DataMessags;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.pojo.data.DataProfile;
import com.ammar.tawseel.pojo.data.DataUser;
import com.ammar.tawseel.pojo.data.Driver;
import com.ammar.tawseel.pojo.data.Rating;
import com.ammar.tawseel.pojo.data.SendMessage;
import com.ammar.tawseel.pojo.data.UserRegister;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResponse {

 public  class ResponseLogin{

     @SerializedName("status")
     @Expose
     private Boolean status;
     @SerializedName("token")
     @Expose
     private String token;
     @SerializedName("user")
     @Expose
     private DataUser user;
     @SerializedName("message")
     @Expose
     private List<String> message;

     public List<String> getMessage() {
         return message;
     }

     public void setMessage(List<String> message) {
         this.message = message;
     }


     public Boolean getStatus() {
         return status;
     }

     public void setStatus(Boolean status) {
         this.status = status;
     }

     public String getToken() {
         return token;
     }

     public void setToken(String token) {
         this.token = token;
     }

     public DataUser getUser() {
         return user;
     }

     public void setUser(DataUser user) {
         this.user = user;
     }


 }

    public  class ResponseRegister{

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("data")
        @Expose
        private UserRegister data;
        @SerializedName("token")
        @Expose
        private String token;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

        public UserRegister getData() {
            return data;
        }

        public void setData(UserRegister data) {
            this.data = data;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


    }



    public  class ResponseProfile {



        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private DataUser data;

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public DataUser getData() {
            return data;
        }

        public void setData(DataUser data) {
            this.data = data;
        }

    }

    public  class ResponseDrivers {


        @SerializedName("data")
        @Expose
        private List<Driver> data = null;
        @SerializedName("stauts")
        @Expose
        private Boolean stauts;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public List<Driver> getData() {
            return data;
        }

        public void setData(List<Driver> data) {
            this.data = data;
        }

        public Boolean getStauts() {
            return stauts;
        }

        public void setStauts(Boolean stauts) {
            this.stauts = stauts;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

}

    public  class ResponseSendMessage {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private SendMessage data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public SendMessage getData() {
            return data;
        }

        public void setData(SendMessage data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

 }
    public  class ResponseChatBetween {
        @SerializedName("data")
        @Expose
        private DataChat data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public DataChat getData() {
            return data;
        }

        public void setData(DataChat data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }
    public  class ResponseShowProfile {
        @SerializedName("data")
        @Expose
        private DataProfile data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public DataProfile getData() {
            return data;
        }

        public void setData(DataProfile data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }

    public  class ResponseCurrentOrder {
        @SerializedName("data")
        @Expose
        private List<DataOrder> data = null;
        @SerializedName("stauts")
        @Expose
        private Boolean stauts;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public List<DataOrder> getData() {
            return data;
        }

        public void setData(List<DataOrder> data) {
            this.data = data;
        }

        public Boolean getStauts() {
            return stauts;
        }

        public void setStauts(Boolean stauts) {
            this.stauts = stauts;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }
    }

    public  class ResponseFinshedOrder {
        @SerializedName("data")
        @Expose
        private List<DataFinshOrder> data = null;
        @SerializedName("stauts")
        @Expose
        private Boolean stauts;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public List<DataFinshOrder> getData() {
            return data;
        }

        public void setData(List<DataFinshOrder> data) {
            this.data = data;
        }

        public Boolean getStauts() {
            return stauts;
        }

        public void setStauts(Boolean stauts) {
            this.stauts = stauts;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }
    }


    public  class ResponseNotification {
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<DataNotification> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<DataNotification> getData() {
            return data;
        }

        public void setData(List<DataNotification> data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }
    }


    public  class ResponseMessages {
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<DataMessags> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<DataMessags> getData() {
            return data;
        }

        public void setData(List<DataMessags> data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }
    }

    public  class ResponseFatora {

        @SerializedName("data")
        @Expose
        private List<DataFatora> data = null;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public List<DataFatora> getData() {
            return data;
        }

        public void setData(List<DataFatora> data) {
            this.data = data;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }
    }


    public  class ResponseRating {

        @SerializedName("ratings")
        @Expose
        private List<Rating> ratings = null;

        public List<Rating> getRatings() {
            return ratings;
        }

        public void setRatings(List<Rating> ratings) {
            this.ratings = ratings;
        }
    }

}
