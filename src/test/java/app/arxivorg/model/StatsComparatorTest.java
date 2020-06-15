package app.arxivorg.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class StatsComparatorTest {

    @Test
    void compare() {
        HashMap<String, Integer> map = new HashMap<>();
        String b = "bigger"; String s = "smaller";
        map.put(b, 2);
        map.put(s, 1);
        int compareValue;
        if(map.get(s) > map.get(b)) compareValue = -1;
        else if(map.get(s) == map.get(b)) compareValue =  0;
        else compareValue = 1;
        assertTrue(compareValue == 1);

        map.replace(s, 5);
        if(map.get(s) > map.get(b)) compareValue = -1;
        else if(map.get(s) == map.get(b)) compareValue =  0;
        else compareValue = 1;
        assertTrue(compareValue == -1);

        map.replace(b, 5);
        if(map.get(s) > map.get(b)) compareValue = -1;
        else if(map.get(s) == map.get(b)) compareValue =  0;
        else compareValue = 1;
        assertTrue(compareValue == 0);
    }
}