import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.of(2026, 11, 2);

        // 1. ShelfLifeParser
        System.out.println("=== 1. ShelfLifeParser ===");
        System.out.println(ShelfLifeParser.parseDays("제조일로부터 180일"));
        System.out.println(ShelfLifeParser.parseDays("제조일로부터 1년 6개월"));
        System.out.println(ShelfLifeParser.parseDays("별도표기일까지"));

        // 2. DateCalculator
        System.out.println("=== 2. DateCalculator ===");
        System.out.println(DateCalculator.calcExpiryDate(today, 10));
        System.out.println(DateCalculator.formatDday(3) + " "
                + DateCalculator.formatDday(0) + " " + DateCalculator.formatDday(-2));
        System.out.println(DateCalculator.isNearExpiry(today, LocalDate.of(2026, 11, 3), 1));
        System.out.println(DateCalculator.isOverdue(today, LocalDate.of(2026, 10, 30), 2));

        // 3. FoodItem
        System.out.println("=== 3. FoodItem ===");
        FoodItem milk = new FoodItem("우유 1L", "8800000000001", today, LocalDate.of(2026, 11, 10));
        System.out.println(milk);
        milk.markEaten(LocalDate.of(2026, 11, 3));
        System.out.println(milk);

        // 4. 통합: 유통기한 텍스트 → 일수 → 날짜 → FoodItem → FoodManager
        System.out.println("=== 4. 통합 ===");
        FoodManager manager = new FoodManager();
        String[][] products = {
            {"두부", "8800000000002", "제조일로부터 1일"},
            {"라면", "8800000000003", "제조일로부터 6개월"},
            {"요거트", "8800000000004", "10일"},
        };
        for (String[] p : products) {
            int days = ShelfLifeParser.parseDays(p[2]);
            LocalDate expiry = DateCalculator.calcExpiryDate(today, days);
            int id = manager.add(new FoodItem(p[0], p[1], today, expiry));
            System.out.println("등록 id " + id + ": " + p[0] + " " + expiry
                    + " (" + DateCalculator.formatDday(DateCalculator.getDday(today, expiry)) + ")");
        }
        // 유통기한 지난 우유 하나 추가
        manager.add(new FoodItem("우유", "8800000000001", LocalDate.of(2026, 10, 25), LocalDate.of(2026, 10, 30)));

        System.out.println("임박: " + manager.getNearExpiryItems(today, 2));
        System.out.println("자동 폐기: " + manager.autoDiscardOverdue(today, 2) + "개");
        manager.markEaten(2, today);
        System.out.println("우유 폐기 횟수: " + manager.countDiscarded("8800000000001"));
        System.out.println("전체 " + manager.size() + "개 / 보관중:");
        for (FoodItem f : manager.getStoredItems()) {
            System.out.println(f);
        }
    }
}