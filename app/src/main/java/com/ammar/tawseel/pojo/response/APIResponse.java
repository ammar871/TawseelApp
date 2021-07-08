package com.ammar.tawseel.pojo.response;

import android.service.autofill.UserData;

import com.ammar.tawseel.pojo.data.DataBill;
import com.ammar.tawseel.pojo.data.DataChat;
import com.ammar.tawseel.pojo.data.DataConfirm;
import com.ammar.tawseel.pojo.data.DataFatora;
import com.ammar.tawseel.pojo.data.DataFinshOrder;
import com.ammar.tawseel.pojo.data.DataMessags;
import com.ammar.tawseel.pojo.data.DataNotification;
import com.ammar.tawseel.pojo.data.DataOrder;
import com.ammar.tawseel.pojo.data.DataProfile;
import com.ammar.tawseel.pojo.data.DataUser;
import com.ammar.tawseel.pojo.data.Driver;
import com.ammar.tawseel.pojo.data.DriverRate;
import com.ammar.tawseel.pojo.data.DriversSearch;
import com.ammar.tawseel.pojo.data.FiltterData;
import com.ammar.tawseel.pojo.data.Message;
import com.ammar.tawseel.pojo.data.Rating;
import com.ammar.tawseel.pojo.data.SendMessage;
import com.ammar.tawseel.pojo.data.ShowOrder;
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
    public class LogOutResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("data")
        @Expose
        private List<Object> data = null;

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

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
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
        private Message data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Message getData() {
            return data;
        }

        public void setData(Message data) {
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
    public  class ResponseUpdateRating {


        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("data")
        @Expose
        private List<Object> data = null;

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

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
        }
    }
    public  class ResponseShowOrder {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private ShowOrder data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public ShowOrder getData() {
            return data;
        }

        public void setData(ShowOrder data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }
    }

    public  class ResponseShowBill {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private DataBill data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public DataBill getData() {
            return data;
        }

        public void setData(DataBill data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }
    }

    public  class ResponseDeleteChat {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private List<Object> message = null;
    @SerializedName("data")
    @Expose
    private List<Object> data = null;

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

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }}


    public  class ResponseSearchDrivers {
        @SerializedName("data")
        @Expose
        private List<DriversSearch> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public List<DriversSearch> getData() {
            return data;
        }

        public void setData(List<DriversSearch> data) {
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


    public  class ResponseWho_us{
        @SerializedName("data")
        @Expose
        private List<String> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
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
    public  class ResponsePolicies{

        @SerializedName("data")
        @Expose
        private List<String> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
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
    public  class ResponseWhatsapp_number{

        @SerializedName("data")
        @Expose
        private String data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public String getData() {
            return data;
        }

        public void setData(String data) {
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


    public  class ResponseFillter{

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<FiltterData> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<FiltterData> getData() {
            return data;
        }

        public void setData(List<FiltterData> data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }
    }

    public class WhoUsResponse {

        @SerializedName("data")
        @Expose
        private List<String> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
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

    public class ContactResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("data")
        @Expose
        private List<Object> data = null;

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

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
        }

    }


    public class NumberWhatsResponse {

        @SerializedName("data")
        @Expose
        private String data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public String getData() {
            return data;
        }

        public void setData(String data) {
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

    public class PoeloyProxyResponse {

        @SerializedName("data")
        @Expose
        private List<String> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("status")
        @Expose
        private Boolean status;

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
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

    public class ConfirmResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private DataConfirm data;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public DataConfirm getData() {
            return data;
        }

        public void setData(DataConfirm data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }}




    public class AddRateResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<Object> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

    }

    public class CanceleResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<Object> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

    }



    public class PaidOrderResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<Object> data = null;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
        }

        public List<Object> getMessage() {
            return message;
        }

        public void setMessage(List<Object> message) {
            this.message = message;
        }

    }
    public class RestPassResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private List<Object> message = null;
        @SerializedName("data")
        @Expose
        private List<Object> data = null;

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

        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
        }

    }
}
