package kz.halykacademy.bookstore.utils.convertor;

import kz.halykacademy.bookstore.dto.OrderDTO;
import kz.halykacademy.bookstore.models.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConvertor {
    private final ModelMapper modelMapper;

    @Autowired
    public OrderConvertor(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public OrderDTO convertToOrderDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    public Order convertToOrder(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }
}
