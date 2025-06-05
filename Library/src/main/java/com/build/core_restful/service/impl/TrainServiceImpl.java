package com.build.core_restful.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.DataSave;
import com.build.core_restful.domain.DataTrain;
import com.build.core_restful.domain.Image;
import com.build.core_restful.domain.response.BookRecommendResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.DataSaveRepository;
import com.build.core_restful.repository.DataTrainRepository;
import com.build.core_restful.service.TrainService;

@Service
public class TrainServiceImpl implements TrainService {
    private final DataTrainRepository dataTrainRepository;
    private final DataSaveRepository dataSaveRepository;
    private final BookRepository bookRepository;

    private static final int MIN_SAMPLES_PER_RULE = 3;

    public TrainServiceImpl(
        DataTrainRepository dataTrainRepository,
        DataSaveRepository dataSaveRepository,
        BookRepository bookRepository
    ) {
        this.dataTrainRepository = dataTrainRepository;
        this.dataSaveRepository = dataSaveRepository;
        this.bookRepository = bookRepository;
    };

    @Override
    @Transactional
    public void trainDataEveryDay() {
        try {
            List<DataTrain> trainData = dataTrainRepository.findAll();
            
            if (trainData.size() < MIN_SAMPLES_PER_RULE) {
                System.out.println("Không đủ dữ liệu để train (cần tối thiểu " + MIN_SAMPLES_PER_RULE + " samples)");
                return;
            }

            dataSaveRepository.deleteAll();

            Map<String, List<DataTrain>> demographicGroups = createDemographicGroups(trainData);

            int rulesCreated = 0;
            for (Map.Entry<String, List<DataTrain>> entry : demographicGroups.entrySet()) {
                List<DataTrain> groupData = entry.getValue();
                
                if (groupData.size() >= MIN_SAMPLES_PER_RULE) {
                    List<DataSave> rules = createRulesFromGroup(groupData);
                    if (!rules.isEmpty()) {
                        dataSaveRepository.saveAll(rules);
                        rulesCreated += rules.size();
                    }
                }
            }

            System.out.println("Training completed. Created " + rulesCreated + " rules from " + trainData.size() + " samples");

        } catch (Exception e) {
            System.err.println("Error during training: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<String, List<DataTrain>> createDemographicGroups(List<DataTrain> trainData) {
        Map<String, List<DataTrain>> groups = new HashMap<>();

        for (DataTrain data : trainData) {
            String ageRange = getAgeRange(data.getAge());
            String groupKey = data.getGender() + "_" + data.getCity() + "_" + ageRange;
            
            groups.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(data);
        }

        return groups;
    }

    private String getAgeRange(Long age) {
        if (age == null) return "UNKNOWN";
        
        if (age < 18) return "UNDER_18";
        else if (age < 25) return "18_24";
        else if (age < 35) return "25_34";
        else if (age < 45) return "35_44";
        else if (age < 55) return "45_54";
        else if (age < 65) return "55_64";
        else return "65_PLUS";
    }

    private List<DataSave> createRulesFromGroup(List<DataTrain> groupData) {
        if (groupData.isEmpty()) return new ArrayList<>();

        List<DataSave> rules = new ArrayList<>();

        Map<String, Long> categoryCount = groupData.stream()
            .collect(Collectors.groupingBy(
                DataTrain::getCategoryId,
                Collectors.counting()
            ));

        DataTrain sample = groupData.get(0);
        long totalCount = groupData.size();
        
        List<Long> ages = groupData.stream()
            .map(DataTrain::getAge)
            .filter(Objects::nonNull)
            .sorted()
            .collect(Collectors.toList());

        if (ages.isEmpty()) return new ArrayList<>();

        Long startAge = ages.get(0);
        Long endAge = ages.get(ages.size() - 1);

        for (Map.Entry<String, Long> entry : categoryCount.entrySet()) {
            String categoryId = entry.getKey();
            long categoryOccurrences = entry.getValue();
            double confidence = (double) categoryOccurrences / totalCount;

            if (confidence >= 0.1 && categoryOccurrences >= 1) {
                DataSave rule = new DataSave();
                rule.setStartAge(startAge);
                rule.setEndAge(endAge);
                rule.setGender(sample.getGender());
                rule.setCity(sample.getCity());
                rule.setCategoryId(Long.valueOf(categoryId));
                rule.setConfidence(confidence);
                rule.setSampleSize((int) totalCount);
                rule.setCategoryCount((int) categoryOccurrences);
                rule.setCreatedDate(Instant.now());
                rule.setAlgorithm("DEMOGRAPHIC_GROUPING");

                rules.add(rule);
            }
        }

        return rules;
    }

    @Override
    public List<BookRecommendResponse> predictCategory(Long age, String gender, String city) {
        List<DataSave> matchingRules = dataSaveRepository.findByGenderAndAgeRange(gender, age);
        if (city != null) {
            matchingRules = dataSaveRepository.findByGenderAndCityAndAgeRange(
            gender, city, age);
        }

        List<BookRecommendResponse> bookResponses = new ArrayList<>();
        for(DataSave data : matchingRules){
            List<Book> books = bookRepository.findByCategoryId(data.getCategoryId());
            for(Book book: books){
                BookRecommendResponse response = BookRecommendResponse.builder()          
                                                    .id(book.getId())
                                                    .name(book.getName())
                                                    .imageUrl(book.getImages().stream()
                                                    .filter(image -> "true".equalsIgnoreCase(image.getIsDefault()))
                                                    .map(Image::getUrl)
                                                    .findFirst()
                                                    .orElse(null)
                                                    )
                                                    .rentalPrice(book.getRentalPrice())
                                                    .authorName(book.getAuthor().getName())
                                                    .categoryName(book.getCategory().getName())
                                                    .stock(book.getStock())
                                                    .build();

                bookResponses.add(response);
            }
        }
        
        return bookResponses;
        
    }
}
