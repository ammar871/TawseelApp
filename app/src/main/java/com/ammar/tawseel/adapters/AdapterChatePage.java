package com.ammar.tawseel.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.tawseel.R;
import com.ammar.tawseel.pojo.data.Message;
import com.ammar.tawseel.ui.DisplayVideoActivity;
import com.ammar.tawseel.uitllis.Cemmon;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterChatePage extends RecyclerView.Adapter {

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
        }else if (list.get(position).getType().equals("contact")) {
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0 ) {
            view = layoutInflater.inflate(R.layout.item_chate_between, parent, false);
            return new ViewHolderText(view);
        } else if (viewType == 2) {
            view = layoutInflater.inflate(R.layout.item_chat_image, parent, false);
            return new ViewHolderImage(view);
        } else if (viewType == 3) {
            view = layoutInflater.inflate(R.layout.item_chat_location, parent, false);
            return new ViewHolderLocation(view);
        } else if (viewType == 4) {
            view = layoutInflater.inflate(R.layout.item_video, parent, false);
            return new ViewHolderVideo(view);
        }else if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.item_chate_audio, parent, false);
            return new ViewHolderAudio(view);

        }else if (viewType == 5) {
            view = layoutInflater.inflate(R.layout.item_chat_contact, parent, false);
            return new ViewHolderContact(view);

        }




        return null;


    }

    private Handler mHandler = new Handler();


    Runnable run;

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0 ) {
            onBindText((ViewHolderText) holder, position);

        } else if (getItemViewType(position) == 1) {
            onBindAudio((ViewHolderAudio) holder, position);

        } else if (getItemViewType(position) == 2) {

            onBindImage((ViewHolderImage) holder, position);

        } else if (getItemViewType(position) == 3) {
            onBindMap((ViewHolderLocation) holder, position);

        }else if (getItemViewType(position) ==4) {
            onBindVideo((ViewHolderVideo) holder, position);

        }else if (getItemViewType(position) ==5) {
            ViewHolderContact viewHolderContact = (ViewHolderContact)holder;
            if (list.get(position).getSenderType().equals("user")) {
                {
                    viewHolderContact.thierMessag.setVisibility(View.GONE);
                    viewHolderContact.myMessage.setVisibility(View.VISIBLE);

                    viewHolderContact.bodyMeMessage.setText(list.get(position).getMessage() + "");
                    viewHolderContact.bodyMeMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CallNumber( list.get(position).getMessage());
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



                        CallNumber( list.get(position).getMessage());


                    }
                });
                if (image != null) {
                    Picasso.with(mcontext).load(Cemmon.BASE_URL + image).placeholder(R.drawable.imagerat)
                            .into(viewHolderContact.imageViewthier);
                }


            }



        }
    }

    private void CallNumber( String message) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
        alert.setMessage("هل تريد الاتصال بهذا الرقم ؟ ");
        alert.setPositiveButton("اتصال  ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+message));//change the number
                mcontext.startActivity(callIntent);
            }
        });
        alert.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
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
//viewHolderVideo.mTexturePreview_me.s
                viewHolderVideo.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(mcontext, DisplayVideoActivity.class);
                        intent.putExtra("uri", Cemmon.BASE_URL+list.get(position).getMessage()+"");
                        mcontext.startActivity(intent);
                    }
                });
                //     holder.bodyMeMessage.setText(list.get(getItemCount() - 1 -position).getMessage());
                if (Cemmon.IMAGE_OF_USER != null) {
                    Picasso.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER)
                            .into(viewHolderVideo.img_me);
                }

            }
        } else {

            viewHolderVideo.touchLayout_me.setVisibility(View.GONE);
            viewHolderVideo.touchLayout_there.setVisibility(View.VISIBLE);


            if (image != null) {
                Picasso.with(mcontext).load(Cemmon.BASE_URL + image).placeholder(R.drawable.imagerat)
                        .into(viewHolderVideo.img_there);
            }


        }
    }

    private void onBindMap(@NonNull ViewHolderLocation holder, int position) {
        ViewHolderLocation viewHolderLocation = holder;
        if (list.get(position).getSenderType().equals("user")) {
            {
                viewHolderLocation.thierMessag.setVisibility(View.GONE);
                viewHolderLocation.myMessage.setVisibility(View.VISIBLE);

                if (list.get(position).getMessage() != null) {

                    String[] separated = list.get(position).getMessage().split(",");
                    String lattuid = separated[0];
                    String longtude = separated[1];
                    Log.d("latLong", "onBindViewHolder: " + lattuid + "," + longtude);
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(mcontext, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(Double.parseDouble(lattuid), Double.parseDouble(longtude),
                                1);
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
// Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        Log.d("assssssssss", "onBindViewHolder:  " + city + state);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                        Picasso.with(mcontext).load(location).placeholder(R.drawable.imagerat)
//                                .into(viewHolderLocation.bodyMeMessage);
                }
                if (Cemmon.IMAGE_OF_USER != null) {
                    Picasso.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                            .into(viewHolderLocation.imageViewme);
                }

            }
        } else {

            viewHolderLocation.thierMessag.setVisibility(View.VISIBLE);
            viewHolderLocation.myMessage.setVisibility(View.GONE);
//                Picasso.with(mcontext).load(Cemmon.BASE_URL + list.get(position).getMessage()).placeholder(R.drawable.imagerat)
//                        .into(viewHolderImage.bodyTheirMessage);

            if (image != null) {
                Picasso.with(mcontext).load(Cemmon.BASE_URL + image).placeholder(R.drawable.imagerat)
                        .into(viewHolderLocation.imageViewthier);
            }


        }
    }

    private void onBindImage(@NonNull ViewHolderImage holder, int position) {
        ViewHolderImage viewHolderImage = holder;
        if (list.get(position).getSenderType().equals("user")) {
            {
                viewHolderImage.thierMessag.setVisibility(View.GONE);
                viewHolderImage.myMessage.setVisibility(View.VISIBLE);
                Picasso.with(mcontext).load(Cemmon.BASE_URL + list.get(position).getMessage()).placeholder(R.drawable.imagerat)
                        .into(viewHolderImage.bodyMeMessage);

                //     holder.bodyMeMessage.setText(list.get(getItemCount() - 1 -position).getMessage());
                if (Cemmon.IMAGE_OF_USER != null) {
                    Picasso.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER)
                            .into(viewHolderImage.imageViewme);
                }

            }
        } else {

            viewHolderImage.thierMessag.setVisibility(View.VISIBLE);
            viewHolderImage.myMessage.setVisibility(View.GONE);
            Picasso.with(mcontext).load(Cemmon.BASE_URL + list.get(position).getMessage())
                    .into(viewHolderImage.bodyTheirMessage);

            if (image != null) {
                Picasso.with(mcontext).load(Cemmon.BASE_URL + image).placeholder(R.drawable.imagerat)
                        .into(viewHolderImage.imageViewthier);
            }


        }
    }

    private void onBindText(@NonNull ViewHolderText holder, int position) {
        ViewHolderText viewHolderText = holder;
        if (list.get(position).getSenderType().equals("user")) {
            {
                viewHolderText.thierMessag.setVisibility(View.GONE);
                viewHolderText.myMessage.setVisibility(View.VISIBLE);

                viewHolderText.bodyMeMessage.setText(list.get(position).getMessage() + "");
                //     holder.bodyMeMessage.setText(list.get(getItemCount() - 1 -position).getMessage());
                if (Cemmon.IMAGE_OF_USER != null) {
                    Picasso.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                            .into(viewHolderText.imageViewme);
                }

            }
        } else {

            viewHolderText.thierMessag.setVisibility(View.VISIBLE);
            viewHolderText.myMessage.setVisibility(View.GONE);

            viewHolderText.bodyTheirMessage.setText(list.get(position).getMessage() + "");
            if (image != null) {
                Picasso.with(mcontext).load(Cemmon.BASE_URL + image).placeholder(R.drawable.imagerat)
                        .into(viewHolderText.imageViewthier);
            }


        }
    }

    private void onBindAudio(@NonNull ViewHolderAudio holder, int position) {
        ViewHolderAudio viewHolderAudio = holder;
        if (list.get(position).getSenderType().equals("user")) {
            //mediaPlayer = MediaPlayer.create(holder.itemView.getContext(), Uri.parse(Cemmon.BASE_URL + list.get(position).getMessage()));
            viewHolderAudio.myMessage.setVisibility(View.VISIBLE);
            viewHolderAudio.thierMessag.setVisibility(View.GONE);

            // Initializing MediaPlayer
            final MediaPlayer mediaPlayer1 = new MediaPlayer();

            mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer1.setDataSource(Cemmon.BASE_URL + list.get(position).getMessage());
                mediaPlayer1.prepare();// might take long for buffering.
            } catch (IOException e) {
                e.printStackTrace();
            }

            viewHolderAudio.seekBarMe.setMax(mediaPlayer1.getDuration());
            viewHolderAudio.seekBarMe.setTag(position);
            viewHolderAudio.seekBarMe.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
            viewHolderAudio.txt_audio_time_me.setText("0:00/" + calculateDuration(mediaPlayer1.getDuration()));

            viewHolderAudio.iconplayme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewHolderAudio.seekBarMe.setProgress(0);

                    if (!mediaPlayer1.isPlaying()) {

                        mediaPlayer1.start();

                        viewHolderAudio.iconplayme.setImageResource(R.drawable.ic_baseline_pause_24);
                        run = new Runnable() {
                            @Override
                            public void run() {
                                // Updateing SeekBar every 100 miliseconds
                                viewHolderAudio.seekBarMe.setProgress(mediaPlayer1.getCurrentPosition());
                                viewHolderAudio.seekBarMe.postDelayed(run, 100);
                                //For Showing time of audio(inside runnable)
                                int miliSeconds = mediaPlayer1.getCurrentPosition();
                                if (miliSeconds != 0) {
                                    //if audio is playing, showing current time;
                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds);
                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                                    if (minutes == 0) {
                                        viewHolderAudio.txt_audio_time_me.setText("0:" + seconds + "/" + calculateDuration(mediaPlayer1.getDuration()));
                                    } else {
                                        if (seconds >= 60) {
                                            long sec = seconds - (minutes * 60);
                                            viewHolderAudio.txt_audio_time_me.setText(minutes + ":" + sec + "/" + calculateDuration(mediaPlayer1.getDuration()));
                                        }
                                    }
                                } else {
                                    //Displaying total time if audio not playing
                                    int totalTime = mediaPlayer1.getDuration();
                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime);
                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
                                    if (minutes == 0) {
                                        viewHolderAudio.txt_audio_time_me.setText("0:" + seconds);
                                    } else {
                                        if (seconds >= 60) {
                                            long sec = seconds - (minutes * 60);
                                            viewHolderAudio.txt_audio_time_me.setText(minutes + ":" + sec);
                                        }
                                    }
                                }
                                if (mediaPlayer1.getDuration() == mediaPlayer1.getCurrentPosition()) {
                                    viewHolderAudio.iconplayme.setImageResource(R.drawable.ic_baseline_play_me_24);
                                    viewHolderAudio.seekBarMe.setProgress(0);
                                }
                            }

                        };
                        run.run();
                    } else {

                        mediaPlayer1.pause();
                        viewHolderAudio.iconplayme.setImageResource(R.drawable.ic_baseline_play_me_24);
                    }

                }
            });


            if (Cemmon.IMAGE_OF_USER != null) {
                Picasso.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                        .into(viewHolderAudio.imageViewme);
            }
        } else {

            viewHolderAudio.thierMessag.setVisibility(View.VISIBLE);
            viewHolderAudio.myMessage.setVisibility(View.GONE);

            // Initializing MediaPlayer
            final MediaPlayer mediaPlayer1 = new MediaPlayer();

            mediaPlayer1.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer1.setDataSource(Cemmon.BASE_URL + list.get(position).getMessage());
                mediaPlayer1.prepare();// might take long for buffering.
            } catch (IOException e) {
                e.printStackTrace();
            }

            viewHolderAudio.seekBarThier.setMax(mediaPlayer1.getDuration());
            viewHolderAudio.seekBarThier.setTag(position);
            viewHolderAudio.seekBarThier.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
            viewHolderAudio.txt_audio_time_thier.setText("0:00/" + calculateDuration(mediaPlayer1.getDuration()));

            viewHolderAudio.iconplaeThere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewHolderAudio.seekBarThier.setProgress(0);

                    if (!mediaPlayer1.isPlaying()) {

                        mediaPlayer1.start();

                        viewHolderAudio.iconplaeThere.setImageResource(R.drawable.pause_thier);
                        run = new Runnable() {
                            @Override
                            public void run() {
                                // Updateing SeekBar every 100 miliseconds
                                viewHolderAudio.seekBarThier.setProgress(mediaPlayer1.getCurrentPosition());
                                viewHolderAudio.seekBarThier.postDelayed(run, 100);
                                //For Showing time of audio(inside runnable)
                                int miliSeconds = mediaPlayer1.getCurrentPosition();
                                if (miliSeconds != 0) {
                                    //if audio is playing, showing current time;
                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds);
                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds);
                                    if (minutes == 0) {
                                        viewHolderAudio.txt_audio_time_thier.setText("0:" + seconds + "/" + calculateDuration(mediaPlayer1.getDuration()));
                                    } else {
                                        if (seconds >= 60) {
                                            long sec = seconds - (minutes * 60);
                                            viewHolderAudio.txt_audio_time_thier.setText(minutes + ":" + sec + "/" + calculateDuration(mediaPlayer1.getDuration()));
                                        }
                                    }
                                } else {
                                    //Displaying total time if audio not playing
                                    int totalTime = mediaPlayer1.getDuration();
                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime);
                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
                                    if (minutes == 0) {
                                        viewHolderAudio.txt_audio_time_thier.setText("0:" + seconds);
                                    } else {
                                        if (seconds >= 60) {
                                            long sec = seconds - (minutes * 60);
                                            viewHolderAudio.txt_audio_time_me.setText(minutes + ":" + sec);
                                        }
                                    }
                                }
                                if (mediaPlayer1.getDuration() == mediaPlayer1.getCurrentPosition()) {
                                    viewHolderAudio.iconplaeThere.setImageResource(R.drawable.ic_baseline_play_thier);
                                    viewHolderAudio.seekBarThier.setProgress(0);
                                }
                            }

                        };
                        run.run();
                    } else {

                        mediaPlayer1.pause();
                        viewHolderAudio.iconplaeThere.setImageResource(R.drawable.ic_baseline_play_thier);
                    }

                }
            });


            if (Cemmon.IMAGE_OF_USER != null) {
                Picasso.with(mcontext).load(Cemmon.BASE_URL + Cemmon.IMAGE_OF_USER).placeholder(R.drawable.imagerat)
                        .into(viewHolderAudio.imageViewme);
            }


        }
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
        TextView bodyMeMessage, bodyTheirMessage, date;

        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;


        public ViewHolderText(@NonNull View itemView) {
            super(itemView);

            thierMessag = itemView.findViewById(R.id.their_message);
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
        TextView txt_audio_time_me, txt_audio_time_thier;
        CircleImageView imageViewme, imageViewthier;
        ProgressBar proMeMessage, proTheirMessage, date;
        ImageView iconplayme, iconplaeThere;
        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;
        SeekBar seekBarMe, seekBarThier;

        public ViewHolderAudio(@NonNull View itemView) {
            super(itemView);

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
        ImageView bodyMeMessage, bodyTheirMessage, date;

        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;


        public ViewHolderImage(@NonNull View itemView) {
            super(itemView);

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
        ImageView bodyMeMessage, bodyTheirMessage, date;

        public RelativeLayout myMessage, layoutAudiome, getLayoutAudioThere;
        public RelativeLayout thierMessag;


        public ViewHolderLocation(@NonNull View itemView) {
            super(itemView);

            thierMessag = itemView.findViewById(R.id.their_layout_location_there);
            myMessage = itemView.findViewById(R.id.layout_me_location);
            bodyMeMessage = itemView.findViewById(R.id.location_img_body_me);
            imageViewme = itemView.findViewById(R.id.location_View1_img_me);
            imageViewthier = itemView.findViewById(R.id.location_View1item_there);
            bodyTheirMessage = itemView.findViewById(R.id.location_img_body_there);


        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }

    public static class ViewHolderVideo extends RecyclerView.ViewHolder {

      public   ImageView mTexturePreview_me, mTexturePreview_there;
        public   ProgressBar bar_me, bar_there;
        public    ImageView placeholder_me, getPlaceholder_there;
        public    FrameLayout controlHolder_me, controlHolder_there;
        public    RelativeLayout touchLayout_me, touchLayout_there;
        public    CircleImageView img_there, img_me;

        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);

            // xml there
            img_there = itemView.findViewById(R.id.imageView1item_there_video);
            mTexturePreview_there = itemView.findViewById(R.id.video_player_there);
            bar_there = itemView.findViewById(R.id.buffereing_there);
            getPlaceholder_there = itemView.findViewById(R.id.holder_there);
            controlHolder_there = itemView.findViewById(R.id.media_controller_there);
            touchLayout_there = itemView.findViewById(R.id.layout_there);


            // xml me
            img_me = itemView.findViewById(R.id.image_View_item_me_video);
            mTexturePreview_me = itemView.findViewById(R.id.video_player_me);
            bar_me = itemView.findViewById(R.id.buffereing_me);
            placeholder_me = itemView.findViewById(R.id.holder_me);
            controlHolder_me = itemView.findViewById(R.id.media_controller_me);
            touchLayout_me = itemView.findViewById(R.id.layout_me);

        }


//        public void restoreItem(ClipData.Item item, int position) {
//            cartList.add(position, item);
//            // notify item added by position
//            notifyItemInserted(position);
//        }
    }
}
