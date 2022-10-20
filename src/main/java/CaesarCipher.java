import java.util.HashMap;

public class CaesarCipher {
    public static final String alphanumeric = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~абвгґдеєжзиіїйклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИІЇЙКЛМНОПРСТУФХЦЧШЩЬЮЯ";
    public static final HashMap<Character, Integer> index;
    public static final HashMap<Integer, Character> revIndex;
    public static final Integer size = alphanumeric.length();

    static {
        char[] chars = alphanumeric.toCharArray();
        index = new HashMap<>();
        revIndex = new HashMap<>();

        int step = 0;
        for (char c : chars) {
            index.put(c, step);
            revIndex.put(step, c);
            step++;
        }
    }

    public static String cipher(String message, int offset, int cr) {
        if (cr == 1) return cipher(message, offset);
        else {
            while (cr >= 1) {
                message = cipher(message, offset);
                cr--;
            }
            return message;
        }
    }

    private static String cipher(String message, int offset) {
        StringBuilder result = new StringBuilder();
        for (char character : message.toCharArray()) {
            if (character != ' ') {
                int newAlphabetPosition = (index.get(character) + offset) % size;
                char newCharacter = revIndex.get(newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public static String decipher(String message, int offset, int cr) {
        return cipher(message, size - (offset % size), cr);
    }
}
