package com.awsjwtservice.controller;


import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.OrderItem;
import com.awsjwtservice.domain.OrderSearch;
import com.awsjwtservice.domain.Orders;
import com.awsjwtservice.domain.item.Item;
import com.awsjwtservice.dto.OrdersDto;
import com.awsjwtservice.service.AccountService;
import com.awsjwtservice.service.ItemService;
import com.awsjwtservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    AccountService userService;

    @Autowired
    ItemService itemService;


    /* make order */
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String createForm(Model model) {
        List<Account> users = userService.getUsers();
        List<Item> items = itemService.findItems();

        model.addAttribute("users", users);
        model.addAttribute("items", items);


        return "order/orderForm";
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String order(@RequestParam("userId") long memberId, @RequestParam("itemId") long itemId, @RequestParam("count") int count) {

        try {
            orderService.order(memberId, itemId, count);
            return "redirect:/orders";
        } catch (Exception e) {
//            return "redirect:/order?msg:nada";
            return "redirect:/order?msg:notEnoughStock";
        }


    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {

        List<Orders> orders = orderService.findOrders(orderSearch);
//        List<OrdersDto> ordersDto = new ArrayList<OrdersDto>();       // used to be like this, but i changed to using dtos

        List<OrdersDto> ordersDto = orders.stream().map(e ->
                OrdersDto.builder()
                        .id(e.getId())
                        .delivery(e.getDelivery())
                        .orderDate(e.getOrderDate())
                        .status(e.getStatus())
                        .username(e.getAccount().getUsername())
                        .orderItems(e.getOrderItems())
                        .orderBoolean(Boolean.valueOf(e.getStatus().toString() == "ORDER" ? true : false))
                        .build()).collect(Collectors.toList());


        model.addAttribute("orders", ordersDto);

        return "order/orderList";
    }


    @RequestMapping(value = "/orders/{orderId}/cancel")
    public String processCancelBuy(@PathVariable("orderId") Long orderId) {

        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }

}
