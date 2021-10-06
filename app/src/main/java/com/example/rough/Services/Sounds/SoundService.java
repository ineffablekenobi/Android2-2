package com.example.rough.Services.Sounds;

public class SoundService {
    EntryMusicService entryMusicService;
    GameOverMusic gameOverMusic;

    public SoundService(){
        entryMusicService = new EntryMusicService();
        Thread thread = new Thread(entryMusicService);
        thread.start();
        gameOverMusic = new GameOverMusic();
        Thread thread1 =  new Thread(gameOverMusic);
        thread1.start();
    }

    public void playEntryMusic(){
        entryMusicService.PlayMusic();
    }

    public void playGameOverMusic(){
        gameOverMusic.PlayMusic();
    }

}
