package app.arxivorg.model;

import java.util.Comparator;
import java.util.HashMap;

class StatsComparator implements Comparator<String> {

    HashMap<String, Integer> map = new HashMap<>();

    public StatsComparator(HashMap<String, Integer> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(map.get(s1) > map.get(s2)) return  -1;
        else if(map.get(s1) > map.get(s2)) return  0;
        return 1;
    }
}