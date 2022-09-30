package com.innocamp.dduha.service;

import com.innocamp.dduha.dto.ResponseDto;
import com.innocamp.dduha.dto.request.CourseAccRequestDto;
import com.innocamp.dduha.dto.request.CourseDetailRequestDto;
import com.innocamp.dduha.jwt.TokenProvider;
import com.innocamp.dduha.model.Member;
import com.innocamp.dduha.model.accommodation.Accommodation;
import com.innocamp.dduha.model.course.Course;
import com.innocamp.dduha.model.course.CourseDetailAcc;
import com.innocamp.dduha.model.course.CourseDetailRest;
import com.innocamp.dduha.model.course.CourseDetailSpot;
import com.innocamp.dduha.model.restaurant.Restaurant;
import com.innocamp.dduha.model.touristspot.TouristSpot;
import com.innocamp.dduha.repository.CourseRepository;
import com.innocamp.dduha.repository.accommodation.AccommodationRepository;
import com.innocamp.dduha.repository.coursedetail.CourseDetailAccReposiotry;
import com.innocamp.dduha.repository.coursedetail.CourseDetailRestRepository;
import com.innocamp.dduha.repository.coursedetail.CourseDetailSpotRepository;
import com.innocamp.dduha.repository.restaurant.RestaurantRepository;
import com.innocamp.dduha.repository.touristspot.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

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

        List<CourseDetailSpot> savedCourseDetailSpotList =
                courseDetailSpotRepository.findAllByCourseAndDetailOrderGreaterThanEqual(course, requestDto.getDetailOrder());
        if (null != savedCourseDetailSpotList) {
            for (CourseDetailSpot courseDetailSpot : savedCourseDetailSpotList) {
                courseDetailSpot.postponeOrder();
                courseDetailSpotRepository.save(courseDetailSpot);
            }
        }

        List<CourseDetailRest> savedCourseDetailRestList =
                courseDetailRestRepository.findAllByCourseAndDetailOrderGreaterThanEqual(course, requestDto.getDetailOrder());
        if (null != savedCourseDetailRestList) {
            for (CourseDetailRest courseDetailRest : savedCourseDetailRestList) {
                courseDetailRest.postponeOrder();
                courseDetailRestRepository.save(courseDetailRest);
            }
        }

        switch (requestDto.getCategory()) {
            case "관광지":
                TouristSpot touristSpot = isPresentTouristSpot(requestDto.getDetailId());
                CourseDetailSpot courseDetailSpot = CourseDetailSpot.builder()
                        .course(course)
                        .touristSpot(touristSpot)
                        .detailOrder(requestDto.getDetailOrder()).build();
                courseDetailSpotRepository.save(courseDetailSpot);
                break;
            case "맛집":
                Restaurant restaurant = isPresentRestaurant(requestDto.getDetailId());
                CourseDetailRest courseDetailRest = CourseDetailRest.builder()
                        .course(course)
                        .restaurant(restaurant)
                        .detailOrder(requestDto.getDetailOrder()).build();
                courseDetailRestRepository.save(courseDetailRest);
                break;
            default:
                break;
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
            default:
                break;
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


        return ResponseDto.success(NULL);
    }


    @Transactional
    public ResponseDto<?> addAccommodation(CourseAccRequestDto courseAccRequestDto, HttpServletRequest request) {

        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        Course course = isPresentCourse(courseAccRequestDto.getCourseId());
        if (null == course) {
            return ResponseDto.fail(COURSE_NOT_FOUND);
        }

        if(!course.getTrip().getMember().getId().equals(member.getId())) {
            return ResponseDto.fail(NOT_AUTHORIZED);
        }

        Accommodation accommodation = isPresentAccommodation(courseAccRequestDto.getAccId());
        if(null == accommodation) {
            return ResponseDto.fail(ACCOMMODATION_NOT_FOUND);
        }

        CourseDetailAcc courseDetailAcc = courseDetailAccRepository.findCourseDetailAccByCourse(course);
        if (null == courseDetailAcc) {
            courseDetailAcc = CourseDetailAcc.builder()
                    .course(course)
                    .accommodation(accommodation).build();
        } else {
            courseDetailAcc.changeAccommodation(accommodation);
        }

        courseDetailAccRepository.save(courseDetailAcc);

        return ResponseDto.success(NULL);
    }

    @Transactional
    public ResponseDto<?> removeAccommodation(Long id, HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        CourseDetailAcc courseDetailAcc = isCourseDetailAcc(id);
        if(null == courseDetailAcc) {
            return ResponseDto.fail(DETAIL_NOT_FOUND);
        }

        if(!courseDetailAcc.getCourse().getTrip().getMember().getId().equals(member.getId())) {
            return ResponseDto.fail(NOT_AUTHORIZED);
        }

        courseDetailAccRepository.delete(courseDetailAcc);

        return ResponseDto.success(NULL);
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


}
