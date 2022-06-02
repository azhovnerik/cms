package com.example.cms.service;

import com.example.cms.exceptions.DuplicatePageException;
import com.example.cms.model.Page;
import com.example.cms.repo.PageRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component

public class PageService {
    @Getter
    private boolean showAllPage; //  true - show all pages; false - show only published pages

    @Autowired
    private PageRepository pageRepository;

    public PageService() {
        showAllPage = false;
    }

    public List<Page> getPages() {


        List<Page> list = pageRepository.findAll()
                .stream()
                .filter(page -> page.getPublishedAt().compareTo(new Date()) < 0 || showAllPage)
                .sorted(Comparator.comparingInt(Page::getPriority))
                .collect(Collectors.toList());
        showAllPage = !showAllPage;
        return list;


    }

    public Optional<Page> findPageBySlug(String slug) {
        return pageRepository.findPageBySlug(slug);
    }

    public Optional<Page> findPageById(Long id) {
        return pageRepository.findPageById(id);
    }

    public void save(Page page, TypeOperation typeOperation) {
        Optional<Page> pageOptional;
        if (typeOperation == TypeOperation.ADD_NEW) {
            pageOptional = findPageBySlug(page.getSlug());
        } else {
            pageOptional = pageRepository.findPageBySlugAndIdIsNot(page.getSlug(), page.getId());
        }

        if (pageOptional.isPresent()) {
            throw new DuplicatePageException("There is existing page with slug=" + page.getSlug());
        }
        pageRepository.save(page);
    }

    public Timestamp stringToTimeStamp(String dateString) {
        try {
            //    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
            Date parsedDate = (Date) dateFormat.parse(dateString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
            return null;
        }


    }

    public void delete(Page page) {
        pageRepository.delete(page);
    }
}
