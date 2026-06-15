import java.util.*;

public class QuestionRepository {
    /**
     * Metode ini berfungsi untuk menyediakan daftar soal.
     * Sesuai prinsip OCP, jika ingin menambah soal, kita cukup menambahkannya di sini
     * tanpa perlu merusak kodingan utama di GamePanel.java.
     */
    public static List<Question> getAllQuestions() {
        List<Question> list = new ArrayList<>();

        // Soal 1
        list.add(new Question("Warna kabel straight?", 
            Arrays.asList("Putih-Oranye", "Oranye"), 
            Arrays.asList("Biru", "Hijau", "Coklat")));

        // Soal 2
        list.add(new Question("Bahasa pemrograman web?", 
            Arrays.asList("JavaScript", "PHP", "Python", "Java"), 
            Arrays.asList("Photoshop", "Corel")));

        // Soal 3
        list.add(new Question("3 jaringan berdasarkan cakupanya?", 
            Arrays.asList("LAN", "WAN", "MAN"), 
            Arrays.asList("Provider", "Indihome", "Telkomsel", "Axis")));

        // Soal 4
        list.add(new Question("Warna RGB dalam desain grafis?", 
            Arrays.asList("Red", "Green", "Blue"), 
            Arrays.asList("Rust", "Gangga", "Burgundy", "Raisin")));

        // Soal 5
        list.add(new Question("Tipe data dalam pemrograman?", 
            Arrays.asList("Int", "String", "Float", "Boolean", "Char"), 
            Arrays.asList("Enum", "Array", "Struct", "Class", "List")));

        // Soal 6
        list.add(new Question("Perangkat input komputer?", 
            Arrays.asList("Keyboard", "Mouse", "Scanner"), 
            Arrays.asList("Monitor", "Printer", "Speaker")));

        // Soal 7
        list.add(new Question("Contoh OS komputer?", 
            Arrays.asList("Windows", "Linux", "MacOS"), 
            Arrays.asList("Photoshop", "Corel", "Figma")));

        // Soal 8
        list.add(new Question("Bahasa pemrograman mobile?", 
            Arrays.asList("Kotlin", "Java"), 
            Arrays.asList("HTML", "CSS", "Excel")));

        // Soal 9
        list.add(new Question("Perangkat jaringan?", 
            Arrays.asList("Router", "Switch", "Modem"), 
            Arrays.asList("Keyboard", "Monitor", "Mouse")));

        // Soal 10
        list.add(new Question("Software desain grafis?", 
            Arrays.asList("Photoshop", "CorelDraw"), 
            Arrays.asList("Windows", "Linux", "Chrome")));

        return list;
    }
}