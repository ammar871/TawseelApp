package com.ammar.tawseel.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterChatePage;
import com.ammar.tawseel.databinding.FragmentChatBinding;
import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.example.audio_record_view.AudioRecordView;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.Message;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;
import com.ammar.tawseel.uitllis.PathVideo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ChatFragment extends Fragment implements AdapterChatePage.OnclickMessageAudio, TextureView.SurfaceTextureListener {

    public ChatFragment() {

    }

    double current_pos, total_duration;

    int audio_index = 0;

    int page = 1;
    private Handler mHandler = new Handler();
    public static String fileName = null;
    private int audioTotalTime;
    private AudioRecordView audioRecordView;
    MediaRecorder recorder = null;
    ArrayList<Message> list = new ArrayList<>();
    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    FragmentChatBinding binding;
    String to = "";
    String orderId;
    boolean isFirst;
    AdapterChatePage adapterChatePage;
    String image;
    TimerTask timerTask;
    double lattuide, longtuide;
    protected LinearLayoutManager mLayoutManager;
    Timer timer;
    private SimpleDateFormat timeFormatter;
    private AudioManager mAudioManager;
    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file

    int PERMISSION_ID = 44;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ClickableViewAccessibility", "WrongConstant"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        shardEditor = new ShardEditor(getActivity());

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(getActivity(), shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + UUID.randomUUID().toString() + System.currentTimeMillis() + "_audio_record.3gp";
        checkPermation();
        inItView();

        Bundle bundle = this.getArguments();

        if (bundle != null) {

            to = getArguments().getString("to");
            orderId = getArguments().getString("orderId");
            Log.d("bundelsdata", "onCreateView: " + to + "\n " + orderId);
            if (orderId == null){
                oneChatBetween(page, to);
                hideKeyboard(getActivity());
            }

            else{
                getChatsBetween(page, to, orderId);
                hideKeyboard(getActivity());
            }


        }

//        binding.scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                if(scrollY == 0){
//                    binding.proBarPag.setVisibility(View.VISIBLE);
//                    if (list.size() > 0) {
//
//                        page++;
//
//
//                        getDataWithPagination(page);
//
//
//                    }else {
//                        binding.proBarPag.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(), "Not Data", Toast.LENGTH_SHORT).show();
//                    }
//                    Log.d("ProductFragment","top");
//                }
//
////                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
////                    binding.proBarPag.setVisibility(View.VISIBLE);
////                    if (list.size() > 0) {
////
////                        page++;
////
////
////                        getDataWithPagination(page);
////
////
////                    }else {
////                        binding.proBarPag.setVisibility(View.GONE);
////                        Toast.makeText(getActivity(), "Not Data", Toast.LENGTH_SHORT).show();
////                    }
////
////
////
////                }
//            }
//        });
        binding.toggls.setOnClickListener((View.OnClickListener) v -> {

            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.draw);
            drawer.openDrawer(Gravity.START);

        });

        binding.imgSend.setOnClickListener(v -> {
            if (!binding.editGchatMessage.getText().toString().isEmpty() ||
                    !binding.editGchatMessage.getText().toString().equals("")) {
                binding.proBar.setVisibility(View.VISIBLE);
                binding.editGchatMessage.setVisibility(View.GONE);
                binding.imgRecord.setEnabled(false);
                binding.imgSend.setEnabled(false);
                binding.imgFile.setEnabled(false);
                callSendTwoMessage(to, "text", binding.editGchatMessage.getText().toString());

                binding.editGchatMessage.setText("");

            } else {
                Toast.makeText(getActivity(), "An empty message cannot be sent", Toast.LENGTH_SHORT).show();
            }


        });

        binding.imgFile.setOnClickListener(v -> showDialogSelecteService());

        intItRecord();
        return binding.getRoot();
    }


    private void stopRecording() {
        //Stop Timer, very obvious


        //Change text on page to file saved


        //Stop media recorder and set it to null for further use to record new audio
        recorder.stop();
        recorder.release();
        recorder = null;

    }

    private void startRecording() {
        //Start timer from 0

        startTimer();
        //Setup Media Recorder for recording


        //Setup Media Recorder for recording
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Start Recording
        recorder.start();


    }

    private void inItView() {
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        binding.rvChat.setLayoutManager(mLayoutManager);
        binding.rvChat.setItemAnimator(itemAnimator);

        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());
    }

    private boolean checkPermation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkWriteExternalPermission()) {

                return true;

            }
        }
        return false;
    }

    private boolean checkWriteExternalPermission() {
        String permission1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String permission2 = android.Manifest.permission.RECORD_AUDIO;
        int res = getActivity().checkCallingOrSelfPermission(permission1);
        int res2 = getActivity().checkCallingOrSelfPermission(permission2);
        return (res == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void intItRecord() {

        binding.imgRecord.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                binding.editGchatMessage.setVisibility(View.GONE);
                binding.textRecorderMessage.setVisibility(View.VISIBLE);

                if (timer == null) {
                    timer = new Timer();
                    timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                }


                startRecording();
                return true;
            }
        });

        binding.imgRecord.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP: {
                        stopRecording();
                        timerTask.cancel();
                        binding.textRecorderMessage.setVisibility(View.GONE);
                        binding.proBar.setVisibility(View.VISIBLE);
                        binding.editGchatMessage.setVisibility(View.GONE);
                        binding.imgRecord.setEnabled(false);
                        binding.imgSend.setEnabled(false);
                        binding.imgFile.setEnabled(false);
                        //  binding.textRecorderMessage.setText(recordTime+"");
                        File file = new File(fileName);

                        RequestBody requestFile = RequestBody.create((MediaType.parse("multipart/form-data")),
                                file);
                        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("message", file.getName(), requestFile);
                        Log.d("ssssssssssssss", "onRecordingCompleted: " + to + "\n " + orderId);
                        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), "audio");
                        callSendFileTwoMessage(Integer.parseInt(to), usernameBody, multipartBody);
                        Log.d("fffffffffff", "onTouch: " + fileName);
                        return true; //indicate we're done listening to this touch listener
                    }

                }
                return false;
            }
        });
    }

    private void callSendFileTwoMessage(int id, RequestBody type, MultipartBody.Part message) {

        Call<APIResponse.ResponseSendMessage> call;
        if (isFirst) {

            call =
                    apiInterFace.sendFileMessage(id, type
                            , Integer.parseInt(orderId),
                            message

                            , "application/json",
                            "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)

                            , shardEditor.loadData().get(ShardEditor.KEY_LANG));

        } else {

            call =
                    apiInterFace.sendFileFirstMessage(id, type,
                            message
                            , true
                            , "application/json",
                            "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)

                            , shardEditor.loadData().get(ShardEditor.KEY_LANG));
        }


        call.enqueue(new Callback<APIResponse.ResponseSendMessage>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseSendMessage> call,
                                   @NonNull Response<APIResponse.ResponseSendMessage> response) {


                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        binding.proBar.setVisibility(View.GONE);
                        binding.editGchatMessage.setVisibility(View.VISIBLE);
                        binding.imgRecord.setEnabled(true);
                        binding.imgSend.setEnabled(true);
                        binding.imgFile.setEnabled(true);
                        isFirst = true;


                        Log.d("oooooooooo", "onResponse: " + response.body().getData().getType());

                        //   adapterChatePage = new AdapterChatePage(list, getActivity(), image);
                        adapterChatePage.refreshData(response.body().getData());
                        refreshAdpter(adapterChatePage, list);
                        adapterChatePage.notifyItemInserted(list.size());
                        binding.rvChat.smoothScrollToPosition(list.size());
                        binding.scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    } else {

                        Log.d("rrrrrrrrrrrrrrrr", "onResponse: " + response.body().getMessage().get(0));

                        binding.proBar.setVisibility(View.GONE);
                        binding.editGchatMessage.setVisibility(View.VISIBLE);
                        binding.imgRecord.setEnabled(true);
                        binding.imgSend.setEnabled(true);
                        binding.imgFile.setEnabled(true);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseSendMessage> call, @NonNull Throwable t) {
                Log.d("rrrrrrrrrrrrrrrr", "onResponse: " + t.getMessage());
                binding.proBar.setVisibility(View.GONE);
                binding.editGchatMessage.setVisibility(View.VISIBLE);
                binding.imgRecord.setEnabled(true);
                binding.imgSend.setEnabled(true);
                binding.imgFile.setEnabled(true);
            }
        });


    }

    private void callSendImageTwoMessage(int id, RequestBody type, MultipartBody.Part message) {

        Call<APIResponse.ResponseSendMessage> call;
        if (isFirst) {

            call =
                    apiInterFace.sendFileMessage(id, type
                            , Integer.parseInt(orderId),
                            message

                            , "application/json",
                            "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)

                            , shardEditor.loadData().get(ShardEditor.KEY_LANG));

        } else {

            call =
                    apiInterFace.sendFileFirstMessage(id, type,
                            message
                            , true
                            , "application/json",
                            "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)

                            , shardEditor.loadData().get(ShardEditor.KEY_LANG));
        }


        call.enqueue(new Callback<APIResponse.ResponseSendMessage>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseSendMessage> call,
                                   @NonNull Response<APIResponse.ResponseSendMessage> response) {


                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        binding.proBar.setVisibility(View.GONE);
                        binding.editGchatMessage.setVisibility(View.VISIBLE);
                        binding.imgRecord.setEnabled(true);
                        binding.imgSend.setEnabled(true);
                        binding.imgFile.setEnabled(true);
                        isFirst = true;

                        adapterChatePage.refreshData(response.body().getData());

                        Log.d("oooooooooo1", "onResponse: " + response.body().getData().getType());

                        //   adapterChatePage = new AdapterChatePage(list, getActivity(), image);
                        refreshAdpter(adapterChatePage, list);
                        adapterChatePage.notifyItemInserted(list.size());
                        binding.rvChat.smoothScrollToPosition(list.size());
                        binding.scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    } else {
                        Log.d("rrrrrrrrrrrrrrrr1", "onResponse: " + response.body().getMessage());
                        binding.proBar.setVisibility(View.GONE);
                        binding.editGchatMessage.setVisibility(View.VISIBLE);
                        binding.imgRecord.setEnabled(true);
                        binding.imgSend.setEnabled(true);
                        binding.imgFile.setEnabled(true);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseSendMessage> call, @NonNull Throwable t) {
                Log.d("rrrrrrrrrrrrrrrr", "onResponse: " + t.getMessage());
                binding.proBar.setVisibility(View.GONE);
                binding.editGchatMessage.setVisibility(View.VISIBLE);
                binding.imgRecord.setEnabled(true);
                binding.imgSend.setEnabled(true);
                binding.imgFile.setEnabled(true);
            }
        });


    }

    private void startTimer() {
        Handler handler = new Handler();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.textRecorderMessage.setText(timeFormatter.format(new Date(audioTotalTime * 1000)));
                        audioTotalTime++;
                    }
                });
            }
        };

        audioTotalTime = 0;
        timer.schedule(timerTask, 0, 1000);
    }

    AlertDialog alertDialog;

    private void showDialogSelecteService() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_chose_file, null);
//        radioGroup = view.findViewById(R.id.radioGroup);
        TextView tv_send_Image = view.findViewById(R.id.send_photo);
        TextView tv_send_location = view.findViewById(R.id.send_location);
        TextView tv_send_vidio = view.findViewById(R.id.send_vedio);
        TextView tv_send_contact = view.findViewById(R.id.send_phone);
//        progressBar = view.findViewById(R.id.progrss_dilog);
//        btn_next = view.findViewById(R.id.tv_next);


        tv_send_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 3);

                alertDialog.dismiss();
            }
        });
        tv_send_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPictureFromGalary();

                alertDialog.dismiss();
            }
        });
        tv_send_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String meassge = lattuide + "," + longtuide;
                binding.proBar.setVisibility(View.VISIBLE);
                binding.editGchatMessage.setVisibility(View.GONE);
                binding.imgRecord.setEnabled(false);
                binding.imgSend.setEnabled(false);
                binding.imgFile.setEnabled(false);
                Log.d("messsssssssssssssss", "onClick: " + meassge);
                callSendTwoMessage(to, "map", meassge);


                alertDialog.dismiss();
            }
        });

        tv_send_vidio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String meassge = lattuide + "," +longtuide;
//                binding.proBar.setVisibility(View.VISIBLE);
//                binding.editGchatMessage.setVisibility(View.GONE);
//                binding.imgRecord.setEnabled(false);
//                binding.imgSend.setEnabled(false);
//                binding.imgFile.setEnabled(false);
//                Log.d("messsssssssssssssss", "onClick: " + meassge);


                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 1);

                alertDialog.dismiss();
            }
        });

        alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void getPictureFromGalary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
    }

    String phoneNumber;

    @SuppressLint("Recycle")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 0:
                if (resultCode == RESULT_OK) {
                    binding.proBar.setVisibility(View.VISIBLE);
                    binding.editGchatMessage.setVisibility(View.GONE);
                    binding.imgRecord.setEnabled(false);
                    binding.imgSend.setEnabled(false);
                    binding.imgFile.setEnabled(false);

                    //    Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                    File file = new File(getRealPathFromURI(data.getData()));
                    RequestBody requestFile = RequestBody.create((MediaType.parse("multipart/form-data")),
                            file);
                    MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("message", file.getName(), requestFile);
                    RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), "image");
                    callSendImageTwoMessage(Integer.parseInt(to), usernameBody, multipartBody);


                    break;


                }


            case 1:
                if (resultCode == RESULT_OK) {

                    binding.proBar.setVisibility(View.VISIBLE);
                    binding.editGchatMessage.setVisibility(View.GONE);
                    binding.imgRecord.setEnabled(false);
                    binding.imgSend.setEnabled(false);
                    binding.imgFile.setEnabled(false);
                    Uri selectedImageUri = data.getData();

                    // MEDIA GALLERY
                    String selectedVideoPath = PathVideo.getPath(getActivity(), selectedImageUri);

                    MultipartBody.Part multipartBody = PathVideo.getMultiPartBody("message", selectedVideoPath);
                    RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), "video");
                    callSendFileTwoMessage(Integer.parseInt(to), usernameBody, multipartBody);


                }


                break;

            case 3:
                if (resultCode == RESULT_OK) {

                    Uri contactData = data.getData();

                    Cursor cur = getActivity().getContentResolver().query(contactData, null, null, null, null);
                    if (cur.getCount() > 0) {// thats mean some resutl has been found
                        if (cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            Log.e("Names", name);

                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                                Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                                while (phones.moveToNext()) {
                                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    Log.d("Number", phoneNumber);
                                    //     Toast.makeText(getActivity(), ""+phoneNumber, Toast.LENGTH_SHORT).show();
                                    binding.proBar.setVisibility(View.VISIBLE);
                                    binding.editGchatMessage.setVisibility(View.GONE);
                                    binding.imgRecord.setEnabled(false);
                                    binding.imgSend.setEnabled(false);
                                    binding.imgFile.setEnabled(false);
                                    callSendTwoMessage(to, "contact", phoneNumber);

                                }
                                phones.close();
                            }

                        }
                    }
                    cur.close();
                }
                break;


        }
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    private void releaseMediaPlayer() {

        if (player != null) {

            player.release();


            player = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }


    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                player.pause();
                player.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                player.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                releaseMediaPlayer();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener
            = mediaPlayer -> releaseMediaPlayer();

    private MediaPlayer player = null;

    private void getChatsBetween(int page, String to, String orderId) {

        Call<APIResponse.ResponseChatBetween> call = apiInterFace
                .chatBetweenUser(to, orderId, page + "", "application/json",
                        "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                        shardEditor.loadData().get(ShardEditor.KEY_LANG)
                );

        call.enqueue(new Callback<APIResponse.ResponseChatBetween>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Response<APIResponse.ResponseChatBetween> response) {
                if (response.code() == 200) {

                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        binding.lauytContent.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);
                        if (response.body().getData().getMessages().size() > 0) {
                            isFirst = true;
                            Log.d("ttttttttttttt", "onResponse: " + isFirst);
                        } else {
                            isFirst = false;
                            Log.d("ttttttttttttt", "onResponse: " + isFirst);
                        }
                        list = (ArrayList<Message>) response.body().getData().getMessages();

                        adapterChatePage = new AdapterChatePage(list
                                , getActivity(), (dataMessags, viewHolderVideo) -> {
                        }, response.body().getData().getDriver().getAvatar());
                        binding.rvChat.setAdapter(adapterChatePage);
                        binding.scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                        if (response.body().getData().getDriver().getIDName() != null) {
                            binding.tvMessgee.setText(response.body().getData().getDriver().getIDName() + "");
                        } else {
                            binding.tvMessgee.setText("بدون اسم");
                        }
                        if (response.body().getData().getDriver().getAvatar() != null) {

                            Picasso.with(getActivity()).
                                    load(Cemmon.BASE_URL + response.body().getData().getDriver().getAvatar())
                                    .placeholder(R.drawable.imagerat)
                                    .into(binding.imageView1);
                        }

                        binding.tvDateRat.setRating(Float.parseFloat(response.body().getData().getDriver().getStars() + ""));

                    } else {
                        binding.lauytContent.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);
                        Log.d("rrrdxc", "onResponse: " + response.body().getMessage().get(0));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Throwable t) {
                binding.lauytContent.setVisibility(View.VISIBLE);
                binding.progress.setVisibility(View.GONE);
                Log.d("rrrdxc", "onResponse: " + t.getMessage());
            }
        });

    }

    private void oneChatBetween(int page, String id) {

        Call<APIResponse.ResponseChatBetween> call = apiInterFace.chatBetweenOneMessage(id, page + "",
                "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                "ar");

        call.enqueue(new Callback<APIResponse.ResponseChatBetween>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseChatBetween> call,
                                   @NonNull Response<APIResponse.ResponseChatBetween> response) {
list.clear();

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        binding.proBarPag.setVisibility(View.GONE);
                        binding.lauytContent.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);
                        if (response.body().getData().getMessages().size() > 0) {

                            isFirst = true;
                            Log.d("ttttttttttttt", "onResponse: " + isFirst);
                        } else {
                            isFirst = false;
                            Log.d("ttttttttttttt", "onResponse: " + isFirst);
                        }
                        list = (ArrayList<Message>) response.body().getData().getMessages();
                        Cemmon.IMAGE_OF_DRIVER = response.body().getData().getDriver().getAvatar();
                        if (Cemmon.IMAGE_OF_DRIVER != null) {
                            adapterChatePage = new AdapterChatePage(list
                                    , getActivity(), (dataMessags, viewHolderVideo) -> {


                            }, Cemmon.IMAGE_OF_DRIVER);
                            binding.rvChat.setAdapter(adapterChatePage);
                        } else {
                            adapterChatePage = new AdapterChatePage(list
                                    , getActivity(), (dataMessags, viewHolderVideo) -> {

                                Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                            }, response.body().getData().getDriver().getAvatar());
                            binding.rvChat.setAdapter(adapterChatePage);
                            binding.scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.scroll.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        }


                        if (response.body().getData().getDriver().getIDName() != null) {
                            binding.tvMessgee.setText(response.body().getData().getDriver().getIDName() + "");
                        } else {
                            binding.tvMessgee.setText("بدون اسم");
                        }
                        if (response.body().getData().getDriver().getAvatar() != null) {

                            Picasso.with(getActivity()).
                                    load(Cemmon.BASE_URL + response.body().getData().getDriver().getAvatar())
                                    .into(binding.imageView1);
                        }

                        binding.tvDateRat.setRating(Float.parseFloat(response.body().getData().getDriver().getStars() + ""));

                    } else {
                        binding.proBarPag.setVisibility(View.GONE);
                        binding.lauytContent.setVisibility(View.VISIBLE);
                        binding.progress.setVisibility(View.GONE);
                        Log.d("rrrdxc", "onResponse: " + response.body().getMessage().get(0));
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Throwable t) {
                binding.lauytContent.setVisibility(View.VISIBLE);
                binding.progress.setVisibility(View.GONE);
                Log.d("rrrdxc", "onResponse: " + t.getMessage());
                binding.proBarPag.setVisibility(View.GONE);
            }
        });


    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message receivedText = intent.getParcelableExtra("com.codinginflow.EXTRA_TEXT");

            if (receivedText.getOrderId().equals(String.valueOf(orderId)))
                adapterChatePage.refreshData(receivedText);
            refreshAdpter(adapterChatePage, list);
            adapterChatePage.notifyItemInserted(list.size());
            binding.rvChat.smoothScrollToPosition(list.size());
            binding.scroll.post(new Runnable() {
                @Override
                public void run() {
                    binding.scroll.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("com.codinginflow.EXAMPLE_ACTION");
        getActivity().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void callSendTwoMessage(String id, String type, String text) {

        Call<APIResponse.ResponseSendMessage> call;
        if (isFirst) {

            call =
                    apiInterFace.sendTwoMessageApi(id, type
                            , orderId,
                            text

                            , "application/json",
                            "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)

                            , shardEditor.loadData().get(ShardEditor.KEY_LANG));

        } else {

            call =
                    apiInterFace.sendFirstMessageApi(id, "text",
                            text
                            , true
                            , "application/json",
                            "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)

                            , shardEditor.loadData().get(ShardEditor.KEY_LANG));
        }


        call.enqueue(new Callback<APIResponse.ResponseSendMessage>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseSendMessage> call,
                                   @NonNull Response<APIResponse.ResponseSendMessage> response) {


                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                        binding.proBar.setVisibility(View.GONE);
                        binding.editGchatMessage.setVisibility(View.VISIBLE);
                        binding.imgRecord.setEnabled(true);
                        binding.imgSend.setEnabled(true);
                        binding.imgFile.setEnabled(true);
                        isFirst = true;
                        try {
                            adapterChatePage.refreshData(response.body().getData());

                            Log.d("oooooooooo", "onResponse: " + response.body().getData().getMessage());

                            //   adapterChatePage = new AdapterChatePage(list, getActivity(), image);
                            refreshAdpter(adapterChatePage, list);
                            adapterChatePage.notifyItemInserted(list.size());
                            binding.rvChat.smoothScrollToPosition(list.size());
                            binding.scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.scroll.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        binding.proBar.setVisibility(View.GONE);
                        binding.editGchatMessage.setVisibility(View.VISIBLE);
                        binding.imgRecord.setEnabled(true);
                        binding.imgSend.setEnabled(true);
                        binding.imgFile.setEnabled(true);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseSendMessage> call, @NonNull Throwable t) {
                binding.proBar.setVisibility(View.GONE);
                binding.editGchatMessage.setVisibility(View.VISIBLE);
                binding.imgRecord.setEnabled(true);
                binding.imgSend.setEnabled(true);
                binding.imgFile.setEnabled(true);
            }
        });


    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void refreshAdpter(AdapterChatePage adapterChatePagep, ArrayList<Message> list) {

        if (list.size() > 1) {
            adapterChatePagep = new AdapterChatePage(list, getActivity(), image);
            //Removed SmoothScroll form here
            adapterChatePagep.notifyDataSetChanged();
            binding.rvChat.setAdapter(adapterChatePagep);

        } else {

            adapterChatePagep = new AdapterChatePage(list, getActivity(), image);
            binding.rvChat.setAdapter(adapterChatePagep);
        }
    }


    // get Location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {


                            lattuide = location.getLatitude();
                            longtuide = location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lattuide = mLastLocation.getLatitude();
            longtuide = mLastLocation.getLongitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    public void itemOnclickPlaying(Message dataMessags, AdapterChatePage.ViewHolderVideo viewHolderVideo) {

    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }

    private void getDataWithPagination(int page) {

        Call<APIResponse.ResponseChatBetween> call = apiInterFace
                .chatBetweenUser(to, orderId, page + "", "application/json",
                        "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                        shardEditor.loadData().get(ShardEditor.KEY_LANG)
                );

        call.enqueue(new Callback<APIResponse.ResponseChatBetween>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Response<APIResponse.ResponseChatBetween> response) {
                if (response.code() == 200) {

                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        list.addAll(response.body().getData().getMessages());
                        binding.proBarPag.setVisibility(View.GONE);

                        adapterChatePage = new AdapterChatePage(list
                                , getActivity(), (dataMessags, viewHolderVideo) -> {
                        }, response.body().getData().getDriver().getAvatar());
                        binding.rvChat.setAdapter(adapterChatePage);


                    } else {
                        binding.proBarPag.setVisibility(View.GONE);
                        Log.d("rrrdxc", "onResponse: " + response.body().getMessage().get(0));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Throwable t) {
                binding.proBarPag.setVisibility(View.GONE);
                Log.d("rrrdxc", "onResponse: " + t.getMessage());
            }
        });

    }

}



