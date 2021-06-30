package com.ammar.tawseel.netWorke.RX;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ammar.tawseel.pojo.response.APIResponse;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class ViewModelRX extends ViewModel {
    //products
    public MutableLiveData<APIResponse.ResponseDrivers> listProductsHome = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLogin = new MutableLiveData<>();


    public void getDrivers(String token, String lang, String accept) {

        Observable observable = APIClintRX.getINSTANCE().getHomeData(token, lang, accept)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        Observer<APIResponse.ResponseDrivers> observer = new Observer<APIResponse.ResponseDrivers>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull APIResponse.ResponseDrivers responseDrivers) {

                if (responseDrivers.getStauts())
                    listProductsHome.setValue(responseDrivers);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("errrrrrrrrrrr", "onError: "+((HttpException) e).code());
                if (((HttpException) e).code()==401) {
                    isLogin.setValue(true);
                }


            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribe(observer);


    }


}
