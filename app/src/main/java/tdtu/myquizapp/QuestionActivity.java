package tdtu.myquizapp;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {
    private static final String FILE_NAME="MYQUIZAPP";
    private static final String KEY_NAME="QUESTIONS";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private TextView question,noIndicator;
    private FloatingActionButton bookmarkBtn;
    private LinearLayout optionsContainer;
    private Button shareBtn,nextBtn;
    private int count;
    private List<QuestionModel> list;
    private int position=0;
    private int score=0;
    private String category;
    private int setNo;
    private Dialog loadingDialog;

    private List<QuestionModel> bookmarksList;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private int matchedQuestionPostition;
    private InterstitialAd interstitialAd;

    private ImageView imageView;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadAds();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        question=findViewById(R.id.question);
        noIndicator=findViewById(R.id.no_indicator);
        bookmarkBtn=findViewById(R.id.bookmark_btn);
        optionsContainer=findViewById(R.id.options_container);
        shareBtn=findViewById(R.id.share_btn);
        nextBtn=findViewById(R.id.next_btn);



        preferences=getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        gson=new Gson();

        getBookmarks();
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modelMatch()){
                    bookmarksList.remove(matchedQuestionPostition);
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                }else {
                    bookmarksList.add(list.get(position));
                    bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                }
            }
        });

        category=getIntent().getStringExtra("category");
        setNo=getIntent().getIntExtra("setNo",2);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        list=new ArrayList<>();

        loadingDialog.show();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    list.add(snapshot1.getValue(QuestionModel.class));

                }


                Glide.with(QuestionActivity.this)
                        .load(list.get(position).getUrl())
                        .into(imageView);
                if(list.size()>0){

                    for(int i=0;i<4;i++){

                        optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkAnswer((Button) view);

                            }
                        });
                    }
                    playAnim(question,0,list.get(position).getQuestion());

                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {



                            nextBtn.setEnabled(false);
                            nextBtn.setAlpha(0.7f);
                            enableOption(true);


                            position++;
                            if(position==list.size()){
                                if (interstitialAd.isLoaded()) {

                                    interstitialAd.show();
                                    return;
                                }
                                Intent scoreIntent = new Intent(QuestionActivity.this,ScoreActivity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("total",list.size());
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }

                            count =0;

                            if(list.get(position).getUrl()==null){
                                imageView.setVisibility(View.GONE);

                            }
                            else {
                                imageView.setVisibility(View.VISIBLE);


                                Glide.with(QuestionActivity.this)
                                        .load(list.get(position).getUrl())
                                        .into(imageView);
                            }
                            playAnim(question,0,list.get(position).getQuestion());

                        }
                    });
                    shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String body=list.get(position).getQuestion()+"\n"+

                                    "A "+list.get(position).getOptionA() + "\n"+
                                    "B "+list.get(position).getOptionB() +"\n"+
                                    "C "+list.get(position).getOptionC() +"\n"+
                                    "D "+list.get(position).getOptionD() ;
                            Intent shareIntent =new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Câu hỏi bộ đề thi lái xe");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(shareIntent,"Share now...."));


                        }
                    });
                }
                else {
                    finish();
                    Toast.makeText(QuestionActivity.this,"no questions",Toast.LENGTH_SHORT).show();
                }


                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    private void playAnim(final View view, final int value, final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                if (value == 0&& count<4) {
                    String option="";

                    if (count == 0) {
                        option = list.get(position).getOptionA();
                    } else if (count == 1) {
                        option = list.get(position).getOptionB();
                    } else if (count == 2) {
                        option = list.get(position).getOptionC();
                    } else if (count == 3) {
                        option = list.get(position).getOptionD();
                    }
                    playAnim(optionsContainer.getChildAt(count), 0, option);
                    count++;
                }

            }
            @Override
            public void onAnimationEnd(Animator animator) {

                if(value==0){
                    try{
                        ((TextView)view).setText(data);
                        noIndicator.setText(position+1+"/"+list.size());
                        if(modelMatch()){

                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark));
                        }else {

                            bookmarkBtn.setImageDrawable(getDrawable(R.drawable.bookmark_border));
                        }
                    }catch (ClassCastException ex){
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view,1,data);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    private void checkAnswer(Button selectOption){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if(selectOption.getText().toString().equals(list.get(position).getCorrectANS())){
            score++;
            selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }else {
            selectOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctOption =(Button) optionsContainer.findViewWithTag(list.get(position).getCorrectANS());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }
    private void enableOption(boolean enable){
        for(int i=0;i<4;i++){
            optionsContainer.getChildAt(i).setEnabled(enable);
            if(enable){
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#dbd3d3")));
            }
        }
    }
    private void getBookmarks(){
        String json=preferences.getString(KEY_NAME,"");
        Type type = new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarksList = gson.fromJson(json,type);
        if(bookmarksList == null){
            bookmarksList=new ArrayList<>();
        }
    }
    private boolean modelMatch(){
        boolean matched = false;
        int i =0;
        for(QuestionModel model: bookmarksList){
            if(model.getQuestion().equals(list.get(position).getQuestion())
                && model.getCorrectANS().equals(list.get(position).getCorrectANS())
                && model.getSetNo() ==list.get(position).getSetNo()){
                matched=true;
                matchedQuestionPostition=i;
            }
            i++;
        }
        return matched;
    }
    private void storeBookmarks(){
        String json = gson.toJson(bookmarksList);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }
    private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitialAd_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent scoreIntent = new Intent(QuestionActivity.this,ScoreActivity.class);
                scoreIntent.putExtra("score",score);
                scoreIntent.putExtra("total",list.size());
                startActivity(scoreIntent);
                finish();
                return;
            }
        });
    }
}
