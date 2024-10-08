package com.example.capstone_project.utils.mapper.report.detail;

import com.example.capstone_project.controller.responses.report.detail.ReportDetailResponse;
import com.example.capstone_project.repository.result.ReportDetailResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportDetailMapper {
    @Mapping(expression = "java(reportDetailResult.getReportId())", target = "reportId")
    @Mapping(expression = "java(reportDetailResult.getName())", target = "name")
    @Mapping(expression = "java(reportDetailResult.getTermId())", target = "term.termId")
    @Mapping(expression = "java(reportDetailResult.getTermName())", target = "term.name")
    @Mapping(expression = "java(reportDetailResult.getStartDate())", target = "term.startDate")
    @Mapping(expression = "java(reportDetailResult.getEndDate())", target = "term.endDate")
    @Mapping(expression = "java(reportDetailResult.isAllowReupload())", target = "term.allowReupload")
    @Mapping(expression = "java(reportDetailResult.getReuploadStartDate())", target = "term.reuploadStartDate")
    @Mapping(expression = "java(reportDetailResult.getReuploadEndDate())", target = "term.reuploadEndDate")
    @Mapping(expression = "java(reportDetailResult.getFinalEndTermDate())", target = "term.finalEndTermDate")
    @Mapping(expression = "java(reportDetailResult.getStatusId())", target = "status.statusId")
    @Mapping(expression = "java(reportDetailResult.getStatusCode())", target = "status.code")
    @Mapping(expression = "java(reportDetailResult.getStatusName())", target = "status.name")
    @Mapping(expression = "java(reportDetailResult.getCreatedAt())", target = "createdAt")
    ReportDetailResponse mapToReportDetailResponseMapping(ReportDetailResult reportDetailResult);
}