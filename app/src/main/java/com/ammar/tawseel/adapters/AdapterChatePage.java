package com.ammar.tawseel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.Message;
import com.ammar.tawseel.ui.DisplayVideoActivity;
import com.ammar.tawseel.ui.ShowImageChatActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AdapterChatePage extends RecyclerView.Adapter {
APIInterFace apiInterFace;
    ArrayList<Message> list;
    private Context mcontext;
    private OnclickMessageAudio onclickMessage;
    String image;
    private AudioManager mAudioManager;

    public interface OnclickMessageAudio {

        void itemOnclickPlaying(Message dataMessags, ViewHolderVideo viewHolderVideo);
    }

    int layout;

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getType().equals("text")) {
            return 0;
        } else if (list.get(position).getType().equals("image")) {
            return 2;
        } else if (list.get(position).getType().equals("map")) {
            return 3;
        } else if (list.get(position).getType().equals("video")) {
            return 4;
        } else if (list.get(position).getType().equals("contact")) {
            return 5;
        }

        return 1;


    }

    public AdapterChatePage(ArrayList<Message> list, Context mcontext, String image) {
        this.list = list;
        this.mcontext = mcontext;
        this.image = image;

    }

    public AdapterChatePage(ArrayList<Message> list, Context mcontext, OnclickMessageAudio onclickMessage, String image) {

        mAudioManager = (AudioManager) mcontext.getSystemService(Context.AUDIO_SERVICE);
        this.mcontext = mcontext;
        this.list = list;
        this.onclickMessage = onclickMessage;
        this.image = image;
        list = list;
        if (list != null && !list.isEmpty()) {
            size = list.size();
        }
    }

//    @Override
//    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
//        if (holder.getItemViewType() == 3) {
//            ViewHolderLocation viewHolderLocation = (ViewHolderLocation) holder;
//            if (viewHolderLocation.mapCurrent != null) {
//                viewHolderLocation.mapCurrent.clear();
//                viewHolderLocation.mapCurrent.setMapType(GoogleMap.MAP_TYPE_NONE);
//            }
//        }
//
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.item_chate_between, parent, false);
            return new ViewHolderText(view);
        } else if (viewType == 2) {
            view = layoutInflater.inflate(R.layout.item_chat_image, parent, false);
            return new ViewHolderImage(view);
        } else if (viewType == 3) {
            view = layoutInflater.inflate(R.layout.item_chat_location, parent, false);
            ViewHolderLocation holderLocation = new ViewHolderLocation(view);
            return holderLocation;
        } else if (viewType == 4) {
            view = layoutInflater.inflate(R.layout.item_video, parent, false);
            return new ViewHolderVideo(view);
        } else if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.item_chate_audio, parent, false);
            return new ViewHolderAudio(view);

        } else if (viewType == 5) {
            view = layoutInflater.inflate(R.layout.item_chat_contact, parent, false);
            return new ViewHolderContact(view);

        }


        return null;


    }

    private Handler mHandler = new Handler();


    Runnable run;
//    Runnable run1;

    private String calculateDuration(int duration) {
        String finalDuration = "";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        if (minutes == 0) {
            finalDuration = "0:" + seconds;
        } else {
            if (seconds >= 60) {
                long sec = seconds - (minutes * 60);
                finalDuration = minutes + ":" + sec;
            }
        }
        return finalDuration;
    }

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm a",
            new Locale("en"));

    Date d = null;


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (getItemViewType(position) == 0) {
            onBindText((ViewHolderText) holder, position);

        } else if (getItemViewType(position) == 1) {
            onBindAudio((ViewHolderAudio) holder, position);

        } else if (getItemViewType(position) == 2) {

            onBindImage((ViewHolderImage) holder, position);

        } else if (getItemViewType(position) == 3) {

            onBindMap((ViewHolderLocation) holder, position);

        } else if (getItemViewType(position) == 4) {

            onBindVideo((ViewHolderVideo) holder, position);

        } else if (getItemViewType(position) == 5) {

            onBindContact((ViewHolderContact) holder, position);


        }
    }

    private void onBindContact(@NonNull ViewHolderContact holder, int position) {
        ViewHolderContact viewHolderContact = holder;

        if (list.get(position).getSenderType().equals("user")) {
            {
                viewHolderContact.thierMessag.setVisibility(View.GONE);
                viewHolderContact.myMessage.setVisibility(View.VISIBLE);

                viewHolderContact.bodyMeMessage.setText(list.get(position).getMessage() + "");
                viewHolderContact.bodyMeMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallNumber(list.get(position).getMessage());
                    }
                });

                //     holder.bodyMeMessage.setText(list.get(getItemCount() - 1 -position).getMessage());
                if (Cemmon.IMAGE_OF_USER != null) {
                    Picasso.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                            .into(viewHolderContact.imageViewme);
                }

            }
        } else {

            viewHolderContact.thierMessag.setVisibility(View.VISIBLE);
            viewHolderContact.myMessage.setVisibility(View.GONE);

            viewHolderContact.bodyTheirMessage.setText(list.get(position).getMessage() + "");
            viewHolderContact.bodyTheirMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    CallNumber(list.get(position).getMessage());


                }
            });
            Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_DRIVER).placeholder(R.drawable.imagerat)
                    .into(viewHolderContact.imageViewthier);


        }
    }

    private void CallNumber(String message) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
        alert.setMessage(R.string.are_you_sur_call);
        alert.setPositiveButton(R.string.call, (dialog, which) -> {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + message));//change the number
            mcontext.startActivity(callIntent);
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void callWithNumber(String message) {
    }

    private void onBindVideo(@NonNull ViewHolderVideo holder, int position) {
        ViewHolderVideo viewHolderVideo = holder;

        if (list.get(position).getSenderType().equals("user")) {
            {
                viewHolderVideo.touchLayout_me.setVisibility(View.VISIBLE);
                viewHolderVideo.touchLayout_there.setVisibility(View.GONE);
                try {
                    d = input.parse(list.get(position).getCreatedAt());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formatted = output.format(d);
                viewHolderVideo.tv_date_me.setText(formatted);
                //  viewHolderVideo.ved_view_me.setVideoURI(Uri.parse(Cemmon.BASE_URL +list.get(position).getMessage()));
                viewHolderVideo.ved_view_me.setOnClickListener(v -> {


                    Intent intent = new Intent(mcontext, DisplayVideoActivity.class);
                    intent.putExtra("uri", Cemmon.BASE_URL + list.get(position).getMessage() + "");
                    mcontext.startActivity(intent);
                });

                if (Cemmon.IMAGE_OF_USER != null) {
                    Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER)
                            .into(viewHolderVideo.img_me);
                }

            }
        } else {

            viewHolderVideo.touchLayout_me.setVisibility(View.GONE);
            viewHolderVideo.touchLayout_there.setVisibility(View.VISIBLE);
            try {
                d = input.parse(list.get(position).getCreatedAt());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            viewHolderVideo.tv_date_there.setText(formatted);
            //       viewHolderVideo.ved_view_there.setVideoURI(Uri.parse(Cemmon.BASE_URL+list.get(position).getMessage()));
            viewHolderVideo.touchLayout_there.setOnClickListener(v -> {


                Intent intent = new Intent(mcontext, DisplayVideoActivity.class);
                intent.putExtra("uri", Cemmon.BASE_URL + list.get(position).getMessage() + "");
                mcontext.startActivity(intent);

            });
            Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_DRIVER).placeholder(R.drawable.imagerat)
                    .into(viewHolderVideo.img_there);


        }


    }

    private void onBindMap(@NonNull ViewHolderLocation holder, int position) {
        ViewHolderLocation viewHolderLocation = holder;


        if (list.get(position).getSenderType().equals("user")) {
            {
                viewHolderLocation.thierMessag.setVisibility(View.GONE);
                viewHolderLocation.myMessage.setVisibility(View.VISIBLE);

                try {
                    d = input.parse(list.get(position).getCreatedAt());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formatted = output.format(d);
                viewHolderLocation.date_me.setText(formatted);


                if (list.get(position).getMessage() != null) {


                    String[] separated = list.get(position).getMessage().split(",");
                    String lattuid = separated[0];
                    String longtude = separated[1];

                    getPhotoMapCall(lattuid,longtude,viewHolderLocation.bodyMeMessage);

                    viewHolderLocation.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String uri = "http://maps.google.com/maps?saddr=" + lattuid + "," + longtude + "&daddr=" + lattuid + "," + longtude;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                            mcontext.startActivity(intent);
                        }
                    });
                }


                viewHolderLocation.bodyMeMessage.setImageResource(R.drawable.location);
        }
        if (Cemmon.IMAGE_OF_USER != null) {
            Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                    .into(viewHolderLocation.imageViewme);
        }


    }  else {

            viewHolderLocation.thierMessag.setVisibility(View.VISIBLE);
            viewHolderLocation.myMessage.setVisibility(View.GONE);
            try {
                d = input.parse(list.get(position).getCreatedAt());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            viewHolderLocation.date_Theier.setText(formatted);
            if (list.get(position).getMessage() != null) {


                String[] separated = list.get(position).getMessage().split(",");
                String lattuid = separated[0];
                String longtude = separated[1];
                getPhotoMapCall(lattuid,longtude,viewHolderLocation.bodyTheirMessage);
                viewHolderLocation.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    //    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(lattuid), Double.parseDouble(longtude));
                        String uri = "http://maps.google.com/maps?saddr=" + lattuid + "," + longtude + "&daddr=" + lattuid + "," + longtude;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        mcontext.startActivity(intent);
                    }
                });
            }
            viewHolderLocation.bodyTheirMessage.setImageResource(R.drawable.location);
            Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_DRIVER).placeholder(R.drawable.imagerat)
                    .into(viewHolderLocation.imageViewthier);


        }


    }

    private void getPhotoMapCall(String lattuid, String longtude,ImageView view) {
        apiInterFace= APIClient.getClientMap().create(APIInterFace.class);

        Call<ResponseBody> call=apiInterFace.getPhoto(lattuid,longtude);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code()==200){
                    try {
                        Glide.with(mcontext).load(response.body().string()).placeholder(R.drawable.location)
                                .into(view);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("suuuuuuuuuuuu", "onResponse: "+t.getMessage());
            }
        });

    }

    private void onBindImage(@NonNull ViewHolderImage holder, int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm a",
                new Locale("en"));

        Date d = null;


        ViewHolderImage viewHolderImage = holder;
        if (list.get(position).getSenderType().equals("user")) {
            {
                viewHolderImage.thierMessag.setVisibility(View.GONE);
                viewHolderImage.myMessage.setVisibility(View.VISIBLE);

                Glide.with(mcontext).load(Cemmon.BASE_URL + list.get(position).getMessage())
                        .into(viewHolderImage.bodyMeMessage);
                try {
                    d = input.parse(list.get(position).getCreatedAt());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formatted = output.format(d);
                viewHolderImage.dateme.setText(formatted);
                //     holder.bodyMeMessage.setText(list.get(getItemCount() - 1 -position).getMessage());
                if (Cemmon.IMAGE_OF_USER != null) {
                    Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER)
                            .into(viewHolderImage.imageViewme);
                }

            }
        } else {

            try {
                d = input.parse(list.get(position).getCreatedAt());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            viewHolderImage.dateTheir.setText(formatted);

            viewHolderImage.thierMessag.setVisibility(View.VISIBLE);
            viewHolderImage.myMessage.setVisibility(View.GONE);
            Glide.with(mcontext).load(Cemmon.BASE_URL + list.get(position).getMessage())
                    .into(viewHolderImage.bodyTheirMessage);
            Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_DRIVER).placeholder(R.drawable.imagerat)
                    .into(viewHolderImage.imageViewthier);
        }
        viewHolderImage.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ShowImageChatActivity.class);
                intent.putExtra("image", Cemmon.BASE_URL + list.get(position).getMessage() + "");
                mcontext.startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void onBindText(@NonNull ViewHolderText holder, int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output =
                new SimpleDateFormat("dd/MM/yyyy HH:mm aa", new Locale("en"));

        Date d = null;


        ViewHolderText viewHolderText = holder;
        if (list.get(position).getSenderType().equals("user")) {
            {
                try {
                    d = input.parse(list.get(position).getCreatedAt());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formatted = output.format(d);
                holder.dateme.setText(formatted);
                viewHolderText.thierMessag.setVisibility(View.GONE);
                viewHolderText.myMessage.setVisibility(View.VISIBLE);

                viewHolderText.bodyMeMessage.setText(list.get(position).getMessage() + "");
                //     holder.bodyMeMessage.setText(list.get(getItemCount() - 1 -position).getMessage());
                if (Cemmon.IMAGE_OF_USER != null) {
                    Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                            .into(viewHolderText.imageViewme);
                }

            }
        } else {
            try {
                d = input.parse(list.get(position).getCreatedAt());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            holder.dateTheir.setText(formatted);

            viewHolderText.thierMessag.setVisibility(View.VISIBLE);
            viewHolderText.myMessage.setVisibility(View.GONE);

            viewHolderText.bodyTheirMessage.setText(list.get(position).getMessage() + "");

                Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_DRIVER).placeholder(R.drawable.imagerat)
                        .into(viewHolderText.imageViewthier);



        }
    }

    private void onBindAudio(@NonNull ViewHolderAudio holder, int position) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm a",
                new Locale("en"));

        Date d = null;
        ViewHolderAudio viewHolderAudio = holder;
        if (list.get(position).getSenderType().equals("user")) {
            try {
                d = input.parse(list.get(position).getCreatedAt());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            viewHolderAudio.dateme.setText(formatted);
            viewHolderAudio.myMessage.setVisibility(View.VISIBLE);
            viewHolderAudio.thierMessag.setVisibility(View.GONE);

            // Initializing MediaPlayer
            playAudio(position, viewHolderAudio.seekBarMe, viewHolderAudio.txt_audio_time_me, viewHolderAudio.iconplayme, R.drawable.ic_baseline_pause_24, R.drawable.ic_baseline_play_me_24);


            if (Cemmon.IMAGE_OF_USER != null) {
                Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                        .into(viewHolderAudio.imageViewme);
            }
        } else {

            viewHolderAudio.thierMessag.setVisibility(View.VISIBLE);
            viewHolderAudio.myMessage.setVisibility(View.GONE);
            try {
                d = input.parse(list.get(position).getCreatedAt());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            viewHolderAudio.datetheir.setText(formatted);
            playAudio(position, viewHolderAudio.seekBarThier, viewHolderAudio.txt_audio_time_thier, viewHolderAudio.iconplaeThere, R.drawable.pause_thier, R.drawable.ic_baseline_play_thier);


            Glide.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_DRIVER).placeholder(R.drawable.imagerat)
                    .into(viewHolderAudio.imageViewthier);


        }
    }

    private void playAudio(int position, SeekBar seekBarThier, TextView txt_audio_time_thier,
                           ImageView iconplaeThere, int p, int p2) {
        // Initializing MediaPlayer
        final MediaPlayer mediaPlayer1 = new MediaPlayer();

        mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer1.setDataSource(Cemmon.BASE_URL + list.get(position).getMessage());
            mediaPlayer1.prepare();// might take long for buffering.
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBarThier.setMax(mediaPlayer1.getDuration());
        seekBarThier.setTag(position);
        seekBarThier.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer1 != null && fromUser) {
                    mediaPlayer1.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        txt_audio_time_thier.setText("0:00/" + calculateDuration(mediaPlayer1.getDuration()));

        iconplaeThere.setOnClickListener(v -> {

            seekBarThier.setProgress(0);

            if (!mediaPlayer1.isPlaying()) {

                mediaPlayer1.start();

                iconplaeThere.setImageResource(p);
                run = new Runnable() {
                    @Override
                    public void run() {
                        // Updateing SeekBar every 100 miliseconds
                        seekBarThier.setProgress(mediaPlayer1.getCurrentPosition());
                        seekBarThier.postDelayed(run, 100);
                        //For Showing time of audio(inside runnable)
                        int miliSeconds = mediaPlayer1.getCurrentPosition();
                        if (miliSeconds != 0) {
                            //if audio is playing, showing current time;
                            long minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds);
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                            if (minutes == 0) {
                                txt_audio_time_thier.setText("0:" + seconds + "/" + calculateDuration(mediaPlayer1.getDuration()));
                            } else {
                                if (seconds >= 60) {
                                    long sec = seconds - (minutes * 60);
                                    txt_audio_time_thier.setText(minutes + ":" + sec + "/" + calculateDuration(mediaPlayer1.getDuration()));
                                }
                            }
                        } else {
                            //Displaying total time if audio not playing
                            int totalTime = mediaPlayer1.getDuration();
                            long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime);
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
                            if (minutes == 0) {
                                txt_audio_time_thier.setText("0:" + seconds);
                            } else {
                                if (seconds >= 60) {
                                    long sec = seconds - (minutes * 60);
                                    txt_audio_time_thier.setText(minutes + ":" + sec);
                                }
                            }
                        }
                        if (mediaPlayer1.getDuration() == mediaPlayer1.getCurrentPosition()) {
                            iconplaeThere.setImageResource(p2);
                            seekBarThier.setProgress(0);
                        }
                    }

                };
                run.run();
            } else {

                mediaPlayer1.pause();
                iconplaeThere.setImageResource(p2);
            }

        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    int size;

    public void addMore(Message dataMessags) {
        list.add(dataMessags);


    }

    public void refreshData(Message add) {

        list.add(add);
        notifyDataSetChanged();


    }


    public static class ViewHolderText extends RecyclerView.ViewHolder {

        CircleImageView imageViewme, imageViewthier;
        TextView bodyMeMessage, bodyTheirMessage, dateme, dateTheir;

        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;


        public ViewHolderText(@NonNull View itemView) {
            super(itemView);

            thierMessag = itemView.findViewById(R.id.their_message);
            dateme = itemView.findViewById(R.id.date_body_me);
            dateTheir = itemView.findViewById(R.id.date_body_there);
            myMessage = itemView.findViewById(R.id.myMessage);
            bodyMeMessage = itemView.findViewById(R.id.message_bodyme);
            imageViewme = itemView.findViewById(R.id.imageView1me);
            imageViewthier = itemView.findViewById(R.id.imageView1);
            bodyTheirMessage = itemView.findViewById(R.id.message_body);


        }
    }

    public static class ViewHolderContact extends RecyclerView.ViewHolder {

        CircleImageView imageViewme, imageViewthier;
        TextView bodyMeMessage, bodyTheirMessage, date;

        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;


        public ViewHolderContact(@NonNull View itemView) {
            super(itemView);

            thierMessag = itemView.findViewById(R.id.their_messagec);
            myMessage = itemView.findViewById(R.id.myMessagec);
            bodyMeMessage = itemView.findViewById(R.id.message_bodymec);
            imageViewme = itemView.findViewById(R.id.imageView1mec);
            imageViewthier = itemView.findViewById(R.id.imageView1c);
            bodyTheirMessage = itemView.findViewById(R.id.message_bodyc);


        }
    }

    public static class ViewHolderAudio extends RecyclerView.ViewHolder {
        TextView txt_audio_time_me, txt_audio_time_thier, dateme, datetheir;
        CircleImageView imageViewme, imageViewthier;
        ProgressBar proMeMessage, proTheirMessage, date;
        ImageView iconplayme, iconplaeThere;
        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;
        SeekBar seekBarMe, seekBarThier;

        public ViewHolderAudio(@NonNull View itemView) {
            super(itemView);
            dateme = itemView.findViewById(R.id.date_body_me_img);
            datetheir = itemView.findViewById(R.id.date_body_there_img);
            thierMessag = itemView.findViewById(R.id.message_layout_their);
            txt_audio_time_me = itemView.findViewById(R.id.txt_audio_time_me);
            txt_audio_time_thier = itemView.findViewById(R.id.date_text_there);
            myMessage = itemView.findViewById(R.id.message_audio_layout_me);
            seekBarMe = itemView.findViewById(R.id.seekBarMe);
            seekBarThier = itemView.findViewById(R.id.seekBar__there);
//            proMeMessage = itemView.findViewById(R.id.progress_bar_me);
//            proTheirMessage = itemView.findViewById(R.id.progress_bar__there);

            iconplayme = itemView.findViewById(R.id.thumbnail_video_icon_me);
            iconplaeThere = itemView.findViewById(R.id.thumbnail_video_icon_there);

            imageViewme = itemView.findViewById(R.id.imageViewaudio_me);
            imageViewthier = itemView.findViewById(R.id.imageViewaudio_there);


        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }

    public static class ViewHolderImage extends RecyclerView.ViewHolder {

        CircleImageView imageViewme, imageViewthier;
        ImageView bodyMeMessage, bodyTheirMessage;

        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;
        TextView dateme, dateTheir;

        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);
            dateme = itemView.findViewById(R.id.date_body_me_img);
            dateTheir = itemView.findViewById(R.id.date_body_there_img);
            thierMessag = itemView.findViewById(R.id.their_layout_img_there);
            myMessage = itemView.findViewById(R.id.layout_me_img);
            bodyMeMessage = itemView.findViewById(R.id.img_bodyme);
            imageViewme = itemView.findViewById(R.id.imageView1_img_me);
            imageViewthier = itemView.findViewById(R.id.imageView1item_there);
            bodyTheirMessage = itemView.findViewById(R.id.imge_body_there);


        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }

    public static class ViewHolderLocation extends RecyclerView.ViewHolder {

        CircleImageView imageViewme, imageViewthier;
        ImageView bodyMeMessage, bodyTheirMessage;
    //    GoogleMap mapCurrent;
        OnclickMessageAudio lisrner;
        TextView date_me, date_Theier;



        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;


        public ViewHolderLocation(@NonNull View itemView) {
            super(itemView);
            date_me = itemView.findViewById(R.id.date_body_me_map);
            date_Theier = itemView.findViewById(R.id.date_body_ther_map);
            thierMessag = itemView.findViewById(R.id.their_layout_location_there);
            myMessage = itemView.findViewById(R.id.layout_me_location);
            bodyMeMessage = itemView.findViewById(R.id.location_img_body_me);
            imageViewme = itemView.findViewById(R.id.location_View1_img_me);
            imageViewthier = itemView.findViewById(R.id.location_View1item_there);
            bodyTheirMessage = itemView.findViewById(R.id.location_img_body_there);
//            if (bodyMeMessage != null) {
//                bodyMeMessage.onCreate(null);
//                bodyMeMessage.onResume();
//                bodyMeMessage.getMapAsync(this);
//            }

        }

//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            MapsInitializer.initialize(getApplicationContext());
//            mapCurrent = googleMap;
//
//
//            //you can move map here to item specific 'location'
//            int pos = getPosition();
//            //get 'location' by 'pos' from data list
//            //then move to 'location'
//
//            mapCurrent.moveCamera(CameraUpdateFactory.newLatLngZoom(Cemmon.latLngUser, 16f));
//            mapCurrent.addMarker(new MarkerOptions().position(Cemmon.latLngUser).title("My Location"));
//        }


    }

    public static class ViewHolderVideo extends RecyclerView.ViewHolder {


        public RelativeLayout touchLayout_me, touchLayout_there;
        public CircleImageView img_there, img_me;
        public ImageView ved_view_me, ved_view_there;
        TextView tv_date_me, tv_date_there;

        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);

            // xml there
            tv_date_there = itemView.findViewById(R.id.date_body_there);
            img_there = itemView.findViewById(R.id.imageView1item_there_video);
            touchLayout_there = itemView.findViewById(R.id.layout_there);
            ved_view_there = itemView.findViewById(R.id.ved_view_there);


            // xml me
            tv_date_me = itemView.findViewById(R.id.date_body_me);
            img_me = itemView.findViewById(R.id.image_View_item_me_video);
            ved_view_me = itemView.findViewById(R.id.ved_view_me);
            touchLayout_me = itemView.findViewById(R.id.layout_me);

        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }
}
