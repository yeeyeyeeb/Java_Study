
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlertPlanner {
	public static ArrayList<String> plan(ArrayList<FoodItem> items, LocalDate today, int threshold) {
		//임박 음식 문구 목록 반환
		ArrayList<FoodItem> near = new ArrayList<>();
		for (FoodItem item : items) {
			//유통기한에 가까운 item들을 near에 추가
			if (item.isStored() && DateCalculator.isNearExpiry(today, item.getExpiryDate(), threshold))
				near.add(item);
		}
		//남은 일수가 적은 순으로 정렬
		Collections.sort(near, new Comparator<FoodItem>() {
			public int compare(FoodItem a, FoodItem b) {
				return Long.compare(DateCalculator.getDday(today, a.getExpiryDate()), 
						DateCalculator.getDday(today, b.getExpiryDate()));
			}
		});
		//새 ArrayList<String> 만들고 near를 돌면서 "제품명 D-n" 문자열 추가
		ArrayList<String> result = new ArrayList<>();
		for (FoodItem item : near) {
			result.add(item.getName() + " " + DateCalculator.formatDday(
					DateCalculator.getDday(today, item.getExpiryDate())));
		}
		return result;
	}
	public static String makeTitle(int count) {
		//알림 제목
		//count가 0이하면 null
		if (count <= 0) return null;
		return "유통기한 임박 음식 " + count + "개";
	}
	public static String makeMessage(ArrayList<String> lines, int maxShown) {
		//알림 내용 (한 줄로 합침)
		String result = "";
		if (lines.isEmpty()) return null;
		int n = Math.min(lines.size(), maxShown);
		for (int i = 0; i < n; i++) {
			if (i > 0) result += ", ";
			result += lines.get(i);
		}
		//lines.size() > maxShown이면 " 외 " + 남은 개수 + "개" 붙이기
		if (lines.size() > maxShown)
			result += " 외 " + (lines.size() - n) + "개";
		return result;
	}
}
