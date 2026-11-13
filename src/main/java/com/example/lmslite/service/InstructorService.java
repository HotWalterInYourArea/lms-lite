package com.example.lmslite.service;

import com.example.lmslite.dto.course.list.CourseListResponse;
import com.example.lmslite.dto.instructor.create.CreateInstructorRequest;
import com.example.lmslite.dto.instructor.detail.InstructorProfileResponse;
import com.example.lmslite.dto.instructor.stats.InstructorStatisticsResponse;
import com.example.lmslite.exception.notFound.InstructorNotFoundException;
import com.example.lmslite.mapper.CourseMapper;
import com.example.lmslite.mapper.InstructorMapper;
import com.example.lmslite.model.Course;
import com.example.lmslite.model.Instructor;
import com.example.lmslite.repository.CourseRepository;
import com.example.lmslite.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorService
{
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public InstructorProfileResponse register(CreateInstructorRequest request)
    {
        Instructor newInstructor = InstructorMapper.toEntity(request);
        instructorRepository.save(newInstructor);
        return InstructorMapper.toProfileResponse(newInstructor);
    }

    @Transactional(readOnly = true)
    public InstructorProfileResponse getById(Long instructorId)
    {
        Instructor instructorToGet = instructorRepository.findById(instructorId).orElseThrow(
                () -> new InstructorNotFoundException(instructorId));

        return InstructorMapper.toProfileResponse(instructorToGet);
    }

    @Transactional(readOnly = true)
    public CourseListResponse getCourses(Long instructorId)
    {
        if(!instructorRepository.existsById(instructorId))
        {
            throw new InstructorNotFoundException(instructorId);
        }
        List<Course> instructorCourses = courseRepository.findAllByAuthorIdWithLessons(instructorId);
        if (!instructorCourses.isEmpty()) {
            courseRepository.fetchQuestionsForQuizLessonsByAuthor(instructorId);
        }
        return CourseMapper.toListResponse(instructorCourses);
    }

    @Transactional(readOnly = true)
    public InstructorStatisticsResponse getInstructorStats(Long instructorId)
    {
        if(!instructorRepository.existsById(instructorId)){
            throw new InstructorNotFoundException(instructorId);
        }
        return instructorRepository.getInstructorStatistics(instructorId);

    }


}
