package com.gmail.rebel249.controller.app;

import com.gmail.rebel249.servicemodule.ItemService;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/showItems")
    public String showItem(Model model, ItemDTO itemDTO) {
        List<ItemDTO> itemsList = itemService.getItems();
        model.addAttribute("itemsList", itemsList);
        model.addAttribute("items", itemDTO);
        return "items";
    }

    @PostMapping("/addItem")
    public String addItems(@Valid ItemDTO itemDTO, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/errors";
        }
        itemService.add(itemDTO);
        List<ItemDTO> itemsList = itemService.getItems();
        model.addAttribute("itemsList", itemsList);
        model.addAttribute("items", itemDTO);
        return "items";
    }

    @PostMapping("/updateItem")
    public String updateItem(ItemDTO itemDTO, Model model) {
        logger.info(itemDTO.toString());
        itemService.update(itemDTO.getId(), itemDTO.getItem_status());
        List<ItemDTO> itemsList = itemService.getItems();
        model.addAttribute("itemsList", itemsList);
        model.addAttribute("items", itemDTO);
        return "items";
    }

    @GetMapping("/items")
    public String showBlankFields() {
        return "items";
    }

    @GetMapping("/errors")
    public String getErrors() {
        return "errors";
    }

}
