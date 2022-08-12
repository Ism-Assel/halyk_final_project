package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.ModelResponseDTO;
import kz.halykacademy.bookstore.dto.order.OrderAdminRequest;
import kz.halykacademy.bookstore.dto.order.OrderUserRequest;
import kz.halykacademy.bookstore.errors.ClientBadRequestException;
import kz.halykacademy.bookstore.errors.ResourceNotFoundException;
import kz.halykacademy.bookstore.models.Book;
import kz.halykacademy.bookstore.models.Order;
import kz.halykacademy.bookstore.models.enums.OrderStatus;
import kz.halykacademy.bookstore.repositories.BookRepository;
import kz.halykacademy.bookstore.repositories.OrderRepository;
import kz.halykacademy.bookstore.repositories.UserRepository;
import kz.halykacademy.bookstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kz.halykacademy.bookstore.utils.AssertUtil.notNull;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final String MESSAGE_NOT_FOUND = "Order is not found with id = %d";
    private final String MESSAGE_SUCCESS = "success";
    private final String MESSAGE_EXISTED = "This order is existed";
    private final String MESSAGE_ORDER_PRICE = "Order price should not be greater than 10000 tg. Total price of order is: %.2f";
    private final Double MAX_PRICE = 10000.0;
    private final String MESSAGE_USER_NOT_FOUND = "User is not found with id = %d";
    private final String MESSAGE_IS_BLOCKED = "User is blocked";
    private final String MESSAGE_LIST_ORDERS = "List of orders are empty";

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            BookRepository bookRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity create(OrderUserRequest request) {
            // проверка параметров запроса
            checkParameters(request);

            // подготовка заказа
            Order order = prepareOrder(request, null);

            // сохранение заказа
            order = orderRepository.save(order);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(order.toOrderDto());
    }

    public ResponseEntity readById(Long id) {
        // Поиск order по id
        Optional<Order> foundOrder = orderRepository.findById(id);

        if (foundOrder.isEmpty()) {
            // Если не найден order
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }

        return new ResponseEntity(foundOrder.map(Order::toOrderDto).get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity readAll() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new ClientBadRequestException(MESSAGE_LIST_ORDERS);
        }

        return new ResponseEntity(
                orders.stream()
                        .map(Order::toOrderDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity update(Long id, OrderUserRequest request) {
        // проверка параметров запроса
        checkParameters(id, request);

        // Поиск order по id
        Optional<Order> foundOrder = orderRepository.findById(id);

        if (foundOrder.isPresent()) {
            // Если найден, обновляем order
            Order order = prepareOrder(request, foundOrder.get());

            order = orderRepository.save(order);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(order.toOrderDto());

        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    @Override
    public ResponseEntity delete(Long id) {
        // Проверка параметра id
        notNull(id, "Id is undefined");

        // Поиск order по id
        Optional<Order> foundOrder = orderRepository.findById(id);

        if (foundOrder.isPresent()) {
            // если нашли, удаляем
            orderRepository.deleteById(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ModelResponseDTO(MESSAGE_SUCCESS));

        } else {
            // иначе выводим сообщение пользователю
            throw new ResourceNotFoundException(String.format(MESSAGE_NOT_FOUND, id));
        }
    }

    protected void checkParameters(Long id, OrderUserRequest request) {
        notNull(id, "Id is undefined");
        checkParameters(request);
    }

    protected void checkParameters(OrderUserRequest request) {
        if (request instanceof OrderAdminRequest) {
            OrderAdminRequest adminRequest = (OrderAdminRequest) request;
            notNull(adminRequest.getStatus(), "Status is undefined");
        }

        if (request.getBooksId().isEmpty()) {
            throw new ClientBadRequestException("List of books id is empty");
        }
    }

    protected List<Book> checkOrderPriceAndGetBooks(OrderUserRequest request) {
        double totalPrice = 0;

        List<Book> books = bookRepository.findBookByIdIn(request.getBooksId());
        for (Book book : books) {
            totalPrice += book.getPrice();
        }

        if (totalPrice > MAX_PRICE) {
            throw new ClientBadRequestException(String.format(MESSAGE_ORDER_PRICE, totalPrice));
        }

        return books;
    }

    protected Order prepareOrder(OrderUserRequest request, Order order) {
        OrderAdminRequest adminRequest = null;
        boolean isAdmin = false;
        if (request instanceof OrderAdminRequest) {
            adminRequest = (OrderAdminRequest) request;
            isAdmin = true;
        }

        OrderStatus status = order != null ? order.getStatus() : OrderStatus.CREATED;
        if (isAdmin) {
            status = OrderStatus.valueOf(adminRequest.getStatus());
        }

        // проверка общей суммы заказа и возращение списка книг
        List<Book> books = checkOrderPriceAndGetBooks(request);

        // проверка user на блокировку
//        User user = checkUser(orderDTO);

        return new Order(
                request.getId(),
                books,
                status,
                order != null ? order.getCreatedAt() : null,
                null
        );
    }

//    protected User checkUser(OrderDTO orderDTO) {
//        Optional<User> user = userRepository.findById(orderDTO.getUserId());
//
//        if (user.isEmpty()) {
//            throw new ClientBadRequestException(String.format(MESSAGE_USER_NOT_FOUND, orderDTO.getUserId()));
//        }
//
//        if (user.get().getIsBlocked()) {
//            throw new ClientBadRequestException(MESSAGE_IS_BLOCKED);
//        }
//
//        return user.get();
//    }
}