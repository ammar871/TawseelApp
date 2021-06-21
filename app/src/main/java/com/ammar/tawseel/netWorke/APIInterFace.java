package com.ammar.tawseel.netWorke;


import com.ammar.tawseel.pojo.response.APIResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    @POST("api/user/profile")
    @Multipart
    Call<APIResponse.ResponseProfile> editProfile(

            @Part("gpsLat") double gpsLat,
            @Part("gpsLng") double gpsLng,
            @Part("gpsAddress") String gpsAddress,
            @Part("name") RequestBody name,
            @Part("phone") RequestBody phone
            ,@Part  MultipartBody.Part avatar,
            @Header("Accept") String Accept,
            @Header("Authorization") String token);





    @FormUrlEncoded
    @POST("api/user/chat/{driverId}//?")
    Call<APIResponse.ResponseChatBetween> chatBetweenOneMessage(
            @Path("driverId") String driverId,
            @Field("page") String page,
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
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @FormUrlEncoded
    @POST("api/user/send-message")
    Call<APIResponse.ResponseSendMessage> sendFirstMessageApi(
            @Field("to") String to,
            @Field("type") String type,
            @Field("message") String message,
            @Field("first") boolean first,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @Multipart
    @POST("api/user/send-message")
    Call<APIResponse.ResponseSendMessage> sendFileMessage(
            @Part("to") int to,
            @Part("type") RequestBody type,
            @Part("order_id") int order_id,
            @Part MultipartBody.Part  message,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );
    @Multipart
    @POST("api/user/send-message")
    Call<APIResponse.ResponseSendMessage> sendFileFirstMessage(
            @Part("to") int to,
            @Part("type") RequestBody type,
            @Part MultipartBody.Part  message,
            @Part("first") boolean first,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @GET("api/user/chat/{idDirver}/{orderId}?")
    Call<APIResponse.ResponseChatBetween> chatBetweenUser(

            @Path("idDirver") String idDirver,
            @Path("orderId") String order_id,
            @Query("page") String page,
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
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Header("Authorization") String token,
            @Header("lang") String lang,
            @Header("Accept") String accept);

    @GET("api/user/current-orders?")
    Call<APIResponse.ResponseCurrentOrder> currentOrder(
            @Query("page") String page,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @GET("api/user/finished-orders?")
    Call<APIResponse.ResponseFinshedOrder> finishedOrders(
            @Query("page") String page,
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


    @GET("api/user/paid-bills?")
    Call<APIResponse.ResponseFatora> getPaid_bills(
            @Query("page") String page,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @GET("api/user/canceled-bills?")
    Call<APIResponse.ResponseFatora> getCanceled_bills(
            @Query("page") String page,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @GET("api/user/ratings?")
    Call<APIResponse.ResponseRating> getRating(
            @Query("page") String page,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @FormUrlEncoded
    @POST("api/user/add-rate")
    Call<APIResponse.AddRateResponse> addRating(
            @Field("to") String to,
            @Field("text") String type,
            @Field("stars") String order_id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @FormUrlEncoded
    @POST("api/user/update-rate/{id}")
    Call<APIResponse.ResponseUpdateRating> upDateRating(

            @Field("to") String to,
            @Field("text") String text,
            @Field("stars") String stars,
            @Path("id") String id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );





    @GET("api/user/order/{id}")
    Call<APIResponse.ResponseShowOrder> showOrder(
            @Path("id") String id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @GET("api/user/bill/{id}")
    Call<APIResponse.ResponseShowBill> showBill(
            @Path("id") String id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @POST("api/user/delete-bill/{id}")
    Call<APIResponse.CanceleResponse> deleteBill(
            @Path("id") String id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );
    @POST("api/user/delete-chat/{idCaht}")
    Call<APIResponse.ResponseDeleteChat> deleteChat(

            @Path("idCaht") String to,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );


    @FormUrlEncoded
    @POST("api/user/search")
    Call<APIResponse.ResponseSearchDrivers> searshDrivires(
            @Field("name") String name,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @FormUrlEncoded
    @POST("api/user/filter-drivers")
    Call<APIResponse.ResponseFillter> filtersDrivires(
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("stars") int stars,
            @Field("kilometers") String kilometers,
            @Field("inside") boolean inside,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );


    @GET("api/who-us")
    Call<APIResponse.WhoUsResponse> WhoUs(
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @FormUrlEncoded
    @POST("api/contact-us")
    Call<APIResponse.ContactResponse> sendContactus(
            @Field("email") String email,
            @Field("content") String content,
            @Field("name") String name,
            @Field("user_type") String user_type,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @GET("api/whatsapp-number")
    Call<APIResponse.NumberWhatsResponse> numberWhatsResponse(
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );


    @GET("api/policies")
    Call<APIResponse.PoeloyProxyResponse> poeloyProxyResponse(
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );


    @POST("api/user/delete-notification/{idCaht}")
    Call<APIResponse.ResponseDeleteChat> deleteNotifecatio(

            @Path("idCaht") String to,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );
    @POST("api/user/confirm-deliverd-order/{id}")
    Call<APIResponse.ConfirmResponse> confirmOrder(
            @Path("id") String id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @POST("api/user/cancel-deliverd-order/{id}")
    Call<APIResponse.CanceleResponse> cancelOrder(
            @Path("id") String id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );


    @FormUrlEncoded
    @POST("api/user/accept-order/{id}")
    Call<APIResponse.PaidOrderResponse> finshOpreatoor(
            @Path("id") String id,
            @Field("payment_method") String payment_method,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @POST("api/user/cancel-order/{id}")
    Call<APIResponse.PaidOrderResponse> cancelOpreatoor(
            @Path("id") String id,
            @Header("Accept") String Accept,
            @Header("Authorization") String token,
            @Header("Accept-Language") String lang
    );

    @FormUrlEncoded
    @POST("api/map/static-image?")
        Call<ResponseBody> getPhoto(
            @Field("lat") String lat,
            @Field("lng") String lng

    );
}
