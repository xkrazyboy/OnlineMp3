package myapp.onlinemp3.Interfaces;

import java.util.ArrayList;

import myapp.onlinemp3.Item.ItemSong;

public interface SongListener {
    void onStart();
    void onEnd(String success, ArrayList<ItemSong> arrayListSong);
}
