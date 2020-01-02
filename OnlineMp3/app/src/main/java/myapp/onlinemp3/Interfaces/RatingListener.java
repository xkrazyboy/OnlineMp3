package myapp.onlinemp3.Interfaces;

public interface RatingListener {
    void onStart();
    void onEnd(String success, String message, int rating);
}
