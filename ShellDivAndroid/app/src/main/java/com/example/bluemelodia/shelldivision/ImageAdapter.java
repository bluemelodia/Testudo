package com.example.bluemelodia.shelldivision;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.GridView;
import android.widget.ImageView;
import android.view.View;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bluemelodia on 3/10/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private static final int Empty = 0;
    private static final int Snapper = 1;
    private static final int Sea = 2;
    public int snapperPopulation;
    public int seaPopulation;

    public List<Organism> tileStates = new ArrayList<>();

    public ImageAdapter(Context c) {
        mContext = c;

        // Each organism starts out empty.
        for(int i = 0; i < 64; i++) {
            Organism newOrganism = new Organism();
            newOrganism.setSpecies(Organism.Species.Empty);
            tileStates.add(newOrganism);
        }
    }

    public int getCount() {
        return 64;
    }

    // return the actual object at the specified position in the adapter
    public Object getItem(int position) {
        return null;
    }

    // get the current state of the tile
    public Organism getTileState(int position) {
        return tileStates.get(position);
    }

    // change the state of the tile
    public void setTileState(int position, Organism newState) {
        tileStates.set(position, newState);
    }

    // return the row id of the item
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 75));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAlpha(0.2f);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        // use the current state of the tile to pick which image goes into it
        Organism organism = tileStates.get(position);
        if (organism.getSpecies() == Organism.Species.Empty) {
            imageView.setImageResource(R.drawable.shell);
        } else if (organism.getSpecies() == Organism.Species.Snapper) {
            imageView.setImageResource(R.drawable.snapper);
        } else if (organism.getSpecies() == Organism.Species.Sea) {
            imageView.setImageResource(R.drawable.sea);
        }
        return imageView;
    }

    // eradication methods
    public void interCompetition() {
        ArrayList<Integer> toDie = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            Organism organism = getTileState(i);
            int competitors = 0;
            if (organism.getSpecies().equals(Organism.Species.Empty)) continue;
            if (i%8 != 0) { // left organism
                Organism left = getTileState(i-1);
                if (!left.getSpecies().equals(Organism.Species.Empty) && !left.getSpecies().equals(organism.getSpecies())) {
                    competitors++;
                }
            }
            if (i%8 != 7) { // right organism
                Organism right = getTileState(i+1);
                if (!right.getSpecies().equals(Organism.Species.Empty) && !right.getSpecies().equals(organism.getSpecies())) {
                    competitors++;
                }
            }
            if (i-8 > 0) { // above organism
                Organism up = getTileState(i-8);
                if (!up.getSpecies().equals(Organism.Species.Empty) && !up.getSpecies().equals(organism.getSpecies())) {
                    competitors++;
                }
            }
            if (i+8 < 56) { // below organism
                Organism down = getTileState(i+8);
                if (!down.getSpecies().equals(Organism.Species.Empty) && !down.getSpecies().equals(organism.getSpecies())) {
                    competitors++;
                }
            }
            Log.i("Competitors:", String.valueOf(competitors));
            if (competitors > 3) toDie.add(Integer.valueOf(i));
        }

        // convert the organisms that were outcompeted
        for (int i = 0; i < toDie.size(); i++) {
            Organism deadOrg = getTileState(toDie.get(i));
            Log.i("Dead org:", String.valueOf(toDie.get(i)));
            if (deadOrg.getSpecies().equals(Organism.Species.Snapper)) {
                deadOrg.setSpecies(Organism.Species.Sea);
            } else if (deadOrg.getSpecies().equals(Organism.Species.Sea)) {
                deadOrg.setSpecies(Organism.Species.Snapper);
            }
            setTileState(toDie.get(i), deadOrg);
        }
    }

    // count population methods
    public int getSnapperPopulation() {
        return snapperPopulation;
    }

    public int getSeaPopulation() {
        return seaPopulation;
    }

    public void countPopulation() {
        snapperPopulation = 0;
        seaPopulation = 0;
        for(int i = 0; i < 64; i++) {
            Organism organism = getTileState(i);
            if (organism.getSpecies().equals(Organism.Species.Snapper)) snapperPopulation++;
            else if (organism.getSpecies().equals(Organism.Species.Sea)) seaPopulation++;
        }
    }
}
