package com.example.foodzero.util;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class ShelfLifeParser {
	public static int parseDays(String text) {
		if (text == null)
			return -1;
		int day = 0;
		// 검색할 패턴 만들기
		Pattern pattern = Pattern.compile("(\\d+)\\s*(일|개월|년)");
		// 입력 문자열에 패턴 적용
        Matcher matcher = pattern.matcher(text);
        // 찾은 내용 확인
        while (matcher.find()) {

            String number = matcher.group(1);
            String unit = matcher.group(2);
            if (unit.equals("년")) day += Integer.parseInt(number) * 365;
            else if (unit.equals("개월")) day += Integer.parseInt(number) * 30;
            else if (unit.equals("일")) day += Integer.parseInt(number);
        }
		return day != 0 ? day : -1;
	}
}
