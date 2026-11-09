import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// [수정] DateCalculatorTest 안의 중첩 클래스 → 별도 파일로 분리 (FoodManager에서 호출하기 위해)
public class DateCalculator {
	public static LocalDate calcExpiryDate(LocalDate baseDate, int days) {
	// baseDate + days. days < 0 이면 null
		if(days<0)
			return null;
		else
			return baseDate.plusDays(days);
	}

	public static long getDday(LocalDate today, LocalDate expiryDate) {
	// 남은 일수. 양수: 남음, 0: 당일, 음수: 지남
		return ChronoUnit.DAYS.between(today, expiryDate);
	}

	public static String formatDday(long dday) {
	// 3 → "D-3", 0 → "D-Day", -2 → "D+2"
		String res;
		if(dday>0) {
			res ="-"+String.valueOf(dday);
		}
		else if(dday==0) {
			res="-Day";                       // [수정] "-day" → "-Day" (대문자 D)
		}
		else
			res="+"+String.valueOf(-dday);    // [수정] 음수를 그대로 붙이면 "D-2"가 됨 → 부호 뒤집고 "+" 붙임
		
		return "D"+res;
	}

	public static boolean isNearExpiry(LocalDate today, LocalDate expiryDate, int threshold) {
	// 0 ≤ 남은 일수 ≤ threshold 이면 true
		long days=ChronoUnit.DAYS.between(today, expiryDate);
		if((days<=threshold)&&(days>=0)) {
			return true;
		}
		else return false;
	}

	public static boolean isOverdue(LocalDate today, LocalDate expiryDate, int graceDays) {
	// (expiryDate + graceDays)가 today보다 이전이면 true
		if(expiryDate.plusDays(graceDays).isBefore(today)) {
			return true;
		}
		else return false;
	}
}