package com.gmail.rebel249.app;

import com.gmail.rebel249.controller.app.ItemController;
import com.gmail.rebel249.servicemodule.ItemService;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    private ItemController itemController;

    private MockMvc mockMvc;
    private List<ItemDTO> itemDTOS = new ArrayList<>();


    @Before
    public void init() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setName("Name");
        itemDTO.setItem_status("READY");
        itemDTOS.add(itemDTO);
        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setId(2L);
        itemDTO1.setName("Name2");
        itemDTO1.setItem_status("READY");
        itemDTOS.add(itemDTO1);
        itemController = new ItemController(itemService);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        when(itemService.getItems()).thenReturn(itemDTOS);
    }

    @Test
    public void allItemsAreAddedToModelForItemsView() {
        Model model = new ExtendedModelMap();
        String items = itemController.showItem(model, itemDTOS.get(0));
        assertThat(items, equalTo("items"));
    }

    @Test
    public void requestForItemsIsSuccessfullyProcessedWithAvailableItemsList() throws Exception {
        this.mockMvc.perform(get("/items.html"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("items"));
    }

    @Test
    public void requestForErrorsIsSuccessfullyProcessed() throws Exception {
        this.mockMvc.perform(get("/errors.html"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("errors"));
    }
}
