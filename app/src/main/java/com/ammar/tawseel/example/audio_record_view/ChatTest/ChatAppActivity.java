package com.ammar.tawseel.example.audio_record_view.ChatTest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.tawseel.R;
import com.ammar.tawseel.adapters.AdapterChatePage;

import com.ammar.tawseel.editor.ShardEditor;
import com.ammar.tawseel.example.audio_record_view.AttachmentOption;
import com.ammar.tawseel.example.audio_record_view.AttachmentOptionsListener;
import com.ammar.tawseel.example.audio_record_view.AudioRecordView;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.Message;
import com.ammar.tawseel.pojo.response.APIResponse;
import com.ammar.tawseel.uitllis.Cemmon;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAppActivity extends AppCompatActivity implements
        AudioRecordView.RecordingListener,
        View.OnClickListener, AttachmentOptionsListener {

    String AudioSavePathInDevice = null;

    Random random;
    private AudioRecordView audioRecordView;
    private RecyclerView recyclerViewMessages;
    private long time;
    ImageView img_backe;
    CircleImageView imageView_person;
    TextView tv_name, tv_message, tv_date;
    AppCompatRatingBar ratingBar;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static String fileName = null;
     MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private Chronometer timer;
    private boolean permissionToRecordAccepted = false;
    private int PERMISSION_CODE = 21;
    ArrayList<Message> list = new ArrayList<>();
    APIInterFace apiInterFace;
    ShardEditor shardEditor;
    private AudioManager mAudioManager;
    String to = "";
    String orderId;
    boolean isFirst;
    AdapterChatePage adapterChatePage;
    String image;
    protected LinearLayoutManager mLayoutManager;
    View containerView;
    RelativeLayout layout_home;
    ProgressBar progressBar;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app);
        //     getSupportActionBar().hide();
        shardEditor = new ShardEditor(this);

        if (shardEditor.loadData().get(ShardEditor.KEY_LANG) != "") {

            Cemmon.setLocale(this, shardEditor.loadData().get(ShardEditor.KEY_LANG));

        }
        audioRecordView = new AudioRecordView();
        // this is to make your layout the root of audio record view,       root layout supposed to be empty..
        audioRecordView.initView((FrameLayout) findViewById(R.id.layoutMain));
        // this is to provide the container layout to the audio record view..
        containerView = audioRecordView.setContainerView(R.layout.layout_chatting);

        //record
        random = new Random();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + UUID.randomUUID().toString() +System.currentTimeMillis()+ "_audio_record.3gp";


        audioRecordView.setRecordingListener(this);
        checkPermation();

        inItView();

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerViewMessages.setLayoutManager(mLayoutManager);
        recyclerViewMessages.setItemAnimator(itemAnimator);
        apiInterFace = APIClient.getClient().create(APIInterFace.class);


        if (getIntent() != null) {
            to = getIntent().getStringExtra("to");
            orderId = getIntent().getStringExtra("orderId");
            Log.d("ggggggggggggg", "onCreate: " + to + orderId);
            if (orderId == null)
                oneChatBetween(to);
            else
                getChatsBetween(to, orderId);

        }

        img_backe.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //   messageAdapter = new MessageAdapter();

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        recyclerViewMessages.setHasFixedSize(false);

        //   recyclerViewMessages.setAdapter(messageAdapter);
        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());

        setListener();
        audioRecordView.getMessageView().requestFocus();


        audioRecordView.setAttachmentOptions(AttachmentOption.getDefaultList(), this);

        audioRecordView.removeAttachmentOptionAnimation(false);
    }

    private void inItView() {

        recyclerViewMessages = containerView.findViewById(R.id.recyclerViewMessages);
        layout_home = containerView.findViewById(R.id.layoutMainchat);
        progressBar = containerView.findViewById(R.id.progressChat);

        img_backe = containerView.findViewById(R.id.img_back_chat);
        tv_name = containerView.findViewById(R.id.tv_name_lablechat);
       // tv_message = containerView.findViewById(R.id.tv_messgee_chat);

        ratingBar = containerView.findViewById(R.id.tv_date_rat_chat);
        imageView_person = containerView.findViewById(R.id.imageView1Chat);

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
        int res = checkCallingOrSelfPermission(permission1);
        int res2 = checkCallingOrSelfPermission(permission2);
        return (res == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED);
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

    private void getChatsBetween(String to, String orderId) {

        Call<APIResponse.ResponseChatBetween> call = apiInterFace
                .chatBetweenUser(to, orderId, "1", "application/json",
                        "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                        shardEditor.loadData().get(ShardEditor.KEY_LANG)
                );

        call.enqueue(new Callback<APIResponse.ResponseChatBetween>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Response<APIResponse.ResponseChatBetween> response) {
                if (response.code() == 200) {

                    assert response.body() != null;
                    if (response.body().getStatus()) {
                        layout_home.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        if (response.body().getData().getMessages().size() > 0) {
                            isFirst = true;
                            Log.d("ttttttttttttt", "onResponse: " + isFirst);
                        } else {
                            isFirst = false;
                            Log.d("ttttttttttttt", "onResponse: " + isFirst);
                        }
                        list = (ArrayList<Message>) response.body().getData().getMessages();

                        adapterChatePage = new AdapterChatePage(list
                                , ChatAppActivity.this, (dataMessags, viewHolderVideo) -> {

                        }, response.body().getData().getDriver().getAvatar());
                        recyclerViewMessages.setAdapter(adapterChatePage);

                        if (response.body().getData().getDriver().getIDName() != null) {
                            tv_name.setText(response.body().getData().getDriver().getIDName() + "");
                        } else {
                            tv_name.setText("بدون اسم");
                        }
                        if (response.body().getData().getDriver().getAvatar() != null) {

                            Picasso.with(ChatAppActivity.this).
                                    load(Cemmon.BASE_URL + response.body().getData().getDriver().getAvatar())
                                    .placeholder(R.drawable.imagerat)
                                    .into(imageView_person);
                        }

                     //   ratingBar.setRating(Float.parseFloat(response.body().getData().getDriver().getStars()+""));

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Throwable t) {

            }
        });

    }

    private void oneChatBetween(String id) {

        Call<APIResponse.ResponseChatBetween> call = apiInterFace.chatBetweenOneMessage(id, "1",
                "application/json",
                "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN),
                shardEditor.loadData().get(ShardEditor.KEY_LANG));

        call.enqueue(new Callback<APIResponse.ResponseChatBetween>() {
            @Override
            public void onResponse(@NonNull Call<APIResponse.ResponseChatBetween> call,
                                   @NonNull Response<APIResponse.ResponseChatBetween> response) {


                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        layout_home.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        if (response.body().getData().getMessages().size() > 0) {

                            isFirst = true;
                            Log.d("ttttttttttttt", "onResponse: " + isFirst);
                        } else {
                            isFirst = false;
                            Log.d("ttttttttttttt", "onResponse: " + isFirst);
                        }
                        list = (ArrayList<Message>) response.body().getData().getMessages();

                        adapterChatePage = new AdapterChatePage(list
                                , ChatAppActivity.this, new AdapterChatePage.OnclickMessageAudio() {
                            @Override
                            public void itemOnclickPlaying(Message dataMessags, AdapterChatePage.ViewHolderVideo viewHolderVideo) {

                            }
                        }, response.body().getData().getDriver().getAvatar());
                        recyclerViewMessages.setAdapter(adapterChatePage);

                        if (response.body().getData().getDriver().getIDName() != null) {
                            tv_name.setText(response.body().getData().getDriver().getIDName() + "");
                        } else {
                            tv_name.setText("بدون اسم");
                        }
                        if (response.body().getData().getDriver().getAvatar() != null) {

                            Picasso.with(ChatAppActivity.this).
                                    load(Cemmon.BASE_URL + response.body().getData().getDriver().getAvatar())
                                    .into(imageView_person);
                        }

                       // ratingBar.setRating(response.body().getData().getDriver().getStars());

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseChatBetween> call, @NonNull Throwable t) {

            }
        });


    }

    private void setListener() {

//        audioRecordView.getEmojiView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                audioRecordView.hideAttachmentOptionView();
//                showToast("Emoji Icon Clicked");
//            }
//        });

        audioRecordView.getCameraView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
                showToast("Camera Icon Clicked");
            }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = audioRecordView.getMessageView().getText().toString().trim();

                callSendTwoMessage(to, msg,"text");
                audioRecordView.getMessageView().setText("");
                //    messageAdapter.add(new Message(msg));
            }
        });
    }

    @Override
    public void onRecordingStarted() {

        if (checkPermation()) {

            startRecording();


        }
        time = System.currentTimeMillis() / (1000);


    }

    @Override
    public void onRecordingLocked() {


        showToast("loced");

    }



    @Override
    public void onRecordingCompleted() {

        stopRecording();
        int recordTime = (int) ((System.currentTimeMillis() / (1000)) - time);

        if (recordTime > 1) {
            Log.d("ssssssssssssss", "onRecordingCompleted: " + fileName);
            File file = new File(fileName);

            RequestBody requestFile = RequestBody.create((MediaType.parse("multipart/form-data")),
                    file);
            MultipartBody.Part   multipartBody = MultipartBody.Part.createFormData("message", file.getName(), requestFile);
            Log.d("ssssssssssssss", "onRecordingCompleted: " + to +"\n "+ orderId);
        //    callSendFileTwoMessage(Integer.parseInt(to), "audio",multipartBody);
        }

        showToast(fileName);
    }

    @Override
    public void onRecordingCanceled() {
      stopRecording();
        File file = new File(fileName);
        file.delete();

        debug("canceled");
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void debug(String log) {
        Log.d("VarunJohn", log);
    }

    @Override
    public void onClick(View view) {
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Created by:\nVarun John\nvarunjohn1990@gmail.com\n\nCheck code on Github :\nhttps://github.com/varunjohn/Audio-Recording-Animation")
                .setCancelable(false)
                .setPositiveButton("Github", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = "https://github.com/varunjohn/Audio-Recording-Animation";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.android.chrome");
                        try {
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            i.setPackage(null);
                            try {
                                startActivity(i);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(AttachmentOption attachmentOption) {
        switch (attachmentOption.getId()) {

            case AttachmentOption.DOCUMENT_ID:
                showToast("Document Clicked");
                break;
            case AttachmentOption.CAMERA_ID:
                showToast("Camera Clicked");
                break;
            case AttachmentOption.GALLERY_ID:
                showToast("Gallery Clicked");
                break;
            case AttachmentOption.AUDIO_ID:
                showToast("Audio Clicked");
                break;
            case AttachmentOption.LOCATION_ID:
                showToast("Location Clicked");
                break;
            case AttachmentOption.CONTACT_ID:
                showToast("Contact Clicked");
                break;
        }
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message receivedText = intent.getParcelableExtra("com.codinginflow.EXTRA_TEXT");

            if (receivedText.getOrderId().equals(String.valueOf(orderId)))
                adapterChatePage.refreshData(receivedText);
            refreshAdpter(adapterChatePage, list);
            adapterChatePage.notifyItemInserted(list.size());
            recyclerViewMessages.smoothScrollToPosition(list.size());
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("com.codinginflow.EXAMPLE_ACTION");
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    private void callSendTwoMessage(String id, String text,String type) {

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

                        isFirst = true;
                        adapterChatePage.refreshData(response.body().getData());

                        Log.d("oooooooooo", "onResponse: " + response.body().getData().getMessage());

                        //   adapterChatePage = new AdapterChatePage(list, getActivity(), image);
                        refreshAdpter(adapterChatePage, list);
                        adapterChatePage.notifyItemInserted(list.size());
                        recyclerViewMessages.smoothScrollToPosition(list.size());


                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse.ResponseSendMessage> call, @NonNull Throwable t) {

            }
        });


    }



//    private void callSendFileTwoMessage(int id, String type,MultipartBody.Part message) {
//
//        Call<APIResponse.ResponseSendMessage> call;
//        if (isFirst) {
//
//            call =
//                    apiInterFace.sendFileMessage(id, type
//                            , Integer.parseInt(orderId),
//                            message
//
//                            , "application/json",
//                            "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)
//
//                            , shardEditor.loadData().get(ShardEditor.KEY_LANG));
//
//        } else {
//
//            call =
//                    apiInterFace.sendFileFirstMessage(id, type,
//                            message
//                            , true
//                            , "application/json",
//                            "Bearer" + " " + shardEditor.loadData().get(ShardEditor.KEY_TOKEN)
//
//                            , shardEditor.loadData().get(ShardEditor.KEY_LANG));
//        }
//
//
//        call.enqueue(new Callback<APIResponse.ResponseSendMessage>() {
//            @Override
//            public void onResponse(@NonNull Call<APIResponse.ResponseSendMessage> call,
//                                   @NonNull Response<APIResponse.ResponseSendMessage> response) {
//
//
//                if (response.code() == 200) {
//
//                    if (response.body().getStatus()) {
//
//                        isFirst = true;
//                        adapterChatePage.refreshData(response.body().getData());
//
//                        Log.d("oooooooooo", "onResponse: " + response.body().getData().getType());
////
////                        //   adapterChatePage = new AdapterChatePage(list, getActivity(), image);
////                        refreshAdpter(adapterChatePage, list);
////                        adapterChatePage.notifyItemInserted(list.size());
////                        recyclerViewMessages.smoothScrollToPosition(list.size());
//                        Toast.makeText(ChatAppActivity.this, "Sended", Toast.LENGTH_SHORT).show();
//
//                    }else {
//                        Log.d("rrrrrrrrrrrrrrrr", "onResponse: " + response.body().getMessage().get(0));
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<APIResponse.ResponseSendMessage> call, @NonNull Throwable t) {
//
//
//            }
//        });
//
//
//    }

    public void refreshAdpter(AdapterChatePage adapterChatePagep, ArrayList<Message> list) {

        if (list.size() > 1) {
            adapterChatePagep = new AdapterChatePage(list, this, image);
            //Removed SmoothScroll form here
            adapterChatePagep.notifyDataSetChanged();
            recyclerViewMessages.setAdapter(adapterChatePagep);

        } else {
            Collections.reverse(list);
            adapterChatePagep = new AdapterChatePage(list, this, image);
            recyclerViewMessages.setAdapter(adapterChatePagep);
        }
    }


    ////////////////////////////////////////////               sound
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                releaseMediaPlayer();
            }
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener
            = mediaPlayer -> releaseMediaPlayer();


    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();

    }


    private void playAudio(int savenod) {


        releaseMediaPlayer();

        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


            mediaPlayer = MediaPlayer.create(this, savenod);


            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mCompletionListener);


        }
    }

    private void startPlaying() {

        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("hjjjjjj", "prepare() failed");
        }
    }

    private void releaseMediaPlayer() {

        if (mediaPlayer != null) {

            mediaPlayer.release();


            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }
}