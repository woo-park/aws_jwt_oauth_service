package com.awsjwtservice.controller;

import com.awsjwtservice.domain.item.Art;
import com.awsjwtservice.domain.item.Book;
import com.awsjwtservice.domain.item.Item;
import com.awsjwtservice.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ItemController {

    // get a logger
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    ItemService itemService;

    @RequestMapping(value = "/items/new", method = RequestMethod.GET)
    public String createForm() {
        return "items/createItemForm";
    }

    @RequestMapping(value = "/items/new", method = RequestMethod.POST)
    public String create(Book item) {

        itemService.saveItem(item);

        logger.info("book item saved: " + item.getName());
        return "redirect:/items";
    }

    @RequestMapping(value = "/items/new/art", method = RequestMethod.POST)
    public String create(Art item) {

        itemService.saveArt(item);

        logger.info("art item saved: " + item.getName());
        return "redirect:/items";
    }

    /**
     * 상품 수정 폼
     */
    @RequestMapping(value = "/items/{itemId}/edit", method = RequestMethod.GET)
    public String updateItemForm(@PathVariable("itemId") long itemId, Model model) {

        Item item = itemService.findOne(itemId);


        // need to figure out types of an item, then show different forms based on their types


        model.addAttribute("item", item);
        return "items/updateItemForm";
    }

    /**
     * 상품 수정
     */
    @RequestMapping(value = "/items/{itemId}/edit", method = RequestMethod.POST)
    public String updateItem(@ModelAttribute("item") Book item) {

        itemService.saveItem(item);
        return "redirect:/items";
    }



    /**
     * 상품 목록
     */
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String list(Model model) {

        List<Item> items = itemService.findItems();

        model.addAttribute("items", items);

        logger.info("/items reached");
        return "items/itemList";
    }

}
