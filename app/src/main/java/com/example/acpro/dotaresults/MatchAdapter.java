package com.example.acpro.dotaresults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MatchAdapter extends BaseAdapter {
    private List<Match> list;
    private LayoutInflater layoutInflater;

    public MatchAdapter(Context context, List<Match> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        if (view1 == null){
            view1 = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        }
        Match match = getMatch(i);

        TextView textTeamL = (TextView) view1.findViewById(R.id.textTeamL);
        TextView textScore = (TextView) view1.findViewById(R.id.textScore);
        TextView textTeamR = (TextView) view1.findViewById(R.id.textTeamR);

        textTeamL.setText(match.getTeamL());
        textScore.setText(match.getScore());
        textTeamR.setText(match.getTeamR());


        return view1;
    }

    private Match getMatch(int pos){
        return (Match) getItem(pos);
    }
}

