import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {

    private static Clip loopClip; // khusus untuk opening

    public static void play(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            // 🎵 kalau opening → loop terus
            if (path.contains("Opening")) {
                loopClip = clip;
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start(); // sound biasa
            }

        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
    }

    // ⛔ stop opening (kalau perlu)
    public static void stopLoop() {
        if (loopClip != null && loopClip.isRunning()) {
            loopClip.stop();
        }
    }
}