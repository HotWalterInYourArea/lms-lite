package com.example.lmslite.dto.lesson.update;

import com.example.lmslite.dto.lesson.create.CreateQuizLessonRequest;
import com.example.lmslite.dto.lesson.create.CreateTextLessonRequest;
import com.example.lmslite.dto.lesson.create.CreateVideoLessonRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "lessonType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateVideoLessonRequest.class, name = "VIDEO"),
        @JsonSubTypes.Type(value = CreateQuizLessonRequest.class, name = "QUIZ"),
        @JsonSubTypes.Type(value = CreateTextLessonRequest.class, name = "TEXT")
})
public interface UpdateLessonRequest
{
    String title();
}
