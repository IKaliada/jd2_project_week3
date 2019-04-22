package com.gmail.rebel249.app;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gmail.rebel249.controller.app.ItemController;
import com.gmail.rebel249.servicemodule.ItemService;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
@ActiveProfiles("test")
public class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private WebClient webClient;

    @MockBean
    private ItemService itemService;

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
        when(itemService.getItems()).thenReturn(itemDTOS);
        webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc)
                .useMockMvcForHosts("app.com")
                .build();
    }

    @Test
    public void requestForUsersIsSuccessfullyProcessedWithAvailableUsersList() throws Exception {
        this.mockMvc.perform(get("/items").accept(MediaType.parseMediaType("text/html;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(allOf(
                        containsString("Name"),
                        containsString("Name2")))
                );
    }

    @Test
    public void usersPageContentIsRenderedAsHtmlWithListOfUsers() throws IOException {
        HtmlPage page = webClient.getPage("http://app.com/users");
        List<String> booksList = page.getElementsByTagName("li")
                .stream().map(DomNode::asText).collect(toList());
        assertThat(booksList, hasItems("1. First, name, address", "2. Second, another name, address"));
    }
}
