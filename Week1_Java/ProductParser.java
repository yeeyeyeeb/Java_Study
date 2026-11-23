public static String getValue(String json, String key) {
    // json 안에서 "key":"값" 형태를 찾아 값(문자열)을 반환
    // json이나 key가 null이거나, 해당 키가 없으면 null 반환
    // 값이 빈 문자열("")이면 빈 문자열 반환
    
    int begin;
    if(json==null||key==null||key.isEmpty()) return null;
    try {
        begin= json.indexOf("\""+key+"\":\"");
        if(begin == -1) return null; // [수정] indexOf는 못 찾으면 예외가 아니라 -1을 반환하므로 필수 추가
    }
    catch(Exception e){
        return null;
    }
    int end = json.indexOf("\"",begin+("\""+key+"\":\"").length());
    String value = json.substring(begin+("\""+key+"\":\"").length(),end);
    if(value.isEmpty()) { // [수정] 문자열 빈 값 비교는 == "" 대신 .isEmpty() 사용
        return "";
    }
    return value;
}

public static ProductInfo parse(String json) {
    // 응답에서 제품 정보를 꺼내 ProductInfo로 반환
    // 아래 경우에는 null 반환
    //   - json이 null이거나 빈 문자열
    //   - CODE가 없거나 "INFO-000"이 아님
    //   - 제품명(PRDLST_NM)이 없거나 빈 문자열
    // 식품유형, 유통기한이 없거나 비어 있으면 빈 문자열("")로 채워서 ProductInfo 생성
    
    if(json==null||json.isEmpty()) { // [수정] 문자열 빈 값 비교는 == "" 대신 .isEmpty() 사용
        return null;
    }
    if((getValue(json,"CODE")==null)||!"INFO-000".equals(getValue(json, "CODE"))){
        return null;
    }
    if(getValue(json,"PRDLST_NM")==null||getValue(json,"PRDLST_NM").isEmpty()){
        return null;
    }
    
    return new ProductInfo(getValue(json,"PRDLST_NM"), 
            (getValue(json,"PRDLST_DCNM")==null)?"":getValue(json,"PRDLST_DCNM"),
            (getValue(json,"POG_DAYCNT")==null)?"":getValue(json,"POG_DAYCNT")); // [수정] .isEmpty() 대신 == null 체크 및 잘못 닫힌 괄호 ) 위치 수정
}