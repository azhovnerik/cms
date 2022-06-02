package com.example.cms.repo;

import com.example.cms.model.Page;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends CrudRepository<Page,Long> {
List<Page> findAll();
Optional<Page> findPageBySlug(String slug);
Optional<Page> findPageBySlugAndIdIsNot(String slug,Long id);
Optional<Page> findPageById(Long id);

}
