
import java.time.LocalDate;

public class FoodItem {

	private int id;                    				//고유번호
	private String name;  							//제품명
	private String barcode;							//바코드 번호
	private LocalDate registeredDate;				//등록일
	private LocalDate expiryDate;					//유통기한
	private int status;								//상태상수(밑에 정의)
	private LocalDate resolvedDate;					//처리일(보관중은 null로)

	public static final int STATUS_STORED=0;  		//보관중
	public static final int STATUS_EATEN=1;			//다씀
	public static final int STATUS_DISCARDED=2;		//버림
	public static final int STATUS_AUTO_DISCARDED=3;//자동 폐기

	public FoodItem(String name, String barcode, LocalDate registeredDate, LocalDate expiryDate) {
		this.name=name;
		this.barcode=barcode;
		this.registeredDate=registeredDate;
		this.expiryDate=expiryDate;
		this.status=STATUS_STORED;
		this.resolvedDate=null;
	}

	//getter
	public int getId() { return id; }
	public String getName() { return name; }
	public String getBarcode() { return barcode; }
	public LocalDate getRegisteredDate() { return registeredDate; }
	public LocalDate getExpiryDate() { return expiryDate; }
	public int getStatus() { return status; }
	public LocalDate getResolvedDate() { return resolvedDate; }

	//setter
	public void setId(int id) { this.id=id; }
	public void setName(String name) { this.name=name; }
	public void setBarcode(String barcode) { this.barcode=barcode; }
	public void setRegisteredDate(LocalDate registeredDate) { this.registeredDate=registeredDate; }
	public void setExpiryDate(LocalDate expiryDate) { this.expiryDate=expiryDate; }
	public void setStatus(int status) { this.status=status; }
	public void setResolvedDate(LocalDate resolvedDate) { this.resolvedDate=resolvedDate; }

	public boolean isStored() {      //보관중이면 true, 아니면 false 리턴
		return status==STATUS_STORED;
	}

	public String getStatusName() {    //Status의 값을 한글로 리턴
		switch(status) {
		case STATUS_STORED:
			return "보관중";
		case STATUS_EATEN:
			return "다 씀";
		case STATUS_DISCARDED:
			return "버림";
		case STATUS_AUTO_DISCARDED:
			return "자동 폐기";
		default:
			return "알 수 없음";
		}
	}

	public void markEaten(LocalDate date) { //다 씀 처리 (보관중일 때만)
		if(!isStored()) return;
		status=STATUS_EATEN;
		resolvedDate=date;
	}

	public void markDiscarded(LocalDate date) { //버림 처리 (보관중일 때만)
		if(!isStored()) return;
		status=STATUS_DISCARDED;
		resolvedDate=date;
	}

	public void markAutoDiscarded(LocalDate date) { //자동 폐기 처리 (보관중일 때만)
		if(!isStored()) return;
		status=STATUS_AUTO_DISCARDED;
		resolvedDate=date;
	}

	@Override
	public String toString() {
		String s="["+getStatusName()+"] "+name+" (바코드: "+barcode+", 유통기한: "+expiryDate;
		if(resolvedDate!=null) s+=", 처리일: "+resolvedDate;
		return s+")";
	}
}
