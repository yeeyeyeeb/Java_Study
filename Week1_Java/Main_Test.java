import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main_Test {
    static int pass = 0;
    static int fail = 0;

    // 기대값과 실제값 비교해서 PASS/FAIL 출력
    static void check(String label, Object expected, Object actual) {
        boolean ok = (expected == null) ? (actual == null) : expected.equals(actual);
        if (ok) {
            pass++;
            System.out.println("  [PASS] " + label);
        } else {
            fail++;
            System.out.println("  [FAIL] " + label);
            System.out.println("         기대: " + expected);
            System.out.println("         실제: " + actual);
        }
    }

    // 두 FoodItem의 저장 대상 필드가 모두 같은지 비교
    static boolean same(FoodItem a, FoodItem b) {
        return a.getName().equals(b.getName())
                && a.getBarcode().equals(b.getBarcode())
                && a.getStatus() == b.getStatus()
                && a.getRegisteredDate().equals(b.getRegisteredDate())
                && a.getExpiryDate().equals(b.getExpiryDate())
                && java.util.Objects.equals(a.getResolvedDate(), b.getResolvedDate());
    }

    public static void main(String[] args) throws Exception {
        LocalDate today = LocalDate.of(2026, 11, 2);

        testCategoryDefaults();
        testFoodStorage(today);
        testAlertPlanner(today);
        testRepurchaseChecker(today);

        System.out.println();
        System.out.println("===== 결과: PASS " + pass + " / FAIL " + fail + " =====");
    }

    // ---------------------------------------------------------------
    static void testCategoryDefaults() {
        System.out.println("=== 1. CategoryDefaults ===");
        check("우유 → 10", 10, CategoryDefaults.getDays("우유"));
        check("발효유 → 14", 14, CategoryDefaults.getDays("발효유"));
        check("유탕면 → 150", 150, CategoryDefaults.getDays("유탕면"));
        check("탄산음료 → 365 (긴 키 우선, 음료 90 아님)", 365, CategoryDefaults.getDays("탄산음료"));
        check("딸기우유 → 10 (포함 매칭)", 10, CategoryDefaults.getDays("딸기우유"));
        check("공백 포함 '초코 우유' → 10", 10, CategoryDefaults.getDays("초코 우유"));
        check("우유두부 → 7 (길이 같으면 짧은 일수)", 7, CategoryDefaults.getDays("우유두부"));
        check("모르는 종류 → -1", -1, CategoryDefaults.getDays("알수없는것"));
        check("빈 문자열 → -1", -1, CategoryDefaults.getDays(""));
        check("공백만 → -1", -1, CategoryDefaults.getDays("   "));
        check("null → -1", -1, CategoryDefaults.getDays(null));
    }

    // ---------------------------------------------------------------
    static void testFoodStorage(LocalDate today) throws Exception {
        System.out.println("=== 2. FoodStorage ===");
        File f = File.createTempFile("foods_test", ".csv");
        f.deleteOnExit();
        String path = f.getAbsolutePath();

        FoodItem a = new FoodItem("우유 1L", "8800000000001", today, today.plusDays(8));
        FoodItem b = new FoodItem("두부", "8800000000002", today, today.plusDays(1));
        b.markEaten(today.plusDays(1));
        FoodItem c = new FoodItem("라면", "8800000000003", today, today.plusDays(180));
        c.markDiscarded(today.plusDays(5));
        FoodItem d = new FoodItem("수제 반찬", "", today, today.plusDays(3));      // 빈 바코드
        FoodItem e = new FoodItem("소스, 매운맛", "8800000000005", today, today.plusDays(90)); // 이름에 쉼표
        e.markAutoDiscarded(today.plusDays(95));

        ArrayList<FoodItem> items = new ArrayList<FoodItem>();
        items.add(a); items.add(b); items.add(c); items.add(d); items.add(e);

        check("save 성공 → true", true, FoodStorage.save(path, items));
        ArrayList<FoodItem> loaded = FoodStorage.load(path);
        check("load 개수 5", 5, loaded.size());
        for (int i = 0; i < items.size() && i < loaded.size(); i++) {
            check("항목 " + (i + 1) + " (" + items.get(i).getName() + ") 복원 일치",
                    true, same(items.get(i), loaded.get(i)));
        }
        check("보관중 항목은 처리일 null", null, loaded.get(0).getResolvedDate());

        check("없는 파일 load → 빈 리스트", 0,
                FoodStorage.load(path + ".none").size());

        // 깨진 줄을 뒤에 붙임: 전부 무시되고 마지막 정상 줄만 추가돼야 함
        Writer w = new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8");
        String[] bad = {
            "",
            "1,2026-11-01",
            "x,2026-11-01,2026-11-10,,111,상태숫자아님",
            "0,2026-13-01,2026-11-10,,111,날짜오류",
            "9,2026-11-01,2026-11-10,2026-11-02,111,상태범위밖",
            "0,2026-11-01,2026-11-10,2026-11-02,111,보관중인데처리일있음",
            "1,2026-11-01,2026-11-10,,111,처리됐는데처리일없음",
            "0,2026-11-01,2026-11-10,,8800000000009,정상줄"
        };
        for (String line : bad) w.write(line + "\n");
        w.close();

        ArrayList<FoodItem> loaded2 = FoodStorage.load(path);
        check("깨진 줄 7개 무시, 정상 줄 포함 → 6개", 6, loaded2.size());
        if (loaded2.size() == 6) {
            check("마지막 항목 이름 '정상줄'", "정상줄", loaded2.get(5).getName());
        }
    }

    // ---------------------------------------------------------------
    static void testAlertPlanner(LocalDate today) {
        System.out.println("=== 3. AlertPlanner ===");
        ArrayList<FoodItem> items = new ArrayList<FoodItem>();
        items.add(new FoodItem("요거트", "1", today, today.plusDays(3)));   // D-3
        items.add(new FoodItem("우유", "2", today, today.plusDays(1)));     // D-1
        items.add(new FoodItem("두부", "3", today, today));                 // D-Day
        items.add(new FoodItem("라면", "4", today, today.plusDays(30)));    // 범위 밖
        items.add(new FoodItem("빵", "5", today, today.minusDays(2)));      // 이미 지남
        FoodItem ham = new FoodItem("햄", "6", today, today.plusDays(1));   // 임박이지만 처리됨
        ham.markEaten(today);
        items.add(ham);

        ArrayList<String> plan = AlertPlanner.plan(items, today, 3);
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("두부 D-Day");
        expected.add("우유 D-1");
        expected.add("요거트 D-3");
        check("plan: 임박만, 남은 일수 적은 순, 처리됨/지남/범위밖 제외", expected, plan);
        check("plan: 원본 items 순서 안 바뀜", "요거트", items.get(0).getName());

        check("plan: 임박 없으면 빈 리스트", 0,
                AlertPlanner.plan(new ArrayList<FoodItem>(), today, 3).size());

        check("makeTitle(3)", "유통기한 임박 음식 3개", AlertPlanner.makeTitle(3));
        check("makeTitle(1)", "유통기한 임박 음식 1개", AlertPlanner.makeTitle(1));
        check("makeTitle(0) → null", null, AlertPlanner.makeTitle(0));
        check("makeTitle(-1) → null", null, AlertPlanner.makeTitle(-1));

        check("makeMessage maxShown=2 → '외 1개'", "두부 D-Day, 우유 D-1 외 1개",
                AlertPlanner.makeMessage(plan, 2));
        check("makeMessage maxShown=3 → 전부, '외' 없음", "두부 D-Day, 우유 D-1, 요거트 D-3",
                AlertPlanner.makeMessage(plan, 3));
        check("makeMessage maxShown=10 → 전부", "두부 D-Day, 우유 D-1, 요거트 D-3",
                AlertPlanner.makeMessage(plan, 10));
        check("makeMessage 빈 리스트 → null", null,
                AlertPlanner.makeMessage(new ArrayList<String>(), 3));
    }

    // ---------------------------------------------------------------
    static void testRepurchaseChecker(LocalDate today) {
        System.out.println("=== 4. RepurchaseChecker ===");
        FoodManager m = new FoodManager();

        // A: 보관중 1 + 버림 1
        m.add(new FoodItem("우유", "A", today, today.plusDays(5)));
        int a2 = m.add(new FoodItem("우유", "A", today, today.plusDays(5)));
        m.markDiscarded(a2, today);

        // B: 보관중 없음, 버림 2 + 다 씀 1
        int b1 = m.add(new FoodItem("두부", "B", today, today.plusDays(5)));
        int b2 = m.add(new FoodItem("두부", "B", today, today.plusDays(5)));
        int b3 = m.add(new FoodItem("두부", "B", today, today.plusDays(5)));
        m.markDiscarded(b1, today);
        m.markDiscarded(b2, today);
        m.markEaten(b3, today);

        // C: 보관중 2, 버린 적 없음
        m.add(new FoodItem("빵", "C", today, today.plusDays(5)));
        m.add(new FoodItem("빵", "C", today, today.plusDays(5)));

        // D: 다 씀만
        int d1 = m.add(new FoodItem("김치", "D", today, today.plusDays(5)));
        m.markEaten(d1, today);

        // E: 자동 폐기만 (유통기한 지남)
        m.add(new FoodItem("햄", "E", today.minusDays(20), today.minusDays(10)));
        m.autoDiscardOverdue(today, 2);

        check("A: 보관중 + 버림 이력 (두 줄)",
                "이미 보관중인 같은 제품이 1개 있어요\n전에 1번 중 1번 버렸어요",
                RepurchaseChecker.check(m, "A"));
        check("B: 보관중 없음 + 버림 이력만 (전체 3번 중 2번)",
                "전에 3번 중 2번 버렸어요",
                RepurchaseChecker.check(m, "B"));
        check("C: 보관중만 2개",
                "이미 보관중인 같은 제품이 2개 있어요",
                RepurchaseChecker.check(m, "C"));
        check("D: 다 씀만 → null", null, RepurchaseChecker.check(m, "D"));
        check("E: 자동 폐기도 버림 횟수에 포함",
                "전에 1번 중 1번 버렸어요",
                RepurchaseChecker.check(m, "E"));
        check("없는 바코드 → null", null, RepurchaseChecker.check(m, "ZZZ"));
        check("null 바코드 → null", null, RepurchaseChecker.check(m, null));
        check("빈 바코드 → null", null, RepurchaseChecker.check(m, ""));
        check("공백 바코드 → null", null, RepurchaseChecker.check(m, "   "));
    }
}
