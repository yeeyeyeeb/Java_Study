import java.util.HashMap;

public class CategoryDefaults {
    private static final HashMap<String, Integer> DAYS = new HashMap<String, Integer>();

    static {
        DAYS.put("우유", 10);
        DAYS.put("가공유", 10);
        DAYS.put("발효유", 14);
        DAYS.put("두부", 7);
        DAYS.put("빵", 5);
        DAYS.put("김치", 30);
        DAYS.put("유탕면", 150);
        DAYS.put("햄", 30);
        DAYS.put("소시지", 30);
        DAYS.put("어묵", 14);
        DAYS.put("음료", 90);
        DAYS.put("탄산음료", 365);
        DAYS.put("과자", 180);
        DAYS.put("소스", 180);
    }

    public static int getDays(String foodType) {
        if (foodType == null) return -1;

        String type = foodType.replaceAll("\\s", "");
        if (type.isEmpty()) return -1;

        String bestKey = null;
        int bestDays = -1;
        for (String key : DAYS.keySet()) {
            if (!type.contains(key)) continue;
            int days = DAYS.get(key);
            if (bestKey == null
                    || key.length() > bestKey.length()
                    || (key.length() == bestKey.length() && days < bestDays)) {
                bestKey = key;
                bestDays = days;
            }
        }
        return bestKey == null ? -1 : bestDays;
  
   
    }
}
