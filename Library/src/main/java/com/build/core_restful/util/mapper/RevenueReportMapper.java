package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.RevenueReports;
import com.build.core_restful.domain.response.RevenueReportResponse;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RevenueReportMapper {
    RevenueReportResponse toRevenueReportResponse(RevenueReports revenueReport);
    List<RevenueReportResponse> toRevenueReportResponseList(List<RevenueReports> revenueReports);
}