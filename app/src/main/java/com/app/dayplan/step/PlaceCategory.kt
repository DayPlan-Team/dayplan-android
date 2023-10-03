package com.app.dayplan.step

enum class PlaceCategory(
    val koreanName: String,
    val comment: String,
) {
    CAFE("카페", "커피 맛집, 디저트 맛집"),
    MOVIE_THEATER("영화,공연", "근처 영화관, 뮤지컬, 공연"),
    RESTAURANT("식당", "동네 유명 맛집"),
    FITNESS("운동시설", "헬스장"),
    ACTIVITY("엑티비티", "체험, 원데이 클래스 등"),
    PARK("공원", "휴식 공간"),
    SHOPPING_MALL("쇼핑몰", "백화점, 브랜드"),
    CONCERT_HALL("콘서트홀", "BTS, 블랙핑크"),
    ZOO("동물원", ""),
    AQUARIUM("수족관", ""),
    LIBRARY("도서관", ""),
}