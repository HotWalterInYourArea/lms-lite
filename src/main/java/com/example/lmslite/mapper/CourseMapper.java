package com.example.lmslite.mapper;

import com.example.lmslite.dto.course.create.CreateCourseRequest;
import com.example.lmslite.dto.course.create.CreateCourseResponse;
import com.example.lmslite.dto.course.detail.CourseDetailResponse;
import com.example.lmslite.dto.course.list.CourseListResponse;
import com.example.lmslite.dto.course.list.CourseResponse;
import com.example.lmslite.dto.course.update.ChangeCourseStatusResponse;
import com.example.lmslite.dto.course.update.UpdateCourseRequest;
import com.example.lmslite.dto.course.update.UpdateCourseResponse;
import com.example.lmslite.model.Course;
import com.example.lmslite.model.Instructor;

import java.util.List;

public class CourseMapper
{
    public static Course toEntity(CreateCourseRequest request, Instructor author)
    {
        Course course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setAuthor(author);
        return course;
    }
    public static void updateEntity(Course course, UpdateCourseRequest request)
    {
        course.setTitle(request.title());
        course.setDescription(request.description());
    }
    public static CourseResponse toResponse(Course course)
    {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getAuthor().getFullName(),
                course.getStatus(),
                course.getTotalLessons(),
                course.getTotalEstimatedMinutes());

    }
    public static CreateCourseResponse toCreateResponse(Course course)
    {
        return new CreateCourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription());

    }
    public static UpdateCourseResponse toUpdateResponse(Course course)
    {
        return new UpdateCourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription());

    }
    public static CourseDetailResponse toDetailResponse(Course course)
    {
        return new CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getAuthor().getFullName(),
                course.getStatus(),
                course.getTotalLessons(),
                course.getTotalEstimatedMinutes(),
                LessonMapper.toResponseList(course.getLessons()));

    }
    public static ChangeCourseStatusResponse toStatusChangeResponse(Course course)
    {
        return new ChangeCourseStatusResponse(
                course.getId(),
                course.getTitle(),
                course.getStatus());

    }

    public static CourseListResponse toListResponse(List<Course> courses)
    {
        List<CourseResponse> toResponseList = courses.stream()
                .map(CourseMapper::toResponse)
                .toList();
        return new CourseListResponse(toResponseList);
    }


}
