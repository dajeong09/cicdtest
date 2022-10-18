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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public ResponseDto<?> addCourseDetail(CourseDetailRequestDto requestDto, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        Course course = isPresentCourse(requestDto.getCourseId());
        if (null == course) {
            return ResponseDto.fail(COURSE_NOT_FOUND);
        }

        if(!course.getTrip().getMember().getId().equals(member.getId())) {
            return ResponseDto.fail(NOT_AUTHORIZED);
        }

        int detailOrder = courseDetailAccRepository.countAllByCourse(course) +
            courseDetailSpotRepository.countAllByCourse(course) +
            courseDetailRestRepository.countAllByCourse(course) + 1;

        if (detailOrder > 10) {
            ResponseDto.fail(EXCEED_MAX_PLACES);
        }

        if(requestDto.getDetailOrder() != 0) {

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

        System.out.println(requestDto.getDetailId());

        switch (requestDto.getCategory()) {
            case "관광지":
                TouristSpot touristSpot = isPresentTouristSpot(requestDto.getDetailId());
                CourseDetailSpot courseDetailSpot = CourseDetailSpot.builder()
                        .course(course)
                        .touristSpot(touristSpot)
                        .detailOrder(detailOrder).build();
                courseDetailSpotRepository.save(courseDetailSpot);
                break;
            case "맛집":
                Restaurant restaurant = isPresentRestaurant(requestDto.getDetailId());
                CourseDetailRest courseDetailRest = CourseDetailRest.builder()
                        .course(course)
                        .restaurant(restaurant)
                        .detailOrder(detailOrder).build();
                courseDetailRestRepository.save(courseDetailRest);
                break;
            case "숙소":
                Accommodation accommodation = isPresentAccommodation(requestDto.getDetailId());
                CourseDetailAcc courseDetailAcc = CourseDetailAcc.builder()
                        .course(course)
                        .accommodation(accommodation)
                        .detailOrder(detailOrder).build();
                courseDetailAccRepository.save(courseDetailAcc);
                break;
            default:
                return ResponseDto.fail(INVALID_CATEGORY);
        }

        return ResponseDto.success(NULL);
    }

    @Transactional
    public ResponseDto<?> removeCourseDetail (CourseDetailRequestDto courseDetailRequestDto, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        Course course = null;
        int detailOrder = 0;
        switch (courseDetailRequestDto.getCategory()) {
            case "관광지":
                CourseDetailSpot courseDetailSpot = isCourseDetailSpot(courseDetailRequestDto.getDetailId());
                if(null == courseDetailSpot) {
                    return ResponseDto.fail(DETAIL_NOT_FOUND);
                }
                if(!courseDetailSpot.getCourse().getTrip().getMember().getId().equals(member.getId())) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                courseDetailSpotRepository.delete(courseDetailSpot);
                course = isPresentCourse(courseDetailSpot.getCourse().getId());
                detailOrder = courseDetailSpot.getDetailOrder();
                break;
            case "맛집":
                CourseDetailRest courseDetailRest = isCourseDetailRest(courseDetailRequestDto.getDetailId());
                if(null == courseDetailRest) {
                    return ResponseDto.fail(DETAIL_NOT_FOUND);
                }
                if(!courseDetailRest.getCourse().getTrip().getMember().getId().equals(member.getId())) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                courseDetailRestRepository.delete(courseDetailRest);
                course = isPresentCourse(courseDetailRest.getCourse().getId());
                detailOrder = courseDetailRest.getDetailOrder();
                break;
            case "숙소":
                CourseDetailAcc courseDetailAcc = isCourseDetailAcc(courseDetailRequestDto.getDetailId());
                if(null == courseDetailAcc) {
                    return ResponseDto.fail(DETAIL_NOT_FOUND);
                }
                if(!courseDetailAcc.getCourse().getTrip().getMember().getId().equals(member.getId())) {
                    return ResponseDto.fail(NOT_AUTHORIZED);
                }
                courseDetailAccRepository.delete(courseDetailAcc);
                course = isPresentCourse(courseDetailAcc.getCourse().getId());
                detailOrder = courseDetailAcc.getDetailOrder();
                break;
            default:
                return ResponseDto.fail(INVALID_CATEGORY);
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

        return ResponseDto.success(NULL);
    }

    public ResponseDto<?> getBookmarkList() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<PlaceResponseDto> bookmarkedPlaceResponseDtoList = new ArrayList<>();
        List<TouristSpotBookmark> touristSpotBookmarkList = touristSpotBookmarkRepository.findAllByMember(member);
        for (TouristSpotBookmark touristSpotBookmark : touristSpotBookmarkList) {
            bookmarkedPlaceResponseDtoList.add(
                    PlaceResponseDto.builder()
                            .id(touristSpotBookmark.getTouristSpot().getId())
                            .category("관광지")
                            .name(touristSpotBookmark.getTouristSpot().getName())
                            .description(touristSpotBookmark.getTouristSpot().getDescription())
                            .likeNum(touristSpotBookmark.getTouristSpot().getLikeNum())
                            .region(touristSpotBookmark.getTouristSpot().getRegion())
                            .thumbnailUrl(touristSpotBookmark.getTouristSpot().getThumbnailUrl())
                            .isBookmarked(true)
                            .createdAt(touristSpotBookmark.getCreatedAt())
                            .build()
            );
        }
        List<RestaurantBookmark> restaurantBookmarkList = restaurantBookmarkRepository.findAllByMember(member);
        for (RestaurantBookmark restaurantBookmark : restaurantBookmarkList) {
            bookmarkedPlaceResponseDtoList.add(
                    PlaceResponseDto.builder()
                            .id(restaurantBookmark.getRestaurant().getId())
                            .category("맛집")
                            .name(restaurantBookmark.getRestaurant().getName())
                            .description(restaurantBookmark.getRestaurant().getDescription())
                            .likeNum(restaurantBookmark.getRestaurant().getLikeNum())
                            .region(restaurantBookmark.getRestaurant().getRegion())
                            .thumbnailUrl(restaurantBookmark.getRestaurant().getThumbnailUrl())
                            .isBookmarked(true)
                            .createdAt(restaurantBookmark.getCreatedAt())
                            .build()
            );
        }
        List<AccommodationBookmark> accommodationBookmarkList = accommodationBookmarkRepository.findAllByMember(member);
        for (AccommodationBookmark accommodationBookmark : accommodationBookmarkList) {
            bookmarkedPlaceResponseDtoList.add(
                    PlaceResponseDto.builder()
                            .id(accommodationBookmark.getAccommodation().getId())
                            .category("숙소")
                            .name(accommodationBookmark.getAccommodation().getName())
                            .description(accommodationBookmark.getAccommodation().getDescription())
                            .likeNum(accommodationBookmark.getAccommodation().getLikeNum())
                            .region(accommodationBookmark.getAccommodation().getRegion())
                            .thumbnailUrl(accommodationBookmark.getAccommodation().getThumbnailUrl())
                            .isBookmarked(true)
                            .createdAt(accommodationBookmark.getCreatedAt())
                            .build()
            );
        }

        if(!bookmarkedPlaceResponseDtoList.isEmpty()) {
            bookmarkedPlaceResponseDtoList.sort(new DateComparator());
        }

        return ResponseDto.success(bookmarkedPlaceResponseDtoList);
    }

    public ResponseDto<?> getNearbyList(double latitude, double longitude) {

        Member member = tokenProvider.getMemberFromAuthentication();

        double differ = 0.0201;
        List<TouristSpot> touristSpotList = touristSpotRepository.findAllByLatitudeBetweenAndLongitudeBetween(latitude-differ,latitude+differ,longitude-differ,longitude+differ);
        List<Restaurant> restaurantList = restaurantRepository.findAllByLatitudeBetweenAndLongitudeBetween(latitude-differ,latitude+differ,longitude-differ,longitude+differ);

        List<CourseNearbyResponseDto> courseNearbyResponseDtoList = new ArrayList<>();

        if (null != member) {
            boolean isBookmarked;
            for (TouristSpot touristSpot : touristSpotList) {
                isBookmarked = false;
                TouristSpotBookmark findTouristSpotBookmark = touristSpotBookmarkRepository.findByMemberAndTouristSpot(member, touristSpot);
                if (null != findTouristSpotBookmark) {
                    isBookmarked = true;
                }
                double newLatitude = touristSpot.getLatitude();
                double newLongitude = touristSpot.getLongitude();
                double theta = longitude - newLongitude;
                double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(newLatitude)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(newLatitude)) * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515 * 1609.344;
                System.out.println("[" + touristSpot.getName() + "] 까지 거리: " + Math.round(dist / 5) * 5 + "m");
                if (dist < 5000 && touristSpot.getLatitude() != latitude) {
                    courseNearbyResponseDtoList.add(CourseNearbyResponseDto.builder()
                                    .id(touristSpot.getId())
                                    .category("관광지")
                                    .name(touristSpot.getName())
                                    .description(touristSpot.getDescription())
                                    .likeNum(touristSpot.getLikeNum())
                                    .region(touristSpot.getRegion())
                                    .thumbnailUrl(touristSpot.getThumbnailUrl())
                                    .isBookmarked(isBookmarked)
                                    .distance((int)Math.round(dist / 5) * 5)
                                    .build());
                }
            }
            for (Restaurant restaurant : restaurantList) {
                isBookmarked = false;
                RestaurantBookmark findRestaurantBookmark = restaurantBookmarkRepository.findByMemberAndRestaurant(member, restaurant);
                if (null != findRestaurantBookmark) {
                    isBookmarked = true;
                }
                double newLatitude = restaurant.getLatitude();
                double newLongitude = restaurant.getLongitude();
                double theta = longitude - newLongitude;
                double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(newLatitude)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(newLatitude)) * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515 * 1609.344;
                System.out.println("[" + restaurant.getName() + "] 까지 거리: " + Math.round(dist / 5) * 5 + "m");
                if (dist < 5000 && restaurant.getLatitude() != latitude) {
                    courseNearbyResponseDtoList.add(CourseNearbyResponseDto.builder()
                            .id(restaurant.getId())
                            .category("맛집")
                            .name(restaurant.getName())
                            .description(restaurant.getDescription())
                            .likeNum(restaurant.getLikeNum())
                            .region(restaurant.getRegion())
                            .thumbnailUrl(restaurant.getThumbnailUrl())
                            .isBookmarked(isBookmarked)
                            .distance((int)Math.round(dist / 5) * 5)
                            .build());
                }
            }
        } else {
            for (TouristSpot touristSpot : touristSpotList) {
                double newLatitude = touristSpot.getLatitude();
                double newLongitude = touristSpot.getLongitude();
                double theta = longitude - newLongitude;
                double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(newLatitude)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(newLatitude)) * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515 * 1609.344;
                System.out.println("[" + touristSpot.getName() + "] 까지 거리: " + Math.round(dist / 5) * 5 + "m");
                if (dist < 5000 && touristSpot.getLatitude() != latitude) {
                    courseNearbyResponseDtoList.add(CourseNearbyResponseDto.builder()
                            .id(touristSpot.getId())
                            .category("관광지")
                            .name(touristSpot.getName())
                            .description(touristSpot.getDescription())
                            .likeNum(touristSpot.getLikeNum())
                            .region(touristSpot.getRegion())
                            .thumbnailUrl(touristSpot.getThumbnailUrl())
                            .distance((int)Math.round(dist / 5) * 5)
                            .build());
                }
            }
            for (Restaurant restaurant : restaurantList) {
                double newLatitude = restaurant.getLatitude();
                double newLongitude = restaurant.getLongitude();
                double theta = longitude - newLongitude;
                double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(newLatitude)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(newLatitude)) * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515 * 1609.344;
                System.out.println("[" + restaurant.getName() + "] 까지 거리: " + Math.round(dist / 5) * 5 + "m");
                if (dist < 5000 && restaurant.getLatitude() != latitude) {
                    courseNearbyResponseDtoList.add(CourseNearbyResponseDto.builder()
                            .id(restaurant.getId())
                            .category("맛집")
                            .name(restaurant.getName())
                            .description(restaurant.getDescription())
                            .likeNum(restaurant.getLikeNum())
                            .region(restaurant.getRegion())
                            .thumbnailUrl(restaurant.getThumbnailUrl())
                            .distance((int)Math.round(dist / 5) * 5)
                            .build());
                }
            }
        }

        if(!courseNearbyResponseDtoList.isEmpty()) {
            //다른 방법 고민해 보기
            courseNearbyResponseDtoList.sort(new DistanceComparator());
        }

        List<CourseNearbyResponseDto> courseNearbyResponseDtoListTop5 = new ArrayList<>();
        for (int i = 0; i < courseNearbyResponseDtoList.size(); i++) {
            if (i == 5) {
                break;
            }
            courseNearbyResponseDtoListTop5.add(courseNearbyResponseDtoList.get(i));

        }
        return ResponseDto.success(courseNearbyResponseDtoListTop5);

    }


    public Course isPresentCourse(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.orElse(null);
    }

    public TouristSpot isPresentTouristSpot(Long id) {
        Optional<TouristSpot> optionalTouristSpot = touristSpotRepository.findById(id);
        return optionalTouristSpot.orElse(null);
    }

    public Restaurant isPresentRestaurant(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        return optionalRestaurant.orElse(null);
    }

    public Accommodation isPresentAccommodation(Long id) {
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findById(id);
        return optionalAccommodation.orElse(null);
    }

    public CourseDetailSpot isCourseDetailSpot(Long id) {
        Optional<CourseDetailSpot> optionalCourseDetailSpot = courseDetailSpotRepository.findById(id);
        return optionalCourseDetailSpot.orElse(null);
    }

    public CourseDetailRest isCourseDetailRest(Long id) {
        Optional<CourseDetailRest> optionalCourseDetailRest = courseDetailRestRepository.findById(id);
        return optionalCourseDetailRest.orElse(null);
    }

    public CourseDetailAcc isCourseDetailAcc(Long id) {
        Optional<CourseDetailAcc> optionalCourseDetailAcc = courseDetailAccRepository.findById(id);
        return optionalCourseDetailAcc.orElse(null);
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