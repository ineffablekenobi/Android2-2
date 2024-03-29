package com.example.rough;

import static java.lang.Math.min;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.rough.DTO.Audio;
import com.example.rough.Services.CheckTrackService;
import com.example.rough.Services.MediaPlayerLoaderService;
import com.example.rough.Services.SkipTrackService;
import com.example.rough.Services.Sounds.SoundService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends AppCompatActivity{
    MediaPlayer m1;
    MediaPlayer m2;
    ImageButton pp;
    EditText writingSpace;
    DocumentReference reference;
    FirebaseFirestore db;
    public static ArrayList<Audio> audioList;

    //Services
    SkipTrackService skipTrackService;
    MediaPlayerLoaderService mediaPlayerLoaderService;
    Audio currentAudio;
    SoundService soundService;

    int sessionPlayIndex ;
    int skipSoundIndex;
    public static final int audioListSize = 15;
    private static int[][] stringCheckDp;

    //==========
    ProgressBar audioProgress;//
    CountDownTimer audioTimer;
    //==========
    TextView tv321, serial;
    Animation fadeOut;
    Button checkBtn, skipBtn;
    //=========
    private final int maxPlayLimit = 2;
    int playLimit = maxPlayLimit;
    private final int maxCheckLimit = 2;
    int checkLimit = maxCheckLimit;
    LottieAnimationView animView;
    LottieAnimationView loadingView;

    public static ArrayList<String> skipSoundSources;



    public static int score = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // when you start the app this function is executed
        // pause feature is removed
        super.onCreate(savedInstanceState);
        this.sessionPlayIndex = 0;
        m2 = new MediaPlayer();
        try {
            m2.setDataSource("https://static.wikia.nocookie.net/dota2_gamepedia/images/1/14/Vo_axe_axe_deny_15.mp3");
            m2.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getData();


        setContentView(R.layout.activity_main);
        //================loading screen
        animView = (LottieAnimationView) findViewById(R.id.animationView);
        loadingView = (LottieAnimationView) findViewById(R.id.loadingtext);



        //==================================
        writingSpace = findViewById(R.id.writingspace);

        //Button signInWithGoogleBtn = findViewById(R.id.signinwithgoogle);

        //signInWithGoogleBtn.setOnClickListener(V->signInWithGoogle());

        audioProgress = (ProgressBar) findViewById(R.id.audioProgress);
        pp = (ImageButton) findViewById(R.id.ppf);
        tv321 = (TextView) findViewById(R.id.tv321);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        checkBtn = (Button) findViewById(R.id.checkanswerbtn);
        skipBtn = (Button) findViewById(R.id.skipbtn);
        serial = (TextView) findViewById(R.id.serial);
        score = 0;
        serial.setText("Audio " + String.valueOf(sessionPlayIndex + 1));
        flex();

        checkBtn.setEnabled(false);

        writingSpace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(MainActivity.this, "Listen First", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        initializePlaySkipTrack();

    }

    private void flex() {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                loadGame();

            }

        }.start();
    }



    public void checkButton(View view){


        if(checkLimit > 0) {
            Log.d("bug", String.valueOf(checkLimit));
            checkLimit--;
            checkAnswer();

        }

        if(checkLimit == 0) {
            checkBtn.setVisibility(View.GONE);
            setPlayBtnDisabled(true);
            playLimit = 0;
            skipBtn.setText("Next");
        }

        // added dependency requires api 23 minimum
        // so it might compile now but be aware
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);



    }

    private int stringComp(String str1, String str2, int i, int j){
        if(i == 0){
            return j;
        }

        if(j == 0){
            return i;
        }

        if(stringCheckDp[i][j] != 1e9){
            return stringCheckDp[i][j];
        }

        if(str1.charAt(i - 1) == str2.charAt(j - 1)){
            return stringCheckDp[i][j] = stringComp(str1, str2, i-1, j-1);
        }

        int ans = (int)1e9;

        //add
        ans =  min(ans , stringComp(str1, str2, i, j-1) + 1);
        // remove
        ans = min(ans, stringComp(str1, str2, i-1, j) + 1);
        //replace
        ans = min(ans,stringComp(str1, str2, i-1, j- 1) + 1);

        return stringCheckDp[i][j] = ans;

    }

    private void checkAnswer() {
        // check the answer here
        //if answers is correct
        String answer = writingSpace.getText().toString();

        int p = 0;
        answer = answer.toLowerCase();
        String correctAnswer = currentAudio.getValidAnswer();
        boolean correct = true;
        String compString = "";
        for(int i = 0; i < answer.length(); i++){
            if(answer.charAt(i) >= 'a' && answer.charAt(i) <= 'z') {
                compString += answer.charAt(i);
            }
        }


        Log.d("Answer Comparison : " + compString, correctAnswer);


        //String comparison using dp
        stringCheckDp = new int[compString.length() + 1][correctAnswer.length() + 1];

        for(int i = 0; i < compString.length() + 1; i++){
            for(int j = 0; j < correctAnswer.length() + 1; j++){
                stringCheckDp[i][j] = (int)1e9;
            }
        }

        // finding the error percentage
        double error =   stringComp(compString, correctAnswer, compString.length(), correctAnswer.length()) + 0.00;
        double goodPercentage = 100.00 -  (error/correctAnswer.length()) * 100;

        // rounding up to 2 decimal places
        BigDecimal bd = new BigDecimal(goodPercentage).setScale(2, RoundingMode.HALF_UP);
        goodPercentage = bd.doubleValue();

        if(goodPercentage < 0){
            goodPercentage = 0;
        }

        //Toast.makeText(this, "Your answer is " + goodPercentage +"% correct", Toast.LENGTH_LONG).show();

        if(goodPercentage >= 80) {
            //score count
            //change according to rules
            if(goodPercentage == 100){
                Thread t1 = new Thread(new CheckTrackService());
                t1.start();

            }


            goodPercentage -= (maxPlayLimit - playLimit - 1) * 10;
            goodPercentage -= (maxCheckLimit - checkLimit - 1) * 15;
            score += (int)goodPercentage;
            Toast.makeText(this, "You've got " + String.valueOf(((int)goodPercentage)) + " points (Total: " + String.valueOf(score) + " )", Toast.LENGTH_SHORT).show();
            skipAudio();
        }else{
            Toast.makeText(this,"Not Good Enough", Toast.LENGTH_SHORT).show();
        }

        writingSpace.setText("");
    }

    private void loadGame() {
        animView.clearAnimation();
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        animView.setVisibility(View.GONE);
        checkBtn.setVisibility(View.VISIBLE);
        skipBtn.setVisibility(View.VISIBLE);
    }
    private void getData() {
        audioList = new ArrayList<>();
        skipSoundSources = new ArrayList<>();

        for(int i  = 0; i < audioListSize; i++) {
            reference = FirebaseFirestore.getInstance().collection("Audio").document(i+"");

            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String validAnswer = documentSnapshot.getString("ValidAnswer");
                        String dataSource = documentSnapshot.getString("DataSource");
                        audioList.add(new Audio(validAnswer,dataSource));
                        if(audioList.size() == audioListSize) {
                            mediaPlayerLoaderService = new MediaPlayerLoaderService(audioList);
                            Thread mediaLoader = new Thread(mediaPlayerLoaderService);
                            mediaLoader.start();
                        }
                        }else{
                        Log.d("this doesnt work", "GG");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("this doesnt work", "GG");
                }
            });

            Log.d("delay" , "done fetching data");


            //loadGame();
        }



        db = FirebaseFirestore.getInstance();
        //processLeaderBoard();
        db.collection("SkipSounds")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                skipSoundSources.add((String) document.get("dataSource"));
                            }

                        } else {
                            Log.w("", "Error getting documents.", task.getException());
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                initializePlaySkipTrack();
                Log.d("String Array SIZE ", skipSoundSources.size() + "" );
            }
        });
    }

    public void play(){
        // the function is called from design
        // when you press the play button the function is a on click listener

        if(m1 == null){
            try {
                Log.d("play: ", "trying");
                startMedia();
                Log.d("play", "could play");
            }catch (IOException io){
                Log.d("ExceptionLog: ", "StarMedia Calling time exception");
            }
        }else{
            stopMedia();
        }

    }
    private void progressWork(int dur) {
        Log.d("progresswork() : ", "doing progress");
        final int duration = dur;//m1.getDuration();
        final long progress = duration/100;

        audioTimer = new CountDownTimer(duration, progress) {
            @Override
            public void onTick(long l) {
                Log.d("ontick: ", " boo");
                if(true){//progress*audioProgress.getProgress() < 100) {

                    int p = audioProgress.getProgress();
                    p+=1;
                    audioProgress.setProgress(p);
                    Log.d("progress: " , String.valueOf(audioProgress.getProgress()));
                }
            }

            @Override
            public void onFinish() {
                audioProgress.setProgress(100);
            }
        }.start();

//        Timer audioTimer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Log.d("inside run: ", " boo");
//                if(progress*audioProgress.getProgress() < 100) {
//
//                    int p = audioProgress.getProgress();
//                    p+=1;
//                    audioProgress.setProgress(p);
//                    Log.d("progress: " , String.valueOf(audioProgress.getProgress()));
//                }
//            }
//        };
//        audioTimer.schedule(timerTask, 0, progress);

    }

    private void startMedia() throws IOException {

        m1 = mediaPlayerLoaderService.getMediaPlayer(sessionPlayIndex);
        currentAudio = mediaPlayerLoaderService.getCurrentAudio(sessionPlayIndex);
        m1.start();
        checkBtn.setEnabled(true);

        writingSpace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        pp.setImageResource(R.drawable.stop_foreground);
        pp.setColorFilter(Color.argb(220, 46, 136, 26));
        progressWork(m1.getDuration());

        m1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            // linking the listener so it gets called after music is complete

            @Override
            public void onCompletion(MediaPlayer mp) {
                //Log.d("Oncompletion", "is called");

                stopMedia();

                if(playLimit > 0 && !pp.isClickable()) {
                    setPlayBtnDisabled(false); //play btn is now enable(clickable)
                    mediaPlayerLoaderService.reloadMedia(currentAudio, sessionPlayIndex);
                }
            }
        });

    }

    private void stopMedia() {
        // planning to use this function to stop any media
        if(m1 != null) {
            m1.release();
            m1 = null;
            pp.setImageResource(R.drawable.play_foreground);
            pp.setColorFilter(Color.argb(255, 253, 68, 11));
            audioTimer.cancel();
            audioProgress.setProgress(0);
        }


    }

    public void initializePlaySkipTrack(){
        // This function is called to load the tracks from checkTrackService
        skipTrackService = new SkipTrackService(skipSoundSources);
        Thread trackLoader = new Thread(skipTrackService);
        trackLoader.start();
    }

    public void skip(View view)  {
        skipTrackService.playSound();
        skipAudio();

        writingSpace.setText("");
        audioProgress.setProgress(0);
        if(playLimit == 0 || checkLimit == 0 ) Toast.makeText(this, "Next Audio", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "skipped ", Toast.LENGTH_SHORT).show();

    }
    private void checkEndOfList() {
        if(sessionPlayIndex == audioListSize - 1) {
            finish();
            startActivity(new Intent(this, YourScore.class));
            serial.setText("Audio " + String.valueOf(sessionPlayIndex + 1));

        }
    }
    private void skipAudio(){

        writingSpace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(MainActivity.this, "Listen First", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //Log.d("buggy", "skipaudio");
        checkLimit = maxCheckLimit; //reset check limit
        playLimit = maxPlayLimit; //reset play limit
        checkBtn.setVisibility(View.VISIBLE);
        setPlayBtnDisabled(false);
        skipBtn.setText("Skip");


        checkEndOfList();


        stopMedia();
        this.sessionPlayIndex++;
        this.sessionPlayIndex = this.sessionPlayIndex % audioListSize;



        if(sessionPlayIndex != 0) serial.setText("Audio " + String.valueOf(sessionPlayIndex + 1));
        pp.setColorFilter(Color.argb(255, 228, 178, 28));

    }

    public void stop(View v2){
        stopMedia();
    }

    public void setPlayBtnDisabled(boolean disable) {
        if(disable) {
            pp.setClickable(false);
            pp.setBackgroundResource(R.drawable.circle);
        }
        else {
            pp.setClickable(true);
            pp.setBackgroundResource(R.drawable.presseffect);
        }
    }
    // countdown 3 2 1 before playing audio
    public void playAudio321(View v1) {
        playLimit--;
        setPlayBtnDisabled(true);
        skipBtn.setClickable(false);

        skipBtn.setText(".....");
        //tv321.setText("3");
        tv321.setVisibility(View.VISIBLE);
        Log.d("init play", "starting playAudio321");
        final Handler handler = new Handler();

        Log.d("init play", "handler & tv placed");
        final java.util.concurrent.atomic.AtomicInteger n = new AtomicInteger(3);

        final Runnable counter = new Runnable() {
            @Override
            public void run() {

                Log.d("loading", "runn");
                tv321.setText(Integer.toString(n.get()));
                if (n.getAndDecrement() >= 1) {
                    tv321.startAnimation(fadeOut);


                    handler.postDelayed(this, 1000);
                }
                else {
                    Log.d("loading" , "end");
                    tv321.setVisibility(View.GONE);
                    skipBtn.setClickable(true);
                    if(playLimit != 0) {
                        skipBtn.setText("Skip");
                    }
                    else {
                        skipBtn.setText("Next");
                    }
                    // start the game
                    play();
                }
            }
        };

        handler.postDelayed(counter, 1000);
    }


}