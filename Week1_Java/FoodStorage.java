import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class FoodStorage {
	public static boolean save(String path, ArrayList<FoodItem> items) {
	    try {
	        BufferedWriter bw = new BufferedWriter(
	                new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
	        // 1. HEADER 쓰고 줄바꿈 (bw.write(...), bw.newLine())
	        bw.write(HEADER);
	        bw.newLine();
	
	        // 2. items를 하나씩 돌면서 한 줄 문자열을 만들어 쓰고 줄바꿈
	    
	        for(FoodItem item:items) {
	        	
	        	String line = item.status()+"," +
	        			item.getRegisteredDate()+"," +
	        			item.getExpiryDate()+"," +
	        			((item.getResolvedDate()==null)?"":item.getResolvedDate())+"," +
	        			item.getBarcode()+"," +
	        			item.getName();
	        	bw.write(line);
	        	
	        }
	        
	        //    - LocalDate는 문자열에 + 로 붙이면 "2026-11-09" 형태로 들어감
	        //    - 처리일이 null이면 "" 를 붙여야 함 ("null"이 들어가면 안 됨)
	        bw.close();   // close 안 하면 파일에 안 써질 수 있음
	        return true;
	    } catch (IOException e) {
	        return false;
	    }
	}
	public static ArrayList<FoodItem> load(String path) {
	    ArrayList<FoodItem> list = new ArrayList<FoodItem>();
	    // 1. new File(path).exists()가 false면 list 바로 반환
	    try {
	        BufferedReader br = new BufferedReader(
	                new InputStreamReader(new FileInputStream(path), "UTF-8"));
	        br.readLine();   // 첫 줄(헤더) 버리기
	        String line;
	        while ((line = br.readLine()) != null) {   // 파일 끝이면 null
	            // 2. parseLine(line) 결과가 null이 아니면 list에 추가
	        }
	        br.close();
	    } catch (IOException e) {
	        // 읽다가 실패하면 그때까지 읽은 것만 반환
	    }
	    return list;
	}
	
	private static FoodItem parseLine(String line) {
		
		int flag=0;
		if(line.trim().isEmpty())
			flag=1;
		String[] p = line.split(",", 6);  
		if(p.length!=6)
			flag=1;
		if(flag==1)
			return null;
	    // 깨진 줄이면 null 반환
	    // 1. 빈 줄 검사 (line.trim().isEmpty())
	    // 2. String[] p = line.split(",", 6);  길이가 6이 아니면 null
		try {
			int status=Integer.parseInt(p[0]);
			LocalDate registeredDate=LocalDate.parse(p[1]);
			LocalDate expiryDate=LocalDate.parse(p[2]);
			if(!((status>=0)&&(status<=3))) {
				return null;
			}
			if(status==0) {
				
				if (!p[3].isEmpty())
					return null;
			}
			FoodItem item=new FoodItem(p[5],p[4],registeredDate,expiryDate);
				item.setStatus(status);
				item.setResolvedDate(LocalDate.parse(p[3]));
			return item;
			
		}

	    // 3. try 안에서
	    //    - status = Integer.parseInt(p[0]), 0~3인지 확인
	    //    - 등록일 = LocalDate.parse(p[1]), 유통기한 = LocalDate.parse(p[2])
	    //    - new FoodItem(p[5], p[4], 등록일, 유통기한)
	    //    - status가 0이면: p[3]이 빈칸이 아니면 null
	    //      status가 1~3이면: setStatus(status), setResolvedDate(LocalDate.parse(p[3]))
	    // 4. catch로 NumberFormatException, DateTimeParseException 잡아서 null
		catch(NumberFormatException |DateTimeParseException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
