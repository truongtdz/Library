package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.RevenueReport;
import com.build.core_restful.domain.response.RevenueReportResponse;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RevenueReportMapper {
    RevenueReportResponse toRevenueReportResponse(RevenueReport revenueReport);
    List<RevenueReportResponse> toRevenueReportResponseList(List<RevenueReport> revenueReports);
}