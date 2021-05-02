package com.ammar.tawseel.netWorke;


import com.ammar.tawseel.pojo.response.APIResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIInterFace {


    @FormUrlEncoded
    @POST("api/user/login")
    Call<APIResponse.ResponseLogin> loginAPI(
            @Field("username") String email,
            @Field("password") String password,
            @Field("device_type") String device_typ,
            @Field("fcm_token") String fcm_token

    );


    @FormUrlEncoded
    @POST("api/user/register")
    Call<APIResponse.ResponseRegister> registerAPI(
            @Header("lang") String lang,
            @Field("username") String username,
            @Field("password") String password
            , @Field("email") String email,
            @Field("device_type") String device_typ,
            @Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST("api/user/social-login/{typ}")
    Call<APIResponse.ResponseLogin> registerSocialGoogle(
            @Path("typ") String typ,
            @Field("email") String email,
            @Field("id") String id,
            @Field("name") String username,
            @Field("device_type") String device_typ,
            @Field("fcm_token") String fcm_token);


//    @FormUrlEncoded
//    @POST("api/user/social-login/facebook")
//    Call<APIResponse.ResponseLogin> registerSocialFacebook(
//            @Field("email") String email,
//            @Field("id") String id,
//            @Field("name") String username,
//            @Field("device_type") String device_typ,
//            @Field("fcm_token") String fcm_token);
//

    @FormUrlEncoded
    @POST("api/user/profile")
    Call<APIResponse.ResponseProfile> editProfile(

            @Field("gpsLat") String gpsLat,
            @Field("gpsLng") String gpsLng,
            @Field("gpsAddress") String gpsAddress,
            @Field("name") String name,
            @Field("phone") String phone
            , @Field("avatar") String avatar,
            @Header("Accept") String Accept,
            @Header("Authorization") String token);


    @FormUrlEncoded
    @POST("api/user/send-message")
    Call<APIResponse.ResponseSendMessage> sendFirstMessageApi(
            @Field("to") String to,
            @Field("type") String type,

            @Field("message") String message,
            @Field("first") String first,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @FormUrlEncoded
    @POST("api/user/send-message")
    Call<APIResponse.ResponseSendMessage> sendMessageApi(
            @Field("to") String to,
            @Field("type") String type,
            @Field("message") String message,
            @Field("first") boolean first,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @FormUrlEncoded
    @POST("api/user/send-message")
    Call<APIResponse.ResponseSendMessage> sendTwoMessageApi(
            @Field("to") String to,
            @Field("type") String type,
            @Field("order_id") String order_id,
            @Field("message") String message,
            @Field("first") boolean first,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );


    @GET("api/user/chat/{idDirver}/{orderId}?")
    Call<APIResponse.ResponseChatBetween> chatBetweenUser(

            @Path("idDirver") String idDirver,
            @Path("orderId") String order_id,
            @Query("page") String id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @GET("api/user/profile")
    Call<APIResponse.ResponseShowProfile> showProfile(
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );


    @GET("api/user/profile")
    Call<APIResponse.ResponseProfile> getProfile(
            @Header("Authorization") String token,
            @Header("lang") String lang,

            @Header("Accept") String accept);

    @GET("api/user/drivers")
    Call<APIResponse.ResponseDrivers> getDrivers(
            @Header("Authorization") String token,
            @Header("lang") String lang,
            @Header("Accept") String accept);

    @GET("api/user/current-orders?")
    Call<APIResponse.ResponseCurrentOrder> currentOrder(
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );
    @GET("api/user/finished-orders?page=1")
    Call<APIResponse.ResponseFinshedOrder> finishedOrders(
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @GET("api/user/notifications?")
    Call<APIResponse.ResponseNotification> getNotification(
            @Query("page") String page,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );


    @GET("api/user/drivers-chat?")
    Call<APIResponse.ResponseMessages> getMessages(
            @Query("page") String page,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

}
