package com.innocamp.dduha.domain.trip.service;

import com.innocamp.dduha.domain.trip.dto.response.CourseDetailResponseDto;
import com.innocamp.dduha.domain.trip.model.CourseDetailSpot;
import com.innocamp.dduha.domain.trip.model.Trip;
import com.innocamp.dduha.domain.trip.repository.CourseDetailRestRepository;
import com.innocamp.dduha.domain.trip.repository.CourseDetailSpotRepository;
import com.innocamp.dduha.domain.trip.repository.CourseRepository;
import com.innocamp.dduha.domain.trip.repository.TripRepository;
import com.innocamp.dduha.global.common.ResponseDto;
import com.innocamp.dduha.domain.trip.dto.request.CourseDetailRequestDto;
import com.innocamp.dduha.domain.trip.dto.request.CourseRequestDto;
import com.innocamp.dduha.domain.trip.dto.request.TripRequestDto;
import com.innocamp.dduha.domain.trip.dto.response.CourseResponseDto;
import com.innocamp.dduha.domain.place.general.dto.response.ListResponseDto;
import com.innocamp.dduha.domain.trip.dto.response.TripResponseDto;
import com.innocamp.dduha.global.util.TokenProvider;
import com.innocamp.dduha.domain.member.model.Member;
import com.innocamp.dduha.domain.bookmark.model.TripBookmark;
import com.innocamp.dduha.domain.trip.model.Course;
import com.innocamp.dduha.domain.trip.model.CourseDetailAcc;
import com.innocamp.dduha.domain.trip.model.CourseDetailRest;
import com.innocamp.dduha.domain.bookmark.repository.TripBookmarkRepository;
import com.innocamp.dduha.domain.trip.repository.CourseDetailAccReposiotry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.innocamp.dduha.global.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final CourseRepository courseRepository;
    private final CourseDetailSpotRepository courseDetailSpotRepository;
    private final CourseDetailRestRepository courseDetailRestRepository;
    private final CourseDetailAccReposiotry courseDetailAccRepository;
    private final TripBookmarkRepository tripBookmarkRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseEntity<?> createTrip(TripRequestDto requestDto) {

        Member member = tokenProvider.getMemberFromAuthentication();

        LocalDate startAt = LocalDate.parse(requestDto.getStartAt(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDate endAt = LocalDate.parse(requestDto.getEndAt(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Trip trip = Trip.builder()
                .title(requestDto.getTitle())
                .isPublic(requestDto.getIsPublic())
                .startAt(startAt)
                .endAt(endAt)
                .isHidden(false)
                .member(member)
                .build();

        Trip savedTrip = tripRepository.save(trip);

        for (int i = 1; i <= ChronoUnit.DAYS.between(startAt, endAt) + 1; i++) {
            Course course = Course.builder()
                    .day(i)
                    .trip(trip)
                    .build();
            courseRepository.save(course);
        }

        return ResponseEntity.ok(ResponseDto.success(savedTrip.getId()));
    }

    public ResponseEntity<?> getMyTrips() {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<TripResponseDto> tripResponseDtoList = new ArrayList<>();
        List<Trip> tripList = tripRepository.findAllByMemberAndIsHiddenIsFalseOrderByCreatedAtDesc(member);

        for (Trip trip : tripList) {
            tripResponseDtoList.add(TripResponseDto.builder()
                    .id(trip.getId())
                    .title(trip.getTitle())
                    .isPublic(trip.getIsPublic())
                    .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))).build()
            );
        }

        return ResponseEntity.ok(ResponseDto.success(tripResponseDtoList));
    }

    public ResponseEntity<?> getMyTripInfo(Long id) {

        Trip trip = tripRepository.findTripByIdAndIsHiddenFalse(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(TRIP_NOT_FOUND)));

        Member member = tokenProvider.getMemberFromAuthentication();

        if (member.getId() != trip.getMember().getId()) {
            throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
        }

        List<Course> courseList = courseRepository.findAllByTrip(trip);
        List<CourseResponseDto> courseResponseDtoList = new ArrayList<>();


        for (Course course : courseList) {
            List<CourseDetailResponseDto> courseDetailResponseDtoList = new ArrayList<>();

            List<CourseDetailSpot> courseDetailSpotList = courseDetailSpotRepository.findAllByCourse(course);
            for (CourseDetailSpot courseDetailSpot : courseDetailSpotList) {
                courseDetailResponseDtoList.add(CourseDetailResponseDto.builder()
                        .detailOrder(courseDetailSpot.getDetailOrder())
                        .detailId(courseDetailSpot.getId())
                        .category("관광지")
                        .latitude(courseDetailSpot.getTouristSpot().getLatitude())
                        .longitude(courseDetailSpot.getTouristSpot().getLongitude())
                        .name(courseDetailSpot.getTouristSpot().getName()).build()
                );
            }

            List<CourseDetailRest> courseDetailRestList = courseDetailRestRepository.findAllByCourse(course);
            for (CourseDetailRest courseDetailRest : courseDetailRestList) {
                courseDetailResponseDtoList.add(CourseDetailResponseDto.builder()
                        .detailOrder(courseDetailRest.getDetailOrder())
                        .detailId(courseDetailRest.getId())
                        .category("맛집")
                        .latitude(courseDetailRest.getRestaurant().getLatitude())
                        .longitude(courseDetailRest.getRestaurant().getLongitude())
                        .name(courseDetailRest.getRestaurant().getName()).build()
                );
            }

            List<CourseDetailAcc> courseDetailAccList = courseDetailAccRepository.findAllByCourse(course);
            for (CourseDetailAcc courseDetailAcc : courseDetailAccList) {
                courseDetailResponseDtoList.add(CourseDetailResponseDto.builder()
                        .detailOrder(courseDetailAcc.getDetailOrder())
                        .detailId(courseDetailAcc.getId())
                        .category("숙소")
                        .latitude(courseDetailAcc.getAccommodation().getLatitude())
                        .longitude(courseDetailAcc.getAccommodation().getLongitude())
                        .name(courseDetailAcc.getAccommodation().getName()).build()
                );
            }
            if (!courseDetailResponseDtoList.isEmpty()) {
                courseDetailResponseDtoList.sort(new CourseDetailComparator());
            }

            courseResponseDtoList.add(CourseResponseDto.builder()
                    .courseId(course.getId())
                    .day(course.getDay())
                    .courseDetails(courseDetailResponseDtoList)
                    .build());
        }


        TripResponseDto tripResponseDto = TripResponseDto.builder()
                .id(trip.getId())
                .title(trip.getTitle())
                .isPublic(trip.getIsPublic())
                .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                .courses(courseResponseDtoList).build();

        return ResponseEntity.ok(ResponseDto.success(tripResponseDto));
    }

    @Transactional
    public ResponseEntity<?> modifyMyTrip(Long id, TripRequestDto requestDto) {

        Trip trip = tripRepository.findTripByIdAndIsHiddenFalse(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(TRIP_NOT_FOUND)));

        Member member = tokenProvider.getMemberFromAuthentication();

        if (member.getId() != trip.getMember().getId()) {
            throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
        }

        int originalDays = (int) ChronoUnit.DAYS.between(trip.getStartAt(), trip.getEndAt()) + 1;
        trip.update(requestDto);
        int modifiedDays = (int) ChronoUnit.DAYS.between(trip.getStartAt(), trip.getEndAt()) + 1;

        if (originalDays < modifiedDays) {
            for (int i = originalDays + 1; i <= modifiedDays; i++) {
                Course course = Course.builder()
                        .day(i)
                        .trip(trip)
                        .build();
                courseRepository.save(course);
            }
        } else {
            courseRepository.deleteByTripAndDayAfter(trip, modifiedDays);
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    @Transactional
    public ResponseEntity<?> deleteTrip(Long id) {

        Trip trip = tripRepository.findTripByIdAndIsHiddenFalse(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(TRIP_NOT_FOUND)));

        Member member = tokenProvider.getMemberFromAuthentication();

        if (member.getId() != trip.getMember().getId()) {
            throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
        }

        trip.doHidden();
        tripRepository.save(trip);

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }

    public ResponseEntity<?> getPublicTrips(int page) {

        Member member = tokenProvider.getMemberFromAuthentication();

        List<TripResponseDto> tripResponseDtoList = new ArrayList<>();
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, 6, sort);

        Page<Trip> trips = tripRepository.findByIsPublicIsTrueAndIsHiddenIsFalse(pageable);

        for (Trip trip : trips) {
            boolean isBookmarked = false;
            if (null != member) {
                TripBookmark findTripBookmark = tripBookmarkRepository.findByMemberAndTrip(member, trip);
                if (null != findTripBookmark) {
                    isBookmarked = true;
                }
            }
            tripResponseDtoList.add(TripResponseDto.builder()
                    .id(trip.getId())
                    .title(trip.getTitle())
                    .isPublic(trip.getIsPublic())
                    .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .isBookmarked(isBookmarked)
                    .build()
            );
        }

        ListResponseDto listResponseDto = ListResponseDto.builder()
                .totalPages(trips.getTotalPages())
                .nextPage(page+1)
                .list(tripResponseDtoList)
                .build();

        return ResponseEntity.ok(ResponseDto.success(listResponseDto));
    }

    public ResponseEntity<?> getPublicTripInfo(Long id) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Trip trip = tripRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(TRIP_NOT_FOUND)));
        if (null == trip || trip.getIsHidden() || !trip.getIsPublic()) {
            throw new NoSuchElementException(String.valueOf(TRIP_NOT_FOUND));
        }

        List<Course> courseList = courseRepository.findAllByTrip(trip);
        List<CourseResponseDto> courseResponseDtoList = new ArrayList<>();


        for (Course course : courseList) {
            List<CourseDetailResponseDto> courseDetailResponseDtoList = new ArrayList<>();

            List<CourseDetailSpot> courseDetailSpotList = courseDetailSpotRepository.findAllByCourse(course);
            for (CourseDetailSpot courseDetailSpot : courseDetailSpotList) {
                courseDetailResponseDtoList.add(CourseDetailResponseDto.builder()
                        .detailOrder(courseDetailSpot.getDetailOrder())
                        .detailId(courseDetailSpot.getId())
                        .category("관광지")
                        .latitude(courseDetailSpot.getTouristSpot().getLatitude())
                        .longitude(courseDetailSpot.getTouristSpot().getLongitude())
                        .name(courseDetailSpot.getTouristSpot().getName()).build()
                );
            }

            List<CourseDetailRest> courseDetailRestList = courseDetailRestRepository.findAllByCourse(course);
            for (CourseDetailRest courseDetailRest : courseDetailRestList) {
                courseDetailResponseDtoList.add(CourseDetailResponseDto.builder()
                        .detailOrder(courseDetailRest.getDetailOrder())
                        .detailId(courseDetailRest.getId())
                        .category("맛집")
                        .latitude(courseDetailRest.getRestaurant().getLatitude())
                        .longitude(courseDetailRest.getRestaurant().getLongitude())
                        .name(courseDetailRest.getRestaurant().getName()).build()
                );
            }

            List<CourseDetailAcc> courseDetailAccList = courseDetailAccRepository.findAllByCourse(course);
            for (CourseDetailAcc courseDetailAcc : courseDetailAccList) {
                courseDetailResponseDtoList.add(CourseDetailResponseDto.builder()
                        .detailOrder(courseDetailAcc.getDetailOrder())
                        .detailId(courseDetailAcc.getId())
                        .category("숙소")
                        .latitude(courseDetailAcc.getAccommodation().getLatitude())
                        .longitude(courseDetailAcc.getAccommodation().getLongitude())
                        .name(courseDetailAcc.getAccommodation().getName()).build()
                );
            }

            if (!courseDetailResponseDtoList.isEmpty()) {
                courseDetailResponseDtoList.sort(new CourseDetailComparator());
            }

            courseResponseDtoList.add(CourseResponseDto.builder()
                    .courseId(course.getId())
                    .day(course.getDay())
                    .courseDetails(courseDetailResponseDtoList)
                    .build());
        }

        boolean isBookmarked = false;
        if (null != member) {
            TripBookmark findTripBookmark = tripBookmarkRepository.findByMemberAndTrip(member, trip);
            if (null != findTripBookmark) {
                isBookmarked = true;
            }
        }
        TripResponseDto tripResponseDto = TripResponseDto.builder()
                    .id(trip.getId())
                    .title(trip.getTitle())
                    .isPublic(trip.getIsPublic())
                    .startAt(trip.getStartAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .endAt(trip.getEndAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .isBookmarked(isBookmarked)
                    .courses(courseResponseDtoList).build();

        return ResponseEntity.ok(ResponseDto.success(tripResponseDto));
    }

    @Transactional
    public ResponseEntity<?> saveCourseDetailOrder(CourseRequestDto courseRequestDto) {

        Member member = tokenProvider.getMemberFromAuthentication();

        Course course = courseRepository.findById(courseRequestDto.getCourseId()).orElseThrow(() ->
                new NoSuchElementException(String.valueOf(COURSE_NOT_FOUND)));
        System.out.println(course.getTrip().getMember().getId());
        if (course.getTrip().getMember().getId() != member.getId()) {
            throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
        }

        for (CourseDetailRequestDto courseDetailRequestDto : courseRequestDto.getCourseDetails()) {
            switch (courseDetailRequestDto.getCategory()) {
                case "관광지":
                    CourseDetailSpot courseDetailSpot = courseDetailSpotRepository.findById(courseDetailRequestDto.getDetailId())
                            .orElseThrow(() -> new NoSuchElementException(String.valueOf(DETAIL_NOT_FOUND)));
                    if (courseDetailSpot.getCourse().getId() != course.getId()) {
                        throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                    }
                    courseDetailSpot.changeOrder(courseDetailRequestDto.getDetailOrder());
                    courseDetailSpotRepository.save(courseDetailSpot);
                    break;
                case "맛집":
                    CourseDetailRest courseDetailRest = courseDetailRestRepository.findById(courseDetailRequestDto.getDetailId())
                            .orElseThrow(() -> new NoSuchElementException(String.valueOf(DETAIL_NOT_FOUND)));
                    if (courseDetailRest.getCourse().getId() != course.getId()) {
                        throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                    }
                    courseDetailRest.changeOrder(courseDetailRequestDto.getDetailOrder());
                    courseDetailRestRepository.save(courseDetailRest);
                    break;
                case "숙소":
                    CourseDetailAcc courseDetailAcc = courseDetailAccRepository.findById(courseDetailRequestDto.getDetailId())
                            .orElseThrow(() -> new NoSuchElementException(String.valueOf(DETAIL_NOT_FOUND)));
                    if (courseDetailAcc.getCourse().getId() != course.getId()) {
                        throw new AuthenticationServiceException(String.valueOf(REQUEST_FORBIDDEN));
                    }
                    courseDetailAcc.changeOrder(courseDetailRequestDto.getDetailOrder());
                    courseDetailAccRepository.save(courseDetailAcc);
                    break;
                default:
                    throw new ValidationException(String.valueOf(INVALID_CATEGORY));
            }
        }

        return ResponseEntity.ok(ResponseDto.success(NULL));
    }
}

class CourseDetailComparator implements Comparator<CourseDetailResponseDto> {

    @Override
    public int compare(CourseDetailResponseDto o1, CourseDetailResponseDto o2) {
        return o1.getDetailOrder() - o2.getDetailOrder();
    }
}