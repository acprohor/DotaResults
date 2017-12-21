package com.example.acpro.dotaresults;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class FutureListActivity extends AppCompatActivity {

    public Elements content, content2;

    public ArrayList<String> matchUrlList = new ArrayList<>();
    public ArrayList<Match> matches = new ArrayList<>();
    private MatchAdapter myAdapter;
    private ListView listView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listView);
        Button buttonPresent = findViewById(R.id.buttonPresent);
        Button buttonFuture = findViewById(R.id.buttonFuture);
        Button buttonLast = findViewById(R.id.buttonLast);
        final Intent intentFuture = new Intent(this, FutureListActivity.class);
        buttonFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentFuture);
            }
        });

        final Intent intent = new Intent(this, PresentListActivity.class);
        buttonPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        final Intent intentLast = new Intent(this, MainActivity.class);
        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentLast);
            }
        });

        new NewThread3().execute();
        myAdapter = new MatchAdapter(this, matches);
        final Intent intentMatch = new Intent(this, MatchActivity.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intentMatch.putExtra("num", matchUrlList.get(i) );
                System.out.println("number of item " + i +"\n" + "url: " + matchUrlList.get(i));
                startActivity(intentMatch);
            }
        });
    }

    public class NewThread3 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            Document document;
            Document document2;
            try {
                document = Jsoup.connect("https://www.cybersport.ru/base/match/list/disciplines/dota2/status/future/page/1/search").userAgent("Mozilla").get();
                document2 = Jsoup.connect("https://dota2.ru/esport/matches/").userAgent("Mozilla").get();
                content = document.select(".matche__entrant");
                content2 = document2.select(".team-vs-team");

                matches.clear();

                for (Element content: content){
                    Match match = new Match();
                    match.setTeamL(content.select(".matche__team--left .team__name .hidden-xs--inline-block").text());
                    match.setScore(content.getElementsByClass("matche__score").text());
                    match.setTeamR(content.select(".matche__team--right .team__name .hidden-xs--inline-block").text());
                    String urlMatch = content.select(".matche__score").select("a").attr("href");
                    if (!match.getScore().equals("") && !match.getTeamL().equals("") && !match.getTeamR().equals("")
                            && !match.getTeamL().equals("TBD") && !match.getTeamR().equals("TBD")
                            && !match.getTeamL().equals("TBA") && !match.getTeamR().equals("TBA")) {
                        matches.add(match);
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
            listView.setAdapter(myAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }
}
