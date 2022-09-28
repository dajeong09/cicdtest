package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.CourseDetailRequestDto;
import com.innocamp.dduha.dto.request.CourseRequestDto;
import com.innocamp.dduha.dto.request.TripRequestDto;
import com.innocamp.dduha.dto.response.CourseAccommodationResponseDto;
import com.innocamp.dduha.dto.response.CourseDetailResponseDto;
import com.innocamp.dduha.dto.response.CourseResponseDto;
import com.innocamp.dduha.dto.response.TripResponseDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.*;
import com.innocamp.dduha.model.bookmark.TripBookmark;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import com.innocamp.dduha.repository.coursedetail.CourseDetailAccReposiotry;
import com.innocamp.dduha.repository.coursedetail.CourseDetailRestRepository;
import com.innocamp.dduha.repository.coursedetail.CourseDetailSpotRepository;
import com.innocamp.dduha.repository.CourseRepository;
import com.innocamp.dduha.repository.TripRepository;
import com.innocamp.dduha.repository.bookmark.TripBookmarkRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.innocamp.dduha.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final CourseRepository courseRepository;
    private final TouristSpotRepository touristSpotRepository;
    private final CourseDetailSpotRepository courseDetailSpotRepository;
    private final RestaurantRepository restaurantRepository;
    private final CourseDetailRestRepository courseDetailRestRepository;
    private final AccommodationRepository accommodationRepository;
    private final CourseDetailAccReposiotry courseDetailAccReposiotry;
    private final TripBookmarkRepository tripBookmarkRepository;


    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> createTrip(TripRequestDto requestDto, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        LocalDate startAt = LocalDate.parse(requestDto.getStartAt(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDate endAt = LocalDate.parse(requestDto.getEndAt(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Trip trip = Trip.builder()
                .title(requestDto.getTitle())
                .isPublic(requestDto.getIsPublic())
                .startAt(startAt)
                .endAt(endAt)
                .member(member)
                .build();

        Trip savedTrip = tripRepository.save(trip);

        for(int i = 1; i <= ChronoUnit.DAYS.between(startAt,endAt)+1; i++) {
            Course course = Course.builder()
                    .day(i)
                    .trip(trip)
                    .build();
            courseRepository.save(course);
        }

        return ResponseDto.success(savedTrip.getId());
    }

    public ResponseDto<?> getMyTrips(HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }
        List<TripResponseDto> tripResponseDtoList = new ArrayList<>();

        List<Trip> tripList = tripRepository.findAllByMember(member);

        for (Trip trip : tripList) {
            tripResponseDtoList.add(TripResponseDto.builder()
                    .id(trip.getId())
                    .title(trip.getTitle())
                    .isPublic(trip.getIsPublic())
                    .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))).build()
            );
        }

        return ResponseDto.success(tripResponseDtoList);
    }

    public ResponseDto<?> getTripInfo(Long id, HttpServletRequest request) {

        Trip trip = isPresentTrip(id);
        if(null == trip) {
            return ResponseDto.fail(TRIP_NOT_FOUND);
        }

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        if (member.getId() != trip.getMember().getId()) {
            return ResponseDto.fail(NOT_AUTHORIZED);
        }

        List<Course> courseList = courseRepository.findAllByTrip(trip);
        List<CourseResponseDto> courseResponseDtoList = new ArrayList<>();


        for (Course course : courseList) {
            List<CourseDetailResponseDto> courseDetailResponseDtoList = new ArrayList<>();
            List<CourseDetailSpot> courseDetailSpotList = courseDetailSpotRepository.findAllByCourse(course);
            for(CourseDetailSpot courseDetailSpot : courseDetailSpotList) {
                courseDetailResponseDtoList.add(CourseDetailResponseDto.builder()
                        .detailOrder(courseDetailSpot.getDetailOrder())
                        .category("관광지")
                        .id(courseDetailSpot.getTouristSpot().getId())
                        .name(courseDetailSpot.getTouristSpot().getName()).build()
                );
            }
            List<CourseDetailRest> courseDetailRestList = courseDetailRestRepository.findAllByCourse(course);
            for(CourseDetailRest courseDetailRest : courseDetailRestList) {
                courseDetailResponseDtoList.add(CourseDetailResponseDto.builder()
                        .detailOrder(courseDetailRest.getDetailOrder())
                        .category("맛집")
                        .id(courseDetailRest.getRestaurant().getId())
                        .name(courseDetailRest.getRestaurant().getName()).build()
                );
            }
            CourseDetailAcc courseDetailAcc = courseDetailAccReposiotry.findCourseDetailAccByCourse(course);
            CourseAccommodationResponseDto accommodation = CourseAccommodationResponseDto.builder()
                    .id(courseDetailAcc.getId())
                    .name(courseDetailAcc.getAccommodation().getName()).build();

            courseResponseDtoList.add(CourseResponseDto.builder()
                            .courseId(course.getId())
                            .day(course.getDay())
                            .courseDetails(courseDetailResponseDtoList)
                            .accommodation(accommodation)
                    .build());
        }


        TripResponseDto tripResponseDto = TripResponseDto.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .isPublic(trip.getIsPublic())
                .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .courses(courseResponseDtoList).build();

        return ResponseDto.success(tripResponseDto);
    }

    @Transactional
    public ResponseDto<?> deleteTrip(Long id, HttpServletRequest request) {

        Trip trip = isPresentTrip(id);
        if(null == trip) {
            return ResponseDto.fail(TRIP_NOT_FOUND);
        }

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        if (member.getId() != trip.getMember().getId()) {
            return ResponseDto.fail(NOT_AUTHORIZED);
        }

        tripRepository.delete(trip);

        return ResponseDto.success(NULL);
    }

    public ResponseDto<?> getPublicTrips() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<TripResponseDto> tripResponseDtoList = new ArrayList<>();
        List<Trip> tripList = tripRepository.findAllByIsPublic(true);

        if (null != member) {
            for (Trip trip : tripList) {
                boolean isbookmarked = false;
                TripBookmark findTripBookmark = tripBookmarkRepository.findByMemberAndTrip(member, trip);
                if (null != findTripBookmark) {
                    isbookmarked = true;
                }
                tripResponseDtoList.add(TripResponseDto.builder()
                        .id(trip.getId())
                        .title(trip.getTitle())
                        .isPublic(trip.getIsPublic())
                        .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                        .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                        .isbookmarked(isbookmarked)
                        .build()
                );
            }
        } else {
            for (Trip trip : tripList) {
                tripResponseDtoList.add(TripResponseDto.builder()
                        .id(trip.getId())
                        .title(trip.getTitle())
                        .isPublic(trip.getIsPublic())
                        .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                        .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))).build()
                );
            }
        }
        return ResponseDto.success(tripResponseDtoList);
    }

    public ResponseDto<?> createCourse(CourseRequestDto courseRequestDto, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        Course course = isPresentCourse(courseRequestDto.getCourseId());
        if(null == course) {
            return ResponseDto.fail(COURSE_NOT_FOUND);
        }

        for(CourseDetailRequestDto requestDto : courseRequestDto.getCourseDetails()) {
            switch (requestDto.getCategory()) {
                case "관광지":
                    TouristSpot touristSpot = isPresentTouristSpot(requestDto.getId());
                    CourseDetailSpot courseDetailSpot = CourseDetailSpot.builder()
                            .course(course)
                            .touristSpot(touristSpot)
                            .detailOrder(requestDto.getDetailOrder()).build();
                    courseDetailSpotRepository.save(courseDetailSpot);
                    break;
                case "맛집":
                    Restaurant restaurant = isPresentRestaurant(requestDto.getId());
                    CourseDetailRest courseDetailRest = CourseDetailRest.builder()
                            .course(course)
                            .restaurant(restaurant)
                            .detailOrder(requestDto.getDetailOrder()).build();
                    courseDetailRestRepository.save(courseDetailRest);
                    break;
                default:
                    break;

            }
        }

        return ResponseDto.success(NULL);
    }

    public Trip isPresentTrip(Long id) {
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        return optionalTrip.orElse(null);
    }

    public Course isPresentCourse(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.orElse(null);
    }

    public Restaurant isPresentRestaurant(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        return optionalRestaurant.orElse(null);
    }

    public TouristSpot isPresentTouristSpot(Long id) {
        Optional<TouristSpot> optionalTouristSpot = touristSpotRepository.findById(id);
        return optionalTouristSpot.orElse(null);
    }

}
