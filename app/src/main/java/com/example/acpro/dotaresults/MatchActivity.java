package com.example.acpro.dotaresults;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity {

    public Elements content, content1, content2, content3, content4, content5;

    public ArrayList<Match> historyList = new ArrayList<>();
    public ArrayList<String> matchUrlList = new ArrayList<>();
    private ListView historyListView;
    private MatchAdapter historyAdapter;
    TextView textContest, textDate, textScore, textTeamL, textTeamR, textBestOf, textWinRateL, textWinRateR;
    String contest, date, score, teamL, teamR, bestOf, winRateL, winRateR, draw, urlImgTeamL, urlImgTeamR, urlNoImage, urlMatch;
    ImageView imageTeamL, imageTeamR;
    ProgressBar meeting;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        historyAdapter = new MatchAdapter(this, historyList);
        Intent intent = getIntent();
        urlMatch = intent.getStringExtra("num");
        urlMatch = "https://www.cybersport.ru" + urlMatch;
        urlNoImage = "/assets/img/no-photo/no-photo-dota2.png?v=f3dba7c8fb17df0eec92f11acc9eb638";
        System.out.println("MATCHACTIVIVTY " + urlMatch);

        imageTeamL = findViewById(R.id.imageTeamL);
        imageTeamR = findViewById(R.id.imageTeamR);

        textContest = findViewById(R.id.textContest);
        textDate = findViewById(R.id.textDate);
        textScore = findViewById(R.id.textScore);
        textTeamL = findViewById(R.id.textTeamLeft);
        textTeamR = findViewById(R.id.textTeamRight);
        textBestOf = findViewById(R.id.textBestOf);
        textWinRateL = findViewById(R.id.textWinRateL);
        textWinRateR = findViewById(R.id.textWinRateR);
        meeting = findViewById(R.id.progressBar2);
        historyListView = findViewById(R.id.historyList);
        loading = findViewById(R.id.progressBar3);
        new NewThreadn().execute();
        final Intent sendHistory = new Intent(this, MatchActivity.class);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sendHistory.putExtra("num", matchUrlList.get(i) );
                System.out.println("number of item " + i +"\n" + "url: " + matchUrlList.get(i));
                startActivity(sendHistory);
            }
        });

    }

    public class NewThreadn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document document;
            try {
                document = Jsoup.connect(urlMatch).get();
                content = document.select(".duel__inner");
                content1 = document.select(".duel__wrapper");
                content2 = document.select(".duel__team--left");
                content3 = document.select(".duel__team--right");
                content4 = document.select(".duel__meeting");
                content5 = document.select(".matche__entrant");
                contest = content1.select(".center-text").select("a").text();
                date = content.select(".duel__score").select("time").text();
                score = content.select(".duel__spoiler").text();
                teamL = content2.select(".duel__logo").select(".duel__title").text();
                teamR = content3.select(".duel__logo").select(".duel__title").text();
                bestOf = content1.select(".duel__score-maps").text();
                urlImgTeamL = content2.select(".duel__logo").select("img").attr("src");
                urlImgTeamR = content3.select(".duel__logo").select("img").attr("src");
                winRateL = content4.select(".duel__points--left").select(".percentage").text();
                winRateR = content4.select(".duel__points--right").select(".percentage").text();
                draw = content4.select(".duel__points--centre").select(".percentage").text();

                historyList.clear();
                for (Element content: content5){
                    Match match = new Match();
                    match.setTeamL(content.select(".matche__team--left .team__name .hidden-xs--inline-block").text());
                    match.setScore(content.getElementsByClass("matche__score").text());
                    match.setTeamR(content.select(".matche__team--right .team__name .hidden-xs--inline-block").text());
                    String urlMatch = content.select(".matche__score").select("a").attr("href");
                    if (!match.getScore().equals("") && !match.getTeamL().equals("") && !match.getTeamR().equals("")) {
                        historyList.add(match);
                        matchUrlList.add(urlMatch);
                    }
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            textContest.setText(contest);
            textDate.setText(date);
            textScore.setText(score);
            textTeamL.setText(teamL);
            textTeamR.setText(teamR);
            textBestOf.setText(bestOf);
            textWinRateL.setText(winRateL);
            textWinRateR.setText(winRateR);
            int factor = Integer.parseInt(draw.substring(0,draw.length()-1));
            int progress = Integer.parseInt(winRateL.substring(0,winRateL.length()-1));
            meeting.setMax(100-factor);
            meeting.setProgress(progress);
            Context context = getApplicationContext();
            if (!urlImgTeamL.contains("assets")) {
                Glide
                        .with(context)
                        .load(urlImgTeamL)
                        .into(imageTeamL);
            }else {
                imageTeamL.setImageResource(R.drawable.no_image);
            }

            if (!urlImgTeamR.contains("assets")) {
                Glide
                        .with(context)
                        .load(urlImgTeamR)
                        .into(imageTeamR);
            }else {
                imageTeamR.setImageResource(R.drawable.no_image);
            }
            historyListView.setAdapter(historyAdapter);
            loading.setVisibility(View.GONE);
        }
    }
}
