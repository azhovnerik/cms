package com.example.cms.controllers;

import com.example.cms.exceptions.DuplicatePageException;
import com.example.cms.exceptions.EmptyPageException;
import com.example.cms.model.Page;
import com.example.cms.repo.PageRepository;
import com.example.cms.service.PageService;
import com.example.cms.service.TypeOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Controller
//@RequestMapping("${server.servlet.context-path}")
@RequiredArgsConstructor
public class MainController {
    @Autowired
    PageService pageService;


    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        //1. get list of existing pages
        System.out.println(pageService.isShowAllPage());
        List<Page> pageList = pageService.getPages();
        System.out.println(pageList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("title", "pages");
        return "menu";
    }

    @GetMapping("/page/add")
    public String addPage(Model model, HttpServletRequest request) {

        return "page-add";
    }

    @GetMapping("/page/{slug}")
    public String addPage(@PathVariable(value = "slug") String slug, Model model) {
        Optional<Page> page = pageService.findPageBySlug(slug);
        if (page.isPresent()) {
            model.addAttribute("el", page.get());
        } else {
            throw new EmptyPageException("No such page");
        }
        return "page-show";
    }

    @PostMapping("/page/add")
    public String newPageAdd(@RequestParam String title, @RequestParam String description, @RequestParam String slug, @RequestParam String menuLabel, @RequestParam String h1, @RequestParam String content, @RequestParam String publishedAt, @RequestParam Integer priority, Model model) {
        Page page = new Page(title, description, slug, menuLabel, h1, content, pageService.stringToTimeStamp(publishedAt), priority);
        try {
            pageService.save(page, TypeOperation.ADD_NEW);
        } catch (DuplicatePageException e) {
            model.addAttribute("errorText", e.getMessage());
            return "errorPage";
        }
        return "redirect:/";
    }

    @PostMapping("/page/{slug}/save")
    public String pageAdd(@RequestParam Long id, @RequestParam String title, @RequestParam String description, @RequestParam String slug, @RequestParam String menuLabel, @RequestParam String h1, @RequestParam String content, @RequestParam String publishedAt, @RequestParam Integer priority, Model model) {
        Optional<Page> pageOptional = pageService.findPageById(id);
        if (pageOptional.isPresent()) {
            Page page = pageOptional.get();
            page.setTitle(title);
            page.setDescription(description);
            page.setSlug(slug.trim());
            page.setMenuLabel(menuLabel);
            page.setH1(h1);
            page.setContent(content);
            Timestamp timestamp = pageService.stringToTimeStamp(publishedAt);
            page.setPublishedAt(timestamp);
            page.setPriority(priority);
            try {
                pageService.save(page, TypeOperation.EDIT);
            } catch (EmptyPageException | DuplicatePageException e) {
                model.addAttribute("errorText", e.getMessage());
                return "errorPage";
            }
        }

        return "redirect:/";

    }

    @GetMapping("/page/{slug}/edit")
    public String pageEdit(@PathVariable(value = "slug") String slug, Model model) {
        Optional<Page> page = pageService.findPageBySlug(slug);
        if (page.isPresent()) {
            model.addAttribute("el", page.get());
            String publishedAtString = page.get().getPublishedAt().toString().substring(0, 16).replace(" ", "T");
            model.addAttribute("publishedAtString", publishedAtString);
        } else {
            throw new EmptyPageException("No such page");
        }

        return "page-edit";
    }

    @GetMapping("/page/{slug}/delete")
    public String pageRemove(@PathVariable(value = "slug") String slug, Model model) {
        Optional<Page> pageOptional = pageService.findPageBySlug(slug);
        if (pageOptional.isPresent()) {
            pageService.delete(pageOptional.get());
        } else {
            throw new EmptyPageException("No such page");
        }
        return "redirect:/";
    }


}
