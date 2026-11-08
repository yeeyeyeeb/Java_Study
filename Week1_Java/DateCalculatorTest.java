
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateCalculatorTest {
	
	public static class DateCalculator {
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
				res="-day";
				
			}
			else
				res=String.valueOf(dday);
			
			return "D"+res;
		}
		public static boolean isNearExpiry(LocalDate today, LocalDate expiryDate, int threshold) {
		// 0 ≤ 남은 일수 ≤ threshold 이면 true'
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
	
	
	

	public static void main(String[] args) {
		 LocalDate today = LocalDate.of(2026, 11, 2);

	        System.out.println(DateCalculator.calcExpiryDate(today, 10));
	        System.out.println(DateCalculator.calcExpiryDate(today, -1));

	        System.out.println(DateCalculator.getDday(today, LocalDate.of(2026, 11, 5)));
	        System.out.println(DateCalculator.getDday(today, LocalDate.of(2026, 11, 2)));
	        System.out.println(DateCalculator.getDday(today, LocalDate.of(2026, 10, 31)));

	        System.out.println(DateCalculator.formatDday(3) + " "
	                + DateCalculator.formatDday(0) + " " + DateCalculator.formatDday(-2));
	        
	        System.out.println(DateCalculator.isNearExpiry(today, LocalDate.of(2026, 11, 3), 1));
	        System.out.println(DateCalculator.isNearExpiry(today, LocalDate.of(2026, 11, 5), 1));
	        System.out.println(DateCalculator.isNearExpiry(today, LocalDate.of(2026, 11, 1), 1));

	        System.out.println(DateCalculator.isOverdue(today, LocalDate.of(2026, 10, 30), 2));
	        System.out.println(DateCalculator.isOverdue(today, LocalDate.of(2026, 10, 31), 2));

	}	}
	