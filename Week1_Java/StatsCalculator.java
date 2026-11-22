import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
public class StatsCalculator {
	public static int countByStatus(ArrayList<FoodItem> items, int status) {
		//상태가 status인 항목의 개수
		int count = 0;
		for (FoodItem item : items) {
			if (item.getStatus() == status)
				count++;
		}
		return count;
	}
	public static double calcDiscardRate(ArrayList<FoodItem> items) {
		// 폐기율(%) = 폐기 개수 / 처리된 개수 * 100
		float discardedCount = countByStatus(items, FoodItem.STATUS_AUTO_DISCARDED) + countByStatus(items, FoodItem.STATUS_DISCARDED); // 폐기 개수
		// 처리된 개수 = 보관중이 아닌 항목 수 (다 씀 + 버림 + 자동 폐기)
		float isNotStoredCount = discardedCount + countByStatus(items, FoodItem.STATUS_EATEN);
		// 처리된 항목이 하나도 없으면 0.0
		if (isNotStoredCount == 0)
			return 0.0f;
		
		return discardedCount / isNotStoredCount * 100;
	}
	public static HashMap<String, Integer> countDiscardedByMonth(ArrayList<FoodItem> items) {
		// 월별 폐기 개수, 키는 처리일의 "yyyy-MM" 문자열, 값은 그 달의 폐기 개수
		// 폐기가 없는 달은 키를 만들지 않음. 폐기가 하나도 없으면 빈 HashMap
		HashMap<String, Integer> map = new HashMap<>();
		for (FoodItem item : items) {
			if (item.getStatus() == FoodItem.STATUS_AUTO_DISCARDED || item.getStatus() == FoodItem.STATUS_DISCARDED) { // 폐기된 아이템인지 확인
				String key = item.getResolvedDate().toString().substring(0, 7); // 폐기 일자를 "2026-11"와 같은 형태로 자름
				map.put(key, map.getOrDefault(key, 0) + 1); // 이미 키가 있으면 기본 값 + 1 아니면 1
			}
		}
		return map;
	}
	public static ArrayList<String> getDiscardRanking(ArrayList<FoodItem> items, int topN) {
		// 제품명별 폐기 횟수가 많은 순으로 최대 topN개를 "이름 N회" 형태의 문자열로 반환
		// 제품명이 같으면 같은 제품으로 셈
		ArrayList<String> list;
		LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
		for (FoodItem item : items) {
			if (item.getStatus() == FoodItem.STATUS_AUTO_DISCARDED || item.getStatus() == FoodItem.STATUS_DISCARDED) { // 폐기된 아이템인지 확인
				String key = item.getName();
				map.put(key, map.getOrDefault(key, 0) + 1); // 이미 키가 있으면 기본 값 + 1 아니면 1
			}
		}
		list = new ArrayList<>(map.keySet());
		list.sort((a, b) -> map.get(b) - map.get(a));
		ArrayList<String> answer = new ArrayList<>();
		for (int i = 0; i < topN; i++) {
			if ( i > list.size() - 1) break;
			answer.add(list.get(i) + " " + map.get(list.get(i)) + "회");
		}
		return answer;
	}
}
