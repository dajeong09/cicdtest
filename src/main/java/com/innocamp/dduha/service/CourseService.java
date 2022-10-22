package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.CourseDetailRequestDto;
import com.innocamp.dduha.dto.response.CourseNearbyResponseDto;
import com.innocamp.dduha.dto.response.PlaceResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.bookmark.AccommodationBookmark;
import com.innocamp.dduha.model.bookmark.RestaurantBookmark;
import com.innocamp.dduha.model.bookmark.TouristSpotBookmark;
import com.innocamp.dduha.model.course.Course;
import com.innocamp.dduha.model.course.CourseDetailAcc;
import com.innocamp.dduha.model.course.CourseDetailRest;
import com.innocamp.dduha.model.course.CourseDetailSpot;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.repository.CourseRepository;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import com.innocamp.dduha.repository.bookmark.AccommodationBookmarkRepository;
import com.innocamp.dduha.repository.bookmark.RestaurantBookmarkRepository;
import com.innocamp.dduha.repository.bookmark.TouristSpotBookmarkRepository;
import com.innocamp.dduha.repository.coursedetail.CourseDetailAccReposiotry;
import com.innocamp.dduha.repository.coursedetail.CourseDetailRestRepository;
import com.innocamp.dduha.repository.coursedetail.CourseDetailSpotRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.xml.bind.ValidationException;
import java.util.*;

import static com.innocamp.dduha.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final TouristSpotRepository touristSpotRepository;
    private final RestaurantRepository restaurantRepository;
    private final AccommodationRepository accommodationRepository;
    private final CourseDetailSpotRepository courseDetailSpotRepository;
    private final CourseDetailRestRepository courseDetailRestRepository;
    private final CourseDetailAccReposiotry courseDetailAccRepository;
    private final TouristSpotBookmarkRepository touristSpotBookmarkRepository;
    private final RestaurantBookmarkRepository restaurantBookmarkRepository;
    private final AccommodationBookmarkRepository accommodationBookmarkRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseEntity<?> addCourseDetail(CourseDetailRequestDto requestDto) throws AuthenticationException, ValidationException {

        Member member = tokenProvider.getMemberFromAuthentication();

        Course course = courseRepository.findById(requestDto.getCourseId()).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(COURSE_NOT_FOUND)));

        if (!course.getTrip().getMember().getId().equals(member.getId())) {
            throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
        }

        int detailOrder = courseDetailAccRepository.countAllByCourse(course) +
                courseDetailSpotRepository.countAllByCourse(course) +
                courseDetailRestRepository.countAllByCourse(course) + 1;

        if (detailOrder > 10) {
            throw new ValidationException(String.valueOf(EXCEED_MAX_PLACES));
        }

        if (requestDto.getDetailOrder() != 0) {

            detailOrder = requestDto.getDetailOrder();

            List<CourseDetailSpot> savedCourseDetailSpotList =
                    courseDetailSpotRepository.findAllByCourseAndDetailOrderGreaterThanEqual(course, detailOrder);
            if (null != savedCourseDetailSpotList) {
                for (CourseDetailSpot courseDetailSpot : savedCourseDetailSpotList) {
                    courseDetailSpot.postponeOrder();
                    courseDetailSpotRepository.save(courseDetailSpot);
                }
            }

            List<CourseDetailRest> savedCourseDetailRestList =
                    courseDetailRestRepository.findAllByCourseAndDetailOrderGreaterThanEqual(course, detailOrder);
            if (null != savedCourseDetailRestList) {
                for (CourseDetailRest courseDetailRest : savedCourseDetailRestList) {
                    courseDetailRest.postponeOrder();
                    courseDetailRestRepository.save(courseDetailRest);
                }
            }

            List<CourseDetailAcc> savedCourseDetailAccList =
                    courseDetailAccRepository.findAllByCourseAndDetailOrderGreaterThanEqual(course, detailOrder);
            if (null != savedCourseDetailRestList) {
                for (CourseDetailAcc courseDetailAcc : savedCourseDetailAccList) {
                    courseDetailAcc.postponeOrder();
                    courseDetailAccRepository.save(courseDetailAcc);
                }
            }
        }

        switch (requestDto.getCategory()) {
            case "관광지":
                TouristSpot touristSpot = touristSpotRepository.findById(requestDto.getDetailId()).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(TOURISTSPOT_NOT_FOUND)));
                CourseDetailSpot courseDetailSpot = CourseDetailSpot.builder()
                        .course(course)
                        .touristSpot(touristSpot)
                        .detailOrder(detailOrder).build();
                courseDetailSpotRepository.save(courseDetailSpot);
                break;
            case "맛집":
                Restaurant restaurant = restaurantRepository.findById(requestDto.getDetailId()).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(RESTAURANT_NOT_FOUND)));
                CourseDetailRest courseDetailRest = CourseDetailRest.builder()
                        .course(course)
                        .restaurant(restaurant)
                        .detailOrder(detailOrder).build();
                courseDetailRestRepository.save(courseDetailRest);
                break;
            case "숙소":
                Accommodation accommodation = accommodationRepository.findById(requestDto.getDetailId()).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(ACCOMMODATION_NOT_FOUND)));
                CourseDetailAcc courseDetailAcc = CourseDetailAcc.builder()
                        .course(course)
                        .accommodation(accommodation)
                        .detailOrder(detailOrder).build();
                courseDetailAccRepository.save(courseDetailAcc);
                break;
            default:
                throw new ValidationException(String.valueOf(INVALID_CATEGORY));
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    @Transactional
    public ResponseEntity<?> removeCourseDetail(CourseDetailRequestDto courseDetailRequestDto) throws AuthenticationException, ValidationException {

        Member member = tokenProvider.getMemberFromAuthentication();

        Course course;
        int detailOrder;
        switch (courseDetailRequestDto.getCategory()) {
            case "관광지":
                CourseDetailSpot courseDetailSpot = courseDetailSpotRepository.findById(courseDetailRequestDto.getDetailId())
                        .orElseThrow(() -> new NoSuchElementException(String.valueOf(DETAIL_NOT_FOUND)));
                if (!courseDetailSpot.getCourse().getTrip().getMember().getId().equals(member.getId())) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                courseDetailSpotRepository.delete(courseDetailSpot);
                course = courseRepository.findById(courseDetailSpot.getCourse().getId()).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(COURSE_NOT_FOUND)));
                detailOrder = courseDetailSpot.getDetailOrder();
                break;
            case "맛집":
                CourseDetailRest courseDetailRest = courseDetailRestRepository.findById(courseDetailRequestDto.getDetailId())
                        .orElseThrow(() -> new NoSuchElementException(String.valueOf(DETAIL_NOT_FOUND)));
                if (!courseDetailRest.getCourse().getTrip().getMember().getId().equals(member.getId())) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                courseDetailRestRepository.delete(courseDetailRest);
                course = courseRepository.findById(courseDetailRest.getCourse().getId()).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(COURSE_NOT_FOUND)));
                detailOrder = courseDetailRest.getDetailOrder();
                break;
            case "숙소":
                CourseDetailAcc courseDetailAcc = courseDetailAccRepository.findById(courseDetailRequestDto.getDetailId())
                        .orElseThrow(() -> new NoSuchElementException(String.valueOf(DETAIL_NOT_FOUND)));
                if (!courseDetailAcc.getCourse().getTrip().getMember().getId().equals(member.getId())) {
                    throw new AuthenticationException(String.valueOf(REQUEST_FORBIDDEN));
                }
                courseDetailAccRepository.delete(courseDetailAcc);
                course = courseRepository.findById(courseDetailAcc.getCourse().getId()).orElseThrow(() ->
                        new NoSuchElementException(String.valueOf(COURSE_NOT_FOUND)));
                detailOrder = courseDetailAcc.getDetailOrder();
                break;
            default:
                throw new ValidationException(String.valueOf(INVALID_CATEGORY));
        }

        List<CourseDetailSpot> savedCourseDetailSpotList =
                courseDetailSpotRepository.findAllByCourseAndDetailOrderGreaterThanEqual(course, detailOrder);
        if (null != savedCourseDetailSpotList) {
            for (CourseDetailSpot courseDetailSpot : savedCourseDetailSpotList) {
                courseDetailSpot.advanceOrder();
                courseDetailSpotRepository.save(courseDetailSpot);
            }
        }

        List<CourseDetailRest> savedCourseDetailRestList =
                courseDetailRestRepository.findAllByCourseAndDetailOrderGreaterThanEqual(course, detailOrder);
        if (null != savedCourseDetailRestList) {
            for (CourseDetailRest courseDetailRest : savedCourseDetailRestList) {
                courseDetailRest.advanceOrder();
                courseDetailRestRepository.save(courseDetailRest);
            }
        }

        List<CourseDetailAcc> savedCourseDetailAccList =
                courseDetailAccRepository.findAllByCourseAndDetailOrderGreaterThanEqual(course, detailOrder);
        if (null != savedCourseDetailAccList) {
            for (CourseDetailAcc courseDetailAcc : savedCourseDetailAccList) {
                courseDetailAcc.advanceOrder();
                courseDetailAccRepository.save(courseDetailAcc);
            }
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    public ResponseEntity<?> getBookmarkList() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<PlaceResponseDto> bookmarkedPlaceResponseDtoList = new ArrayList<>();
        int bookmarkNum;
        List<TouristSpotBookmark> touristSpotBookmarkList = touristSpotBookmarkRepository.findAllByMember(member);
        for (TouristSpotBookmark touristSpotBookmark : touristSpotBookmarkList) {
            bookmarkNum = touristSpotBookmark.getTouristSpot().getLikeNum() + touristSpotBookmarkRepository.countByTouristSpot(touristSpotBookmark.getTouristSpot());
            bookmarkedPlaceResponseDtoList.add(
                    PlaceResponseDto.builder()
                            .id(touristSpotBookmark.getTouristSpot().getId())
                            .category("관광지")
                            .name(touristSpotBookmark.getTouristSpot().getName())
                            .description(touristSpotBookmark.getTouristSpot().getDescription())
                            .bookmarkNum(bookmarkNum)
                            .region(touristSpotBookmark.getTouristSpot().getRegion())
                            .thumbnailUrl(touristSpotBookmark.getTouristSpot().getThumbnailUrl())
                            .isBookmarked(true)
                            .createdAt(touristSpotBookmark.getCreatedAt())
                            .build()
            );
        }
        List<RestaurantBookmark> restaurantBookmarkList = restaurantBookmarkRepository.findAllByMember(member);
        for (RestaurantBookmark restaurantBookmark : restaurantBookmarkList) {
            bookmarkNum = restaurantBookmark.getRestaurant().getLikeNum() + restaurantBookmarkRepository.countByRestaurant(restaurantBookmark.getRestaurant());
            bookmarkedPlaceResponseDtoList.add(
                    PlaceResponseDto.builder()
                            .id(restaurantBookmark.getRestaurant().getId())
                            .category("맛집")
                            .name(restaurantBookmark.getRestaurant().getName())
                            .description(restaurantBookmark.getRestaurant().getDescription())
                            .bookmarkNum(bookmarkNum)
                            .region(restaurantBookmark.getRestaurant().getRegion())
                            .thumbnailUrl(restaurantBookmark.getRestaurant().getThumbnailUrl())
                            .isBookmarked(true)
                            .createdAt(restaurantBookmark.getCreatedAt())
                            .build()
            );
        }
        List<AccommodationBookmark> accommodationBookmarkList = accommodationBookmarkRepository.findAllByMember(member);
        for (AccommodationBookmark accommodationBookmark : accommodationBookmarkList) {
            bookmarkNum = accommodationBookmark.getAccommodation().getLikeNum() + accommodationBookmarkRepository.countByAccommodation(accommodationBookmark.getAccommodation());
            bookmarkedPlaceResponseDtoList.add(
                    PlaceResponseDto.builder()
                            .id(accommodationBookmark.getAccommodation().getId())
                            .category("숙소")
                            .name(accommodationBookmark.getAccommodation().getName())
                            .description(accommodationBookmark.getAccommodation().getDescription())
                            .bookmarkNum(bookmarkNum)
                            .region(accommodationBookmark.getAccommodation().getRegion())
                            .thumbnailUrl(accommodationBookmark.getAccommodation().getThumbnailUrl())
                            .isBookmarked(true)
                            .createdAt(accommodationBookmark.getCreatedAt())
                            .build()
            );
        }

        if (!bookmarkedPlaceResponseDtoList.isEmpty()) {
            bookmarkedPlaceResponseDtoList.sort(new DateComparator());
        }

        return ResponseEntity.ok(ResponseDto.success(bookmarkedPlaceResponseDtoList));
    }

    public ResponseEntity<?> getNearbyList(double latitude, double longitude) {

        double differ = 0.0201;
        List<TouristSpot> touristSpotList = touristSpotRepository.findAllByLatitudeBetweenAndLongitudeBetween(latitude - differ, latitude + differ, longitude - differ, longitude + differ);
        List<Restaurant> restaurantList = restaurantRepository.findAllByLatitudeBetweenAndLongitudeBetween(latitude - differ, latitude + differ, longitude - differ, longitude + differ);

        List<CourseNearbyResponseDto> courseNearbyResponseDtoList = new ArrayList<>();
        double newLatitude;
        double newLongitude;
        double theta;
        double dist;
        double convertedDist;

        for (TouristSpot touristSpot : touristSpotList) {
            newLatitude = touristSpot.getLatitude();
            newLongitude = touristSpot.getLongitude();
            theta = longitude - newLongitude;
            dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(newLatitude))
                    + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(newLatitude)) * Math.cos(deg2rad(theta));
            convertedDist = rad2deg(Math.acos(dist)) * 60 * 1.1515 * 1609.344;
            if (convertedDist < 5000 && touristSpot.getLatitude() != latitude) {
                courseNearbyResponseDtoList.add(CourseNearbyResponseDto.builder()
                        .id(touristSpot.getId())
                        .category("관광지")
                        .name(touristSpot.getName())
                        .description(touristSpot.getDescription())
                        .likeNum(touristSpot.getLikeNum())
                        .region(touristSpot.getRegion())
                        .thumbnailUrl(touristSpot.getThumbnailUrl())
                        .distance((int) Math.round(convertedDist / 5) * 5)
                        .build());
            }
        }

        for (Restaurant restaurant : restaurantList) {
            newLatitude = restaurant.getLatitude();
            newLongitude = restaurant.getLongitude();
            theta = longitude - newLongitude;
            dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(newLatitude))
                    + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(newLatitude)) * Math.cos(deg2rad(theta));
            convertedDist = rad2deg(Math.acos(dist)) * 60 * 1.1515 * 1609.344;
            if (convertedDist < 5000 && restaurant.getLatitude() != latitude) {
                courseNearbyResponseDtoList.add(CourseNearbyResponseDto.builder()
                        .id(restaurant.getId())
                        .category("맛집")
                        .name(restaurant.getName())
                        .description(restaurant.getDescription())
                        .likeNum(restaurant.getLikeNum())
                        .region(restaurant.getRegion())
                        .thumbnailUrl(restaurant.getThumbnailUrl())
                        .distance((int) Math.round(convertedDist / 5) * 5)
                        .build());
            }
        }

        if (!courseNearbyResponseDtoList.isEmpty()) {
            courseNearbyResponseDtoList.sort(new DistanceComparator());
        }

        List<CourseNearbyResponseDto> courseNearbyResponseDtoListTop5 = new ArrayList<>();
        for (int i = 0; i < courseNearbyResponseDtoList.size(); i++) {
            if (i == 5) {
                break;
            }
            courseNearbyResponseDtoListTop5.add(courseNearbyResponseDtoList.get(i));
        }
        return ResponseEntity.ok(ResponseDto.success(courseNearbyResponseDtoListTop5));

    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}

class DateComparator implements Comparator<PlaceResponseDto> {

    @Override
    public int compare(PlaceResponseDto o1, PlaceResponseDto o2) {
        return o2.getCreatedAt().compareTo(o1.getCreatedAt());
    }
}

class DistanceComparator implements Comparator<CourseNearbyResponseDto> {

    @Override
    public int compare(CourseNearbyResponseDto o1, CourseNearbyResponseDto o2) {
        return o1.getDistance() - o2.getDistance();
    }
}